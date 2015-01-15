package net.quduo.pixel.interfaces.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.quduo.pixel.R;

public class DragDropListView extends ListView implements AbsListView.OnScrollListener {

    private boolean isDropDownStyle = true;
    private boolean isOnBottomStyle = true;
    private boolean isAutoLoadOnBottom = false;

    private String mTopDefaultText;
    private String mTopPullText;
    private String mTopReleaseText;
    private String mTopLoadingText;
    private String mBottomDefaultText;
    private String mBottomLoadingText;
    private String mBottomNoMoreText;

    private Context context;

    /**
     * top layout view *
     */
    private RelativeLayout mTopLayout;
    private ImageView mTopImageView;
    private ProgressBar mTopProgressBar;
    private TextView mTopTitleTextView;
    private TextView mTopSubTitleTextView;

    /**
     * bottom layout view *
     */
    private RelativeLayout mBottomLayout;
    private ProgressBar mBottomProgressBar;
    private Button mBottomButton;

    private OnDropDownListener onDropDownListener;
    private OnScrollListener onScrollListener;

    /**
     * rate about drop down distance and header padding top when drop down *
     */
    private float mTopPaddingTopRate = 1.5f;
    /**
     * min distance which header can release to loading *
     */
    private int mTopReleaseMinDistance;

    /**
     * whether bottom listener has more *
     */
    private boolean hasMore = true;
    /**
     * whether show bottom loading progress bar when loading *
     */
    private boolean isShowBottomProgressBar = true;
    /**
     * whether show bottom when no more data *
     */
    private boolean isShowBottomWhenNoMore = false;

    private int currentScrollState;
    private int currentTopStatus;

    /**
     * whether reached top, when has reached top, don't show header layout *
     */
    private boolean hasReachedTop = false;

    /**
     * image flip animation *
     */
    private RotateAnimation flipAnimation;
    /**
     * image reverse flip animation *
     */
    private RotateAnimation reverseFlipAnimation;

    /**
     * header layout original height *
     */
    private int topOriginalHeight;
    /**
     * header layout original padding top *
     */
    private int topOriginalTopPadding;
    /**
     * y of point which user touch down *
     */
    private float actionDownPointY;

    /**
     * whether is on bottom loading *
     */
    private boolean isOnBottomLoading = false;

    public DragDropListView(Context context) {
        super(context);
        init(context);
    }

