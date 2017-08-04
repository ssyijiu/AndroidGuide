> 版权声明：  本文来自 [书生依旧](http://www.jianshu.com/p/1b6e33aaddc3) 的简书，转载请注明出处。

## 第7章：UI fragment 和 fragment 管理器
- Fragement 的引入使 Adnroid UI 设计更加灵活。

- Fragment 并不具备在屏幕上显示视图的能力，只有将它放在 Activity 的视图层级结构中，Fragment 才能显示在屏幕上。 

- id 的类型使用 UUID

- Fragment 的生命周期由托管它的 Activity 调用。

- ![](http://upload-images.jianshu.io/upload_images/1342220-7f1e6d6c072ae14f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- Activity 添加 Fragment 的两种方式
    - 在布局中写死（不够灵活，不推荐使用）
    - 使用代码动态添加

- 使用 FrameLayout 作为 Fragment 的容器视图。

- ```java
    FragmentManager fm = getFragmentManager();

    // 内存重启或者屏幕旋转时，FragmentManager 会先保存 fragment 队列，然后将 fragment 队列恢复到原来的状态。
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

- FragmentManager 负责调用 Activity 的生命周期，add() fragment 时候，onAttach、onCreate、onCreateView 等生命周期才会调用。

- 封装可复用组件是 Google 设计 fragment 的本意。

- 合理的使用 fragment 一个良好的原则：应用单屏至多使用 2~3 个 fragment。

    ![](http://upload-images.jianshu.io/upload_images/1342220-95c58b411785164f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



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

- CompoundButton ![](http://upload-images.jianshu.io/upload_images/1342220-91ed1bd548eb3d48.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- style 是 XML  资源文件，含有描述组件行为和外观的定义，可以自己定义。

- Theme 是 style 的一种。

- style="?android:listSeparatorTextViewStyle" 在 TextView 下方加一个分割线，在不同的主题中样式可能有不同。

- 不以 layout 开头的属性作用于组件。

- 以 layout 开头的属性作用于组件的父组件，它们会告诉父布局如何安排自己的子元素。

- 创建水平布局 ![](http://upload-images.jianshu.io/upload_images/1342220-e11fff822aafa1ea.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- layout_weight 将 layout_width(layout_height) 分配后的剩余空间按照比例分配。

- ```java
  // 判断横竖屏
  public static boolean isScreenPortrait() {
          return Common.getAppResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
}
  ```



## 第9章：使用 RecyclerView 显示列表 

- 应用在内存里存多久，单例就存多久。

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

- ViewHolder 只做一件事：容纳 View 视图。

- RecyclerView.setAdapter() 

  - getItemCount 获取要显示的列表数量。
  - onCreateViewHolder 创建 ViewHolder 及其要显示的视图。
  - onBindViewHolder 将内容显示到视图上。

- RecyclerView 点击事件，在 ViewHolder 的构造方法中：itemView.setOnClickListener(this);

- adapter.notifyItemMoved(0,5); 将 5 位置的 View 覆盖到 0，有动画效果，数据不变，notifyDataSetChanged 后恢复原样。

- RecyclerView 最简单代码：

  ```java
  	// Adapter
  	private class CrimeAdapter extends RecyclerView.Adapter<CrimeViewHolder> {
          List<Crime> crimeList;
          CrimeAdapter(List<Crime> crimeList) {
              this.crimeList = crimeList;
          }

          @Override
          public CrimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
              LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
              View view = layoutInflater
                  .inflate(R.layout.item_crime, parent, false);

              return new CrimeViewHolder(view);
          }

          @Override public void onBindViewHolder(CrimeViewHolder holder, int position) {
              holder.bindCrime(crimeList.get(position));
          }

          @Override public int getItemCount() {
              return crimeList.size();
          }
      }
      
  	// ViewHolder
  	class CrimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
          @BindView(R.id.item_crime_tv_title) TextView tvTitle;
          @BindView(R.id.item_crime_cb_solved) CheckBox cbSolved;
          @BindView(R.id.item_crime_tv_date) TextView tvDate;
    
          private Crime crime;
    
          CrimeViewHolder(View itemView) {
              super(itemView);
              ButterKnife.bind(this, itemView);
              itemView.setOnClickListener(this);
          }
    
          void bindCrime(Crime crime) {
              this.crime = crime;
              tvTitle.setText(crime.title);
              cbSolved.setChecked(crime.solved);
              tvDate.setText(crime.date.toString());
          }
          
          @Override public void onClick(View v) {
              ToastUtil.show(crime.title + " clicked!");
          }
      }

      // 使用
      crimeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
      adapter = new CrimeAdapter(CrimeLab.get().getCrimeList());
      crimeRecycler.setAdapter(adapter);
  ```

## 第10章：使用 fragment argument

- fragment 获取 Activity Intent 中数据的两种方法
  - 直接获取 getActivity.getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID); 
    - 优点：简单方便
    - 缺点：强耦合，这个 Fragment 只能用于 CrimeActivity，失去了 Fragment 的复用性。

  - 使用 fragment argument

    - ```java
      // 1. 修改 fragment 的创建方法
      public static Fragment newInstance(UUID id) {
              Bundle args = new Bundle();
              args.putSerializable(ARG_CRIME_ID,id);
              CrimeFragment fragment = new CrimeFragment();
              fragment.setArguments(args);
              return fragment;
      }

      // 2. Activity 创建 fragment 时传入 id
      CrimeFragment fragment = CrimeFragment.newInstance(crimeId);

      // 3. 在 fragment 中获取 id
      UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
      ```

    - 这种方法使用稍微复杂，但是保证了 fragment 的通用性。

    - 为什么不使用一些变量保存数据呢？内存重启时变量保存的数据被清空。

  - fragment 的生命周期会与托管其的 Activity 的生命周期保持一致，比如当 Activity 调用 onResume 的时候，它托管的 fragment 也会调用这个方法。

  - fragment 拥有 startActivityForResult、onActivityResult 但是没有 setResult 方法，需要调用 getActivity.setResult 

  - 两个不同 Activity 中的 fragment 之间传递数据并获取返回值流程：

    ```java
    // 1. startActivityForResult
    Intent intent = CrimeActivity.newIntent(context, crime.id, positon);
    startActivityForResult(intent,REQUEST_CODE_CRIME);

    // 2. 在 Activity 中解析 Intent 中的数据
    crimePosition = intent.getIntExtra(EXTRA_CRIME_POSITION, 0);

    // 3. 使用 argument 传递给 fragment
    return CrimeFragment.newInstance(crimeId, crimePosition);

    // 4. 在 fragment 中解析出数据
    int crimePosition = arguments.getInt(ARG_CRIME_POSITION);

    // 5. 使用 Activity setResult
    context.setResult(RESULT_CODE_CRIME_POSITION,
                new Intent().putExtra(RESULT_CRIME_POSITION, crimePosition));
    // 6. 在 fragment 中提供一个解析数据的方法
    public static int resultPosition(Intent intent) {
        if (intent != null) {
          return intent.getIntExtra(RESULT_CRIME_POSITION, 0);
        }
        return 0;
    }

    // 7. 在上一级 fragment 中解析出数据
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_CRIME
           && resultCode == CrimeFragment.RESULT_CODE_CRIME_POSITION) {
          int position = CrimeFragment.resultPosition(data);
          updateUiItem(position);

        }
    }
    ```
