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

package net.quduo.pixel.interfaces.android.widget;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import net.quduo.pixel.BuildConfig;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class FragmentViewPager extends ViewPager {

    private static final String TAG = FragmentViewPager.class.getName();

    private static final boolean DEBUG = BuildConfig.DEBUG;

    /**
     * 最大的缩小比例
     */
    // private static final float SCALE_MAX = 0.5f;

    // private float mTrans;
    // private float mScale;

    /**
     * 保存position与对于的View
     */
    private HashMap<Integer, Fragment> mChildrenViews = new LinkedHashMap<Integer, Fragment>();

    private HashMap<Integer, View> mItemViews = new LinkedHashMap<Integer, View>();

    /**
     * 滑动时左边的元素
     */
    private Fragment mLeft;
    /**
     * 滑动时右边的元素
     */
    private Fragment mRight;

    public FragmentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        if (DEBUG)
            Log.d(TAG, "position=" + position + ", positionOffset = " + positionOffset + " ,positionOffsetPixels =  " + positionOffsetPixels + " , currentPos = " + getCurrentItem());

        //滑动特别小的距离时，我们认为没有动，可有可无的判断
        float effectOffset = isSmall(positionOffset) ? 0 : positionOffset;

        //获取左边的View
        mLeft = findViewFromObject(position);
        if (DEBUG) Log.d(TAG, "mLeft:" + mLeft);
        //获取右边的View
        mRight = findViewFromObject(position + 1);
        if (DEBUG) Log.d(TAG, "mLeft:" + mRight);

        View leftItem = getIndicatorItemView(position);
        View rightItem = getIndicatorItemView(position + 1);

        if (effectOffset > 0) {

            // 添加切换动画效果
            if (leftItem != null && rightItem != null) {
                animateStack(leftItem, rightItem, effectOffset, positionOffsetPixels);
            }
            if (mLeft != null && mRight != null) {
                animateStack(mLeft.getView(), mRight.getView(), effectOffset, positionOffsetPixels);
            }
        }
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
        // Make sure we're running on HONEYCOMB or higher to use APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Fragment fragment = findViewFromObject(item);
            if (fragment != null && fragment.getView() != null) {
                fragment.getView().setAlpha(1);
            }
        }
    }

    /**
     * 设置当前位置View
     *
     * @param fragment
     * @param position
     */
    public void setObjectForPosition(int position, Fragment fragment) {
        this.mChildrenViews.put(position, fragment);
    }

    /**
     * 通过过位置获得对应的View
     *
     * @param position
     * @return
     */
    public Fragment findViewFromObject(int position) {
        return mChildrenViews.get(position);
    }

    public HashMap<Integer, Fragment> getChildrenViews() {
        return mChildrenViews;
    }

    public View getIndicatorItemView(int position) {
        return mItemViews.get(position);
    }

    public void setIndicatorItemView(int position, View view) {
        this.mItemViews.put(position, view);
    }

    private boolean isSmall(float positionOffset) {
        return Math.abs(positionOffset) < 0.0001;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void animateStack(View left, View right, float effectOffset, int positionOffsetPixels) {
        /*
        if (right != null) {
            // 缩小比例 如果手指从右到左的滑动（切换到后一个）：0.0~1.0，即从一半到最大
            // 如果手指从左到右的滑动（切换到前一个）：1.0~0，即从最大到一半
            mScale = (1 - SCALE_MAX) * effectOffset + SCALE_MAX;
            // x偏移量： 如果手指从右到左的滑动（切换到后一个）：0-720 如果手指从左到右的滑动（切换到前一个）：720-0
            mTrans = -getWidth() - getPageMargin() + positionOffsetPixels;
            ViewHelper.setScaleX(right, mScale);
            ViewHelper.setScaleY(right, mScale);
            ViewHelper.setTranslationX(right, mTrans);
        }
        if (left != null) {
            left.bringToFront();
        }
        */

        // Make sure we're running on HONEYCOMB or higher to use APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (left != null && right != null) {
                if (DEBUG) Log.d(TAG, "effectOffset:" + effectOffset);
                left.setAlpha(1 - effectOffset);
                right.setAlpha(effectOffset);
            }
        }
    }

}