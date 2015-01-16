/*
 * Copyright (C) 2014 The Android Open Source Project
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

package net.quduo.pixel.interfaces.android.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.quduo.pixel.BuildConfig;
import net.quduo.pixel.R;
import net.quduo.pixel.interfaces.android.MainApplication;
import net.quduo.pixel.interfaces.android.fragment.ChatFragment;
import net.quduo.pixel.interfaces.android.fragment.ContactFragment;
import net.quduo.pixel.interfaces.android.fragment.DiscoveryFragment;
import net.quduo.pixel.interfaces.android.fragment.NearbyFragment;
import net.quduo.pixel.interfaces.android.fragment.UserFragment;
import net.quduo.pixel.interfaces.android.widget.FragmentViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * implements NavigationDrawerFragment.NavigationDrawerCallbacks
 * ActionBarActivity
 *
 * @author hongquanli <hongquanli@qq.com>
 * @version 1.0 2015-01-13 17:6:02
 */
public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, ContactFragment.OnFragmentInteractionListener, DiscoveryFragment.OnFragmentInteractionListener, NearbyFragment.OnFragmentInteractionListener, UserFragment.OnFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getName();

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static final String SHARED_PREFERENCES_NAME = "first_pref";

    private SharedPreferences preferences;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    // private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private MainApplication mApp;

    private Typeface typeface;

    // private ActionBar mActionBar;

    private Button mSearchActionitem, mPlusActionItem;
    private ArrayList<HashMap<String, Object>> mPopupWindowListItem;

    private boolean isScrolled = false;
    private FragmentViewPager mViewPager;
    private int mSelectPosition = 0;

    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private List<TextView> mToolbarMenuSelectedItems = new ArrayList<TextView>();
    private TextView mChatMenuIcon, mChatMenuIconSelected;
    private TextView mContactMenuIcon, mContactMenuIconSelected;
    private TextView mDiscoveryMenuIcon, mDiscoveryMenuIconSelected;
    private TextView mNearbyMenuIcon, mNearbyMenuIconSelected;
    private TextView mUserMenuIcon, mUserMenuIconSelected;

    private RelativeLayout mChatMenuItem, mContactMenuItem, mDiscoveryMenuItem, mNearbyMenuItem, mUserMenuItem;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.chat_menu_item:
                    if (mSelectPosition != 0) {
                        setSelectedItem(0);
                        // True to smoothly scroll to the new item, false to transition immediately
                        mViewPager.setCurrentItem(0, false);
                    }
                    break;
                case R.id.contact_menu_item:
                    if (mSelectPosition != 1) {
                        setSelectedItem(1);
                        mViewPager.setCurrentItem(1, false);
                    }
                    break;
                case R.id.discovery_menu_item:
                    if (mSelectPosition != 2) {
                        setSelectedItem(2);
                        mViewPager.setCurrentItem(2, false);
                    }
                    break;
                case R.id.nearby_menu_item:
                    if (mSelectPosition != 3) {
                        setSelectedItem(3);
                        mViewPager.setCurrentItem(3, false);
                    }
                    break;
                case R.id.user_menu_item:
                    if (mSelectPosition != 4) {
                        setSelectedItem(4);
                        mViewPager.setCurrentItem(4, false);
                    }
                    break;
                case R.id.search_action_item:
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    MainActivity.this.startActivity(intent);
                    MainActivity.this.finish();
                    // 设置切换动画
                    overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_close_exit);
                    break;
                case R.id.plus_action_item:
                    /*
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), mPlusActionItem);
                        popupMenu.getMenuInflater().inflate(R.menu.menu_global, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    popupMenu.dismiss();
                                }
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                    */
                    LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                    // 引入窗口配置文件 
                    View view = inflater.inflate(R.layout.layout_popup_menu, null);
                    // 创建PopupWindow对象 
                    final PopupWindow popupWindow = new PopupWindow(view, 560, ViewGroup.LayoutParams.WRAP_CONTENT, false);
                    // 需要设置一下此参数，点击外边可消失
                    // new ColorDrawable(0) getResources().getDrawable(R.drawable.popup_window_background)
                    popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_dropdown_panel_pi));
                    // 设置点击窗口外边窗口消失 
                    popupWindow.setOutsideTouchable(true);
                    // 设置此参数获得焦点，否则无法点击 
                    popupWindow.setFocusable(true);
                    // 设置动画样式
                    // popupWindow.setAnimationStyle(R.style.PopupAnimation);
                    // 设置窗口消失事件
                    // popupWindow.setOnDismissListenerd(new PopupWindow.OnDismissListener(){});
                    /*
                    // 点击PopupWindow区域外部,PopupWindow消失
                    popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                                popupWindow.dismiss();
                                return true;
                            }
                            return false;
                        }
                    });
                    */
                    if (popupWindow.isShowing()) {
                        // 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏 
                        popupWindow.dismiss();
                    } else {


                        // SimpleAdapter mSimpleAdapter = new SimpleAdapter(getApplicationContext(), mPopupWindowListItem, R.layout.main_popup_menu_item, new String[]{"item_icon", "item_title"}, new int[]{R.id.popup_menu_item_icon, R.id.popup_menu_item_title});

                        BaseAdapter mSimpleAdapter = new BaseAdapter() {

                            @Override
                            public int getCount() {
                                return mPopupWindowListItem.size();
                            }

                            @Override
                            public Object getItem(int i) {
                                return null;
                            }

                            @Override
                            public long getItemId(int i) {
                                return 0;
                            }

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {

                                if (convertView == null) {
                                    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                                    convertView = inflater.inflate(R.layout.list_item_popup_menu, parent, false);
                                }
                                TextView popupMenuItemIcon = (TextView) convertView.findViewById(R.id.popup_menu_item_icon);
                                if (DEBUG)
                                    Log.d(TAG, "item_icon:" + mPopupWindowListItem.get(position).get("item_icon"));
                                popupMenuItemIcon.setTypeface(typeface);
                                // if (popupMenuItemIcon.getTypeface() != typeface) {
                                //    popupMenuItemIcon.setTypeface(typeface);
                                // }
                                popupMenuItemIcon.setText(Integer.valueOf(mPopupWindowListItem.get(position).get("item_icon").toString()));

                                TextView popupMenuItemTitle = (TextView) convertView.findViewById(R.id.popup_menu_item_title);
                                popupMenuItemTitle.setText(mPopupWindowListItem.get(position).get("item_title").toString());

                                return convertView;
                            }
                        };

                        ListView mPopupMenuListView = (ListView) view.findViewById(R.id.main_popup_menu_list_view);
                        mPopupMenuListView.setAdapter(mSimpleAdapter);

                        // 显示窗口
                        // 设置显示PopupWindow的位置位于View的右下方，x,y表示坐标偏移量
                        popupWindow.showAsDropDown(v, -460, 0);
                        // （以某个View为参考）,表示弹出窗口以parent组件为参考，位于左侧，偏移-90。
                        // Gravity.TOP|Gravity.LEFT, 0, 150
                        // popupWindow.showAtLocation(v, Gravity.RIGHT, 0, 0);
                        popupWindow.update();
                    }
                    break;
            }
        }
    };

    private FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public Fragment getItem(int position) {

            // FragmentTransaction ft = getFragmentManager().beginTransaction();
            // ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            // ft.addToBackStack("detail");

            if (DEBUG) Log.d(TAG, "Fragment:" + mFragments.get(position));
            mViewPager.setObjectForPosition(position, mFragments.get(position));
            mViewPager.setIndicatorItemView(position, mToolbarMenuSelectedItems.get(position));
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    };

    private ViewPager.SimpleOnPageChangeListener mSimpleOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);

            if (positionOffset > 0) {
                isScrolled = true;
                for (Map.Entry<Integer, Fragment> fragmentEntry : mViewPager.getChildrenViews().entrySet()) {
                    if (fragmentEntry.getValue() instanceof ContactFragment) {
                        ((ContactFragment) fragmentEntry.getValue()).onScrolled(isScrolled);
                    }
                }
            } else {
                isScrolled = false;
                if (mViewPager.findViewFromObject(position) instanceof ContactFragment) {
                    ((ContactFragment) mViewPager.findViewFromObject(position)).onScrolled(isScrolled);
                }
            }

        }

        @Override
        public void onPageSelected(int position) {
            setSelectedItem(position);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // #去标题栏
        // getWindow().requestFeature(Window.FEATURE_PROGRESS);
        // // 自定义标题栏
        // this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        // // // 隐藏标题栏
        // // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // setContentView(R.layout.activity_main);
        // this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_title);

        setContentView(R.layout.activity_main);

        // #获得Application对象
        mApp = (MainApplication) getApplication();
        // 获取进程中的全局变量值，看是否是初始化值
        if (DEBUG) Log.d(TAG, "Application 初始值:" + mApp.getValue());
        // 重新设置值
        mApp.setValue("Harvey Ren");
        // 再次获取进程中的全局变量值，看是否被修改
        if (DEBUG) Log.d(TAG, "Application 修改后:" + mApp.getValue());

        // #获取当前系统语言及地区,并更改语言
        // a.首先获得当前的语言或者国家
        String able = getResources().getConfiguration().locale.getCountry();
        // b.进行判断：如果是中文则返回的able.equals("CN")
        // c.进行设置的代码为 选择中文
        Configuration config = getResources().getConfiguration();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        config.locale = Locale.SIMPLIFIED_CHINESE;
        getResources().updateConfiguration(config, dm);

        // #清除通知
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(0);

        // #竖屏/横屏
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // TODO 竖屏......
        } else {
            // TODO 横屏......
        }

        // setupActionBar();

        /*
        // #drawer
        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
        */

        initViews();

        if (savedInstanceState != null && savedInstanceState.containsKey("main_status")) {
            mSelectPosition = savedInstanceState.getInt("main_status");
        }

        preferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        /*
        // 设置已经引导过了，下次启动不用再次引导
        SharedPreferences.Editor editor = preferences.edit();
        // 存入数据
        editor.putInt("main_toolbar_selected_index", 0);
        // 提交修改
        editor.commit();
        */


        mPopupWindowListItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        // map1.put("item_image", R.drawable.icon_message);
        map1.put("item_icon", R.string.icon_chat_add_group);
        // map1.put("item_text", "这是第" + i + "行");
        map1.put("item_title", "发起群聊");
        mPopupWindowListItem.add(map1);

        HashMap<String, Object> map2 = new HashMap<String, Object>();
        // map2.put("item_image", R.drawable.icon_message);
        map2.put("item_icon", R.string.icon_friend_add);
        // map2.put("item_text", "这是第" + i + "行");
        map2.put("item_title", "添加朋友");
        mPopupWindowListItem.add(map2);

        HashMap<String, Object> map3 = new HashMap<String, Object>();
        // map3.put("item_image", R.drawable.icon_message);
        map3.put("item_icon", R.string.icon_scan);
        // map3.put("item_text", "这是第" + i + "行");
        // 扫一扫
        map3.put("item_title", "信息扫描");
        mPopupWindowListItem.add(map3);

        HashMap<String, Object> map4 = new HashMap<String, Object>();
        // map4.put("item_image", R.drawable.icon_message);
        map4.put("item_icon", R.string.icon_mail);
        // map4.put("item_text", "这是第" + i + "行");
        map4.put("item_title", "帮助反馈");
        mPopupWindowListItem.add(map4);

    }

    /*
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        // Save its state
        outState.putInt("main_status", mSelectPosition);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onSaveInstanceState(outState, outPersistentState);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        int position = -1;
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("main_status")) {
                position = savedInstanceState.getInt("main_status", -1);
            }
        }
        // myListView.setSelection(pos);
    }
    */

    @Override
    protected void onStart() {
        super.onStart();

        // 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
        mSelectPosition = preferences.getInt("main_toolbar_selected_index", 0);

        mViewPager.setCurrentItem(mSelectPosition, false);
        setSelectedItem(mSelectPosition);
    }

    /*
    @Override
    protected void onPause(){
        super.onPause();

        // Get the activity preferences object.
        SharedPreferences uiState = getPreferences(0);

        // Get the preferences editor.
        SharedPreferences.Editor editor = uiState.edit();

        // Add the UI state preference values.
        editor.putInt("main_status", mSelectPosition);

        // Commit the preferences.
        editor.commit();
    }

    private void restoreViewState() {
        // Get the activity preferences object.
        SharedPreferences settings = getPreferences(Activity.MODE_PRIVATE);

        // Read the UI state values, specifying default values.
        Integer status = settings.getInt("main_status", mSelectPosition);
        // Boolean adding = settings.getBoolean(ADDING_ITEM_KEY, false);

        // Restore the UI to the previous state.
        // if (adding) {
        //     addNewItem();
        // }
    }
    */

    /*
    private void setupActionBar() {
        // #Setup actionbar
        mActionBar = getSupportActionBar();

        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

        mActionBar.setDefaultDisplayHomeAsUpEnabled(false);
        // 左侧图标点击事件使能
        mActionBar.setHomeButtonEnabled(true);
        // 使左上角图标(系统)是否显示
        mActionBar.setDisplayShowHomeEnabled(false);
        // 返回箭头（默认不显示）
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.setDisplayUseLogoEnabled(false);
        // 显示标题
        mActionBar.setDisplayShowTitleEnabled(false);
        // }

        // 显示自定义视图
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        //*
        LinearLayout actionBarLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, 0);
        layoutParams.gravity = Gravity.LEFT;
        actionBarLinearLayout.setLayoutParams(layoutParams);

        View actionBarButtons = getLayoutInflater().inflate(R.layout.main_action, actionBarLinearLayout, false);
        //*

        // View actionBarButtons = getLayoutInflater().inflate(R.layout.main_action, new LinearLayout(this), false);
        // View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.main_action, null);
        // mActionBar.setCustomView(actionBarButtons);
        //
        // ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        // layoutParams.setMargins(0, 0, 0, 0);
        // mActionBar.setCustomView(getLayoutInflater().inflate(R.layout.main_action, null), layoutParams);
        mActionBar.setCustomView(R.layout.main_action);

        /// if(DEBUG) Log.d(TAG, mActionBar.set)

        // mActionBar.setTitle(getString(R.string.app_name));
        // mActionBar.setTitle(new StringBuilder(getTitle()).append("(19)").toString());
        // logo bar_icon
        // mActionBar.setLogo(R.drawable.ic_launcher);

        // // 更改背景
        // Resources resources = getApplicationContext().getResources();
        // Drawable drawable = resources.getDrawable(R.drawable.abc_ab_share_pack_holo_dark);
        // mActionBar.setBackgroundDrawable(drawable);

        // // hide()隐藏Action Bar, 自然也有show()
        // mActionBar.hide();

        mTitle = new StringBuilder(getTitle()).append("(19)").toString();
    }
    */

    private void initViews() {

        // 初始化内容页
        ChatFragment chatFragment = new ChatFragment();
        ContactFragment contactFragment = new ContactFragment();
        DiscoveryFragment discoveryFragment = new DiscoveryFragment();
        NearbyFragment nearbyFragment = new NearbyFragment();
        UserFragment userFragment = new UserFragment();

        mFragments.add(chatFragment);
        mFragments.add(contactFragment);
        mFragments.add(discoveryFragment);
        mFragments.add(nearbyFragment);
        mFragments.add(userFragment);

        typeface = Typeface.createFromAsset(getAssets(), "font/icon/iconfont.ttf");

        // 顶部 Action 菜单
        // mSearchActionitem = (Button) mActionBar.getCustomView().findViewById(R.id.search_action_item);
        mSearchActionitem = (Button) findViewById(R.id.search_action_item);
        mSearchActionitem.setOnClickListener(mOnClickListener);
        // mPlusActionItem = (Button) mActionBar.getCustomView().findViewById(R.id.plus_action_item);
        mPlusActionItem = (Button) findViewById(R.id.plus_action_item);
        mPlusActionItem.setOnClickListener(mOnClickListener);
        mSearchActionitem.setTypeface(typeface);
        mPlusActionItem.setTypeface(typeface);

        mChatMenuItem = (RelativeLayout) findViewById(R.id.chat_menu_item);
        mContactMenuItem = (RelativeLayout) findViewById(R.id.contact_menu_item);
        mDiscoveryMenuItem = (RelativeLayout) findViewById(R.id.discovery_menu_item);
        mNearbyMenuItem = (RelativeLayout) findViewById(R.id.nearby_menu_item);
        mUserMenuItem = (RelativeLayout) findViewById(R.id.user_menu_item);

        mChatMenuItem.setOnClickListener(mOnClickListener);
        mContactMenuItem.setOnClickListener(mOnClickListener);
        mDiscoveryMenuItem.setOnClickListener(mOnClickListener);
        mNearbyMenuItem.setOnClickListener(mOnClickListener);
        mUserMenuItem.setOnClickListener(mOnClickListener);

        // 底部导航菜单 Item
        mChatMenuIcon = (TextView) findViewById(R.id.chat_menu_icon);
        mChatMenuIconSelected = (TextView) findViewById(R.id.chat_menu_icon_selected);
        mContactMenuIcon = (TextView) findViewById(R.id.contact_menu_icon);
        mContactMenuIconSelected = (TextView) findViewById(R.id.contact_menu_icon_selected);
        mDiscoveryMenuIcon = (TextView) findViewById(R.id.discovery_menu_icon);
        mDiscoveryMenuIconSelected = (TextView) findViewById(R.id.discovery_menu_icon_selected);
        mNearbyMenuIcon = (TextView) findViewById(R.id.nearby_menu_icon);
        mNearbyMenuIconSelected = (TextView) findViewById(R.id.nearby_menu_icon_selected);
        mUserMenuIcon = (TextView) findViewById(R.id.user_menu_icon);
        mUserMenuIconSelected = (TextView) findViewById(R.id.user_menu_icon_selected);

        // 设置字体
        mChatMenuIcon.setTypeface(typeface);
        mChatMenuIconSelected.setTypeface(typeface);
        mContactMenuIcon.setTypeface(typeface);
        mContactMenuIconSelected.setTypeface(typeface);
        mDiscoveryMenuIcon.setTypeface(typeface);
        mDiscoveryMenuIconSelected.setTypeface(typeface);
        mNearbyMenuIcon.setTypeface(typeface);
        mNearbyMenuIconSelected.setTypeface(typeface);
        mUserMenuIcon.setTypeface(typeface);
        mUserMenuIconSelected.setTypeface(typeface);

        mToolbarMenuSelectedItems.add(mChatMenuIconSelected);
        mToolbarMenuSelectedItems.add(mContactMenuIconSelected);
        mToolbarMenuSelectedItems.add(mDiscoveryMenuIconSelected);
        mToolbarMenuSelectedItems.add(mNearbyMenuIconSelected);
        mToolbarMenuSelectedItems.add(mUserMenuIconSelected);

        mViewPager = (FragmentViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mViewPager.setOnPageChangeListener(mSimpleOnPageChangeListener);
        /*
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (mViewPager.findViewFromObject(position) instanceof ContactFragment) {
                    ((ContactFragment) mViewPager.findViewFromObject(position)).mLetterQueryView.setVisibility(View.GONE);
                } else if (mViewPager.findViewFromObject(position + 1) instanceof ContactFragment) {
                    ((ContactFragment) mViewPager.findViewFromObject(position + 1)).mLetterQueryView.setVisibility(View.GONE);
                }
                return true;
            }
        });
        */
    }

    private void setSelectedItem(int position) {

        // Make sure we're running on HONEYCOMB or higher to use APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            for (int i = 0; i < mToolbarMenuSelectedItems.size(); i++) {
                mToolbarMenuSelectedItems.get(i).setAlpha(0);
            }
            mToolbarMenuSelectedItems.get(position).setAlpha(1);
        }
        mSelectPosition = position;

        // 设置已经引导过了，下次启动不用再次引导
        SharedPreferences.Editor editor = preferences.edit();
        // 存入数据
        editor.putInt("main_toolbar_selected_index", mSelectPosition);
        // 提交修改
        editor.commit();
    }

    /*
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(position)).commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            default:
        }
    }
    */

    public void restoreActionBar() {
        // ActionBar mActionBar = getSupportActionBar();
        // mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        // mActionBar.setDisplayShowTitleEnabled(false);
        // mActionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // *if (!mNavigationDrawerFragment.isDrawerOpen()) {
        // Only show items in the action bar relevant to this screen
        // if the drawer is not showing. Otherwise, let the drawer
        // decide what to show in the action bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        restoreActionBar();
        // *return true;
        // *}
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*
        // 退出
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Are you sure you want to exit? Yes No
        builder.setMessage("你确定要退出?").setCancelable(false).setPositiveButton("是", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                MainActivity.this.finish();
            }
        }).setNegativeButton("否", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        */
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(String id) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            // *((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
