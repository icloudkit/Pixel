Intent intent = new Intent();
intent.setClass(MainActivity.this,SearchActivity.class);
startActivity(intent);


分辨率			icon大小
hdpi(即WVGA)		72x72
mdpi(即HVGA)		48x48
ldpi(即QVGA)		32x32


ifRoom 会显示在Item中，但是如果已经有4个或者4个以上的Item时会隐藏在溢出列表中。当然个数并不仅仅局限于4个，依据屏幕的宽窄而定
never 永远不会显示。只会在溢出列表中显示，而且只显示标题，所以在定义item的时候，最好把标题都带上。
always 无论是否溢出，总会显示。
withText withText值示意Action bar要显示文本标题。Action bar会尽可能的显示这个标题，但是，如果图标有效并且受到Action bar空间的限制，文本标题有可能显示不全。
collapseActionView 声明了这个操作视窗应该被折叠到一个按钮中，当用户选择这个按钮时，这个操作视窗展开。否则，这个操作视窗在默认的情况下是可见的，并且即便在用于不适用的时候，也要占据操作栏的有效空间。一般要配合ifRoom一起使用才会有效果。



方法一：
DisplayMetrics dm = new DisplayMetrics();
getWindowManager().getDefaultDisplay().getMetrics(dm);
int width = dm.widthPixels;
int height = dm.heightPixels;
判断宽高那个大

方法二：
if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
	// 竖屏
} else {
    // 横屏时
}
 设置：
方法一：在AndroidManifest.xml中配置
如果不想让软件在横竖屏之间切换，最简单的办法就是在项目的AndroidManifest.xml中找到你所指定的activity中加上android:screenOrientation属性，他有以下几个参数：
"unspecified":默认值 由系统来判断显示方向.判定的策略是和设备相关的，所以不同的设备会有不同的显示方向. 
"landscape":横屏显示（宽比高要长） 
"portrait":竖屏显示(高比宽要长) 
"user":用户当前首选的方向 
"behind":和该Activity下面的那个Activity的方向一致(在Activity堆栈中的) 
"sensor":有物理的感应器来决定。如果用户旋转设备这屏幕会横竖屏切换。 
"nosensor":忽略物理感应器，这样就不会随着用户旋转设备而更改了（"unspecified"设置除外）。

方法二：在java代码中设置
设置横屏代码：setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
设置竖屏代码：setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
因为横屏有两个方向的横法，而这个设置横屏的语句，如果不是默认的横屏方向，会把已经横屏的屏幕旋转180°。
所以可以先判断是否已经为横屏了，如果不是再旋转，不会让用户觉得转的莫名其妙啦！代码如下：
if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
}


Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.package.address");
startActivity( LaunchIntent );


<?xml version="1.0" encoding="utf-8"?>
<!--
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

