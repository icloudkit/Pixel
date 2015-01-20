package net.quduo.pixel.interfaces.android.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * 使用weight来自动调整ViewPager的高度
 * <android.support.v4.view.ViewPager
 *  android:id="@+id/pager"
 *  android:layout_width="match_parent"
 *  android:layout_height="0dp"
 *  android:layout_weight="1.0" />
 * 通过获取view的高度，动态的设置ViewPager的高度等于view的高度
 * <code>
 * int viewPagerIndex = main.indexOf(viewPager);
 * // 获取ViewPager的子View的高度
 * int childViewHeight = getChildViewHeight();
 * // 这里设置params的高度
 * LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, childViewHeight );
 * main.removeView(viewPager);
 * // 使用这个params
 * main.addView(viewPager, viewPagerIndex , params);
 * </code>
 */
public class WrapContentHeightViewPager extends ViewPager {

    public WrapContentHeightViewPager(Context context) {
        super(context);
    }

    public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        //下面遍历所有child的高度
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            // 采用最大的view的高度。
            if (h > height) {
                height = h;
            }
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}