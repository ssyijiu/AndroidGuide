# Android 编程权威指南阅读笔记

## 第24章 Handler、Looper、HandlerThread 

### 一. What、Handler 是什么

Handler 与 MessageQueue、Message、Looper 一起构成了 Android 的消息机制，Android 系统通过大量的消息来与用户进行交互，View 的绘制、点击事件、Activity 的生命周期回调等都作为消息由主线程的 Handler 来处理。

Handler 在消息机制中的作用是：发送和处理消息。

Handler 还有另一个重要的作用，跨线程通信。最常见的就是子线程请求网络，然后使用 Handler 将请求到的数据 post 到主线程刷新 UI，大名鼎鼎的 Retrofit 也是这么做的。

### 二. How、如何使用 Handler

- 创建 Handler 

  ```java
  private Handler handler = new Handler() {
    // 重写 handleMessage 来根据不同 what 来处理 Message
    // 这个方法在 Handler 创建的线程执行
    @Override public void handleMessage(Message msg) {
          super.handleMessage(msg);
          switch (msg.what) {
              case 0:
                MLog.i(msg.obj);
                break;
              case 1:
                break;
              default:

          }
      }
  };
  ```

- 创建并发送 Message

  ```java
  // 获取一个 Message
  Message message = Message.obtain();
  message.what = 0;
  message.obj = new Object();
  // 使用 Handler 发送 Message
  // 消息发送完成后 Handler 的 handleMessage(Message msg) 会处理消息
  handler.sendMessage(message);

  // 延迟 1s 发送 Message
  handler.sendMessageDelayed(message, 1000);
  // 发送一个空的 Message
  handler.sendEmptyMessage(msg.what);  
  // 延迟发送一个空的 Message
  handler.sendEmptyMessageDelayed(0, 1000);


  //-----------------还可以这样--------------------

  // 创建 Message 并绑定 Handler
  Message message = handler.obtainMessage();
  message.what = 0;
  message.obj = new Object();
  // 发送 Message
  message.sendToTarget();
  ```

- 使用 Handler 子线程请求数据，主线程刷新 UI

  ```java
  // 1. 在主线程创建 Handler（略）
  // 2. 子线程请求数据，主线程刷新 UI
  new Thread(new Runnable() {
      @Override public void run() {
          // 获取网络数据
          final List<Object> datas = getNetData();
          
          // 方法一：将数据作为 Message 的 obj 发送出去，在 handleMessage 中刷新 UI
          Message msg = Message.obtain();
          msg.what = 1;
          msg.obj = data;
          handler.sendMessage(msg);
        
          // 方法二：直接在 post 中刷新 UI
          handler.post(new Runnable() {
              @Override public void run() {
                	// 使用 datas 刷新 UI
                  // 这个方法也会在 Handler 创建的线程执行
              }
          });
      }
  }).start();
  ```

  ​

### 三. Handler 的内存泄漏

不得不说，上面使用 Handler 的方法会有内存泄漏的风险

- Handler 内存泄漏的两个原因

  - Java 中非静态内部类和匿名内部类会持有外部类的引用

    ```java
    // 这是一个外部类 Handler 不会持有外部类引用
    // 显然 handleMessage 没地方写了
    Handler handler = new Handler();

    // 这是一个内部类 Handler，因为 handleMessage 是在外部中实现的
    // 它持有外部类引用，可能会引起内存泄漏
    Handler handler = new Handler() {
      @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                  MLog.i(msg.obj);
                  break;
                case 1:
                  break;
                default:

            }
        }
    };

    // 这里 Handler 是一个匿名类，但不是内部类
    // Runnable 是一个匿名内部类，持有外部类引用，可能会引起内存泄漏
    new Handler().post(new Runnable() {
        @Override public void run() {
          // ...
        }
    });
    ```

    ​

  - Handler 、Message 的生命周期比外部类长。

