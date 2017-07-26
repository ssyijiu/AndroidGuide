## Android Handler 消息机制详述  

### 一. What、Handler 是什么

Handler 与 Message、MessageQueue、Looper 一起构成了 Android 的消息机制，Android 系统通过大量的消息来与用户进行交互，View 的绘制、点击事件、Activity 的生命周期回调等都作为消息由主线程的 Handler 来处理。

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


### 三. Handler 的内存泄漏

不得不说，上面使用 Handler 的方法会有内存泄漏的风险

- Handler 内存泄漏的两个原因

  - Java 中非静态内部类和匿名内部类会持有外部类的引用

    ```java
    // 这是一个外部类 Handler 不会持有外部类引用
    // 显然 handleMessage 没地方写了
    Handler handler = new Handler();

    // 重写 handleMessage 后将得到一个内部类 Handler，以内 handleMessage 是在外部类中实现的
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

  - Handler 的生命周期比外部类长。

- 分析

  - 非静态的内部 Handler 子类、匿名 Handler 子类会持有外部类的引用(Activity)，而 Handler 可能会因为要等待处理耗时操作导致存活时间超过 Activity，或者消息队列中存在未被 Looper 处理的 Message ，而 Message 会持有 Handler 的引用。于是，在 Activity 退出时，其引用还是被 Handler 持有，导致 Activity 无法被及时回收，造成内存泄露。
  - 非静态的内部 Runnable 子类、匿名 Runnable 子类 post 到任意 Handler 上时，Runnable 其实是 Massage中的 Callback，持有 Message 引用，如果这个 Massage 在消息队列还没有被处理，那么就会造成 Runnable 一直持有外部类的引用而造成内存泄露。

- 解决方案：

  - 通过静态内部类或者外部类来声明 Handler 和 Runnable。
  - 通过弱引用来拿到外部类的变量。
  - 在 Activity/Fragment 销毁的时候请空 MessageQueue 中的消息。

- 代码

  ```java
  // Handler 弱引用封装
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

  // 想重写 handleMessage 的话，要创建静态内部类或者外部类，否则有内存泄漏风险
  private static class MyHandler extends SafetyHandler<MyFragment> {

      MyHandler(MyFragment fragment) {
        	super(fragment);
      }

      @Override public void handleMessage(Message msg) {
          super.handleMessage(msg);
          if(getTarget() != null) {
              MyFragment fragment = getTarget();
              switch (msg.what) {
                 // 操作 fragment
              }
          }
      }
  }

  // 声明 Handler
  MyHandler handler = new MyHandler(this);

  // 使用 Handler
  handler.sendMessage() ...
    
  // onDestroy   
  @Override public void onDestroy() {
    	super.onDestroy();
    	handler.removeCallbacksAndMessages(null);
  }
  ```

  ​

### 四. Why、Handler 消息机制的原理

> 这部分从 ActivityThread 的 main 方法出发，打通整个消息机制的流程，结合源码体验效果更佳。

#### 概述

介绍消息机制的原理前，我们先来看一下 Handler 与 Message、MessageQueue、Looper 这个四个类的作用

- Handler：前面已经说过，Handler 负责发送和处理 Message。
- Message：消息，负责传递标示(what) 和数据(obj) ；每个 Message 都会通过 target 这个成员变量来绑定一个 Handler，由这个 Handler 来发送和处理 Message。
- MessageQueue：消息队列，负责存放有 Handler 发送过来的消息；每个 Handler 中都有一个 final MessageQueue mQueue，Handler 发送消息就是把消息加入这个 MessageQueue 。
- Looper：负责不断的从 MessageQueue 中取出消息然后交给 Handler（Message#target ） 处理；每个 Looper 中都有一个唯一的消息队列（final MessageQueue mQueue），每个 Handler 中都有一个 final Looper mLooper，Handler 中的 MessageQueue 就是来自 Looper。

注意：每个线程只能有一个 Looper 和 一个 MessageQueue，可以有多个 Handler，每个 Handler 可以发送和处理多个 Message。

另外，提到消息机制就不得不说一下 Android 中的主线程（UI 线程）

![](http://obe5pxv6t.bkt.clouddn.com/main_thread.png)

Android 中的主线程通过 Looper.loop() 进入一个无线循环中，不断的从一个 MessageQueue 取出消息，处理消息，我们每触发一个事件，就会向这个 MessageQueue 中添加一个消息，Looper 取出这个消息，Handler 处理这个消息，正是 Looper.loop()  在驱动着 Android 应用运行下去 ，这也是为什么 Looper.loop 为什么不会阻塞住主线程的原因（当然前提是在 ActivityThread 的 main 函数 中调用）。

#### 正式进入源码分析

> 本源码分析基于 API 25，以下源码中删除了一些无关的代码

1、在主线程的入口，ActivityThread 的 main 方法

```java
public static void main(String[] args) {
        // 准备主线程的 Looer		
        Looper.prepareMainLooper();
        // 创建 ActivityThread
        ActivityThread thread = new ActivityThread();
        thread.attach(false);
        // 获取主线程的 Handler 
        if (sMainThreadHandler == null) {
            sMainThreadHandler = thread.getHandler();
        }
  
        // 对消息队列进行无线轮询，处理消息
        Looper.loop();
	    // 一旦跳出循环，抛出异常（Android 不允许跳出主线程的 Looper.loop()）
        throw new RuntimeException("Main thread loop unexpectedly exited");
    }
