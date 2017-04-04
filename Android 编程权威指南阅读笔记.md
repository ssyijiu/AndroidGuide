# Android 编程权威指南阅读笔记

## 第1章：Android 开发初体验 

- 根元素，也有父视图(View)——Android提供该父视图来容纳应用的整个视图层级结构。

- setTitle(String/ResID) 设置 Activity 的标题

- Command + F10：Instant Run

- Control + R：Run

- Android 编译过程

  ![](http://obe5pxv6t.bkt.clouddn.com/Android%20%E7%BC%96%E8%AF%91%E8%BF%87%E7%A8%8B.png)

  (1) 使用 aapt 将资源文件、清单文件编译打包

  (2) Java 源文件(.java) -> Java 字节码(.class) -> Dalvik 字节码(.dex)

  (3) 将已编译好的资源和 dex 签名打包成 apk。


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

- 覆盖 Activity 的生命周期时，在onCreate(...)方法里，必须首先调用超类的实现方法，然后再调用其他方法，而在其他几个方法中，是否首先调用超类方法就不那么重要了。

- onPause：Activity 仍然可见，但是不能与用户交互，如：弹出 Dialog 的时候。

- onStop：Activity 没有被销毁，但是不可见，如：按下 Home 键。

- LogCat 的 No Filters 选项控制只显示系统输出信息。


  ​		
  ​	

  ​		
  ​	