    public DragDropListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
        init(context);
    }

    public DragDropListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getAttrs(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        initDropDownStyle();
        initOnBottomStyle();

        // should set, to run onScroll method and so on
        super.setOnScrollListener(this);
    }

    /**
     * init drop down style, only init once
     */
    private void initDropDownStyle() {
        if (mTopLayout != null) {
            if (isDropDownStyle) {
                addHeaderView(mTopLayout);
            } else {
                removeHeaderView(mTopLayout);
            }
            return;
        }
        if (!isDropDownStyle) {
            return;
        }

        mTopReleaseMinDistance = context.getResources().getDimensionPixelSize(R.dimen.drag_drop_list_view_top_release_min_distance);
        flipAnimation = new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        flipAnimation.setInterpolator(new LinearInterpolator());
        flipAnimation.setDuration(250);
        flipAnimation.setFillAfter(true);
        reverseFlipAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseFlipAnimation.setInterpolator(new LinearInterpolator());
        reverseFlipAnimation.setDuration(250);
        reverseFlipAnimation.setFillAfter(true);

        mTopDefaultText = context.getString(R.string.drag_drop_list_view_top_default_text);
        mTopPullText = context.getString(R.string.drag_drop_list_view_top_pull_text);
        mTopReleaseText = context.getString(R.string.drag_drop_list_view_top_release_text);
        mTopLoadingText = context.getString(R.string.drag_drop_list_view_top_loading_text);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTopLayout = (RelativeLayout) inflater.inflate(R.layout.layout_drag_drop_list_view_top, this, false);
        mTopTitleTextView = (TextView) mTopLayout.findViewById(R.id.drag_drop_list_view_title_text);
        mTopImageView = (ImageView) mTopLayout.findViewById(R.id.drag_drop_list_view_image_view);
        mTopProgressBar = (ProgressBar) mTopLayout.findViewById(R.id.drag_drop_list_view_refresh_progress_bar);
        mTopSubTitleTextView = (TextView) mTopLayout.findViewById(R.id.drag_drop_list_view_subtitle_text);
        mTopLayout.setClickable(true);
        mTopLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onDropDown();
            }
        });
        mTopTitleTextView.setText(mTopDefaultText);
        addHeaderView(mTopLayout);

        measureHeaderLayout(mTopLayout);
        topOriginalHeight = mTopLayout.getMeasuredHeight();
        topOriginalTopPadding = mTopLayout.getPaddingTop();
        currentTopStatus = TOP_STATUS_CLICK_TO_LOAD;
    }

    /**
     * init on bottom style, only init once
     */
    private void initOnBottomStyle() {
        if (mBottomLayout != null) {
            if (isOnBottomStyle) {
                addFooterView(mBottomLayout);
            } else {
                removeFooterView(mBottomLayout);
            }
            return;
        }
        if (!isOnBottomStyle) {
            return;
        }

        mBottomDefaultText = context.getString(R.string.drag_drop_list_view_bottom_default_text);
        mBottomLoadingText = context.getString(R.string.drag_drop_list_view_bottom_loading_text);
        mBottomNoMoreText = context.getString(R.string.drag_drop_list_view_bottom_no_more_text);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBottomLayout = (RelativeLayout) inflater.inflate(R.layout.layout_drag_drop_list_view_bottom, this, false);
        mBottomButton = (Button) mBottomLayout.findViewById(R.id.drag_drop_list_view_button);
        mBottomButton.setDrawingCacheBackgroundColor(0);
        mBottomButton.setEnabled(true);

        mBottomProgressBar = (ProgressBar) mBottomLayout.findViewById(R.id.drag_drop_list_view_load_progress_bar);
        addFooterView(mBottomLayout);
    }

    /**
     * @return isDropDownStyle
     */
    public boolean isDropDownStyle() {
        return isDropDownStyle;
    }

    /**
     * @param isDropDownStyle
     */
    public void setDropDownStyle(boolean isDropDownStyle) {
        if (this.isDropDownStyle != isDropDownStyle) {
            this.isDropDownStyle = isDropDownStyle;
            initDropDownStyle();
        }
    }

    /**
     * @return isOnBottomStyle
     */
    public boolean isOnBottomStyle() {
        return isOnBottomStyle;
    }

    /**
     * @param isOnBottomStyle
     */
    public void setOnBottomStyle(boolean isOnBottomStyle) {
        if (this.isOnBottomStyle != isOnBottomStyle) {
            this.isOnBottomStyle = isOnBottomStyle;
            initOnBottomStyle();
        }
    }

    /**
     * @return isAutoLoadOnBottom
     */
    public boolean isAutoLoadOnBottom() {
        return isAutoLoadOnBottom;
    }

    /**
     * set whether auto load when on bottom
     *
     * @param isAutoLoadOnBottom
     */
    public void setAutoLoadOnBottom(boolean isAutoLoadOnBottom) {
        this.isAutoLoadOnBottom = isAutoLoadOnBottom;
    }

    /**
     * get whether show bottom loading progress bar when loading
     *
     * @return the isShowBottomProgressBar
     */
    public boolean isShowBottomProgressBar() {
        return isShowBottomProgressBar;
    }

    /**
     * set whether show bottom loading progress bar when loading
     *
     * @param isShowFooterProgressBar
     */
    public void setShowBottomProgressBar(boolean isShowFooterProgressBar) {
        this.isShowBottomProgressBar = isShowFooterProgressBar;
    }

    /**
     * get isShowBottomWhenNoMore
     *
     * @return the isShowBottomWhenNoMore
     */
    public boolean isShowBottomWhenNoMore() {
        return isShowBottomWhenNoMore;
    }

    /**
     * set isShowBottomWhenNoMore
     *
     * @param isShowFooterWhenNoMore the isShowBottomWhenNoMore to set
     */
    public void setShowBottomWhenNoMore(boolean isShowFooterWhenNoMore) {
        this.isShowBottomWhenNoMore = isShowFooterWhenNoMore;
    }

    /**
     * get bottom button
     *
     * @return
     */
    public Button getBottomButton() {
        return mBottomButton;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        if (isDropDownStyle) {
            setSecondPositionVisible();
        }
    }

    @Override
    public void setOnScrollListener(AbsListView.OnScrollListener listener) {
        onScrollListener = listener;
    }

    /**
     * @param onDropDownListener
     */
    public void setOnDropDownListener(OnDropDownListener onDropDownListener) {
        this.onDropDownListener = onDropDownListener;
    }

    /**
     * @param onBottomListener
     */
    public void setOnBottomListener(OnClickListener onBottomListener) {
        mBottomButton.setOnClickListener(onBottomListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isDropDownStyle) {
            return super.onTouchEvent(event);
        }

        hasReachedTop = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                actionDownPointY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                adjustHeaderPadding(event);
                break;
            case MotionEvent.ACTION_UP:
                if (!isVerticalScrollBarEnabled()) {
                    setVerticalScrollBarEnabled(true);
                }
                /**
                 * set status when finger leave screen if first item visible and top status is not
                 * TOP_STATUS_LOADING
                 * <ul>
                 * <li>if current top status is TOP_STATUS_RELEASE_TO_LOAD, call onDropDown.</li>
                 * <li>if current top status is TOP_STATUS_DROP_DOWN_TO_LOAD, then set top status to
                 * TOP_STATUS_CLICK_TO_LOAD and hide top layout.</li>
                 * </ul>
                 */
                if (getFirstVisiblePosition() == 0 && currentTopStatus != TOP_STATUS_LOADING) {
                    switch (currentTopStatus) {
                        case TOP_STATUS_RELEASE_TO_LOAD:
                            onDropDown();
                            break;
                        case TOP_STATUS_DROP_DOWN_TO_LOAD:
                            setHeaderStatusClickToLoad();
                            setSecondPositionVisible();
                            break;
                        case TOP_STATUS_CLICK_TO_LOAD:
                        default:
                            break;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (isDropDownStyle) {
            if (currentScrollState == SCROLL_STATE_TOUCH_SCROLL && currentTopStatus != TOP_STATUS_LOADING) {
                /**
                 * when state of ListView is SCROLL_STATE_TOUCH_SCROLL(ListView is scrolling and finger is on screen)
                 * and top status is not TOP_STATUS_LOADING
                 * <ul>
                 * if top layout is visiable,
                 * <li>if height of top is higher than a fixed value, then set top status to
                 * TOP_STATUS_RELEASE_TO_LOAD.</li>
                 * <li>else set top status to TOP_STATUS_DROP_DOWN_TO_LOAD.</li>
                 * </ul>
                 * <ul>
                 * if top layout is not visiable,
                 * <li>set top status to TOP_STATUS_CLICK_TO_LOAD.</li>
                 * </ul>
                 */
                if (firstVisibleItem == 0) {
                    mTopImageView.setVisibility(View.VISIBLE);
                    int pointBottom = topOriginalHeight + mTopReleaseMinDistance;
                    if (mTopLayout.getBottom() >= pointBottom) {
                        setHeaderStatusReleaseToLoad();
                    } else if (mTopLayout.getBottom() < pointBottom) {
                        setHeaderStatusDropDownToLoad();
                    }
                } else {
                    setHeaderStatusClickToLoad();
                }
            } else if (currentScrollState == SCROLL_STATE_FLING && firstVisibleItem == 0
                    && currentTopStatus != TOP_STATUS_LOADING) {
                /**
                 * when state of ListView is SCROLL_STATE_FLING(ListView is scrolling but finger has leave screen) and
                 * first item(top layout) is visible and top status is not TOP_STATUS_LOADING, then hide first
                 * item, set second item visible and set hasReachedTop true.
                 */
                setSecondPositionVisible();
                hasReachedTop = true;
            } else if (currentScrollState == SCROLL_STATE_FLING && hasReachedTop) {
                /**
                 * when state of ListView is SCROLL_STATE_FLING(ListView is scrolling but finger has leave screen) and
                 * hasReachedTop is true(it's because flick back), then hide first item, set second item visible.
                 */
                setSecondPositionVisible();
            }
        }

        // if isOnBottomStyle and isAutoLoadOnBottom and hasMore, then call onBottom function auto
        if (isOnBottomStyle && isAutoLoadOnBottom && hasMore) {
            if (firstVisibleItem > 0 && totalItemCount > 0 && (firstVisibleItem + visibleItemCount == totalItemCount)) {
                onBottom();
            }
        }
        if (onScrollListener != null) {
            onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isDropDownStyle) {
            currentScrollState = scrollState;

            if (currentScrollState == SCROLL_STATE_IDLE) {
                hasReachedTop = false;
            }
        }

        if (onScrollListener != null) {
            onScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    /**
     * drop down begin, adjust view status
     */
    private void onDropDownBegin() {
        if (isDropDownStyle) {
            setHeaderStatusLoading();
        }
    }

    /**
     * on drop down loading, you can call it by manual, but you should manual call onBottomComplete at the same time.
     */
    public void onDropDown() {
        if (currentTopStatus != TOP_STATUS_LOADING && isDropDownStyle && onDropDownListener != null) {
            onDropDownBegin();
            onDropDownListener.onDropDown();
        }
    }

    /**
     * drop down complete, restore view status
     *
     * @param secondText display below top text, if null, not display
     */
    public void onDropDownComplete(CharSequence secondText) {
        if (isDropDownStyle) {
            setTopSubTitleTextView(secondText);
            onDropDownComplete();
        }
    }

    /**
     * set top second text
     *
     * @param secondText secondText display below top text, if null, not display
     */
    public void setTopSubTitleTextView(CharSequence secondText) {
        if (isDropDownStyle) {
            if (secondText == null) {
                mTopSubTitleTextView.setVisibility(View.GONE);
            } else {
                mTopSubTitleTextView.setVisibility(View.VISIBLE);
                mTopSubTitleTextView.setText(secondText);
            }
        }
    }

    /**
     * drop down complete, restore view status
     */
    public void onDropDownComplete() {
        if (isDropDownStyle) {
            setHeaderStatusClickToLoad();

            if (mTopLayout.getBottom() > 0) {
                invalidateViews();
                setSecondPositionVisible();
            }
        }
    }

    /**
     * on bottom begin, adjust view status
     */
    private void onBottomBegin() {
        if (isOnBottomStyle) {
            if (isShowBottomProgressBar) {
                mBottomProgressBar.setVisibility(View.VISIBLE);
            }
            mBottomButton.setText(mBottomLoadingText);
            mBottomButton.setEnabled(false);
        }
    }

    /**
     * on bottom loading, you can call it by manual, but you should manual call onBottomComplete at the same time.
     */
    public void onBottom() {
        if (isOnBottomStyle && !isOnBottomLoading) {
            isOnBottomLoading = true;
            onBottomBegin();
            mBottomButton.performClick();
        }
    }

    /**
     * on bottom load complete, restore view status
     */
    public void onBottomComplete() {
        if (isOnBottomStyle) {
            if (isShowBottomProgressBar) {
                mBottomProgressBar.setVisibility(View.GONE);
            }
            if (!hasMore) {
                mBottomButton.setText(mBottomNoMoreText);
                mBottomButton.setEnabled(false);
                if (!isShowBottomWhenNoMore) {
                    removeFooterView(mBottomLayout);
                }
            } else {
                mBottomButton.setText(mBottomDefaultText);
                mBottomButton.setEnabled(true);
            }
            isOnBottomLoading = false;
        }
    }

    /**
     * OnDropDownListener, called when top released
     *
     * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-5-31
     */
    public interface OnDropDownListener {

        /**
         * called when top released
         */
        public void onDropDown();
    }

    /**
     * set second position visible(index is 1), because first position is top layout
     */
    public void setSecondPositionVisible() {
        if (getAdapter() != null && getAdapter().getCount() > 0 && getFirstVisiblePosition() == 0) {
            setSelection(1);
        }
    }

    /**
     * set whether has more. if hasMore is false, onBottm will not be called when listView scroll to bottom
     *
     * @param hasMore
     */
    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    /**
     * get whether has more
     *
     * @return
     */
    public boolean isHasMore() {
        return hasMore;
    }

    /**
     * get top layout view
     *
     * @return
     */
    public RelativeLayout getTopLayout() {
        return mTopLayout;
    }

    /**
     * get bottom layout view
     *
     * @return
     */
    public RelativeLayout getBottomLayout() {
        return mBottomLayout;
    }

    /**
     * get rate about drop down distance and top padding top when drop down
     *
     * @return mTopPaddingTopRate
     */
    public float getTopPaddingTopRate() {
        return mTopPaddingTopRate;
    }

    /**
     * set rate about drop down distance and top padding top when drop down
     *
     * @param mTopPaddingTopRate
     */
    public void setTopPaddingTopRate(float mTopPaddingTopRate) {
        this.mTopPaddingTopRate = mTopPaddingTopRate;
    }

    /**
     * get min distance which top can release to loading
     *
     * @return mTopReleaseMinDistance
     */
    public int getTopReleaseMinDistance() {
        return mTopReleaseMinDistance;
    }

    /**
     * set min distance which top can release to loading
     *
     * @param mTopReleaseMinDistance
     */
    public void setTopReleaseMinDistance(int mTopReleaseMinDistance) {
        this.mTopReleaseMinDistance = mTopReleaseMinDistance;
    }

    /**
     * get top default text, default is R.string.drag_drop_list_view_top_default_text
     *
     * @return
     */
    public String getDefaultTitleText() {
        return mTopDefaultText;
    }

    /**
     * set top default text, default is R.string.drag_drop_list_view_top_default_text
     *
     * @param mDefaultTitleText
     */
    public void setDefaultTitleText(String mDefaultTitleText) {
        this.mTopDefaultText = mDefaultTitleText;
        if (mTopTitleTextView != null && currentTopStatus == TOP_STATUS_CLICK_TO_LOAD) {
            mTopTitleTextView.setText(mDefaultTitleText);
        }
    }

    /**
     * get top pull text, default is R.string.drag_drop_list_view_top_pull_text
     *
     * @return
     */
    public String getTopPullText() {
        return mTopPullText;
    }

    /**
     * set top pull text, default is R.string.drag_drop_list_view_top_pull_text
     *
     * @param mTopPullText
     */
    public void setTopPullText(String mTopPullText) {
        this.mTopPullText = mTopPullText;
    }

    /**
     * get top release text, default is R.string.drag_drop_list_view_top_release_text
     *
     * @return
     */
    public String getTopReleaseText() {
        return mTopReleaseText;
    }

    /**
     * set top release text, default is R.string.drag_drop_list_view_top_release_text
     *
     * @param mTopReleaseText
     */
    public void setTopReleaseText(String mTopReleaseText) {
        this.mTopReleaseText = mTopReleaseText;
    }

    /**
     * get top loading text, default is R.string.drag_drop_list_view_top_loading_text
     *
     * @return
     */
    public String getTopLoadingText() {
        return mTopLoadingText;
    }

    /**
     * set top loading text, default is R.string.drag_drop_list_view_top_loading_text
     *
     * @param mTopLoadingText
     */
    public void setTopLoadingText(String mTopLoadingText) {
        this.mTopLoadingText = mTopLoadingText;
    }

    /**
     * get bottom default text, default is R.string.drag_drop_list_view_bottom_default_text
     *
     * @return
     */
    public String getBottomDefaultText() {
        return mBottomDefaultText;
    }

    /**
     * set bottom default text, default is R.string.drag_drop_list_view_bottom_default_text
     *
     * @param mBottomDefaultText
     */
    public void setBottomDefaultText(String mBottomDefaultText) {
        this.mBottomDefaultText = mBottomDefaultText;
        if (mBottomButton != null && mBottomButton.isEnabled()) {
            mBottomButton.setText(mBottomDefaultText);
        }
    }

    /**
     * get bottom loading text, default is R.string.drag_drop_list_view_bottom_loading_text
     *
     * @return
     */
    public String getBottomLoadingText() {
        return mBottomLoadingText;
    }

    /**
     * set bottom loading text, default is R.string.drag_drop_list_view_bottom_loading_text
     *
     * @param mBottomLoadingText
     */
    public void setBottomLoadingText(String mBottomLoadingText) {
        this.mBottomLoadingText = mBottomLoadingText;
    }

    /**
     * get bottom no more text, default is R.string.drag_drop_list_view_bottom_no_more_text
     *
     * @return
     */
    public String getBottomNoMoreText() {
        return mBottomNoMoreText;
    }

    /**
     * set bottom no more text, default is R.string.drag_drop_list_view_bottom_no_more_text
     *
     * @param mBottomNoMoreText
     */
    public void setBottomNoMoreText(String mBottomNoMoreText) {
        this.mBottomNoMoreText = mBottomNoMoreText;
    }

    /**
     * status which you can click to load, init satus *
     */
    public static final int TOP_STATUS_CLICK_TO_LOAD = 1;
    /**
     * status which you can drop down and then release to excute onDropDownListener, when height of top layout lower
     * than a value
     */
    public static final int TOP_STATUS_DROP_DOWN_TO_LOAD = 2;
    /**
     * status which you can release to excute onDropDownListener, when height of top layout higher than a value *
     */
    public static final int TOP_STATUS_RELEASE_TO_LOAD = 3;
    /**
     * status which is loading *
     */
    public static final int TOP_STATUS_LOADING = 4;

    /**
     * set top status to {@link #TOP_STATUS_CLICK_TO_LOAD}
     */
    private void setHeaderStatusClickToLoad() {
        if (currentTopStatus != TOP_STATUS_CLICK_TO_LOAD) {
            resetHeaderPadding();

            mTopImageView.clearAnimation();
            mTopImageView.setVisibility(View.GONE);
            mTopProgressBar.setVisibility(View.GONE);
            mTopTitleTextView.setText(mTopDefaultText);

            currentTopStatus = TOP_STATUS_CLICK_TO_LOAD;
        }
    }

    /**
     * set top status to {@link #TOP_STATUS_DROP_DOWN_TO_LOAD}
     */
    private void setHeaderStatusDropDownToLoad() {
        if (currentTopStatus != TOP_STATUS_DROP_DOWN_TO_LOAD) {
            mTopImageView.setVisibility(View.VISIBLE);
            if (currentTopStatus != TOP_STATUS_CLICK_TO_LOAD) {
                mTopImageView.clearAnimation();
                mTopImageView.startAnimation(reverseFlipAnimation);
            }
            mTopProgressBar.setVisibility(View.GONE);
            mTopTitleTextView.setText(mTopPullText);

            if (isVerticalFadingEdgeEnabled()) {
                setVerticalScrollBarEnabled(false);
            }

            currentTopStatus = TOP_STATUS_DROP_DOWN_TO_LOAD;
        }
    }

    /**
     * set top status to {@link #TOP_STATUS_RELEASE_TO_LOAD}
     */
    private void setHeaderStatusReleaseToLoad() {
        if (currentTopStatus != TOP_STATUS_RELEASE_TO_LOAD) {
            mTopImageView.setVisibility(View.VISIBLE);
            mTopImageView.clearAnimation();
            mTopImageView.startAnimation(flipAnimation);
            mTopProgressBar.setVisibility(View.GONE);
            mTopTitleTextView.setText(mTopReleaseText);

            currentTopStatus = TOP_STATUS_RELEASE_TO_LOAD;
        }
    }

    /**
     * set top status to {@link #TOP_STATUS_LOADING}
     */
    private void setHeaderStatusLoading() {
        if (currentTopStatus != TOP_STATUS_LOADING) {
            resetHeaderPadding();

            mTopImageView.setVisibility(View.GONE);
            mTopImageView.clearAnimation();
            mTopProgressBar.setVisibility(View.VISIBLE);
            mTopTitleTextView.setText(mTopLoadingText);

            currentTopStatus = TOP_STATUS_LOADING;
            setSelection(0);
        }
    }

    /**
     * adjust top padding according to motion event
     *
     * @param ev
     */
    private void adjustHeaderPadding(MotionEvent ev) {
        // adjust top padding according to motion event history
        int pointerCount = ev.getHistorySize();
        if (isVerticalFadingEdgeEnabled()) {
            setVerticalScrollBarEnabled(false);
        }
        for (int i = 0; i < pointerCount; i++) {
            if (currentTopStatus == TOP_STATUS_DROP_DOWN_TO_LOAD || currentTopStatus == TOP_STATUS_RELEASE_TO_LOAD) {
                mTopLayout.setPadding(mTopLayout.getPaddingLeft(), (int) (((ev.getHistoricalY(i) - actionDownPointY) - topOriginalHeight) / mTopPaddingTopRate), mTopLayout.getPaddingRight(), mTopLayout.getPaddingBottom());
            }
        }
    }

    /**
     * reset top padding
     */
    private void resetHeaderPadding() {
        mTopLayout.setPadding(mTopLayout.getPaddingLeft(), topOriginalTopPadding, mTopLayout.getPaddingRight(), mTopLayout.getPaddingBottom());
    }

    /**
     * measure top layout
     *
     * @param child
     */
    private void measureHeaderLayout(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * get attrs
     *
     * @param context
     * @param attrs
     */
    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DragDropListView);
        isDropDownStyle = ta.getBoolean(R.styleable.DragDropListView_isDropDownStyle, false);
        isOnBottomStyle = ta.getBoolean(R.styleable.DragDropListView_isOnBottomStyle, false);
        isAutoLoadOnBottom = ta.getBoolean(R.styleable.DragDropListView_isAutoLoadOnBottom, false);
        ta.recycle();
    }
}