```

-> Looper.prepareMainLooper()

   ```java
public static void prepareMainLooper() {
        // 准备一个 Looper
        prepare(false);
        synchronized (Looper.class) {
            // main Looper 只能初始化一次，再次初始化会抛出异常
            if (sMainLooper != null) {
                throw new IllegalStateException("The main Looper has already been prepared.");
            }
            // 获取 main Looper
            sMainLooper = myLooper();
        }
    }
   ```

-> prepare(false)

   ```java
// 准备一个 Looper，quitAllowed 是否允许 Looper 中的 MessageQueue 退出
// 默认 prepare() 允许退出，主线程这里不允许退出
private static void prepare(boolean quitAllowed) {
    // 先看下 sThreadLocal 是什么
    // static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<Looper>();
    // ThreadLocal:线程本地存储区，每个线程都有本地存储区域，这个区域是每个线程私有的，不同的线程不能之间不能彼此访问
    // 如果 sThreadLocal 中有数据，抛出异常，换句话说 prepare() 这个函数每个线程只能执行一次
    if (sThreadLocal.get() != null) {
      	throw new RuntimeException("Only one Looper may be created per thread");
    }
    // 创建 Looper 保存到该线程的 ThreadLocal 中
    sThreadLocal.set(new Looper(quitAllowed));
}
   ```

-> new Looper(quitAllowed)

   ```java
private Looper(boolean quitAllowed) {
    // 在 Looper 创建的时候创建一个消息队列
    // quitAllowed:消息队列是否可以退出，主线的消息队列肯定不允许退出，所以上面是 prepare(false)
    // quitAllowed 为 false 执行 MessageQueue#quit 退出消息队列时会出现异常
	mQueue = new MessageQueue(quitAllowed);
    // 获取 Looper 存在于哪个线程
	mThread = Thread.currentThread();
}
   ```

-> sMainLooper = myLooper()

   ```java
public static @Nullable Looper myLooper() {
    // 从 sThreadLocal 中获取当前线程的 Looper 
    // 如果当前线程没有掉用 Looper.prepare 返回 null
	return sThreadLocal.get();
}
   ```

->  sMainThreadHandler = thread.getHandler();

   ```java
final Handler getHandler() {
    // 返回 mH
	return mH;
}

// mH 在成员变量的位置 new H()
final H mH = new H();

// H 继承了 Handler 封装了一系列关于 Acitivty、Service 以及其他 Android 相关的操作
private class H extends Handler 
   ```

总结：在主线程的 main 方法中，会创建主线程的 Looper、MessageQueue，然后进入 Looper.loop() 循环中，不断的取出消息，处理消息，以此来驱动 Android 应用的运行。

2、Handler 的创建，Handler 的所有构造方法都会跳转到下面两个之一

   ```java
