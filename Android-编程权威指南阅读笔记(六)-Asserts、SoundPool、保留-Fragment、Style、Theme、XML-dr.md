> 版权声明：  本文来自 [书生依旧](http://www.jianshu.com/p/98915d2854ed) 的简书，转载请注明出处。
原文链接： http://www.jianshu.com/p/98915d2854ed 

## 第18章：Assets 

- asserts：可以看做应用的微型文件系统，支持任意层次的目录结构，类似游戏这样需要加载大量的图片和声音时通常会使用它，asserts 目录下所有的资源文件都会随应用打包。

- 创建 asserts

  ![](http://upload-images.jianshu.io/upload_images/1342220-60ab334a813d4c06.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 访问 asserts 文件

  ```java
  // sample_sounds 是 asserts 的子文件夹（asserts/sample_sounds）
  private static final String SOUNDS_FOLDER = "sample_sounds";
  AssetManager assetManager = Common.getContext().getAssets();
  // 获取 asserts/sample_sounds 下的所有文件
  String[] soundNames = assetManager.list(SOUNDS_FOLDER);

  // 打开 asserts 中的文件（filename 是 soundNames 的一个元素）
  String assetPath = SOUNDS_FOLDER + "/" + filename;
  InputStream soundData = mAssets.open(assetPath);  			
  ```



  	

## 第19章：使用 SoundPool 播放音频

- 创建 SoundPool 

  ```java
  // Android 5.0 以前使用 SoundPool(int, int, int)
  // 实际开发中为了兼容 Android 4.x 请使用 SoundPool(int, int, int)
  SoundPool.Builder builder = new SoundPool.Builder();
  builder.setMaxStreams(MAX_SOUNDS);  // 最多同时播放 5 个音频

  // 创建音频流类型，Android 有很多音频流，它们有各自独立的音量控制
  // 这就是调低音量，闹钟音量不受影响的原因
  // AudioManager.STREAM_MUSIC 是使用同音乐和游戏一样的音量控制
  builder.setAudioAttributes(new AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build());

  // 创建 SoundPool
  soundPool = builder.build();
  ```

- 播放音频文件

  ```java

  /**
   * 封装音频对象
   */
  public class Sound {
    private String assetPath;    // 音频的 asserts 路径
    private String name;         // 音频名称
    private Integer soundId;     // 音频加载 id
 }

  /**
   * 加载音频
   */
  private void load(Sound sound) throws IOException {
      AssetFileDescriptor afd = assetManager.openFd(sound.getAssetPath());
      // 加载音频，准备播放，加载失败返回 null
      Integer soundId = soundPool.load(afd, 1);
      // 记录下 id
      sound.setSoundId(soundId);
  }

  /**
   * 播放音频
   */
  public void play(Sound sound) {
  	Integer soundId = sound.getSoundId();
  	if(soundId == null) {
  		return;
  	}
    
      // 播放音频
      soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
      // @param soundID a soundID returned by the load() function
      // @param leftVolume left volume value (range = 0.0 to 1.0)
      // @param rightVolume right volume value (range = 0.0 to 1.0)
      // @param priority stream priority (0 = lowest priority)
      // @param loop loop mode (0 = no loop, -1 = loop forever)
      // @param rate playback rate (1.0 = normal playback, range 0.5 to 2.0)
  }
  ```

- 释放 SoundPool

  ```java
  soundPool.release();
  ```


  -----

  ​

- 保留 fragment 

  ```java
  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setRetainInstance(true);  // 保留 fragment，默认 false
  }
  ```
  - 保留 fragment 可以销毁和重建 fragment 视图，但无需销毁 fragment 本身。
  - 已经保留的 fragment 不会随着 activity 一起销毁，它会一直保留并在需要的时候原封不动的传递给 activity。

  - 在设备配置发生变化时，FragmentManager 首先销毁队列中的 fragment 视图（新的配置可能需要新的资源来匹配，当有更合适的资源时，需要重建视图），紧接着检查每个 fragment 的  retainInstance 的属性值，如果是 false FragmentManager 会立刻销毁该 fragment 实例及其视图，随后为适应新的配置，新的 Activity 中 新的 FragmentManager 会创建一个新的 fragment 及其视图；如果是 true 则 FragmentManager 只会销毁 fragment 视图，但不销毁 fragment 本身，当新的 Activity 创建后，新的 FragmentManager 会找到被保留的 fragment 并重建它的视图。

  - 保留 fragment 中的 View 全部会销毁重建，其他成员变量不会销毁。

  - 虽然保留的 fragment 没有被销毁，但是它已脱离销毁的 activity 并处于保留状态，此时 fragment 仍然存在，但是没有任何 activity 托管它。

  - 保留的 fragment ，在设备配置发生变化时，不会走 onDestory 方法，自然也不会走 onCreate 方法。

    ![](http://upload-images.jianshu.io/upload_images/1342220-9cb47e0155086571.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



- fragment 进入保留状态的两个条件
  - 设置 setRetainInstance(true); 
  - 因设备配置发生变化，托管的 Activity 正在被销毁。（Activity 被系统强杀不行）
- 保留 fragment 的缺点
  - 对于 Android 系统来说相比非保留 fragment 而言更加复杂。
  - Activity 因系统需要回收内存被销毁时，保留 fragment 也会被销毁。 （使用 onSaveInstanceState 解决）
- 什么时候使用保留 fragment ？
  - 设备配置发生变化时，不重复请求数据，可以在 onCreate 中请求数据，然后将 fragment 设置为保留
  - 在设备配置发生变化时，用来保存一些 savedInstanceState 不容易保存的东西，比如  SoundPool
  - 短暂保留数据时，可以使用保留 fragment 来代替 savedInstanceState，如果需要持久保存数据，还是要使用 savedInstanceState，因为当系统因回收内存销毁 Activity 是，fragment 也会被销毁，保留 fragment 也会随之被销毁。



## 第20章：样式和主题

- style 继承

  ```xml
  <style name="BeatBoxButton">
    	<item name="android:background">@color/dark_blue</item>
  </style>

  // 通过主题名方式继承 BeatBoxButton 并添加一个 textStyle = bold 属性
  // 使用这种方式继承，有继续关系的两个主题必须位于同一个包中
  <style name="BeatBoxButton.Strong">
    	<item name="android:textStyle">bold</item>
  </style>

  // 通过 parent 方式继承
  <style name="StrongBeatBoxButton" parent="BeatBoxButton">
    	<item name="android:textStyle">bold</item>
  </style>
  ```

- 开发时，如果是继承自己内部的主题，使用主题名指定父主题即可，如果是继承 Android 操作系统中的主题记得使用 parent 属性。

- 可以把主题看作样式的加强版，同样定义一套公共主题属性，样式属性需要逐个添加，而主题属性会自动应用于整个应用（给 application 设置主题）。

- 修改 app 所有的背景颜色

  ```xml
  // 添加到 AppTheme 中，这属性来自 Android 系统，前面要加 android 命名空间
  // 修改主题原来默认的背景色
  // 连 app 启动时的空白窗口的背景颜色都改掉了
  <item name="android:colorBackground">@color/your_color</item>
  ```

- 修改 app 所有按钮的属性

  ```xml
  // 添加到 AppTheme 中
  <item name="android:buttonStyle">@style/BeatBoxButton</item>

  // Widget.Holo.Button 是 Android 主题的默认 style
  // 如果不指定这个 parent 所有按钮的样式将会变得不像个按钮
  <style name="BeatBoxButton" parent="android:style/Widget.Holo.Button">
  	<item name="android:background">@color/dark_blue</item>
  </style>
  ```

- 在主题中引用资源时，使用 ？

  ```xml
  android:background="?attr/colorAccent" 
  ```

- 在代码中使用主题属性

  ```java
  Resources.Theme theme = getActivity().getTheme(); 
  int[] attrsToFetch = { R.attr.colorAccent }; 
  TypedArray a = theme.obtainStyledAttributes(R.style.AppTheme, attrsToFetch); 
  int accentColor = a.getInt(0, 0); 
  a.recycle(); 
  ```

  ​

- 创建多版本主题：

  创建一个带资源修饰符的 styles.xml 文件：values-v21/styles.xml 

  这样在版本 >= 21 时，会使用 values-v21/styles.xml 的主题，版本 < 21 使用 values/styles.xml 的主题


## 第21章：XML drawable 

- shape 的使用

  [官方文档](https://developer.android.google.cn/guide/topics/resources/drawable-resource.html#Shape)

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <shape xmlns:android="http://schemas.android.com/apk/res/android"
      android:shape="oval"  
      >

      <!-- 圆角 radius-->
      <corners
              android:radius="9dp"
              android:topLeftRadius="2dp"
              android:topRightRadius="2dp"
              android:bottomLeftRadius="2dp"
              android:bottomRightRadius="2dp"/>

      <!-- 渐变 gradient-->
      <gradient
              android:startColor="@android:color/white"
              android:centerColor="@android:color/black"
              android:endColor="@android:color/black"
              android:angle="45"
              android:type="radial"
              android:centerX="0"
              android:centerY="0"
              android:gradientRadius="90"/>

      <!-- 间隔 padding-->
      <padding
              android:left="2dp"
              android:top="2dp"
              android:right="2dp"
              android:bottom="2dp"/><!-- 各方向的间隔 -->

      <!-- 大小 size-->
      <size
              android:width="50dp"
              android:height="50dp"/><!-- 宽度和高度 -->

      <!-- 填充 solid-->
      <solid
              android:color="@android:color/white"/><!-- 填充的颜色 -->

      <!-- 描边 stroke-->
      <stroke
              android:width="2dp"
              android:color="@android:color/black"
              android:dashWidth="1dp"   
              android:dashGap="2dp"/>   

  </shape>
  ```

- selector 使用

  [官方文档](https://developer.android.google.cn/guide/topics/resources/drawable-resource.html#StateList)

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <selector xmlns:android="http://schemas.android.com/apk/res/android">
      <item android:state_pressed="true"
            android:drawable="@drawable/button_pressed" /> <!-- pressed -->
      <item android:drawable="@drawable/button_normal" />  <!-- default -->
  </selector>
  ```

- layer-list 使用

  [官方文档](https://developer.android.google.cn/guide/topics/resources/drawable-resource.html#LayerList)

  ```xml
  <layer-list
      xmlns:android="http://schemas.android.com/apk/res/android">
      <item>
          <shape
              android:shape="oval">

              <solid
                  android:color="@color/red"/>
          </shape>
      </item>
      <item>
          <shape
              android:shape="oval">

              <stroke
                  android:width="4dp"
                  android:color="@color/dark_red"/>

          </shape>
      </item>
  </layer-list>
  ```

- 像 shape、selector 、layer-list 都可以叫做 XML drawable

- XML drawable 的优点（相比图片）

  - 容易维护，一秒钟改个颜色
  - 体积小
  - 无需考虑屏幕适配

- 点 9 图

  - 点 9 图左边和上面的黑线用来确定伸缩区域，右边和下边的黑线用来确定内容区域。

  - 伸缩区域将点 9 图分为 9 部分，其中 4 个角落（1、3、7、9）不会被伸缩、边缘的 4 个部分（2、4、6、8）只按一个维度伸缩、中间部分（5）按照两个维度伸缩。  
  
    ![](http://obe5pxv6t.bkt.clouddn.com/9patch_1.bmp)

    ![](http://obe5pxv6t.bkt.clouddn.com/9patch_2.bmp)

  - 内容区域介绍[戳这里](http://isux.tencent.com/android-ui-9-png.html)

  - 制作点 9 图：重新命名你的图片为 xxx.9.png，使用 Android Studio 重新打开，使用鼠标标记出伸缩区域和内容区域即可，按住 Shift 可以去掉标记的区域。

- 把应用的启动图标放在 mipmap 中，其他图片反正 drawable 中。
