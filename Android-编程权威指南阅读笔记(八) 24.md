# Android 编程权威指南阅读笔记

## 第24章 Handler、Looper、HandlerThread 

### 一. What、Handler 是什么

Handler 与 MessageQueue、Message、Looper 一起构成了 Android 的消息机制，Android 系统通过大量的消息来与用户进行交互，View 的绘制、点击事件、Activity 的生命周期回调等都作为消息由主线程的 Handler 来处理。

Handler 在消息机制中的作用是：发送和处理消息。

Handler 还有另一个重要的作用，跨线程通信。最常见的就是子线程请求网络，然后使用 Handler 将请求到的数据 post 到主线程刷新 UI，大名鼎鼎的 Retrofit 也是这么做的。

### 二. How、如何使用 Handler

### 三. Why、Handler 消息机制的原理

### 四. 避免 Handler 的内存泄漏

### 五. HandlerThread 的使用及原理 

- HandlerThread 源码解读

  - HandlerThread 继承了 Thread，本质是一个线程。

  - 构造方法

    ​

###     



http://gityuan.com/2015/12/26/handler-message-framework/

https://blog.piasy.com/2017/01/12/Android-Basics-Handler/

