# Android 编程权威指南阅读笔记

## 第17章：Master-Detail 用户界面 

​

#### 平板适配

![](http://obe5pxv6t.bkt.clouddn.com/table_phone.png)

1. 创建这两个布局

```xml
// activity_fragment.xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout
    android:id="@+id/fragment_container"
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.ssyijiu.criminalintent.CrimePagerActivity">

</FrameLayout>

// activity_twopane.xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:divider="?android:attr/dividerHorizontal"
    android:showDividers="middle"
    android:orientation="horizontal">

    <FrameLayout
        android:id="@+id/fragment_container"
        android:layout_width="0dp"
        android:layout_height="match_parent"
        android:layout_weight="1">

    </FrameLayout>

    <FrameLayout
        android:id="@+id/fragment_container_detailed"
        android:layout_width="0dp"
        android:layout_height="match_parent"
        android:layout_weight="3">

    </FrameLayout>

</LinearLayout>

```

2. 注意，**List 部份放 CrimeListFragment 的 FrameLayout 的 id 都是 fragment_container** ，在 CrimeListActivity 中判断是手机还是平板选择加载 activity_fragment.xml（手机） 还是 activity_twopane.xml（平板），然后创建 CrimeListFragment 放到 fragment_container 中。

   新建 values/refs,xml

   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <resources>
       <item name="activity_masterdetail" type="layout">@layout/activity_fragment</item>
   </resources>

   // 给 @layout/activity_fragment 去取一个别名 activity_masterdetail，并指定类型为 layout
   // 这样我们就可以使用 R.layout.activity_masterdetail 来代替 R.layout.activity_fragment
   ```

   新建 values-sw600dp/refs.xml

   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <resources>
       <item name="activity_masterdetail" type="layout">@layout/activity_twopane</item>
   </resources>
   ```

   sw 是 smallest width 的缩写，虽然字面的意思是宽度，但是实际指最小尺寸，-sw600dp 配置修饰符表明：对任何最小尺寸大于等于 600dp 的设备都使用该资源，这样在我们使用 activity_masterdetail 时，在手机上回对应 activity_fragment，在平板上会对应 activity_twopane

   -------

   ​

- 给 LinearLayout 的子 View 之间添加分割线

  ```xml
  android:divider="?android:attr/dividerHorizontal"  // 可以自己指定图片、shape 等
  android:showDividers="middle"  	// 分割线的位置
  ```

  ​