# Android 编程权威指南阅读笔记

## 第22章：深入学习 Intent 和任务

- 获取所有 Launcher Activity 信息

  ```java
  public static List<ResolveInfo> getAllLauncher() {
      Intent startupIntent = new Intent(Intent.ACTION_MAIN);
      startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);
      final PackageManager pm = Common.getContext().getPackageManager();
      return pm.queryIntentActivities(startupIntent, 0);
  }

  // 获取 Activity 的 Label
  String label = resolveInfo.loadLabel(pm).toString();

  // 获取 Activity 的 Icon
  Drawable icon = this.resolveInfo.loadIcon(pm);
  ```

- 不区分大小写 String 排序

  ```java
  Collections.sort(strs, new Comparator<String>() {
      public int compare(String a, String b) {
        return String.CASE_INSENSITIVE_ORDER.compare(a,b);
      }
  });
  ```

- 通过 ResolveInfo 打开 Activity

  ```java
  // 获取 ActivityInfo
  ActivityInfo activityInfo = mResolveInfo.activityInfo;

  // 通过包名和类名创建显示 Intent
  Intent i = new Intent();
  i.setClassName(activityInfo.applicationInfo.packageName,activityInfo.name);

  context.startActivity(i);
  ```

- `Intent.FLAG_ACTIVITY_NEW_TASK` 在新的任务栈中启动 Activity，不会重复创建任务栈。

- 将应用设置为可选的主桌面

  ```xml
  <category android:name="android.intent.category.HOME" />
  <category android:name="android.intent.category.DEFAULT" />
  ```

- 任务与进程

  - 每一个 Activity 实例都仅存在与一个进程和一个任务之中。
  - 任务只包含 Activity ，这些 Activity 通常来自不同的应用，进程包包含了应用全部运行代码和对象。
  - Activity 哪个应用的代码则属于哪个进程，和它所在的任务没有关系。



## 第23章：HTTP 与后台任务

- 使用 HttpURLConnection 访问网络

  ```java
  @Nullable
  public byte[] getUrlBytes(String urlSpec) {

      HttpURLConnection connection = null;
      ByteArrayOutputStream out = null;
      InputStream in = null;

      try {
          URL url = new URL(urlSpec);
          connection = (HttpURLConnection) url.openConnection();

          out = new ByteArrayOutputStream();
          in = connection.getInputStream();

          if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException(connection.getResponseMessage() +
                                  ": with " +
                                  urlSpec);
          }

          int bytesRead;
          byte[] buffer = new byte[1024];
          while ((bytesRead = in.read(buffer)) > 0) {
            out.write(buffer, 0, bytesRead);
          }

          return out.toByteArray();
      } catch (IOException e) {
        	return null;
      } finally {
          IOUtil.close(out);
          IOUtil.close(in);
          IOUtil.close(connection);
      }

  }
  ```

  ​

- Androd 中主线程不能访问网络，子线程不能刷新 UI。

- Android 中的主线程处于一个无线循环的运行状态，等待着系统或者用户触发事件，事件触发后，主线程便负责执行代码，以响应这些事件。

  ![](http://obe5pxv6t.bkt.clouddn.com/main_thread.png)

- 构造带参数的 Uri

  ```java
  String uri = Uri.parse("https://api.flickr.com/services/rest/")
              .buildUpon()
              .appendQueryParameter("method", "flickr.photos.getRecent")
              .appendQueryParameter("api_key", "API_KEY")
              .appendQueryParameter("format", "json")
              .appendQueryParameter("nojsoncallback", "1")
              .appendQueryParameter("extras", "url_s")
              .build().toString();

  // uri
  // https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=API_KEY&format=json&nojsoncallback=1&extras=url_s
  ```

- 异步任务结束后刷新 fragment UI 先判断 isAdded

- AsyncTask 的基本使用

  ```java
  public abstract class MeiZhiTask extends AsyncTask<String, Integer, MeiZhi> {

      // String doInBackground 的参数, AsyncTask.execute 的参数，执行前的参数
      // Integer 用于更新进度，在 doInBackground 使用 publishProgress 发送进度
      //         在 onProgressUpdate 接收进度更新 UI
      // MeiZhi doInBackground 的返回 onPostExecute 参数，后台线程的操作结果

    @Override protected MeiZhi doInBackground(String... params) {
          for (String param : params) {
            	MLog.i(param);
          }
          for (int i = 0; i < 20; i++) {
            	publishProgress(i);   // 发送进度，必须在 doInBackground 调用
          }
          MeiZhi meiZhi = null;
          try {
              String result = new HttpUtil().getUrlString(Host.host);
              meiZhi = Gsons.json2Bean(result, MeiZhi.class);
          } catch (IOException e) {
            	MLog.e("Failed to fetch URL: ", e);
          }
          return meiZhi;
      }
    
    // 更新进度
    @Override protected void onProgressUpdate(Integer... values) {
      	super.onProgressUpdate(values);
      	MLog.i(values[0]);
     }

    @Override protected void onPostExecute(MeiZhi meiZhi) {
        super.onPostExecute(meiZhi);
        afterMeiZhi(meiZhi);
    }
    
    protected abstract void afterMeiZhi(MeiZhi meiZhi);
    
  }

  // 使用 MeiZhiTask
  meiZhiTask = new MeiZhiTask() {
    @Override protected void afterMeiZhi(MeiZhi meiZhi) {
        if (isAdded()) {
        	  mRecyclerView.setAdapter(new PhotoAdapter(meiZhi.results));
        }
    }
  };
  meiZhiTask.execute("ssyijiu", "android");

  // onDestroy 取消异步任务
  @Override public void onDestroy() {
    super.onDestroy();
    meiZhiTask.cancel(false);
  } 
  ```

- RecyclerView 分页加载 

  ```java
  mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      	@Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        	super.onScrolled(recyclerView, dx, dy);
        	if (dy < 0) return;

            // item 总数，假设是 100
            final int itemCount = layoutManager.getItemCount();

            // 最后可见 item 的 position，最大值会达到 99
            final int lastVisiblePosition
              = layoutManager.findLastCompletelyVisibleItemPosition();

            // itemCount-1 是 99，只有当lastVisiblePosition 达到最大时（99）才会加载下一页
            final boolean isBottom = (lastVisiblePosition >= itemCount - 1);
            if (isBottom) {
              requestData(++mPage);
            }
      	}
    });
  ```

- RecyclerView 动态计算网格数目

  ```java
  // 定义网格宽度
  mCellWidth = DensityUtil.dp2px(77);

  mRecyclerView.getViewTreeObserver()
              .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                  @Override public void onGlobalLayout() {
                      mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                      // 获取 RecyclerView 宽度
                    	int width = mRecyclerView.getWidth();
                    	// 计算网格个数
                      int cellNum = width / mCellWidth;
                      layoutManager = new GridLayoutManager(mContext, cellNum);
                      mRecyclerView.setLayoutManager(layoutManager);
                      if(mRecyclerView.getAdapter() == null) {
                          setAdapter();
                      }

                  }
              });
  ```

  ​

