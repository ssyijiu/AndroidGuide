# Android 编程权威指南阅读笔记

## 第11章：使用 ViewPager 

- ViewPager 和 fragment 一起使用

  - ```java
    FragmentManager fragmentManager = getSupportFragmentManager();
    // FragmentStatePagerAdapter 负责 ViewPager 和 Fragment 协同工作
    viewPagerRoot.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
          @Override public Fragment getItem(int position) {
            	Crime crime = crimeList.get(position);
            	return CrimeFragment.newInstance(crime.id,position);
          }


          @Override public int getCount() {
            	return crimeList.size();
          }
    });
    ```

  - ViewPager 默认加载当前屏幕的列表项，以及左右相邻的页面，可以使用 setOffscreenPageLimit(int) 定制预加载左右页面的数量。

  - FragmentStatePagerAdapter 与 FragmentPagerAdapter

    - FragmentStatePagerAdapter 会销毁不需要的 fragment ，事务提交后，FragmentManager 中的 fragment 会被彻底移除。
    - FragmentPagerAdapter 对于不需要的 fragment 会使用 detach 方法，只是销毁了 fragment 的视图，fragment 的实例还保存在 FragmentManager 中，FragmentPagerAdapter 创建 fragment 永远保存在内存中，不会被销毁。
    - FragmentStatePagerAdapter 更省内存，FragmentPagerAdapter 使用与展示少量而固定的 fragment。

  - PageAdapter 的使用：

    ```
    class MyAdapter extends PagerAdapter {

    		/**
    		 * ·µ»ØÓÐ¶àÉÙ¸öpage
    		 */
    		@Override
    		public int getCount() {
    			// TODO Auto-generated method stub
    //			return list.size();
    			return Integer.MAX_VALUE;
    		}

    		/**
    		 * ÅÐ¶ÏinstantiateItemµÄ·µ»ØÊÇ²»ÊÇview
    		 */
    		@Override
    		public boolean isViewFromObject(View view, Object object) {
    			return view == object;
    		}

    		/**
    		 * ³õÊ¼»¯page
    		 */
    		@Override
    		public Object instantiateItem(ViewGroup container/* ViewPager */,
    				int position) {

    			// System.out.println("position=" + position);

    			ImageView imageView = new ImageView(MainActivity.this);
    			imageView.setImageResource(list.get(position%list.size()).icon);

    			// Í¼Æ¬Ìî³äÂú¿Ø¼þ
    			imageView.setScaleType(ScaleType.FIT_XY);

    			container.addView(imageView);

    			return imageView;
    		}

    		/**
    		 * Ïú»Ùpage
    		 */
    		@Override
    		public void destroyItem(ViewGroup container, int position// µ±Ç°Ïú»ÙpageµÄposition
    				, Object object/* childView */) {

    			// System.out.println("position=" + position);
    			container.removeView((View) object);
    		}

    	}
    ```

    ​


    ​			
    ​		
    ​	

    ​


    ​			
    ​	


    ​			
    ​		
    ​	