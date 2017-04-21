# Android 编程权威指南阅读笔记

## 第7章：UI fragment 和 fragment 管理器
- Fragement 的引入使 Adnroid UI 设计更加灵活。

- Fragment 并不具备在屏幕上显示视图的能力，只有将它放在 Activity 的视图层级结构中，Fragment 才能显示在屏幕上。 

- id 的类型使用 UUID

- Fragment 的生命周期有托管它的 Activity 调用。

- ![](http://obe5pxv6t.bkt.clouddn.com/complete_android_fragment_lifecycle.png)

- Activity 添加 Fragment 的两种方式
    - 在布局中写死（不够灵活，不推荐使用）
    - 使用代码动态添加

- 使用 FrameLayout 作为 Fragment 的容器视图。

- ```java
    FragmentManager fm = getFragmentManager();

    // 内存重启或者屏幕旋转时，FragmentManager会先保存fragment队列，然后将fragment队列恢复到原来的状态。
    // 此时如果 fragment 已经在 fragment 队列中了，直接获取就好。
    Fragment fragment = fm.findFragmentById(R.id.fragment_container);

    if(fragment == null) {
      fragment = new CrimeFragment();
      fm.beginTransaction()
        .add(R.id.fragment_container,fragment)
        .commit();
    }
    ```

- FragmentTransaction 用来添加、移除、附加、分离、替换 fragment 队列中的 fragment。

- FragmentManager 用来管理 fragment 的回退栈。

- R.id.fragment_container 作用：

    - 告诉 Activity fragment 视图应该出现在什么位置。
    - fragment 队列中唯一标示 fragment

- 如果要向 Activity 添加 多个 fragment，通常要分别为每个 fragment 创建不同ID的容。这句话不敢苟同。。 

- 内存重启或者屏幕旋转时，FragmentManager 会先保存 fragment 队列，然后将 fragment 队列到原来的状态。

- FragmentManager 负责调用 Activity 的生命周期，add() 时候，onAttach、onCreate、onCreateView 等生命周期才会调用。

- 封装可复用组件是 Google 设计 fragment 的本意。

- 合理的使用 fragment 一个良好的原则：应用单屏至多使用 2~3 个 fragment。

    ![](http://obe5pxv6t.bkt.clouddn.com/use-fragment.png)



- 为什么使用 v4 包的 fragment：Google 在 Android 4.2 的时候支持 fragment 嵌套，如果使用 app 的 fragment，这个特性就无法在 4.0 实现，可能还有一些其他这样的例子。



## 第8章：使用布局与组件创建用户界面

- Button setEnabled(false) 后，外观也会发生变化，变成灰色。

- CheckBox extends CompoundButton 

- ```java
  CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                  // buttonView CheckBox 所在的背景 Button
                  // 选中 isChecked 是 true，未选中 false 
              }
          });
  ```

- CompoundButton ![](http://obe5pxv6t.bkt.clouddn.com/CompoundButton.png)

- style 是 XML  资源文件，含有描述组件行为和外观的定义，可以自己定义。

- Theme 是 style 的一种。

- style="?android:listSeparatorTextViewStyle" 在 TextView 下方加一个分割线，在不同的主题中样式可能有不同。

- 不以 layout 开头的属性作用于组件。

- 以 layout 开头的属性作用于组件的父组件，它们会告诉父布局如何安排自己的子元素。

- 创建水平布局 ![](http://obe5pxv6t.bkt.clouddn.com/create_land_layout.png)

- layout_weight 将 layout_width(layout_height) 分配后的剩余空间按照比例分配。

- ```java
  // 判断横竖屏
  public static boolean isScreenPortrait() {
          return Common.getAppResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
      }
  ```



## 第9章：使用 RecyclerView 显示列表 

- 应用在内存里存多久，单例久存多久。

- 抽象 SingleFragmentActivity

  ```java
  // 其他一个 Activity 如果只放一个 fragment 的话继续这个类就好了
  public abstract class SingleFragmentActivity extends FragmentActivity { 
      protected abstract Fragment createFragment();

      @Override 
      public void onCreate(Bundle savedInstanceState) { 
          super.onCreate(savedInstanceState); 
          setContentView(R.layout.activity_fragment); 
   
          FragmentManager fm = getSupportFragmentManager(); 
          Fragment fragment = fm.findFragmentById(R.id.fragment_container); 
   
          if (fragment == null) { 
              fragment = createFragment(); 
              fm.beginTransaction() 
                  .add(R.id.fragment_container, fragment) 
                  .commit(); 
          } 
      } 
  } 
  ```

- 顾名思义，RecyclerView所做的就是回收再利用，循环往复。