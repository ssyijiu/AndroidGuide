### 第 25章：搜索

- SearchView 是一个操作视图，所谓操作视图，就是内置在工具栏中的视图。

- 先声明一个 menu 文件，指定 app:actionViewClass="android.support.v7.widget.SearchView"

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <menu
      xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto">

      <item
          android:id="@+id/menu_item_search"
          android:title="@string/search"
          app:actionViewClass="android.support.v7.widget.SearchView"
          app:showAsAction="always"/>

      <item
          android:id="@+id/menu_item_clear"
          android:title="@string/clear_search"
          app:showAsAction="ifRoom"/>

  </menu>
  ```

- 开启 OptionsMenu (也可在自己的 Toolbar 中使用)

  ```java
  setHasOptionsMenu(true);
  ```

- 重写 onCreateOptionsMenu

  ```java
  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
          super.onCreateOptionsMenu(menu, inflater);
          inflater.inflate(R.menu.meun_search, menu);
          MenuItem menuItem = menu.findItem(R.id.menu_item_search);
          final SearchView searchView = (SearchView) menuItem.getActionView();
          
          // 点击搜索加载以前的搜索关键字
          searchView.setOnSearchClickListener(new View.OnClickListener() {
              @Override public void onClick(View v) {
                  searchView.setQuery(loadQueryKey(), false);
              }
          });
    
          searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
              @Override public boolean onQueryTextSubmit(String query) {
                  MLog.i("onQueryTextSubmit:" + query);
                  search(query);  // 搜索
                  saveQueryKey(query);   // 保存搜索的关键字
                  searchView.setIconified(true);  // 点击 x 清空关键词
          	    searchView.setIconified(true);  // 同时关闭 searchView 视图
                  return true;
              }
            
              @Override public boolean onQueryTextChange(String newText) {
              	MLog.i("onQueryTextChange:" + newText);
              	return false;
            	}
          });
  }
  ```


- 创建 SharedPreferences

  ```java
  PreferenceManager.getDefaultSharedPreferences(context);
  context.getSharedPreferences("sp_name", Context.MODE_PRIVATE);
  ```




### 第 26 章：后台服务

> 以后会专门写一篇文章分析 Service 和 IntentService，这里先不写了

- 使用 AlarmManager 延迟运行服务

  ```Java
  private static final int POLL_INTERVAL = 1000 * 60; // 60 s
  public static void setServiceAlarm(boolean isOn) {
  	Intent intent = PollService.newIntent();
    	// Context context, int requestCode, Intent intent, int flags
    	PendingIntent pi = PendingIntent.getService(App.getContext(), 0, intent, 0);

    	// 获取定时管理器
    	AlarmManager alarmManager = (AlarmManager)
      	App.getContext().getSystemService(Context.ALARM_SERVICE);

    	if (isOn) {
      	// 设置定时器
      	// type：时间类型 ELAPSED_REALTIME(从开机开始到现在经过的时间)
      	// triggerAtMillis：触发事件，与上面类型对应
      	// intervalMillis：时间间隔
      	// PendingIntent 延迟任务
      	alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                                       SystemClock.elapsedRealtime(), POLL_INTERVAL, pi);
    	} else {
      	// 取消定时器
      	alarmManager.cancel(pi);
      	pi.cancel();
    	}
  }

  // 判断定时器是否存在
  // 定时器是通过 PendingIntent 开启的，定时器撤销后 PendingIntent 也会被撤销
  public static boolean isServiceAlarmOn() {
    	Intent intent = PollService.newIntent();
    	PendingIntent pendingIntent = PendingIntent
      	.getService(App.getContext(),0,intent,PendingIntent.FLAG_NO_CREATE);
    	return pendingIntent != null;
  }
  ```




- 通知： [推荐阅读](http://reezy.me/p/20161228/android-notification/)

  ```java
  Notification notification = new NotificationCompat.Builder(this)
    	.setSmallIcon(android.R.drawable.ic_menu_report_image)   // 小图标
    	.setContentTitle(ResourceUtil.getString(R.string.new_pictures_title))  // 标题
    	.setContentText(ResourceUtil.getString(R.string.new_pictures_text))    // 文字
    	.setContentIntent(pendingIntent)   // 点击的 Intent
    	.setAutoCancel(true)               // 点击后通知消失
    	.build();

  NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

  // 发送通知, id 相同下一条通知会顶掉上一条
  notificationManager.notify(0, notification);	
  ```



#### 最后

现在发现以读书笔记的形式写博客真心不好写，比如前面提到 Handler 、本章的服务、上面提到的通知，这些东西每个都可以单独拿出来写一篇文章，全部写在笔记里篇幅太长，不写这个笔记又不完整，写一部分更不好，前面 Fragment 的内容就分布在了几篇笔记中，这样对读者阅读以及我以后复习都非常不友好。

所以，决定不再写笔记了，这个系列到此结束。

本书已经读完，后面的内容会结合实际开发经验以技术总结的形式写出来。

电子书、代码、笔记都在 https://github.com/ssyijiu/AndroidGuide

刚刚离职，[个人简历在这里](https://github.com/ssyijiu/resume-public)，如果您有提供工作的机会欢迎联系我。