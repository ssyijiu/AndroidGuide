## Android Service 和 IntentService

### IntentService 源码解析

 #### Service 可以做耗时操作吗

不可以，Service 的生命周期周期是在主线程调用的，如果想在 Service 的 onCreate、onStartCommand方法中做耗时操作，请放在子线程来完成。