-->
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/search_bar"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="horizontal"
    >

    <!-- This is actually used for the badge icon *or* the badge label (or neither) -->
    <TextView
        android:id="@+id/search_badge"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:gravity="center_vertical"
        android:layout_marginBottom="2dip"
        android:drawablePadding="0dip"
        android:textAppearance="?android:attr/textAppearanceMedium"
        android:textColor="?android:attr/textColorPrimary"
        android:visibility="gone"
    />

    <ImageView
        android:id="@+id/search_button"
        style="?android:attr/actionButtonStyle"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_gravity="center_vertical"
        android:src="?android:attr/searchViewSearchIcon"
        android:contentDescription="@string/searchview_description_search"
    />

    <LinearLayout
        android:id="@+id/search_edit_frame"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:layout_gravity="center_vertical"
        android:layout_marginTop="4dip"
        android:layout_marginBottom="4dip"
        android:layout_marginLeft="8dip"
        android:layout_marginRight="8dip"
        android:orientation="horizontal">

        <ImageView
            android:id="@+id/search_mag_icon"
            android:layout_width="@dimen/dropdownitem_icon_width"
            android:layout_height="wrap_content"
            android:scaleType="centerInside"
            android:layout_marginLeft="@dimen/dropdownitem_text_padding_left"
            android:layout_gravity="center_vertical"
            android:src="?android:attr/searchViewSearchIcon"
            android:visibility="gone"
        />

        <!-- Inner layout contains the app icon, button(s) and EditText -->
        <LinearLayout
            android:id="@+id/search_plate"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:layout_gravity="center_vertical"
            android:orientation="horizontal"
            android:background="?android:attr/searchViewTextField">

            <view class="android.widget.SearchView$SearchAutoComplete"
                android:id="@+id/search_src_text"
                android:layout_height="36dip"
                android:layout_width="0dp"
                android:layout_weight="1"
                android:minWidth="@dimen/search_view_text_min_width"
                android:layout_gravity="bottom"
                android:paddingLeft="@dimen/dropdownitem_text_padding_left"
                android:paddingRight="@dimen/dropdownitem_text_padding_right"
                android:singleLine="true"
                android:ellipsize="end"
                android:background="@null"
                android:inputType="text|textAutoComplete|textNoSuggestions"
                android:imeOptions="actionSearch"
                android:dropDownHeight="wrap_content"
                android:dropDownAnchor="@id/search_edit_frame"
                android:dropDownVerticalOffset="0dip"
                android:dropDownHorizontalOffset="0dip"
                android:contentDescription="@string/searchview_description_query"
            />

            <ImageView
                android:id="@+id/search_close_btn"
                android:layout_width="wrap_content"
                android:layout_height="match_parent"
                android:paddingLeft="8dip"
                android:paddingRight="8dip"
                android:layout_gravity="center_vertical"
                android:background="?android:attr/selectableItemBackground"
                android:src="?android:attr/searchViewCloseIcon"
                android:focusable="true"
                android:contentDescription="@string/searchview_description_clear"
            />

        </LinearLayout>

        <LinearLayout
            android:id="@+id/submit_area"
            android:orientation="horizontal"
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:background="?android:attr/searchViewTextFieldRight">
    
            <ImageView
                android:id="@+id/search_go_btn"
                android:layout_width="wrap_content"
                android:layout_height="match_parent"
                android:layout_gravity="center_vertical"
                android:paddingLeft="16dip"
                android:paddingRight="16dip"
                android:background="?android:attr/selectableItemBackground"
                android:src="?android:attr/searchViewGoIcon"
                android:visibility="gone"
                android:focusable="true"
                android:contentDescription="@string/searchview_description_submit"
            />

            <ImageView
                android:id="@+id/search_voice_btn"
                android:layout_width="wrap_content"
                android:layout_height="match_parent"
                android:layout_gravity="center_vertical"
                android:paddingLeft="16dip"
                android:paddingRight="16dip"
                android:src="?android:attr/searchViewVoiceIcon"
                android:background="?android:attr/selectableItemBackground"
                android:visibility="gone"
                android:focusable="true"
                android:contentDescription="@string/searchview_description_voice"
            />
        </LinearLayout>
    </LinearLayout>

</LinearLayout>

private void traversSearchView(View view, int index) {
     if (view instanceof SearchView) {
         SearchView searchView = (SearchView)view;
         for (int i = 0; i < searchView.getChildCount(); ++i) {
             traverseSearchView(searchView.getChildAt(i), i);
         }
     }
     else if (view instanceof LinearLayout) {
         if (/*id value from search_view.xml*/ == view.getId()) {
             //TODO
         }
         LinearLayout linearlayout = (LinearLayout)view;
         for (int i = 0; i < linearlayout.getChildAt(i), i) {
	    traverseSearchView(linearlayout.getChildAt(i), i);
	 }
     }
     else if (view instanceof EditText) {
         if (/*id value from search_view.xml*/ == view.getId()) {
             //TODO
         }
         //TODO
     }
     else if (view instanceof ImageView) {
         if (/*id value from search_view.xml*/ == view.getId()) {
             //TODO
         }
         //TODO
     }
     //other 'else if' clause
 }
 
android:paddingTop="?actionBarSize"


