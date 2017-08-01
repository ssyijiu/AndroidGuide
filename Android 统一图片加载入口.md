### Android 统一图片加载入口，Picasso、Glide、Fresco 任意切换

#### 为什么要统一图片加载入口

推荐看下这篇文章：[如何正确使用开源项目？](http://stormzhang.com/android/2016/05/08/how-to-choose-open-source-project/)

我曾经反编译一个 apk，发现里面用了 UniversalImageLoader、Picasso、Glide 三个图片加载框架。

当时的场景可能是这样的，项目开始时选择了当时流行的 UniversalImageLoader，后来发现UniversalImageLoader 不再维护了，换成了 Picasso，再后来随着项目的推进 Picasso 有点满足不了现在的需求，于是又引入了功能更强大加载速度更快的 Glide，但是前面 UniversalImageLoader、Picasso 已经大量使用，又没有人去推动移除它们，于是出现了一个项目中三个图片加载框架并存的局面。（话说如果没有测试支持，作为一个程序员你敢随便修改代码吗？尤其是那些写的非常乱、牵一发动全身的项目）





​