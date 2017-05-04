## Android 异常踩坑

### RecyclerView 相关

- Cannot call this method while RecyclerView is computing a layout or scrolling

  - 触发场景：onBindViewHolder 中 调用了 notifyDataSetChanged

  - 原因分析：onBindViewHolder 将数据绑定到视图上，此时 RecyclerView 会进行 layout 并进入 lockdown state，在这个状态结束前调用 notifyDataSetChanged 更新视图会在 assertNotInLayoutOrScroll 而触发这个异常。

  - 解决方案：

    ```java
    // 判断 RecyclerView 是否完成 layout
    if(recyclerCrime.isComputingLayout()) {
        // 没有的话使用 Handler 调用 notifyDataSetChanged
        // RecyclerView 的 layout 事件也是 MessageQueue 中的一个 message
        // Handler 执行到我们这个 message 时，肯定前面那个 lauyout 的 message 已经执行完了
      	handler.post(new Runnable() {
            @Override public void run() {
              	adapter.notifyDataSetChanged();
            }
        });
    } else {
        // 完成 layout 正常调用
      	adapter.notifyDataSetChanged();
    }
    ```

    ​

  - 相关链接：[http://stackoverflow.com/questions/27070220/recycleview-notifydatasetchanged-illegalstateexception](http://stackoverflow.com/questions/27070220/recycleview-notifydatasetchanged-illegalstateexception)


### Fragment 相关

- android.os.Handler android.support.v4.app.FragmentHostCallback.getHandler()' on a null object reference

    - v4 包的 bug

    - 解决方案：

        ```java
        // 在 FragmentStatePagerAdapter 重写下面的方法
        @Override
        public void finishUpdate(ViewGroup container) {
            try{
              	super.finishUpdate(container);
            } catch (NullPointerException ignored){
            }
        }
        ```

    - 相关链接：[http://blog.csdn.net/shineflowers/article/details/64125260](http://blog.csdn.net/shineflowers/article/details/64125260)