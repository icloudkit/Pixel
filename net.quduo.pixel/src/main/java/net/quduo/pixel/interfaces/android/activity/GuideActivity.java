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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.quduo.pixel.BuildConfig;
import net.quduo.pixel.R;

import java.util.ArrayList;

/**
 * GuideActivity
 *
 * @author hongquanli <hongquanli@qq.com>
 * @version 1.0 2015-01-13 17:6:02
 */
public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener {

    private static final String TAG = GuideActivity.class.getName();

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private ViewPager mViewPager;

    private ArrayList<View> views;

    private ImageView mImageView;
    private ImageView[] indicators;
    private ViewGroup mViewGroup;

    private Button mLoginButton;
    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        // 要获取屏幕的宽和高等参数，首先需要声明一个DisplayMetrics对象，屏幕的宽高等属性存放在这个对象中
        DisplayMetrics DM = new DisplayMetrics();
        // 获取窗口管理器,获取当前的窗口,调用getDefaultDisplay()后，其将关于屏幕的一些信息写进DM对象中,最后通过getMetrics(DM)获取
        getWindowManager().getDefaultDisplay().getMetrics(DM);

        int wdip = px2dip(getApplicationContext(), getApplicationContext().getResources().getDisplayMetrics().widthPixels);
        int hdip = px2dip(getApplicationContext(), getApplicationContext().getResources().getDisplayMetrics().heightPixels);

        LayoutInflater inflater = getLayoutInflater();

        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.layout_guide_item_01, null));
        views.add(inflater.inflate(R.layout.layout_guide_item_02, null));
        views.add(inflater.inflate(R.layout.layout_guide_item_03, null));
        views.add(inflater.inflate(R.layout.layout_guide_item_04, null));
        views.add(inflater.inflate(R.layout.layout_guide_item_05, null));

        indicators = new ImageView[views.size()];

        mViewGroup = (ViewGroup) findViewById(R.id.viewGroup);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        for (int i = 0; i < views.size(); i++) {
            mImageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
            layoutParams.setMargins(8, 0, 8, 20);
            mImageView.setLayoutParams(layoutParams);
            indicators[i] = mImageView;
            mImageView.setBackgroundResource(R.drawable.selector_indicator_view);
            mImageView.setEnabled(false);
            if (i == 0) {
                mImageView.setEnabled(true);
            }
            mViewGroup.addView(mImageView);
        }

        mViewPager.setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(views.get(position % views.size()), 0);
                return views.get(position % views.size());
            }

            @Override
            public void restoreState(Parcelable arg0, ClassLoader arg1) {

            }

            @Override
            public Parcelable saveState() {
                return null;
            }

            @Override
            public void startUpdate(ViewGroup container) {
                super.startUpdate(container);
            }

            @Override
            public void finishUpdate(ViewGroup container) {
                super.finishUpdate(container);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views.get(position % views.size()));
            }

        });
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(0);

        mLoginButton = (Button) findViewById(R.id.login_button);
        mRegisterButton = (Button) findViewById(R.id.register_button);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
                GuideActivity.this.startActivity(intent);
                // 设置切换动画
                overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_close_exit);
                GuideActivity.this.finish();
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(GuideActivity.this, RegisterActivity.class);
                GuideActivity.this.startActivity(intent);
                // 设置切换动画
                overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_close_exit);
                GuideActivity.this.finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_global, menu);
        return true;
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
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // 水平直接滚动800px，如果想效果更平滑可以使用smoothScrollTo(int x, int y)
        // mGuideTitles.get(position).scrollTo(Float.valueOf(800 * positionOffset).intValue(), 0);
    }

    @Override
    public void onPageSelected(int position) {

        if(DEBUG) Log.d(TAG, "你当前选择的是:" + position);
        setIndicatorStatus(position % views.size());
    }

    private void setIndicatorStatus(int selectItems) {
        for (int i = 0; i < indicators.length; i++) {
            if (i == selectItems) {
                indicators[i].setEnabled(true);
            } else {
                indicators[i].setEnabled(false);
            }
        }
    }

    /**
     * Change value from pix to dip
     *
     * @param context
     * @param pxValue
     * @return
     */
    public final static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
