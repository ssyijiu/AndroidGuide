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

- Android 中的主线程处于一个无线循环的运行状态，等待着系统或者用户触发事件，事件触发后，主线程便负责执行代码，已响应这些事件。

  ![](http://obe5pxv6t.bkt.clouddn.com/main_thread.png)