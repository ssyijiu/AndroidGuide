# Android 编程权威指南阅读笔记


## 第15章：隐式 Intent 

- 使用隐式 intent，只需要告诉操作系统我们想要做什么，操作系统就会去启动能够胜任工作任务的activity。如果找到多个符合的activity，用提供一个可选的应用列供用户选择。

- 隐式 Intent 的组成：
  - action：要执行的动作。例如，要访问某个 URL，可以使用 `Intent.ACTION_ VIEW` ，要发行邮件，可以使用 `Intent.ACTION_SEND`。
  - data：携带的数据
    - mimeType 数据类型
    - scheme 数据协议 

- 隐式 Intent 也可以使用 putExtra 来传递数据。

- ```java
  // 设置意图为 ACTION_SEND, 向其他 Activity 发送数据
  Intent intent = new Intent(Intent.ACTION_SEND);
  intent.setType("text/plain");  // 设置 mimeType 
  intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());  // 设置文本
  intent.putExtra(Intent.EXTRA_SUBJECT, subject); // 设置主题
  //  Intent.createChooser(intent, subject) 设置选择器标题
  startActivity(Intent.createChooser(intent, subject));  
  ```

  效果如图：

  ![](http://upload-images.jianshu.io/upload_images/1342220-e69274054b5b8be4.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)






- 读取联系人

  ```java
  // 1. startActivityForResult
  final Intent pickContact = new Intent(Intent.ACTION_PICK,
              ContactsContract.Contacts.CONTENT_URI);
  startActivityForResult(pickContact, REQUEST_CONTACT);

  // 2. onActivityResult
  @Override public void onActivityResult(int requestCode, int resultCode, final Intent data) {

         if (requestCode == REQUEST_CONTACT && data != null) {
           
              String[] userInfo = parseContactInfo(data);
              if (!TextUtils.isEmpty(userInfo[0])) {
                  final String suspect = userInfo[0];

                  RealmUtil.transaction(new Realm.Transaction() {
                      @Override public void execute(Realm realm) {
                          crime.suspect = suspect;
                      }
                  });
                  btnChooseSuspect.setText(suspect);
              }

              if (!TextUtils.isEmpty(userInfo[1])) {
                  btnCallReport.setText(userInfo[1]);
              }
          }
      }

  // 3. parseContactInfo
  private String[] parseContactInfo(Intent data) {

          String[] userInfo = new String[2];

          if (data == null) {
              return userInfo;
          }

          Uri contactUri = data.getData();

          // 1> 查询联系人 id 和 name
          String[] queryFields = new String[] {
              ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME
          };

          // 2> 获取查询结果
          Cursor cursor = context.getContentResolver()
              .query(contactUri, queryFields, null, null, null);

          Cursor phoneCursor = null;

          try {
              // Double-check that you actually got results
              if (cursor == null || cursor.getCount() == 0) {
                  return userInfo;
              }

              cursor.moveToFirst();  // 游标移动到第一行
              // 获取第0列数据，对应前面 queryFields#ContactsContract.Contacts._ID
              String contactId = cursor.getString(0);
              String name = cursor.getString(1);
              userInfo[0] = name;

              String phoneNumber;

              // 3> 使用 id 查询电话号码
              phoneCursor = context.getContentResolver()
                  .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, // uri
                      // 查询的列
                      new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
                      // 查询条件
                      ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                      // 条件参数
                      new String[] { contactId },
                      // 是否排序
                      null);

              if (phoneCursor == null || phoneCursor.getCount() == 0) {
                  return userInfo;
              }

              phoneCursor.moveToFirst();
              phoneNumber = phoneCursor.getString(0);
              userInfo[1] = phoneNumber;

              return userInfo;

          } finally {
              IOUtil.close(cursor);
              IOUtil.close(phoneCursor);
          }
      }
  ```

- 联系人应用返回包含在 intent 中的 URI 数据给父 activity 时，会添加一个Intent.FLAG_GRANT_READ_URI_PERMISSION 标志，该标志告诉 Android 我们的父 Activity 可以使用联系人数据库一次。

- 检查意图是否可用：

  ```java
  /**
    * 检查意图是否可用
    */
  public static boolean checkIntentAvailable(Intent intent) {
      PackageManager packageManager = Common.getContext().getPackageManager();
      // 只匹配清单文件中带 CATEGORY_DEFAULT 标志的 activity
      return packageManager.resolveActivity(intent,
                   PackageManager.MATCH_DEFAULT_ONLY) != null;
  }
  ```

- 使用 ShareCompat.IntentBuilder 来进行分享

  ```java
  ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(context);
  builder.setType("text/plain");    
  builder.setText(extraText);
  builder.setSubject(extraSubject);
  builder.setChooserTitle(chooser);
  context.startActivity(builder.createChooserIntent());
  ```

## 第16章：使用 Intent 拍照

#### Android 存储相关知识   

> 注意，以下存储路径可能由于各个 ROM 的不同而改变

- 内部存储：

  - 特点：默认是只能被你的 app 访问、用户卸载 app 是数据会被清除、确保不被其他 app 访问的最佳存储区域。

    ```java
    context.getFilesDir();   // /data/data/pacgage_name/files
    context.getCacheDir();   // /data/data/package_name/cache

    // 一个 FileInputStream 位置 /data/data/package_name/files/name
    context.openFileInput(String name);

    // 一个 FileOutputStream 位置 /data/data/package_name/files/name
    // mode: 
    // MODE_PRIVATE: 默认模式，仅自己的 App 可以访问
    // MODE_APPEND: if the file already exists then write data to the end of the existing file instead of erasing it.
    context.openFileOutput(String name, int mode)  
      
    // /data/data/package_name/name
    context.getDir(String name, int mode)
    ```

- 外部存储

  - 私有存储

  -  特点：相对私有、App 卸载时文件会被清除

      ```java
      // /sdcard/Android/date/package_name/cache
      // 非常适合存放缓存数据
      context.getExternalCacheDir()

      // /sdcard/Android/date/package_name/files/
      context.getExternalFilesDir(String type)  
      // type 用来指定数据类型，例如 Environment.DIRECTORY_MUSIC
      // -> /sdcard/Android/date/package_name/files/Music
      // type 为 null 时, 目录为: /sdcard/Android/date/package_name/files
      ```

  - 公共存储

    - 特点：完全对用户和其他应用可见、App 卸载后数据不会被清除

      ```java
      // sdcard根目录
      Environment.getExternalStorageDirectory()
      // sdcard根目录/Music
      Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)

      // 以上两个 API 权限完全相同
      // 区别是 getExternalStoragePublicDirectory(String type) 必须指定一个 type 指定存储类型 
      ```


- files 与 cache 的区别

  如果手机的内部存储空间不够了，会自行选择 cache 目录进行删除，因此，不要把重要的文件放在 cache 文件里面。

  清除数据：删除 files  和 cache 下所有的数据

  清除缓存：只删除 cache 下的数据

- type 的作用

  让系统正确对待你的文件，例如：以 Environment.DIRECTORY_MUSIC 类型保存的文件系统会认为是音乐

- 获取磁盘缓存

  ```java
     /**
       * 获取磁盘缓存
       *
       * @param name 缓存的文件或者目录名称
       * @return sd卡可用路径为   /sdcard/Android/data/package_name/cache/fileName
       *       sd卡不可用路径为 /data/data/package_name/cache/fileName
       */
      public static File getDiskCache(String name) {
          String cachePath;
          if (!isAvailable()) {
              cachePath = Common.getContext().getCacheDir().getAbsolutePath();
          } else {

              StringBuilder sb = new StringBuilder();
              File file = Common.getContext().getExternalCacheDir();

              // In some case, getExternalCacheDir will return null
              if (file != null) {
                  sb.append(file.getAbsolutePath());
              } else {
                  sb.append(getSDCardPath())
                      .append("Android/data/")
                      .append(Common.getContext().getPackageName())
                      .append("/cache");
              }

              cachePath = sb.toString();

              File cacheFile = new File(cachePath);
              if(!cacheFile.exists()) {
                  if(!cacheFile.mkdirs()) {
                      return Common.getContext().getCacheDir();
                  }
              }
          }

          return new File(cachePath + File.separator + name);
      }
  ```

- 仅在 api 18 及以下使用 READ_EXTERNAL_STORAGE 权限
```xml
  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"
          android:maxSdkVersion="18"
          />
```

#### 使用相机及 Android 7.0 适配

- 使用相机拍照

  ```java
  // 1. 打开相机
  // ACTION_IMAGE_CAPTURE 这个属性默认只支持拍摄缩略图，并且图片会保存在 onActivityResult 返回的 Intent 中
  Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

  Uri uri;
  // 适配 Android 7.0
  if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
    uri = Uri.fromFile(crimePhotoFile);
  } else {
    uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".crime_images", crimePhotoFile);
  }

  // 使用 EXTRA_OUTPUT 才能获取全尺寸图片，图片会保存到文件系统中，并对应一个 uri
  // 也就是这个 uri 要适配 Android 7.0
  photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
  startActivityForResult(photoIntent, REQUEST_PHOTO);

  // 2. onActivityResult
  @Override public void onActivityResult(int requestCode, int resultCode, final Intent data) {
      if(requestCode == REQUEST_PHOTO
            && resultCode == Activity.RESULT_OK) {
        // 操作图片文件 crimePhotoFile
      }
  }
  ```

- Android 7.0 FileProvider 适配

  在 targetSdkVersion >= 24，运行在 Android 7.0 及以上设备上时，使用 uri = Uri.fromFile(crimePhoto); 会触发 **FileUriExposedException** 异常。原因是从 Android 7.0 开始，不再允许通过 **file://** URI 访问其他应用的私有目录文件或者让其他应用访问自己的私有目录文件，同时也不运行我们在应用中使用包含 **file://** URI 的 Intent 离开自己的应用。   

  适配步骤： 

  1、在 AndroidManifest.xml 注册 FileProvider

  ```xml
  <provider
    android:name="android.support.v4.content.FileProvider"
    android:authorities="${applicationId}.crime_images"
    android:exported="false"
    android:grantUriPermissions="true">  
      <meta-data
      android:name="android.support.FILE_PROVIDER_PATHS"
      android:resource="@xml/provider_takephoto" />
  </provider>
  ```

  android:authorities 属性在代码中会用到。它的值是一个由 build.gradle 文件中的 **applicationId** 值和自定义的名称组成的 Uri 字符串（约定俗成）。

  android:grantUriPermissions  是否授予 URI 临时访问权限。

  2、res/xml/provider_takephoto.xml

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <paths>
      <external-files-path
          name="crime_images"     // name 别名，生成 uri 时用来指代 path 
          path="crime_images"/>   // path 用于指定共享目录，不能是文件
  </paths>
  ```

  paths 元素必须包含一到多个子元素, 用于指定共享目录，必须是这些元素之一：  
  - <files-path>：内部存储空间应用私有目录下的 files/ 目录，等同于 Context.getFilesDir() 所获取的目录路径；  
  - <cache-path>：内部存储空间应用私有目录下的 cache/ 目录，等同于 Context.getCacheDir() 所获取的目录路径；
  - <external-path>：外部存储空间根目录，等同于 Environment.getExternalStorageDirectory() 所获取的目录路径；
  - <external-files-path>：外部存储空间应用私有目录下的 files/ 目录，等同于 Context.getExternalFilesDir(null) 所获取的目录路径；
  - <external-cache-path>：外部存储空间应用私有目录下的 cache/ 目录，等同于 Context.getExternalCacheDir()；

  3、生成 uri

  ```java 
  Uri getUriForFile(Context context, String authority, File file)
  // context:可以使用 ApplicationContext
  // authority:前面 AndroidManifest 中 provider 下声明的 authorities
  // file:共享文件，一定要位于前面 path 指定的目录下

  // 举一个栗子
  Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".crime_images", crimePhoto);
  // uri:content://com.ssyijiu.criminalintent.crime_images/crime_images/IMG_6ca2e635-b0be-425e-84cf-61b21fdc781f.jpg
  // 是不是很熟悉，和 ContentProvider 的 uri 格式一样
  // 其实 FileProvider 就是继承了 ContentProvider
  ```


- Bitmap 压缩

  ```java
  public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
      // Read in the dimensions of the image on disk
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeFile(path, options);

      float srcWidth = options.outWidth;
      float srcHeight = options.outHeight;

      // Figure out how much to scale down by
      int inSampleSize = 1;
      if (srcHeight > destHeight || srcWidth > destWidth) {
          if (srcWidth > srcHeight) {
              inSampleSize = Math.round(srcHeight / destHeight);
          } else {
              inSampleSize = Math.round(srcWidth / destWidth);
          }
      }

      options = new BitmapFactory.Options();
      options.inSampleSize = inSampleSize;

      // Read in and create final bitmap
      return BitmapFactory.decodeFile(path, options);
  }
  ```

- imageView.setImageBitmap(null);  将 ImageView 设置的图片去掉

- uses-feature 标签，位于 manifest 节点下
```xml
    // 告诉 Android 系统该应用需要使用相机
    // 如果设备缺少相机，类似 Google Play 商店的安装长袖会拒绝安装应用。
    <uses-feature android:name="android.hardware.camera"
            // 让 Android 系统知道，即使设备没有相机，也不影响使用
            // 感觉和 uses-feature 冲突呢 。。
            android:required="false" 
            />
```





- 使用视图树在 Activity 的 onCreate 方法中获取 View 的宽高

  ```java
  final ViewTreeObserver observer = view.getViewTreeObserver();
  observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override public void onGlobalLayout() {
          int width = view.getWidth();
          int height = view.getHeight();
          if (observer.isAlive()) {
              observer.removeOnGlobalLayoutListener(this);
          }
      }
  });
  ```

  ​