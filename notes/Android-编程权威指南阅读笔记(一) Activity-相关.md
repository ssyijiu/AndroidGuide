> 版权声明：  本文来自[书生依旧](http://www.jianshu.com/p/6212c59ab59d)简书，转载请注明出处。

一本基础书，最近半年一直研究 MVP、RxJava、Retrofit 相关的东西，复习下基础。

## 第1章：Android 开发初体验 

- 根元素也有父视图(View)——Android提供该父视图来容纳应用的整个视图层级结构。

- setTitle(String/ResID) 设置 Activity 的标题

- Command + F10：Instant Run

- Control + R：Run

- Android 编译过程
  ![](http://upload-images.jianshu.io/upload_images/1342220-e3cded99a36184ad.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

        1、使用 aapt 打包资源文件，生成 R.java 文件。
        2、使用 aidl 工具处理 aidl 文件，生成相应  java 文件。
        3、使用 javac 编译工程源代码，生成相应 class 文件。
        4、使用 dx 转换所有 class 文件，生成 classes.dex 文件。
        5、使用 apkbuilder 将资源文件、 dex 文件、so 文件打包生成未签名的 apk。
        6、使用 jarsigner 对 apk 文件进行签名。
        7、使用 zipalign 对签名后的 apk 文件进行对齐处理。

- setContentView() 使用 LayoutInflater 实例化布局文件中的 View 对象。   

## 第2章：Android 与 MVC 设计模式

- M：模型对象，存储和管理应用的数据以及对应的业务逻辑，不关心界面。

- V：视图对象，负责屏幕试图的绘制以及响应用户的输入，由 xml 中的各种组件构成。

- C：控制对象，视图对象与模型对象的纽带，响应视图对象触发的各种事件，管理模型对象与视图之间的数据流动，有 Activity、Fragment 等构成。

  ![](http://upload-images.jianshu.io/upload_images/1342220-d8e0740359deb521.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 思考：这种模型看起来非常不错，但是由于 xml 文件的功能太弱，自己无法独立承担视图层的任务，更多的时候需要 Activity/Fragment 的帮助来控制各个 View 的状态，于是 Activity/Fragment 的代码的会变得异常臃肿，导致后期维护及其困难。而 [MVP](https://github.com/googlesamples/android-architecture) 的出现大的缓解了这个问题。

- TextView 及其子控件可以设置旁边的图片和间距。
   - android:drawableRight="@drawable/arrow_right"     
   - android:drawablePadding="4dp"


## 第3章：Activity 的生命周期
> [官方文档](https://developer.android.google.cn/guide/components/activities.html)

![](http://upload-images.jianshu.io/upload_images/1342220-3e2b542a02994dc8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
- 覆盖 Activity 的生命周期时，先调用超类的实现方法，然后再调用其他方法。

- onPause：Activity 仍然可见，但是不能与用户交互，如：打开一个 Dialog 主题的 Activity 的时候。

- onStop：Activity 没有被销毁，但是不可见，如：按下 Home 键、打开一个新的 Activity。
- 当用户完成当前 Activity 并按“返回”按钮时，系统会从堆栈中将其弹出（并销毁），然后恢复前一个Activity。
- isFinishing()：判断当前 Activity 是否 finish，“返回键”出栈或者调用 finish() 方法会返回 true，横竖屏切换返回、当系统为了恢复内存而销毁某个 Activity 返回 false。
- LogCat 的 No Filters 选项控制只显示系统输出信息。
### 设备配置与备选资源
> [提供备选资源](https://developer.android.google.cn//guide/topics/resources/providing-resources.html#AlternativeResources)

- 设备配置是用来描述设备当前状态的一系列特征，包括：屏幕方向、屏幕密度、屏幕尺寸、键盘类型、底座模式、以及语言等等。   
- 通常，为了匹配不同的设备配置，应用会提供不同的备选资源，例如屏幕适配和字符串的国际化。   
- 在应用运行时如果设备配置发生改变，可能会有更合适的资源来匹配新的设别配置，此时 Android 会自动发现并使用它。   
- 为 Activity 提供横竖屏不同的布局：新建 layout-land 文件夹，在里面新建横屏布局 xml 文件，保证文件名与竖屏相同，在 Acitivty 中判断下横竖屏然后分别处理不同布局的 View。    
- Android可以自动完成最佳匹配资源的调用，但前提是它必须过新建一个 Activity 来实现。在应用运行时，只要设备配置发了改变，Android就会销毁当前的 Activity，然后再创建新的 Activity。因此在横竖屏切换时 默认会改变Activity的生命周期： 先销毁再重新创建。    
- 横竖屏切换时不改变 Activity 生命周期的办法：
    - 写死横竖屏
    - android:configChanges="orientation|keyboardHidden|screenSize"     
        orientation：在屏幕方向改变时不去寻找最佳设备配置  
        keyboardHidden：计算屏幕宽高时候隐藏软键盘（键盘也会改变屏宽高从而触发系统寻找最佳设备配置 ）   
        screenSize：屏幕尺寸更改时不去寻找最佳设备配置      
    - 注意：上面两种方法都会使设置横竖屏不同的布局无效。

### 设备旋转前保存数据
-  在 onSaveInstanceState(Bundle outState) 保存数据，这个方法在 onPause() 和 onStop() 之间调用（Android 5.0），Activity 因内存不足被系统杀死时，这个方法也会被调用。
-  在 onCreate 中恢复数据，if(null != savedInstanceState) {} 。
-  也可以在 onRestoreInstanceState 中恢复数据，这个方法在 onStart 和 onResume 之间调用。
-  暂存 Activity：当 Activity 因为内存不足被系统杀死，这个过程可能不会调用 onStop() 和 onDestroy()，此时该 Activity 进入暂存状态，我们需要在 onSaveInstanceState() 保存用户数据，在 onPause() 处理一些其他事情。另外 onPause() 不应该做太多事情，这会妨碍向下一个 Activity 的跳转并拖慢用户体验。
-  系统不会杀死前台 Activity ，所以 Activity 在进入暂存状态之前一定会调用 onPause() 方法。
-  暂存 Activity 可以保存多久：系统重启或者长时间不用这个 Activity，暂存 Activity 会被清除。
-  模拟 Actiivty 被后台强杀：设置 -> 开发者选项 -> 开启不保留活动，点击 Home 键回到桌面，当前 Activity 被后台强杀。

### 几个常见的生命周期变化

- Activity_A -> Activity_B 生命周期：

  -  先打开 A
  -  A：onCreate -> onStart -> onResume 
  -  再打开 B
  -  A：onPause
  -  B：onCreate -> onStart -> onResume
  -  A：onSaveInstanceState -> onStop
  -  再次点击返回，回到 A
  -  B：onPause
  -  A：onRestart -> onStart -> onResume
  -  B：onStop -> onDestroy

- 横竖屏切换生命周期：

   -  打开 Activity：onCreate -> onStart -> onResume
   -  横竖屏切换：onPause -> **onSaveInstanceState** -> onStop -> onDestroy -> onCreate -> onStart -> onRestoreInstanceState -> onResume

- Activity_A -> Activity_B，然后切换横竖屏：

   -  先走 Activity_A -> Activity_B 生命周期
   -  然后走 B 横竖屏切换的生命周期，此时 A 的生命周期不改变
   -  现在 A 处于 onStop，B 处于 onResume，B 处于 A 的上方，此时点击返回
   -  B：onPause
   -  A：onDestroy -> onCreate -> onStart -> **onRestoreInstanceState** -> onResume
   -  B：onStop -> onDestroy

   ----------

   ​

- e(String tag, String msg, Throwable tr) 与 e.printStackTrace() 相比仅仅是打印的时候多出了 tag 和 msg。

## 第4章：Android 应用的调试
- 设置异常断点：Run -> View Breakpoints -> + -> Java Exception Breakpoints -> RuntimeException
- Lint：Analyze -> Inspect Code

## 第5章：第二个 Activity
- 显示横屏预览 ![](http://upload-images.jianshu.io/upload_images/1342220-0c568381245e3b9c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  

- startActivity(Intent) 实际上将请求发送给了操作系统的 ActivityManager。在启动 Activity 前，ActivityManager 会检查指定的 Class 是否在清单文件中配置，如果配置则创建 Activity 并调用其 onCreate 方法，未配置则抛出 ActivityNotFoundException。
    ![](http://upload-images.jianshu.io/upload_images/1342220-0d2544d398c1dcb3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- Intent 是 Android 四大组件与操作系统通信的一种媒介。

  ![](http://upload-images.jianshu.io/upload_images/1342220-0f407be5c625d682.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- ​Intent.putExtra(...) 返回 Intent 本身，可以进行链式调用。

- startActivityForResult() 在子 Activity 中直接点击返回键，父 Activity 的 onActivityResult 中的 resultCode 值为 RESULT_CANCELED = 0，子 Activity 可以使用 setResult() 来改变父 Activity 的 onActivityResult 中的 resultCode 值和向父 Activity 传递数据。

- startActivityForResult 最佳实践：
    ```java
    // 1. 子 Activity 声明 newIntent 方法
    public static Intent newIntent(Context context, boolean answer) {
            Intent intent = new Intent(context, CheatActivity.class);
            intent.putExtra(EXTRA_ANSWER_IS_TRUE, answer);
            return intent;
    }

    // 2. 父 Activity startActivityForResult
    boolean answer = questions[currentQuestionIndex].answer;
    Intent intent = CheatActivity.newIntent(context, answer);
    startActivityForResult(intent, REQUEST_CODE_CHEAT);

    // 3. 子 Activity 解析数据、设置返回数据、提供返回数据解析
    // (1) 解析数据   
    answer = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
    // (2) 设置返回数据    
    setResult(RESULT_OK, new Intent().putExtra(EXTRA_ANSWER_SHOW, isAnswerShow));
    // (3) 提供返回数据解析    
    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOW, false);
    }
    // 4. 父 Activity 的 onActivityResult
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data != null) {
                isCheat = CheatActivity.wasAnswerShown(data);
            }
        }
    }
    -------------------------------------------------------
    // 1、2 步可以改为
    public static void startForResult(Activity context, int requestCode, boolean answer) {
        Intent intent = new Intent(context, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answer);
        context.startActivityForResult(intent, requestCode);
    }
    CheatActivity.startForResult(this, REQUEST_CODE_CHEAT, answer);   
    ```

- 从桌面点击应用图标时，并不是启动整个应用，仅仅是启动 Launcher Activity。

- ActivityManager 维护着一个非特定应用独享的回退栈，所有 Activity 共享这个回退栈。



## 第6章：Android SDK 版本与兼容

- 以最低版本设置值为标准，系统会拒绝将应用安装在低于标准版本的设备上。
- 版本适配：if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {}
- Build.MODEL：手机型号
- Build.VERSION.RELEASE：Android 版本号
- Build.VERSION.SDK_INT：API 级别
