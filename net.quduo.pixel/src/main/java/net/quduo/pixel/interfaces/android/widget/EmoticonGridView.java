package net.quduo.pixel.interfaces.android.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class EmoticonGridView extends GridView {

    public EmoticonGridView(Context context) {
        super(context);
    }

    public EmoticonGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmoticonGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
