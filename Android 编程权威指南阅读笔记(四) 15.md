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

  ![](http://obe5pxv6t.bkt.clouddn.com/send_intent.jpg)






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

- Android 存储相关知识   

  - 内部存储：

    - 特点：默认是只能被你的 app 访问、用户卸载 app 是数据会被清除、确保不被其他 app 访问的最佳存储区域。

    - ```java
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

      - 特点：相对私有、App 卸载时文件会被清除

      ```java
      // /sdcard/Android/date/package_name/cache
      // 非常适合存放缓存数据
      context.getExternalCacheDir()
        
      // /sdcard/Android/date/package_name/files/
      context.getExternalFilesDir(String type)  
      // type 用来指定数据类型，例如 Environment.DIRECTORY_MUSIC
      // -> /sdcard/Android/date/package_name/files/Music
      ```

      ​

    - 公共存储

      - 特点：完全对用户和其他应用可见、App 卸载后数据不会被清除

        ```java
        // sdcard根目录
        Environment.getExternalStorageDirectory()
        // sdcard根目录/Music
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)

        // 以上两个 API 权限完全相同. 只不过 getExternalStoragePublicDirectory(String type) 必须指定一个 type 指定存储类型 
        ```

- files 与 cache 的区别

  如果手机的内部存储空间不够了，会自行选择 cache 目录进行删除，因此，不要把重要的文件放在 cache 文件里面。

  清除数据：删除 files  和 cache 下所有的数据

  清除缓存：只删除 cache 下的数据

- type 的作用

  让系统正确对待你的文件，以 Environment.DIRECTORY_MUSIC 类型保存的文件系统会认为是音乐

