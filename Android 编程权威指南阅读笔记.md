# Android 编程权威指南阅读笔记

## 第1章：Android 开发初体验 

- 根元素，也有父视图(View)——Android提供该父视图来容纳应用的整个视图层级结构。

- setTitle(String/ResID) 设置 Activity 的标题

- Command + F10：Instant Run

- Control + R：Run

- Android 编译过程
![](http://obe5pxv6t.bkt.clouddn.com/android%20%E6%89%93%E5%8C%85%E8%BF%87%E7%A8%8B.png)

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

  ![](http://obe5pxv6t.bkt.clouddn.com/Android-MVC.png)

- 思考：这种模型看起来非常不错，但是由于 xml 文件的功能太弱，自己无法独立承担视图层的任务，更多的时候需要 Activity/Fragment 的帮助来控制各个 View 的状态，于是 Activity/Fragment 的代码的会变得异常臃肿，导致后期维护及其困难。而 [MVP](https://github.com/googlesamples/android-architecture) 的出现大的缓解了这个问题。

- TextView 及其子控件可以设置旁边的图片和间距。	
    - android:drawableRight="@drawable/arrow_right"     
    - android:drawablePadding="4dp"


## 第3章：Activity 的生命周期
> [官方文档](https://developer.android.google.cn/guide/components/activities.html)

![](http://obe5pxv6t.bkt.clouddn.com/android-lifecycle.png)
- 覆盖 Activity 的生命周期时，先调用超类的实现方法，然后再调用其他方法。

- onPause：Activity 仍然可见，但是不能与用户交互，如：弹出 Dialog 的时候。

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
- 在 onCreate 中恢复数据，if(null != savedInstanceState) {} 。
- 暂存 Activity：当 Activity 因为内存不足被系统杀死，这个过程可能不会调用 onStop() 和 onDestroy()，此时该 Activity 进入暂存状态，我们需要在 onSaveInstanceState() 保存用户数据，在 onPause() 处理一些其他事情。另外 onPause() 不应该做太多事情，这会妨碍向下一个 Activity 的跳转并拖慢用户体验。
- 系统不会杀死前台 Activity ，所以 Activity 在进入暂存状态之前一定会调用 onPause() 方法。
- 暂存 Activity 可以保存多久：系统重启或者长时间不用这个 Activity，暂存 Activity 会被清除。
- 模拟 Actiivty 被后台强杀：设置 -> 开发者选项 -> 开启不保留活动，点击 Home 键回到桌面，当前 Activity 被后台强杀。
- - - - --
- e(String tag, String msg, Throwable tr) 与 e.printStackTrace() 相比仅仅是打印的时候多出了 tag 和 msg。

## 第4章：Android 应用的调试
- 设置异常断点：Run -> View Breakpoints -> + -> Java Exception Breakpoints -> RuntimeException
- Lint：Analyze -> Inspect Code

## 第5章：第二个 Activity
- 显示横屏预览 ![](http://obe5pxv6t.bkt.clouddn.com/landscape.png)  
- 

  ​		
  ​	

  ​		
  ​	