public Handler(Callback callback, boolean async) {
    // Hanlder 是匿名类、内部类、本地类时，如果没有声明为 static 则会出现内存泄漏的警告
    if (FIND_POTENTIAL_LEAKS) {
        final Class<? extends Handler> klass = getClass();
        if ((klass.isAnonymousClass() || klass.isMemberClass() || klass.isLocalClass()) &&
            (klass.getModifiers() & Modifier.STATIC) == 0) {
          	Log.w(TAG, "The following Handler class should be static or leaks might occur: " + klass.getCanonicalName());
        }
    }
    
    // 获取 Looper
    mLooper = Looper.myLooper();
    if (mLooper == null) {
      	throw new RuntimeException("Can't create handler inside thread that has not called Looper.prepare()");
    }
    // 消息队列，从 Looper 中获取
    mQueue = mLooper.mQueue;
    // 处理消息的回调接口
    mCallback = callback;
    // 处理消息的方式是否为异步，默认同步
    mAsynchronous = async;
}

public Handler(Looper looper, Callback callback, boolean async) {
    mLooper = looper;
    mQueue = looper.mQueue;
    mCallback = callback;
    mAsynchronous = async;
}
   ```

总结：在 Handler 的构造方法中，Handler 和 Looper、MessageQueue 绑定起来，如果当前线程没有 Looper 抛出异常（这也是为什么直接在子线程创建 Handler 会出现异常）。

3、使用 Handler 发送消息

-> sendMessageAtTime(Message msg, long uptimeMillis)

  ```java
// 除了 sendMessageAtFrontOfQueue，Handler 所有的 post、sendMessage 都会跳到这个方法
// Message msg: 要发送的消息
// long uptimeMillis: 发送消息的绝对时间，通过 SystemClock.uptimeMillis() 加上我们自己的延迟时间 delayMillis 计算而来
public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
    MessageQueue queue = mQueue;
    // 消息队列为空（可能已经退出）返回 false 入队失败
    if (queue == null) {
      RuntimeException e = new RuntimeException(
        this + " sendMessageAtTime() called with no mQueue");
      Log.w("Looper", e.getMessage(), e);
      return false;  
    }
    // 消息入队
    return enqueueMessage(queue, msg, uptimeMillis);
}
  ```

-> sendMessageAtFrontOfQueue(Message msg)

   ```java
// 发送消息到 MessageQueeu 的队头
public final boolean sendMessageAtFrontOfQueue(Message msg) {
    MessageQueue queue = mQueue;
    if (queue == null) {
        RuntimeException e = new RuntimeException(
        this + " sendMessageAtTime() called with no mQueue");
        Log.w("Looper", e.getMessage(), e);
        return false;
    }
    // 通过设置 uptimeMillis 为 0，是消息加入到 MessageQueue 的队头
    return enqueueMessage(queue, msg, 0);
}
   ```

-> enqueueMessage(MessageQueue queue, Message msg, long uptimeMillis)

   ```java
// 所有 Handler 的 post 、sendMessage 系列方法和 runOnUiThread 最终都会调用这个方法
private boolean enqueueMessage(MessageQueue queue, Message msg, long uptimeMillis) {
    // msg.target 是一个 Handler，将 Message 和 Handler 绑定
    // 也就是用哪个 Handler 发送消息，这个 Message 就和哪个 Handler 绑定
    msg.target = this;
    // 如果设置了消息处理方式为异步处理
    if (mAsynchronous) {
        msg.setAsynchronous(true);
    }
    // MessageQueue 的方法，将消息入队
    return queue.enqueueMessage(msg, uptimeMillis);
}
   ```

->  MessageQueue#enqueueMessage(Message msg, long when)

   ```java