- 分析

  - 非静态的内部 Handler 子类、匿名 Handler 子类会持有外部类的引用(Activity)，而 Handler 可能会因为要等待处理耗时操作导致存活时间超过 Activity，或者消息队列中存在未被 Looper 处理的 Message ，而 Message 会持有 Handler 的引用。于是，在 Activity 退出时，其引用还是被 Handler 持有，导致 Activity 无法被及时回收，造成内存泄露。
  - 非静态的内部 Runnable 子类、匿名 Runnable 子类 post 到任意 Handler 上时，Runnable 其实是 Massage中的 Callback，持有 Message 引用，如果这个 Massage 在消息队列还没有被处理，那么就会造成 Runnable 一直持有外部类的引用而造成内存泄露。

- 解决方案：

  - 通过静态内部类或者外部类来声明 Handler 和 Runnable。
  - 通过弱引用来拿到外部类的变量。
  - 在 Activity 销毁的时候请空 MessageQueue 中的消息。

- 封装了一个解决内存泄漏的 Handler

  ```java
  public class SafetyHandler<T> extends Handler {
      /**
       * 外部引用, 例如 Activity, Fragment, Dialog, View 等
       */
      private WeakReference<T> mTargetRef;
      public SafetyHandler() {
      }
      public SafetyHandler(T target) {
          this.mTargetRef = new WeakReference<>(target);
      }
      
      public T getTarget() {
          if (isTargetAlive()) {
              return mTargetRef.get();
          } else {
              removeCallbacksAndMessages(null);
              return null;
          }
      }
    
      public void setTarget(T target) {
          this.mTargetRef = new WeakReference<>(target);
      }
    
      private boolean isTargetAlive() {
          return mTargetRef != null && mTargetRef.get() != null;
      }
  }

  //----------------在 Fragment 中使用方法-------------------------------

  // 创建一个静态内部类
  private static class BeatHandler extends SafetyHandler<BeatBoxFragment> {

      BeatHandler(BeatBoxFragment fragment) {
        	super(fragment);
      }

      @Override public void handleMessage(Message msg) {
          super.handleMessage(msg);
          if(getTarget() != null) {
              BeatBoxFragment fragment = getTarget();
              switch (msg.what) {
                 // 操作 fragment
              }
          }
      }
  }

  // 声明 Handler
  BeatHandler handler = new BeatHandler(this);

  // 使用 Handler
  handler.sendMessage() ...

  ```

  ​

### 四. Why、Handler 消息机制的原理

### 五. HandlerThread 的使用及源码解读 

#### 接触 HandlerThread 前先看这个问题：在子线程中能创建 Handler 吗？

- ```java
  new Thread(new Runnable() {
      @Override public void run() {
          new Handler().post(new Runnable() {
              @Override public void run() {
                	MLog.i("Handler in " + Thread.currentThread().getName());
              }
          });
      }
  }).start();
  ```

- 答案显然是不能，执行上面的代码会出现 java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare() 这个异常，异常提示我们，不能再没有调用 Looper.prepare() 的线程中创建 Handler。

- 简单修改下代码，给线程准备好 Looper

  ```java
  new Thread(new Runnable() {
      @Override public void run() {
          // 准备一个 Looper，Looper 创建时对应的 MessageQueue 也会被创建
          Looper.prepare();
          // 创建 Handler 并 post 一个 Message 到 MessageQueue
          new Handler().post(new Runnable() {
              @Override public void run() {
                	MLog.i("Handler in " + Thread.currentThread().getName());
              }
          });
          // Looper 开始不断的从 MessageQueue 取出消息并再次交给 Handler 执行
          // 此时 Lopper 进入到一个无限循环中，后面的代码都不会被执行
          Looper.loop();
      }
  }).start();
  ```


- 上面的操作 Android 都帮我们封装好了，正是 HandlerThread  这个类。

#### HandlerThread 的简单使用

