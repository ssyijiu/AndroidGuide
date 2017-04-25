# Android 编程权威指南阅读笔记

## 第11章：使用 ViewPager 

- ViewPager 和 fragment 一起使用

   ```java
    FragmentManager fragmentManager = getSupportFragmentManager();
       // FragmentStatePagerAdapter 负责 ViewPager 和 Fragment 协同工作
       viewPagerRoot.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
             @Override public Fragment getItem(int position) {
               	Crime crime = crimeList.get(position);
               	return CrimeFragment.newInstance(crime.id,position);
             }

             @Override public int getCount() {
               	return crimeList.size();
             }
       });
   ```


-   ViewPager 默认加载当前屏幕的列表项，以及左右相邻的页面，可以使用 setOffscreenPageLimit(int) 定制预加载左右页面的数量。

  - FragmentStatePagerAdapter 与 FragmentPagerAdapter

    - FragmentStatePagerAdapter 会销毁不需要的 fragment ，事务提交后，FragmentManager 中的 fragment 会被彻底移除（调用 FragmentTransaction 的 remove 方法）。
    - FragmentPagerAdapter 对于不需要的 fragment 会调用 FragmentTransaction 的 detach 方法，只是销毁了 fragment 的视图，fragment 的实例还保存在 FragmentManager 中，FragmentPagerAdapter 创建 fragment 永远保存在内存中，不会被销毁。
    - FragmentStatePagerAdapter 更省内存，FragmentPagerAdapter 使用与展示少量而固定的 fragment。

