### Android 统一图片加载入口，Picasso、Glide、Fresco 任意切换

#### 为什么要统一图片加载入口

推荐看下这篇文章：[如何正确使用开源项目？](http://stormzhang.com/android/2016/05/08/how-to-choose-open-source-project/)

我曾经反编译了一个 apk，发现里面用了 UniversalImageLoader、Picasso、Glide 三个图片加载框架。

当时的场景可能是这样的：项目开始时选择了那个时候流行的 UniversalImageLoader，后来发现UniversalImageLoader 不再维护了，换成了 Picasso，再后来随着项目的推进 Picasso 有点满足不了现在的需求，于是又引入了功能更强大加载速度更快的 Glide，但是前面 UniversalImageLoader、Picasso 已经大量使用，又没有人去移除它们，于是出现了一个项目中三个图片加载框架并存的局面。（话说如果没有测试支持，作为一个程序员你敢随便修改代码吗？尤其是那些写的非常乱、牵一发动全身的项目）



#### 开始封装

##### ImageLoader 所有图片加载的方法都定义在这里

```java
public interface ImageLoader {
    void init(Context context);   // 初始化
    void loadImage(String url, ImageView imageView);   // 默认加载图片的方法
    // 根据 ImageOptions 的配置项加载图片
    // 比如占位图、圆角图片、缓存策略等等都可以在 ImageOptions 配置
    void loadImage(String url, ImageView imageView, ImageOptions options);
}
```

##### ImageOptions 加载图片时的配置

```java
public class ImageOptions {

    private static final int UN_SET = -1;  // 未设置的默认值
    private int error = UN_SET;   // 加载图片失败显示的图片
    // ... 其他的配置项就不举例了

    // 设置加载图片失败时显示的图片
    public ImageOptions error(int resourceId) {
        error = resourceId;
        return this;
    }

    public int error() {
        return error;
    }

    // 检查是否设置了某个配置项
    boolean isSet(int flag) {
        return flag != UN_SET;
    }
    
    // ImageOptions 单例
    private static final ImageOptions defaultOptions = new ImageOptions();
    public static ImageOptions getOptions() {
      // clean all Options
      defaultOptions.error(UN_SET);
      return defaultOptions;
  	}
}
```

##### GlideLoader 实现 ImageLoader，使用 Gilde 加载图片

```java
class GlideLoader implements ImageLoader {

    static final GlideLoader INSTANCE = new GlideLoader();  // 单例
    // Glide 的配置项，这个是 Glide 自己实现的
    private RequestOptions defaultOptions;


    private GlideLoader() {
    }

    // 初始化 Glide 默认的配置
    @Override public void init(Context context) {
        defaultOptions = new RequestOptions()
            .centerCrop()
            .error(R.color.colorAccent)
            .dontAnimate()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.IMMEDIATE);
    }


    @Override public void loadImage(String url, ImageView imageView) {
        loadImage(url, imageView, null);
    }


    @Override public void loadImage(String url, ImageView imageView, ImageOptions options) {

        RequestOptions requestOptions = new RequestOptions().apply(defaultOptions);
        if (options != null) {
            // 如果设置了加载失败后显示的图片，把这个配置设置给 Glide
            if (options.isSet(options.error())) {
                requestOptions.error(options.error());
            }
        }
        // 使用 Glide 加载图片
        Glide.with(getContext(imageView))
            .load(url)
            .apply(requestOptions)
            .into(imageView);
    }


    private Context getContext(ImageView imageView) {
        Context context = imageView.getContext();
        if (context instanceof Activity) {
            if (!((Activity) context).isDestroyed()) {
                return context;
            }
        }
        return App.getContext();
    }
}
```

##### Vinci 图片加载的入口（不要吐槽我起了 达芬奇 这个名字）

```java
public class Vinci {

    private Vinci() {
    }
    
    // 初始化
    public static void init(Context context) {
        instance().init(context);
    }

    // 懒加载（这个懒加载、预加载其实没有太大影响）
    private static class Lazy {
        // 当我们切换其他图片加载库的时候，只要实现一个 xxxLoader 替换 GlideLoader 就好了
        // 关于 Fresco 的特殊往下面看
        static final ImageLoader INSTANCE = GlideLoader.INSTANCE;

    }
    
    // 返回 ImageLoader
    public static ImageLoader instance() {
        return Lazy.INSTANCE;
    }

}
```
##### 使用 Vinci 

```java
// 在 Application 中初始化
public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();
        Vinci.init(this);
    }
}

// 加载图片
ImageOptions options = ImageOptions.getOptions();
options.error(android.R.color.darker_gray);  // 设置加载失败时的错误图片
Vinci.instance().loadImage(url, imageView, options);
```
##### Fresco SimpleDraweeView 解决方案

- 如果是一个新项目，或者项目还不大，自己继承一个 ImageView  在 xml 中使用

  ````java
  // 使用 Picasso、Glide 时
  public class ImageVinci extends AppCompatImageView {
      public ImageVinci(Context context) {
          super(context);
      }


      public ImageVinci(Context context, AttributeSet attrs) {
          super(context, attrs);
      }


      public ImageVinci(Context context, AttributeSet attrs, int defStyleAttr) {
          super(context, attrs, defStyleAttr);
      }
  }

  // Fresco
  public class ImageVinci extends SimpleDraweeView {

      public ImageVinci(Context context, GenericDraweeHierarchy hierarchy) {
          super(context, hierarchy);
      }


      public ImageVinci(Context context) {
          super(context);
      }


      public ImageVinci(Context context, AttributeSet attrs) {
          super(context, attrs);
      }


      public ImageVinci(Context context, AttributeSet attrs, int defStyle) {
          super(context, attrs, defStyle);
      }


      public ImageVinci(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
          super(context, attrs, defStyleAttr, defStyleRes);
      }
  }
  ````

  这样在 xml 中使用 ImageVinci 替换 ImageView，通过修改 ImageVinci 继承，就可以了切换 Fresco 和其他图片加载库了，至于 SimpleDraweeView 的 xml 属行，在代码中完成。

  ​


- 如果你的项目很大，已经没有办法替换 xml 中的 ImageView 了，参考下面两篇文章

    - [项目重构之路——Fresco非入侵式替换Glide](http://www.jianshu.com/p/477437326b58)
    - [一种使用 Fresco 非侵入式加载图片的方式](http://fucknmb.com/2017/07/27/%E4%B8%80%E7%A7%8D%E4%BD%BF%E7%94%A8Fresco%E9%9D%9E%E4%BE%B5%E5%85%A5%E5%BC%8F%E5%8A%A0%E8%BD%BD%E5%9B%BE%E7%89%87%E7%9A%84%E6%96%B9%E5%BC%8F/)




#####  FrescoLoader

```java
class FrescoLoader implements ImageLoader {

    // 单例
    static final FrescoLoader INSTANCE = new FrescoLoader();
    // Fresco 的默认配置
    private GenericDraweeHierarchyBuilder defaultHierarchyBuilder;


    private FrescoLoader() {
    }


    @Override public void init(Context context) {
        Fresco.initialize(context);
        // 初始化 Fresco 默认配置
        defaultHierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(context.getResources());
        setHierarchyBuilder(defaultHierarchyBuilder);
    }


    @Override public void loadImage(String url, ImageView imageView) {
        loadImage(url, imageView, null);
    }


    @Override public void loadImage(String url, ImageView imageView, ImageOptions options) {
        if (imageView instanceof SimpleDraweeView) {
            SimpleDraweeView draweeView = (SimpleDraweeView) imageView;
            // 将 ImageOptions 的配置设置给 Fresco
            if (options != null) {
                if (options.isSet(options.error())) {
                    defaultHierarchyBuilder.setFailureImage(options.error());
                }
            }
 
            draweeView.setHierarchy(defaultHierarchyBuilder.build());
            // 加载图片
            draweeView.setImageURI(url);
            // 恢复 Fresco 配置
            resetHierarchyBuilder(defaultHierarchyBuilder);
        }
    }


    private void resetHierarchyBuilder(GenericDraweeHierarchyBuilder builder) {
        builder.reset();
        setHierarchyBuilder(builder);
    }

    private void setHierarchyBuilder(GenericDraweeHierarchyBuilder builder) {
        builder.setFailureImage(R.color.colorAccent);
    }
}
```



##### PicassoLoader

````java
public class PicassoLoader implements ImageLoader {

    static final PicassoLoader INSTANCE = new PicassoLoader();

    private Context context;


    private PicassoLoader() {
    }


    @Override public void init(Context context) {
        this.context = context;
    }


    @Override public void loadImage(String url, ImageView imageView) {
        loadImage(url, imageView);
    }


    @Override public void loadImage(String url, ImageView imageView, ImageOptions options) {
        RequestCreator request = picasso(url);
        if (options != null) {
            if (options.isSet(options.error())) {
                request.error(options.error());
            }
        }
        request.into(imageView);
    }


    /**
     * 获取默认的 Picasso 配置
     */
    private RequestCreator picasso(String url) {
        return Picasso.with(context)
            .load(url)
            .error(R.color.colorPrimary)
            .priority(Picasso.Priority.HIGH);
    }
}
````



​