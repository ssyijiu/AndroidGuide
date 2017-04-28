# Android 编程权威指南阅读笔记

## 第14章：SQLite 数据库 

- 这部分笔记不写 SQLite 了，学习下 [Realm](https://realm.io/) 的使用，然而 Realm 的文档狮子

- 自动更新

- 写操作必须放在事物里。

- ```java
  CrimeLab.instance().queryAllCrimesAsync().addChangeListener(
              new RealmChangeListener<RealmResults<Crime>>() {
                  @Override public void onChange(RealmResults<Crime> element) {
                      
                    	// 这个方法之间查询的数据变了（增、删、修改改变了查询结果）就会回调
                    	mDatas.clear();
                      mDatas.addAll(element);
                      updateUIAsync();
                      MLog.i("realm","queryAllCrimesAsync");
                  }
              });
              
              
  ```

- ​
