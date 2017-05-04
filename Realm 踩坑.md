# Android 编程权威指南阅读笔记

## 第14章：SQLite 数据库 

- 这部分笔记不写 SQLite 了，学习下 [Realm](https://realm.io/) 的使用，Realm 有 [中文文档](https://realm.io/cn/docs/java/latest/) 而且十分的详细，这里主要记一下自己使用过程中的一些问题。

### Auto-Updating Objects

当你从 Realm 得到一个 `RealmObject` 或者 `RealmObject` 然后修改它们的时候，会自动再次保存到 Realm 中，不需要你手动调用保存或者插入。

官网的例子：

```java
final Dog myDog;
realm.executeTransaction(new Realm.Transaction() {
    @Override
    public void execute(Realm realm) {
        myDog = realm.createObject(Dog.class);
      	// 下面修改 myDog 后会自动保存到 Realm 
        myDog.setName("Fido");
        myDog.setAge(1);
    }
});

realm.executeTransaction(new Realm.Transaction() {
    @Override
    public void execute(Realm realm) {
      	// 取出 myDog 并赋值给 myPuppy
        Dog myPuppy = realm.where(Dog.class).equalTo("age", 1).findFirst();
        // 修改 myPuppy 后 myDog 也会被修改
      	myPuppy.setAge(2);
    }
});

myDog.getAge(); // => 2
```



### 所有的写操作（添加、修改和删除对象），必须包含在写入事务（transaction）中

由于 Realm 的自动更新特性，在你修改从 Realm 获取的 `RealmObject` 或者 `RealmObject` 时已经执行了写操作，记得放在事物中。

```java
Crime crime = realm.where(Crime.class).equalTo("id", id).findFirst();
etCrimeTitle.addTextChangedListener(new AfterTextWatcher() {
    @Override public void afterTextChanged(final Editable s) {
     
      // crime.title = s.toString(); 已经在执行写操作了，要放在事务中
      RealmUtil.transaction(new Realm.Transaction() {
          @Override public void execute(Realm realm) {
            	crime.title = s.toString();
          }
      });
    }
});
```

### 遍历已经删除的元素会 Crash

我们在 `RealmResults` 中删除了一个元素，紧接着遍历这个 `RealmResults` ， `RealmResults` 的自动更新机制是通过 looper 事件触发，但如果在事件到来之前（删除元素后还未更新）进行遍历，这时会遍历到已经删除的元素会 Crash。

官网的例子：

```java
// Crash
final RealmResults<User> users = getUsers();
realm.executeTransaction(new Realm.Transaction() {
    @Override
    public void execute(Realm realm) {
        users.get(0).deleteFromRealm(); // indirectly delete object
    }
});

for (User user : users) {
    showUser(user); // Will crash for the deleted user
}


// 解决办法
final RealmResults<User> users = getUsers();
realm.executeTransaction(new Realm.Transaction() {
    @Override
    public void execute(Realm realm) {
        users.deleteFromRealm(0); // Delete and remove object directly
    }
});

for (User user : users) {
    showUser(user); // Deleted user will not be shown
}
```

### 异步查询

```java
// 1. 创建异步查询
public RealmResults<Crime> queryAllCrimesAsync() {
  	// 请注意，这里的调用并不会阻塞，而是立即返回一个 RealmResults<User>
    // 查询将会在后台线程中被执行，当其完成时，之前返回的 RealmResults 实例会被更新。
  	// 虽然这个方法不会阻塞，但是获取的 RealmResults 不能立刻使用 ..
  	return realm.where(Crime.class).findAllAsync();
}

// 2. 注册异步查询回调
List<Crime> mDatas = new ArrayList<>();
listener = new RealmChangeListener<RealmResults<Crime>>() {
  
    // 这个函数在 插入、删除、修改后改变了查询结果 都会调用
    @Override public void onChange(RealmResults<Crime> element) {
        mDatas.clear();
        mDatas.addAll(element);
        updateUI();  // 更新 UI
    }
};
queryAllCrimesAsync().addChangeListener(listener);

// 3. 移除回调
@Override public void onDestroy() {
  	super.onDestroy();
  	realm.removeAllChangeListeners();
}

```



### 增删改查​的代码

```java
   /**
     * 查询所有
     */
    public RealmResults<Crime> queryAllCrimes() {
        return realm.where(Crime.class).findAll();
    }


    /**
     * 异步查询所有
     */
    public RealmResults<Crime> queryAllCrimesAsync() {
        return realm.where(Crime.class).findAllAsync();
    }


    /**
     * 根据 id 查询 Item
     */
    public Crime getCrime(String id) {
        return realm.where(Crime.class).equalTo("id", id).findFirst();
    }


    /**
     * 插入 or 更新
     */
    public void insertOrUpdateCrime(final Crime crime) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(crime);
            }
        });
    }

    /**
     * 删除
     */
    public void deleteCrime(final String id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                RealmResults<Crime> crimes = queryAllCrimes();
                int indexOf = crimes.indexOf(getCrime(id));
                crimes.deleteFromRealm(indexOf);
            }
        });
    }
```

