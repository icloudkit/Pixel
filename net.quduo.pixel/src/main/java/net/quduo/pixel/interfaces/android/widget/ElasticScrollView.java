package net.quduo.pixel.interfaces.android.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * 当ScrollView里面的内容很多的时候，感觉卡
 */
public class ElasticScrollView extends ScrollView {

    /*
    Context mContext;
    private View mView;
    private Rect mRect = new Rect();
    private float touchY;

    // 最大滑动距离
    private static final int MAX_SCROLL_HEIGHT = 200;
    // 阻尼系数
    private static final float SCROLL_RATIO = 0.5f;

    public ElasticScrollView(Context context) {
        super(context);
        this.mContext = context;
    }

    public ElasticScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public ElasticScrollView(Context context, AttributeSet attrs, int defStyle) {
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
            touchY = arg0.getY();
        }
        return super.onInterceptTouchEvent(arg0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mView == null) {
            return super.onTouchEvent(ev);
        } else {
            int action = ev.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    touchY = ev.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    if (isNeedAnimation()) {
                        animation();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    final float preY = touchY;
                    float nowY = ev.getY();
                    int deltaY = (int) (preY - nowY);
                    touchY = nowY;
                    if (isNeedMove()) {
                        if (mRect.isEmpty()) {
                            mRect.set(mView.getLeft(), mView.getTop(), mView.getRight(), mView.getBottom());
                        }
                        int offset = mView.getTop() - deltaY;
                        if (offset < MAX_SCROLL_HEIGHT && offset > -MAX_SCROLL_HEIGHT) {
                            mView.layout(mView.getLeft(), mView.getTop() - (int) (deltaY * SCROLL_RATIO), mView.getRight(), mView.getBottom() - (int) (deltaY * SCROLL_RATIO));
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    private boolean isNeedMove() {
        int viewHeight = mView.getMeasuredHeight();
        int scrollHeight = getHeight();
        int offset = viewHeight - scrollHeight;
        int scrollY = getScrollY();
        if (scrollY == 0 || scrollY == offset) {
            return true;
        }
        return false;
    }

    private boolean isNeedAnimation() {
        return !mRect.isEmpty();
    }

    private void animation() {
        TranslateAnimation ta = new TranslateAnimation(0, 0, mView.getTop(), mRect.top);
        ta.setDuration(200);
        mView.startAnimation(ta);
        mView.layout(mRect.left, mRect.top, mRect.right, mRect.bottom);
        mRect.setEmpty();

    }
    */

    Context mContext;
    private View mView;
    private float touchY;
    private int scrollY = 0;
    private boolean handleStop = false;
    private int eachStep = 0;

    // 最大滑动距离
    private static final int MAX_SCROLL_HEIGHT = 200;
    // 阻尼系数,越小阻力就越大
    private static final float SCROLL_RATIO = 0.4f;

    public ElasticScrollView(Context context) {
        super(context);
        this.mContext = context;
    }

    public ElasticScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public ElasticScrollView(Context context, AttributeSet attrs, int defStyle) {
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
            touchY = event.getY();
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
                case MotionEvent.ACTION_UP:
                    if (mView.getScrollY() != 0) {
                        handleStop = true;
                        // animation
                        scrollY = mView.getScrollY();
                        eachStep = scrollY / 10;
                        resetPositionHandler.sendEmptyMessage(0);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    float nowY = event.getY();
                    int deltaY = (int) (touchY - nowY);
                    touchY = nowY;
                    if (isNeedMove()) {
                        int offset = mView.getScrollY();
                        if (offset < MAX_SCROLL_HEIGHT && offset > -MAX_SCROLL_HEIGHT) {
                            mView.scrollBy(0, (int) (deltaY * SCROLL_RATIO));
                            handleStop = false;
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
        int viewHeight = mView.getMeasuredHeight();
        int scrollHeight = getHeight();
        int offset = viewHeight - scrollHeight;
        int scrollY = getScrollY();
        if (scrollY == 0 || scrollY == offset) {
            return true;
        }
        return false;
    }

    Handler resetPositionHandler = new Handler() {
        public void handleMessage(android.os.Message message) {
            if (scrollY != 0 && handleStop) {
                scrollY -= eachStep;
                if ((eachStep < 0 && scrollY > 0) || (eachStep > 0 && scrollY < 0)) {
                    scrollY = 0;
                }
                mView.scrollTo(0, scrollY);
                this.sendEmptyMessageDelayed(0, 5);
            }
        }

        ;
    };

}
