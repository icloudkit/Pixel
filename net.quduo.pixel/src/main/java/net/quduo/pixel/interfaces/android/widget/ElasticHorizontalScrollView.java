package net.quduo.pixel.interfaces.android.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;


/**
 * 当ScrollView里面的内容很多的时候，感觉卡
 */
public class ElasticHorizontalScrollView extends HorizontalScrollView {


    Context mContext;
    private View mView;
    private Rect mRect = new Rect();
    private float touchX;

    // 最大滑动距离
    private static final int MAX_SCROLL_WIDTH = 200;
    // 阻尼系数
    private static final float SCROLL_RATIO = 0.5f;

    public ElasticHorizontalScrollView(Context context) {
        super(context);
        this.mContext = context;
    }

    public ElasticHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public ElasticHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            this.mView = getChildAt(0);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touchX = event.getX();
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mView == null) {
            return super.onTouchEvent(event);
        } else {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    touchX = event.getX();
                    break;
                case MotionEvent.ACTION_UP:
                    if (isNeedAnimation()) {
                        animation();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    final float preX = touchX;
                    float nowX = event.getX();
                    int deltaX = (int) (preX - nowX);
                    touchX = nowX;
                    if (isNeedMove()) {
                        if (mRect.isEmpty()) {
                            mRect.set(mView.getLeft(), mView.getTop(), mView.getRight(), mView.getBottom());
                        }
                        int offset = mView.getTop() - deltaX;
                        if (offset < MAX_SCROLL_WIDTH && offset > -MAX_SCROLL_WIDTH) {
                            mView.layout(mView.getLeft() - (int) (deltaX * SCROLL_RATIO), mView.getTop(), mView.getRight() - (int) (deltaX * SCROLL_RATIO), mView.getBottom());
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    private boolean isNeedMove() {
        int viewWidth = mView.getMeasuredWidth();
        int scrollWidth = getWidth();
        int offset = viewWidth - scrollWidth;
        int scrollX = getScrollX();
        if (scrollX == 0 || scrollX == offset) {
            return true;
        }
        return false;
    }

    private boolean isNeedAnimation() {
        return !mRect.isEmpty();
    }

    private void animation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, mView.getTop(), mRect.top);
        translateAnimation.setDuration(200);
        mView.startAnimation(translateAnimation);
        mView.layout(mRect.left, mRect.top, mRect.right, mRect.bottom);
        mRect.setEmpty();

    }

    /*
    Context mContext;
    private View mView;
    private float touchX;
    private int scrollX = 0;
    private boolean handleStop = false;
    private int eachStep = 0;

    private static final int MAX_SCROLL_WIDTH = 200;// 最大滑动距离
    private static final float SCROLL_RATIO = 0.4f;// 阻尼系数,越小阻力就越大

    public ElasticHorizontalScrollView(Context context) {
        super(context);
        this.mContext = context;
    }

    public ElasticHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public ElasticHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            this.mView = getChildAt(0);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
            touchX = arg0.getX();
        }
        return super.onInterceptTouchEvent(arg0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mView == null) {
            return super.onTouchEvent(ev);
        } else {
            commonOnTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    private void commonOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                if (mView.getScrollX() != 0) {
                    handleStop = true;
                    animation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float nowX = ev.getX();
                int deltaX = (int) (touchX - nowX);
                touchX = nowX;
                if (isNeedMove()) {
                    int offset = mView.getScrollX();
                    if (offset < MAX_SCROLL_WIDTH && offset > -MAX_SCROLL_WIDTH) {
                        mView.scrollBy((int) (deltaX * SCROLL_RATIO), 0);
                        handleStop = false;
                    }
                }

                break;
            default:
                break;
        }
    }

    private boolean isNeedMove() {
        int viewWidth = mView.getMeasuredWidth();
        int srollWidth = getWidth();
        int offset = viewWidth - srollWidth;
        int scrollX = getScrollX();
        if (scrollX == 0 || scrollX == offset) {
            return true;
        }
        return false;
    }

    private void animation() {
        scrollX = mView.getScrollX();
        eachStep = scrollX / 10;
        resetPositionHandler.sendEmptyMessage(0);
    }

    Handler resetPositionHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (scrollX != 0 && handleStop) {
                scrollX -= eachStep;
                if ((eachStep < 0 && scrollX > 0) ||  (eachStep > 0 && scrollX < 0)) {
                    scrollX = 0;
                }
                mView.scrollTo(scrollX, 0);
                this.sendEmptyMessageDelayed(0, 5);
            }
        };
    };
    */
}
