### 第 25章：搜索

- SearchView 是一个操作视图，所谓操作视图，就是内置在工具栏中的视图。

- 先声明一个 menu 文件，指定 app:actionViewClass="android.support.v7.widget.SearchView"

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <menu
      xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto">

      <item
          android:id="@+id/menu_item_search"
          android:title="@string/search"
          app:actionViewClass="android.support.v7.widget.SearchView"
          app:showAsAction="always"/>

      <item
          android:id="@+id/menu_item_clear"
          android:title="@string/clear_search"
          app:showAsAction="ifRoom"/>

  </menu>
  ```

- 开启 OptionsMenu (也可在自己的 Toolbar 中使用)

  ```java
  setHasOptionsMenu(true);
  ```

- 重写 onCreateOptionsMenu

  ```java
  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
          super.onCreateOptionsMenu(menu, inflater);
          inflater.inflate(R.menu.meun_search, menu);
          MenuItem menuItem = menu.findItem(R.id.menu_item_search);
          final SearchView searchView = (SearchView) menuItem.getActionView();
          
          // 点击搜索加载以前的搜索关键字
          searchView.setOnSearchClickListener(new View.OnClickListener() {
              @Override public void onClick(View v) {
                  searchView.setQuery(loadQueryKey(), false);
              }
          });
    
          searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
              @Override public boolean onQueryTextSubmit(String query) {
                  MLog.i("onQueryTextSubmit:" + query);
                  search(query);  // 搜索
                  saveQueryKey(query);   // 保存搜索的关键字
                  searchView.setIconified(true);  // 点击 x 清空关键词
          	    searchView.setIconified(true);  // 再次点击 x 关闭 searchView 视图
                  return true;
              }
            
              @Override public boolean onQueryTextChange(String newText) {
              	MLog.i("onQueryTextChange:" + newText);
              	return false;
            	}
          });
  }
  ```


- 创建 SharedPreferences

  ```java
  PreferenceManager.getDefaultSharedPreferences(context);
  context.getSharedPreferences("sp_name", Context.MODE_PRIVATE);
  ```

  ​