boolean enqueueMessage(Message msg, long when) {
        // Messgae 没有绑定 Handler 抛出异常
        if (msg.target == null) {
            throw new IllegalArgumentException("Message must have a target.");
        }
        // Messgae 正在使用 抛出异常
        if (msg.isInUse()) {
            throw new IllegalStateException(msg + " This message is already in use.");
        }
	
        synchronized (this) {
            // 消息队列正在退出，回收 Message
            if (mQuitting) {
                IllegalStateException e = new IllegalStateException(
                        msg.target + " sending message to a Handler on a dead thread");
                Log.w(TAG, e.getMessage(), e);
                msg.recycle();  // 调用 Message#recycleUnchecked() 
                return false;
            }
            msg.markInUse();  // 标记 Message 正在使用
            msg.when = when;  // 设置 Message 的触发时间
          
            // mMessages 记录着 MessageQueue 的队头的消息 
            Message p = mMessages;  
            boolean needWake;
            // MessageQueue 没有消息、Message 触发时间为 0、Messgae 触发时间比队头 Message 早
            // 总之这个 Message 在 MessageQueue 中需要最先被分发
            if (p == null || when == 0 || when < p.when) {
                // New head, wake up the event queue if blocked.
                msg.next = p;     // 将以前的队头 Message 链接在这个 Message 后面
                mMessages = msg;  // 将这个 Message 赋值给 mMessages
                needWake = mBlocked;  // 队列是否阻塞
            } else {
                // 标记队列是否阻塞
                needWake = mBlocked && p.target == null && msg.isAsynchronous();
                Message prev;
              
                // 按照时间顺序将 Message 插入消息队列
                for (;;) {
                    prev = p;   // prev 记录队头
                    p = p.next; // p 记录队头的后一个
                    // 队头后面没有消息或者其触发事件比要插入的 Message 晚，跳出循环
                    if (p == null || when < p.when) {
                        break;
                    }
                    if (needWake && p.isAsynchronous()) {
                        needWake = false;
                    }
                }
                // 将 Message 插入队列
                msg.next = p; 
                prev.next = msg;
            }

            // We can assume mPtr != 0 because mQuitting is false.
            if (needWake) {
                nativeWake(mPtr);
            }
        }
        return true;
    }
   ```

总结：到现在为止，我们的 Handler 已经将 Message 发送到了 MessageQueue，Message 静静的等待被处理。

4、Looper.loop() 还记得这个方法在 ActivityThread 的 main 调用了吗？正是它在不断处理 MessageQueue 里面的消息。

   ```java
 public static void loop() {
        // 获取 Looper.Looper.prepare 准备好的 Looper
        final Looper me = myLooper();
        if (me == null) {
            throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        }
        // 获取 Looper 中的消息队列
        final MessageQueue queue = me.mQueue;
   
   	    // 进入无线循环
        for (;;) {
            // 取出下一条消息
            Message msg = queue.next(); 
            
            // 没有消息，退出 loop
            // 其实上面 queue.next() 也是一个无限循环，获取到消息就返回，没有消息就一直循环
            if (msg == null) {
                return;
            }

            try {
                // msg.target 实际上就是一个 Handler
                // 获取到了消息，使用绑定的 Handler#dispatchMessage 分发消息
                msg.target.dispatchMessage(msg);
            } finally {
                
            }

            // 释放消息，把 Message 的各个变量清空然后放进消息池中
            msg.recycleUnchecked();
        }
    }
   ```

5、Handler#dispatchMessage(msg) 消息是如何处理的

   ```java
public void dispatchMessage(Message msg) {
    // 1
    if (msg.callback != null) {
    	handleCallback(msg);
    } else {
        // 2
    	if (mCallback != null) {
    		if (mCallback.handleMessage(msg)) {
    			return;
    		}
    	}
        // 3. 看到这个方法没有！就是我们创建 Handler 时重写的 handleMessage
        // OK 整个流程打通！
    	handleMessage(msg);
    }
}
   ```

总结：流程虽然通了，但是处理 Message 的方法貌似有三种(我标记了序号)，而且我们的 handleMessage 的优先级最低，其他方法会在什么情况下执行呢？ 直接说结论了，调用 Handler 的 post 系列方法会走序号1的处理，创建 Handler 传入 Callback 会走序号2 的处理。



Handler 机制总结：想使用 Handler 必须要有 Looper，创建 Looper 的时候会创建 MessageQueue，在 Handler 的构造的时候会绑定这个 Looper 和 MessageQueue，Handler 将 Message 发送到 MessageQueue 中，Looper.loop() 会不断的从 MessageQueue 取出消息再交给这个 Handler  处理。

### 五. HandlerThread 的使用及源码解读 

#### 在子线程中能直接创建 Handler 吗？

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

- 答案前面提到了是不能，执行上面的代码会出现 java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare() 这个异常，异常提示我们，不能再没有调用 Looper.prepare() 的线程中创建 Handler。

- 简单修改下代码就可以了，给线程准备好 Looper

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

- run 方法：创建子线程的 Looper

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

  ### 六.参考文章

  - Android API 25 源码
  - [Android消息机制1-Handler(Java层)](http://gityuan.com/2015/12/26/handler-message-framework/)

