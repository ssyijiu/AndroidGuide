# Android 编程权威指南阅读笔记

## 第14章：SQLite 数据库 

- 这部分笔记不写 SQLite 了，学习下 [Realm](https://realm.io/cn/docs/java/latest/) 的使用。

- 在 Android 上使用 Realm 数据库

  - Project 下 build.gradle

    ```groovy
    buildscript {
        repositories {
            jcenter()
        }
        dependencies {
            classpath 'com.android.tools.build:gradle:2.3.1'
    	    // 加入这一行
            classpath "io.realm:realm-gradle-plugin:3.1.3"
        }
    }
    ```

  - app 下 build.gradle

    ```groovy
    apply plugin: 'realm-android'
    ```

  - 可以找到 RealmObject 这个类说明 Realm 已经依赖成功了。 

    ![](http://obe5pxv6t.bkt.clouddn.com/realm_succes.png)



