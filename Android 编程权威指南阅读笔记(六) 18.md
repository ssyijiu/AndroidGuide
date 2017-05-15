# Android 编程权威指南阅读笔记

## 第18章：Assets 

- asserts：可以看做应用的微型文件系统，支持任意层次的目录结构，类似游戏这样需要加载大量的图片和声音时通常会使用它，asserts 目录下所有的资源文件都会随应用打包。

- 创建 asserts

  ![](http://obe5pxv6t.bkt.clouddn.com/load_assert.png)

- 访问 asserts 文件

  ```java
  AssetManager assetManager = Common.getContext().getAssets();
  // 获取 asserts/SOUNDS_FOLDER 下的所有文件
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
  private void load(Sound sound) throws IOException {
      AssetFileDescriptor afd = assetManager.openFd(sound.getAssetPath());
      // 加载音频，准备播放，加载失败返回 null
      Integer soundId = soundPool.load(afd, 1);
      // 记录下 id
      sound.setSoundId(soundId);
  }


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

  - 已经保留的 fragment 不会随着 activity 一起销毁，它会一直保留并在需要的时候原封不动的传递给 activity。

  - 在设备配置发生变化时，FragmentManager 首先销毁队列中的 fragment 视图，紧接着检查每个 fragment 的  retainInstance 的属性值，如果是 false FragmentManager 会立刻销毁该 fragment 实例，如果是 true 则不销毁 fragment 实例。

  - 虽然保留的 fragment 没有被销毁，但是它已脱离销毁的 activity 并处于保留状态，此时 fragment 仍然存在，但是没有任何 activity 托管它。

  - 保留的 fragment ，在设备配置发生变化时，不会走 onDestory 方法

    ![](http://obe5pxv6t.bkt.clouddn.com/fragment_life.png)



- fragment 进入保留状态的两个条件
  - 设置 setRetainInstance(true); 
  - 因设备配置发生变化，托管的 Activity 正在被销毁。（Activity 被系统强杀不行）
- 保留 fragment 的缺点
  - 对于 Android 系统来说相比非保留 fragment 而言更加复杂。
  - Activity 因系统需要回收内存被销毁时，保留 fragment 也会被销毁。 （使用 onSaveInstanceState 解决）



## 第20章：样式和主题

- style 继承

  ```xml
  <style name="BeatBoxButton">
    	<item name="android:background">@color/dark_blue</item>
  </style>

  // 通过命名方式继承 BeatBoxButton 并添加一个 textStyle = bold 属性
  <style name="BeatBoxButton.Strong">
    	<item name="android:textStyle">bold</item>
  </style>

  // 通过 parent 方式继承
  <style name="StrongBeatBoxButton" parent="BeatBoxButton">
    	<item name="android:textStyle">bold</item>
  </style>
  ```

  ​