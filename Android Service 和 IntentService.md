## Android Service 和 IntentService

### IntentService 源码解析

 #### Service 可以做耗时操作吗

不可以，Service 的生命周期周期是在主线程调用的，如果想在 Service 的 onCreate、onStartCommand 方法中做耗时操作，请放在子线程来完成。

#### IntentService 是什么

IntentService 继承 Service，里面封装了 HandlerThread，使用 IntentService 可以非常简单的在 Service 中的耗时操作。

#### IntentService 简单使用

```java
public class PollService extends IntentService {

    // 构造方法
    public PollService() {
        super("PollService");
    }

    // 重写 onHandleIntent 方法，这个方法通过 startService 来调用
  	// intent 是 startService(intent) 传过来的 intent
    // 这个方法在子线程回调，可以直接在里面进行耗时操作
    @Override protected void onHandleIntent(@Nullable Intent intent) {
      	// 	do you things
    }
}

// 开启方法和 Service 一样
startService(new Intent(this,PollService.class))；

```

#### IntentService 源码分析

```java
public abstract class IntentService extends Service {
    private volatile Looper mServiceLooper;    		  // 子线程的 Looper
    private volatile ServiceHandler mServiceHandler;  // 子线程的 Handler
    private String mName;    // 子线程 name
    private boolean mRedelivery;  
  
    // 定义 ServiceHandler
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // 回调 onHandleIntent 处理耗时操作
            onHandleIntent((Intent)msg.obj);
            // 处理完成后停止服务
            stopSelf(msg.arg1);
        }
    }

    // 构造方法
    public IntentService(String name) {
        super();
        mName = name;
    }

    // 设置 Redelivery 是否生效
    // enabled 为 true onStartCommand 返回 START_REDELIVER_INTENT
    // enabled 为 false onStartCommand 返回 START_NOT_STICKY
    public void setIntentRedelivery(boolean enabled) {
        mRedelivery = enabled;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 创建 HandlerThread
        HandlerThread thread = new HandlerThread("IntentService[" + mName + "]");
        // 准备 HandlerThread 的 Looper
        thread.start();
        // 获取 HandlerThread 的 Looper 并赋值给 mServiceLooper
        mServiceLooper = thread.getLooper();
        
        // 创建 ServiceHandler，并绑定子线程的 Looper
        // 这样 ServiceHandler 的 handleMessage 就会在子线程回调了
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        // 创建 Message，并将 intent 作为 msg.obj 发送出去
        // 之后 mServiceHandler 会回调 onHandleIntent((Intent)msg.obj) 来执行我们的操作
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        msg.obj = intent;
        mServiceHandler.sendMessage(msg);
    }

   
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        onStart(intent, startId);
        return mRedelivery ? START_REDELIVER_INTENT : START_NOT_STICKY;
    }

  
    // onDestroy 中退出 Looper
    @Override
    public void onDestroy() {
        mServiceLooper.quit();
    }

    @Override
    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    @WorkerThread
    protected abstract void onHandleIntent(@Nullable Intent intent);
}

```

#### 总结

我们使用 startService(new Intent(this,IntentService.class))；来开启 IntentService.class，重写 onHandleIntent 来执行我们自己的操作，在 onHandleIntent 完成后 IntentService 会自动调用 stopSelf 来停止服务。



