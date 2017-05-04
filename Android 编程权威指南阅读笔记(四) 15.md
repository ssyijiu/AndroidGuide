# Android 编程权威指南阅读笔记

## 第15章：隐式 Intent 

- 使用隐式 intent，只需要告诉操作系统我们想要做什么，操作系统就会去启动能够胜任工作任务的activity。如果找到多个符合的activity，用提供一个可选的应用列供用户选择。

- 隐式 Intent 的组成：
  - action：要执行的动作。例如，要访问某个 URL，可以使用 `Intent.ACTION_ VIEW` ，要发行邮件，可以使用 `Intent.ACTION_SEND`。
  - data：携带的数据
    - mimeType 数据类型
    - scheme 数据协议 

- 隐式 Intent 也可以使用 putExtra 来传递数据

- ```java
  // 设置意图为 ACTION_SEND, 向其他 Activity 发送数据
  Intent intent = new Intent(Intent.ACTION_SEND);

  intent.setType("text/plain");  // 设置 mimeType 
  intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());  // 设置文本

  String subject = getString(R.string.crime_report_subject);
  intent.putExtra(Intent.EXTRA_SUBJECT, subject); // 设置主题
  //  Intent.createChooser(intent, subject) 设置选择器标题
  startActivity(Intent.createChooser(intent, subject));  
  ```

  效果如图：

  ![](http://obe5pxv6t.bkt.clouddn.com/send_intent.jpg)