- 使用 Dump Java Heap 检测 CrimeFragment 的数量，在 ViewPager 中滑动 20 次，点击 GC 之后

  - 使用 FragmentStatePagerAdapter 内存中存在 3 个 CrimeFragment 
  - ![](http://obe5pxv6t.bkt.clouddn.com/FragmentStatePagerAdapter_count.png)
  - 使用 FragmentPagerAdapter 内存中存在 23 个 CrimeFragment  
  - ![](http://obe5pxv6t.bkt.clouddn.com/FragmentPagerAdapter_count.png)

- PageAdapter 的使用：

    ```java

    class ViewPagerAdapter extends PagerAdapter {

        @Override public int getCount() {
          	return list.size();
        }

        @Override public boolean isViewFromObject(View view, Object object) {
          	return view == object;
        }
    	
        // 创建 pager
        @Override public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            container.addView(imageView);
            return imageView;
        }

       	// 移除 pager
        @Override public void destroyItem(ViewGroup container, int position, Object object) {
          	container.removeView((View) object);
        }
    }
    ```

- FragmentStatePagerAdapter 和 FragmentPagerAdapter  都继承了 PageAdapter  并重写了 isViewFromObject 、insubstantial 、destroyItem 方法。它们的 getItem 方法都会在 insubstantial  中调用，在 destroyItem 一个使用 FragmentTransaction 的 remove ，一个使用 FragmentTransaction 的 detach。



## 第12章：对话框

- AppCompat 兼容库能将部分最新系统的特色功能移植到 Android 旧版本系统中。

- 单独使用 AlertDialog 是，旋转屏幕 AlertDialog 会消失。将 AlertDialog 封装在 DialogFragment 则不会出现此问题（FragmentManager 会恢复 fragment，fragment 恢复 AlertDialog ）。

- 在 DialogFragment 中的 onCreateDialog 方法创建 AlertDialog

  ```java
  public Dialog onCreateDialog(Bundle savedInstanceState) {

      AlertDialog.Builder builder = new AlertDialog.Builder(context);
      builder.setPositiveButton(android.R.string.ok,null)
      .setView(R.layout.dialog_datepicker);
      return builder.create();
  }
  ```

- AlertDialog 有三种类型的按钮：Positive、Negative 和 Neutral

  ```java
  AlertDialog.Builder builder = new AlertDialog.Builder(context);
  builder
    .setPositiveButton(android.R.string.ok,null)
    .setNegativeButton(android.R.string.cancel,null)
    .setNeutralButton(android.R.string.copy,null)
    .setView(R.layout.dialog_datepicker);
  return builder.create();
  ```

  上述代码的按钮效果：

  ![](http://obe5pxv6t.bkt.clouddn.com/alertdialog_button_type.png)

  注意：PositiveButton 总是在最右边，NeutralButton 总是在最左边，NeutralButton 如果有 PositiveButton 的话在中间，没有的话在最右边。

- 显示 DialogFragment

  ```java
  public void show(FragmentManager manager, String tag) 
  public void show(FragmentTransaction transaction, String tag) 
  ```

- 自定义 AlertDialog 布局，使用 AlertDialog.Builder#setView

- 设备配置改变时，具有 id 属性的视图可以保存运行状态，在本例中为 DatePicker 添加 id ，选中一个日期，旋转屏幕后会恢复你选中的日期，去掉 id 后则不会有这个效果。

- 同一个 Activity 托管的两个 fragment 之间传递数据

  - ![](http://obe5pxv6t.bkt.clouddn.com/fragment_data_fragment.png)

  ​

  ```java
  // 1. 在 DatePickerFragment 新建 newInstance(date) 方法
  public static DatePickerFragment newInstance(Date date) {
      Bundle args = new Bundle();
      args.putSerializable(ARGS_CRIME_DATE, date);
      DatePickerFragment fragment = new DatePickerFragment();
      fragment.setArguments(args);
      return fragment;
  }
  // 2. setTargetFragment(Fragment fragment, int requestCode) 
  FragmentManager manager = getFragmentManager();
  DatePickerFragment dialog = DatePickerFragment.newInstance(crime.date);
  // 设置要返回数据的 fragment 和 requestCode
  dialog.setTargetFragment(CrimeFragment.this, REQUEST_CRIME_DATE);
  dialog.show(manager, dialog.getClass().getSimpleName());

  // 3. 使用 getTargetFragment().onActivityResult 返回数据
   private void setResult(int resultCode, Date date) {
       if (getTargetFragment() != null) {
         Intent intent = new Intent();
         intent.putExtra(EXTRA_CRIME_DATE, date);  // 要返回的数据
         getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
         // getTargetFragment 前面 setTargetFragment 的 fragment 参数
         // getTargetRequestCode 前面 setTargetFragment 的请求码
       }
   }

  // 调用
  setResult(REQUEST_CRIME_DATE, date);

  // 4. 封装解析数据的方法（这里做了一些严谨性的判空）
  public static Date resultDate(Intent intent) {
      Date date = null;
      if(intent != null) {
        	date = (Date) intent.getSerializableExtra(EXTRA_CRIME_DATE);
      }

      if(date != null) {
        	return date;
      }
      return new Date();
  }

  // 5. 在要返回数据的 fragment 中重写 onActivityResult
  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
      if(requestCode == REQUEST_CRIME_DATE
         && resultCode == DatePickerFragment.REQUEST_CRIME_DATE) {
          Date date = DatePickerFragment.resultDate(data);
          crime.date = date;
          updateDate();
      }
  }
  ```

## 第13章：工具栏

- AppCompat 库自带 3 中主题：

  - Theme.AppCompat 黑色主题
  - Theme.AppCompat.Light 浅色主题
  - Theme.AppCompat.Light.DarkActionBar 带黑色工具栏的浅色主题

- 菜单是一种类似布局的资源，创建方法：右键Res -> New Android resource file -> 选择 Menu -> OK

  ```html
  <?xml version="1.0" encoding="utf-8"?>
  <menu xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      >
      <item
          android:id="@+id/menu_item_new_crime"
          android:icon="@drawable/ic_menu_add"
          android:title="@string/new_crime"
          app:showAsAction="ifRoom|withText"/>

      <item
          android:id="@+id/menu_item_show_subtitle"
          android:title="@string/show_subtitle"
          app:showAsAction="ifRoom"/>

  </menu>
  ```

  ​

- app:showAsAction 指定菜单选项在工具栏上显示还是隐藏

  - never  隐藏
  - always 显示
  - ifRoom 空间够的话则显示，不够隐藏
  - ifRoom|withText 空间够的话优先显示图标，其次显示文字描述

- 在 fragment 中使用 OptionsMenus （也可以自己在布局写 Toolbar，然后使用 Toolbar）

  ```java

  // 1. 创建 OptionsMenu
  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	super.onCreateOptionsMenu(menu, inflater);
    	inflater.inflate(R.menu.fragment_crime_list,menu);
  }

  // 2. 设置 OptionsMenu 生效
  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setHasOptionsMenu(true);
  }

  // 3. 设置点击事件
  @Override public boolean onOptionsItemSelected(MenuItem item) {

      switch (item.getItemId()) {
          case R.id.menu_item_new_crime:
              Crime crime = new Crime();
              addCrime(crime);
              Intent intent = CrimePagerActivity.newIntent(context,crime.id,indexOfCrime(crime));
              startActivityForResult(intent,REQUEST_CODE_CRIME);
              return true;
          case R.id.menu_item_show_subtitle:
              updateSubtitle();
              return true;
          default:
            	return super.onOptionsItemSelected(item);
      }
  }
  ```

- 层级式导航

  - AndroidManifest 在相应的Activity 中添加 android:parentActivityName=".CrimeListActivity" 出现下图效果

    ![](http://obe5pxv6t.bkt.clouddn.com/parentActivity.png)

  - 点击左面箭头会执行

    ```java
    Intent intent = new Intent(this, CrimeListActivity.class); 
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
    startActivity(intent); 
    finish();
    ```

  - Intent.FLAG_ACTIVITY_CLEAR_TOP ：在回退栈中寻找指定的 Activity，如果存在，弹出栈内其他 Activity，让启动的目标 Activity 出现在栈顶。

    ```html
    If set, and the activity being launched is already running in the
    current task, then instead of launching a new instance of that activity,
    all of the other activities on top of it will be closed and this Intent
    will be delivered to the (now on top) old activity as a new Intent.

    For example, consider a task consisting of the activities: A, B, C, D.
    If D calls startActivity() with an Intent that resolves to the component
    of activity B, then C and D will be finished and B receive the given
    Intent, resulting in the stack now being: A, B.
    ```

  - 获取 xml 中字符串，并设置占位符

    - ```html
      <string name="hello">%1$s hello %2$s</string>
      // %1$s 表示是 getString(resId,formatArgs) formatArgs 可变参数中的第一个参数
      ```

    - ```java
      public final String getString(@StringRes int resId, Object... formatArgs)
      ```

    - ```java
      String str = getString(R.string.hello, "Android", "world");
      // str 的结果为：Android hello world
      ```