## Android 编程权威指南疑问汇总

- RecyclerView Adapter 原理。

- ViewPager，PageAdapter 、FragmentPagerAdapter 、FragmentStatePagerAdapter  原理及区别。

- ```java
  public void show(FragmentManager manager, String tag) 
  public void show(FragmentTransaction transaction, String tag) 
  // 原理及区别
  ```

- DialogFragment，onCreateView、onCreateDialog 区别。

- OptionsMenu 和 Toolbar 的关系区别

- android:showDividers="end" 无效 

- 调用 startActivity(Intent) 方法（或者 startActivityForResult(...) 方法）发送隐式 intent 时，操作系统会悄悄的为目标 intent 添加 Intent.CATEGORY_ DEFAULT 。 

- AsyncTaskLoader 的使用