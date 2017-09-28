> 版权声明：  本文来自 [书生依旧](http://www.jianshu.com/p/f7138c9fb534) 的简书，转载请注明出处。

## 第17章：Master-Detail 用户界面 

​
#### 平板适配

![](http://upload-images.jianshu.io/upload_images/1342220-d694533e22096f22.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

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

2. 根据设备的尺寸选择加载不同的布局

   注意，**List 部份放 CrimeListFragment 的 FrameLayout 的 id 都是 fragment_container** ，在 CrimeListActivity 中根据设备的尺寸选择加载 activity_fragment.xml（手机） 还是 activity_twopane.xml（平板），然后创建 CrimeListFragment 放到 fragment_container 中。

   新建 values/refs.xml

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

   sw 是 smallest width 的缩写，虽然字面的意思是宽度，但是实际指最小尺寸，-sw600dp 配置修饰符表明：对任何最小尺寸大于等于 600dp 的设备都使用该资源，这样在我们使用 activity_masterdetail 作为布局文件时，在手机上回对应 activity_fragment，在平板上会对应 activity_twopane

3. 处理 List 的点击事件

   在手机上：打开一个新的 Activity

   在平板上：添加 CrimeFragment 到 R.id.fragment_container_detailed 的位置

   ```java
   // 根据能否找到 R.id.fragment_container_detailed 来判断是手机还是平板
   public boolean isPhone() {
     	return findViewById(R.id.fragment_container_detailed) == null;
   }
   ```

   思考：在平板上如何实现点击 List，然后将 CrimeFragment 添加到 Activity ?

   这实际是 Activity 中两个 Fragment 之间的通信，为保持 fragment 的独立性，我们可以在 fragment 中定义   回调接口，委托拖管 activity 来完成那些不应由 fragment 处理的任务。  

   - 在 CrimeListFragment 声明回调接口

     ```java
     // 声明回调接口
     public OnCrimeListItemListener listener;
     public interface OnCrimeListItemListener {
       	void onCrimeItemClick(Crime crime, int position);
     }

     // 绑定
     @Override public void onAttach(Activity activity) {
             super.onAttach(activity);
             if(activity instanceof OnCrimeListItemListener) {
                 listener = (OnCrimeListItemListener) activity;
             } else {
                 throw new RuntimeException(context.toString()
                     + " must implement OnCrimeListItemListener");
             }

         }

      // 解绑
      @Override public void onDetach() {
          super.onDetach();
          if (listener != null) {
            	listener = null;
          }
      }
     ```

   - 实现回调接口

     ```java
     public class CrimeListActivity extends SimpleFragmentActivity
         implements CrimeListFragment.OnCrimeListItemListener {
         
       @Override protected int getLayoutResId() {
             // 自动根据最小尺寸选择加载不同的布局
     		return R.layout.activity_masterdetail;
         }

         @Override public void onCrimeItemClick(Crime crime, int position) {

             // 手机
             if (isPhone) {
                 Intent intent = CrimePagerActivity.newIntent(context, crime.id, position);
                 startActivity(intent);
             } else {  // 平板
                 Fragment newDetail = CrimeFragment.newInstance(id, position);
                 getSupportFragmentManager().beginTransaction()
                     .replace(R.id.fragment_container_detailed, newDetail)
                     .commit();
             }
         }

         public boolean isPhone() {
             return findViewById(R.id.fragment_container_detailed) == null;
         }
     }
     ```

   - 在 RecyclerView 的 Adapter 设置点击事件

     ```java
     fragment.listener.onCrimeItemClick(crime, position);
     ```

     ​

#### Fragment 回调接口方式详解

- 在 AndroidStudio 中新建 Fragment   

  ![](http://upload-images.jianshu.io/upload_images/1342220-95f70831ecf84793.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



- 一起看一下谷歌推荐的 Fragment 使用方式     

  ```java

  public class BlankFragment extends Fragment {
      
      // 给 fragment 传递数据的 key
      private static final String ARG_PARAM1 = "param1";
      private static final String ARG_PARAM2 = "param2";

      // 给 fragment 传递的数据
      private String mParam1;
      private String mParam2;

      private OnFragmentInteractionListener mListener;
    
      // fragment 回调接口定义、托管的 Activity 要实现这个接口
      public interface OnFragmentInteractionListener {   
          void onFragmentInteraction(Uri uri);
      }

      // 创建 fragment
      public static BlankFragment newInstance(String param1, String param2) {
          BlankFragment fragment = new BlankFragment();
          Bundle args = new Bundle();
       
          // 使用 arguments 给 fragment 传递数据
          args.putString(ARG_PARAM1, param1);
          args.putString(ARG_PARAM2, param2);
          fragment.setArguments(args);
          return fragment;
      }

      @Override
      public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          
        	// 获取传递给 fragment 的数据
        	if (getArguments() != null) {
              mParam1 = getArguments().getString(ARG_PARAM1);
              mParam2 = getArguments().getString(ARG_PARAM2);
          }
      }

      @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
          // Inflate the layout for this fragment
          return inflater.inflate(R.layout.fragment_blank, container, false);
      }

      // 在需要的位置调用 onFragmentInteraction
      // onFragmentInteraction 的实现在托管的 Activity 中
      public void onButtonPressed(Uri uri) {
          if (mListener != null) {
              mListener.onFragmentInteraction(uri);
          }
      }

      // 将托管的 Activty 作为 OnFragmentInteractionListener 绑定到 fragment
      @Override
      public void onAttach(Context context) {
          super.onAttach(context);
          if (context instanceof OnFragmentInteractionListener) {
              mListener = (OnFragmentInteractionListener) context;
          } else {
              throw new RuntimeException(context.toString()
                  + " must implement OnFragmentInteractionListener");
          }
      }

      // 解绑
      @Override
      public void onDetach() {
          super.onDetach();
          mListener = null;
      }
  }
  ```


- 托管的 Activity  

  ```java
  public class BlackActivity extends AppCompatActivity
      implements BlankFragment.OnFragmentInteractionListener {

      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_black);

          // 添加 fragment
          BlankFragment fragment = BlankFragment.newInstance("param1", "param2");
          getSupportFragmentManager().beginTransaction()
              .add(R.id.fragment_container, fragment).commit();
          
      }

      // 实现 fragment 的回调接口
      @Override public void onFragmentInteraction(Uri uri) {
          ToastUtil.show("onFragmentInteraction");
      }
  }
  ```

  ​

- Fragment 回调接口的好处

  - 实现 fragment 与 Activity 之间的通信
  - 将 fragment 与托管它的 Activity 解耦、保持 fragment 的独立性

----------



- 给 LinearLayout 的子 View 之间添加分割线

  ```xml
  android:divider="?android:attr/dividerHorizontal"  // 可以自己指定图片、shape 等
  android:showDividers="middle"  	// 分割线的位置
  ```

- 屏幕尺寸修饰符

  - wXXXdp：宽度大于等于 XXX dp
  - hXXXdp： 高度大于等于 XXX dp
  - swXXXdp：宽度或高度（两者中小的那个）大于等于 XXX dp

  假设某个布局适用于屏幕宽度至少 300dp 的设备，可以使用 w300dp 修饰符，并将布局文件放在 res/layout-w300dp 目录下。

  设备方向变换的话，设备的宽和高也会交换，为确定某个具体的屏幕尺寸，我们可以使用 sw 。