```java
// 1. 创建 HandlerThread
handlerThread = new HandlerThread("myHandlerThread") {
    // onLooperPrepared 这个方法子线程执行，由线程的 run 方法调用，可以在里面直接创建 Handler
    @Override protected void onLooperPrepared() {
        super.onLooperPrepared();
        new Handler().post(new Runnable() {
            @Override public void run() {
                // 注意：Handler 在子线程创建，这个方法也会运行在子线程，不可以更新 UI
              	MLog.i("Handler in " + Thread.currentThread().getName());
            }
        });
    }
};

// 2. 准备 HandlerThread 的 Looper 并调用 onLooperPrepared
handlerThread.start();


// 3. 退出
@Override public void onDestroy() {
    super.onDestroy();
    handlerThread.quit();
}

//------------也可以这样用----------------

// 1. 创建 HandlerThread 并准备 Looper
handlerThread = new HandlerThread("myHandlerThread");
handlerThread.start();

// 2. 创建 Handler 并绑定 handlerThread 的 Looper
new Handler(handlerThread.getLooper()).post(new Runnable() {
    @Override public void run() {
      	// 注意：Handler 绑定了子线程的 Looper，这个方法也会运行在子线程，不可以更新 UI
      	MLog.i("Handler in " + Thread.currentThread().getName());
    }
});

// 3. 退出
@Override public void onDestroy() {
    super.onDestroy();
    handlerThread.quit();
}
```

####  HandlerThread 源码解读

- HandlerThread 继承了 Thread，本质是一个拥有 Looper 的线程，因此在 HandlerThread  我们可以直接使用 Handler。

- 构造方法

  ```java
  public HandlerThread(String name) {
      super(name);
      mPriority = Process.THREAD_PRIORITY_DEFAULT;
  }

  // 传入线程的名称和优先级
  // 注意 priority 的值必须来自 android.os.Process 不能来自 java.lang.Thread
  public HandlerThread(String name, int priority) {
      super(name);
      mPriority = priority;
  }
  ```

-  run 方法：创建子线程的 Looper

  ```java
  @Override
  public void run() {
      mTid = Process.myTid();
      // 准备一个 Looper
      Looper.prepare();
      synchronized (this) {
          // 获取 Looper
          mLooper = Looper.myLooper();
          // Looper 获取成功后，唤醒 getLooper 的 wait
          notifyAll();
  	}
  	Process.setThreadPriority(mPriority);
      // Looper 准备好的回调，在这个方法里可以使用 Handler 了
      onLooperPrepared();
      // Looper 开始循环取消息
      Looper.loop();
      mTid = -1;
  }
  ```

- getLooper 方法：获取子线程的 Looper

  ```java
  public Looper getLooper() {
      // 线程没有开始或者死亡，返回 null
    	if (!isAlive()) {
        	return null;
      }

      // If the thread has been started, wait until the looper has been created.
      // Looper 的创建时在子线程完成的，而 getLooper 可能会在主线程调用
      // 当 Looper 没有创建完成时，使用 wait 阻塞等待
      // 上面在 Looper 创建好后会 notifyAll 来唤醒 wait
       synchronized(this) {
          while (isAlive() && mLooper == null) {
              try {
                	wait();
              } catch (InterruptedException e) {
              }
          }
      }
      return mLooper;
  }
  ```

- quit 和 quitSafely ：结束 Looper 的运行

  ```java
  // quit
  quit() -> looper.quit() -> mQueue.quit(false);
  // quitSafely 
  quitSafely() -> looper.quitSafely() -> mQueue.quit(true);

  // 这两个方法最终都会调用到 MessageQueue 的 void quit(boolean safe) 方法
  // 前者会直接移除 MessageQueue 中的所有消息，然后终止 MessageQueue
  // 后者会将 MessageQueue 中已有消息处理完成后（不再接收新消息）终止 MessageQueue
  ```

  ​

   

  ​

###     



http://gityuan.com/2015/12/26/handler-message-framework/

https://blog.piasy.com/2017/01/12/Android-Basics-Handler/

http://blog.csdn.net/wzy_1988/article/details/38346637

