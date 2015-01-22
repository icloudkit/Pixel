//package net.quduo.pixel.interfaces.android.service;
//
//import android.content.Context;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v4.view.ViewPager.OnPageChangeListener;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.LinearLayout.LayoutParams;
//
//import net.quduo.pixel.R;
//import net.quduo.pixel.interfaces.android.activity.ChatFormActivity;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//public class EmoticonManager implements View.OnClickListener {
//
//    private static final int EMOTICON = 0x01;
//    private static final int EMOTICON_CLASSICAL = 0x02;
//
//    private Context mContext;
//    private ViewPager mViewPager;
//    private LinearLayout mPagerIndexPanel;
//    private LinearLayout mEmojiToolBarView;
//    private NativePagerAdapter mAdapter;
//    private Map<Integer, ViewHolder> mEmoticonPages;
//    private Map<Integer, ViewHolder> mEmoticonClassicalPages;
//
//    private NativeOnPageChangeListener mPageChangeListener;
//
//    private static ArrayList<Integer> mEmoticons;
//    private static ArrayList<String> mEmoticonDescList;
//    private ArrayList<ImageView> mEmoticonIndicatorImageViews;
//
//    private static int[] mEmoticonClassicals;
//    private static int[] mEmoticonClassicalStatics;
//    private static String[] mClassicalEmotionDescs;
//    private ArrayList<ImageView> mEmoticonClassicalIndicatorImageViews;
//
//    private Handler mHandler;
//
//    private int mCurrentEmoticon = EMOTICON_CLASSICAL;
//    private int mCurrentEmoticonPage = 0;
//    private int mCurrentClassicalEmoticonPage = 0;
//
//    private boolean mShow;
//
//    public EmoticonManager(Context context, ViewPager viewPager, View pagerIndex, View emojiToolBar, Handler handler) {
//
//        mContext = context;
//        mViewPager = viewPager;
//        mPagerIndexPanel = (LinearLayout) pagerIndex;
//        mEmojiToolBarView = (LinearLayout) emojiToolBar;
//        mEmoticonPages = new HashMap<Integer, EmoticonManager.ViewHolder>();
//        mEmoticonClassicalPages = new HashMap<Integer, EmoticonManager.ViewHolder>();
//
//        mAdapter = new NativePagerAdapter();
//        mPageChangeListener = new NativeOnPageChangeListener();
//
//        mHandler = handler;
//
//        initializeEmoji(mContext);
//        initializeClassicalEmoji(mContext);
//
//        initializeEmojiData();
//        initializeClassicalEmojiData();
//
//        Button emojiButton = (Button) mEmojiToolBarView.findViewById(R.id.emoji_button);
//        emojiButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mCurrentEmoticon = EMOTICON;
//                destroy();
//                initialize();
//                mAdapter.notifyDataSetChanged();
//            }
//        });
//
//        Button emojiClassicalButton = (Button) mEmojiToolBarView.findViewById(R.id.emoji_classical_button);
//        emojiClassicalButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mCurrentEmoticon = EMOTICON_CLASSICAL;
//                destroy();
//                initialize();
//                mAdapter.notifyDataSetChanged();
//            }
//        });
//    }
//
//    public boolean isShow() {
//        return mShow;
//    }
//
//    private void initializeEmojiData() {
//        mEmoticonIndicatorImageViews = new ArrayList<ImageView>();
//        int totalPages = (int) Math.ceil(mEmoticons.size() / 8.0F);
//        for (int i = 0; i < totalPages; i++) {
//            ImageView imageView = new ImageView(mContext);
//
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//            params.leftMargin = 5;
//            imageView.setLayoutParams(params);
//            mEmoticonIndicatorImageViews.add(imageView);
//        }
//    }
//
//    public static void initializeEmoji(Context context) {
//        if (mEmoticons != null && mEmoticonDescList != null) {
//            return;
//        }
//
//        mEmoticons = new ArrayList<Integer>();
//        mEmoticonDescList = new ArrayList<String>();
//
//        for (int i = 1; i < 53; i++) {
//            mEmoticons.add(Integer.valueOf(context.getResources().getIdentifier("emo_" + i, "drawable", context.getPackageName())));
//            mEmoticonDescList.add(String.format(":emo:%d:", i));
//        }
//    }
//
//    private void initializeClassicalEmojiData() {
//        mEmoticonClassicalIndicatorImageViews = new ArrayList<ImageView>();
//        int totalPages = (int) Math.ceil(mEmoticonClassicals.length / 21.0F);
//        for (int i = 0; i < totalPages; i++) {
//            ImageView imageView = new ImageView(mContext);
//
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//            params.leftMargin = 5;
//            imageView.setLayoutParams(params);
//            mEmoticonClassicalIndicatorImageViews.add(imageView);
//        }
//    }
//
//    public static void initializeClassicalEmoji(Context context) {
//        if (mClassicalEmotionDescs != null && mEmoticonClassicals != null && mEmoticonClassicalStatics != null) {
//            return;
//        }
//
//        String[] classical = context.getResources().getStringArray(R.array.classical_emoji);
//        String[] classicalStatic = context.getResources().getStringArray(R.array.classical_static_emoji);
//
//        mEmoticonClassicals = new int[classical.length];
//        mEmoticonClassicalStatics = new int[classicalStatic.length];
//        for (int i = 0; i < classicalStatic.length; i++) {
//            mEmoticonClassicals[i] = context.getResources().getIdentifier(classical[i], "drawable", context.getPackageName());
//            mEmoticonClassicalStatics[i] = context.getResources().getIdentifier(classicalStatic[i], "drawable", context.getPackageName());
//        }
//
//        mClassicalEmotionDescs = context.getResources().getStringArray(R.array.classical_emoji_desc);
//    }
//
//    public void initialize() {
//        mShow = true;
//        mEmojiToolBarView.setVisibility(View.VISIBLE);
//        mViewPager.setAdapter(mAdapter);
//
//        ArrayList<ImageView> imageViews = null;
//        if (mCurrentEmoticon == EMOTICON) {
//            imageViews = mEmoticonIndicatorImageViews;
//        } else {
//            imageViews = mEmoticonClassicalIndicatorImageViews;
//        }
//
//        for (ImageView imageView : imageViews) {
//            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.selector_indicator_view_normal));
//            mPagerIndexPanel.addView(imageView);
//        }
//        mViewPager.setOnPageChangeListener(mPageChangeListener);
//
//        int page = 0;
//        if (mCurrentEmoticon == EMOTICON) {
//            page = mCurrentEmoticonPage;
//        } else {
//            page = mCurrentClassicalEmoticonPage;
//        }
//
//        ImageView imageView = imageViews.get(page);
//        imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.selector_indicator_view_focused));
//
//        mViewPager.setCurrentItem(page);
//    }
//
//    public void destroy() {
//        mShow = false;
//        mEmojiToolBarView.setVisibility(View.GONE);
//
//        mViewPager.setAdapter(null);
//        mViewPager.removeAllViews();
//        mPagerIndexPanel.removeAllViews();
//        mViewPager.setOnPageChangeListener(null);
//    }
//
//    private ViewHolder instanceView(int position, int emoji) {
//        ViewHolder viewHolder = new ViewHolder();
//
//        View rootView = null;
//        int index = 0;
//        int count = 0;
//        if (emoji == EMOTICON) {
//            count = 8;
//            index = position * count;
//
//            rootView = LayoutInflater.from(mContext).inflate(R.layout.list_item_emoticon_view, null);
//        } else {
//            count = 21;
//            index = position * count;
//            rootView = LayoutInflater.from(mContext).inflate(R.layout.list_item_emoticon_view, null);
//        }
//
//        viewHolder.emotionViews = new ArrayList<ImageButton>();
//
//        for (int i = 0; i < count; i++) {
//            ImageButton view = (ImageButton) rootView.findViewById(mContext.getResources().getIdentifier("emo_" + (i + 1), "id", mContext.getPackageName()));
//            view.setOnClickListener(this);
//            view.setTag(String.valueOf(index + i));
//            viewHolder.emotionViews.add(view);
//        }
//
//        viewHolder.rootView = rootView;
//        return viewHolder;
//    }
//
//    private class NativePagerAdapter extends PagerAdapter {
//
//        @Override
//        public int getCount() {
//            if (mCurrentEmoticon == EMOTICON) {
//                return (int) Math.ceil(mEmoticons.size() / 8.0f);
//            } else {
//                return mEmoticonClassicals.length / 21;
//            }
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            Map<Integer, ViewHolder> pages = null;
//            if (mCurrentEmoticon == EMOTICON) {
//                pages = mEmoticonPages;
//            } else {
//                pages = mEmoticonClassicalPages;
//            }
//
//            ViewHolder viewHolder = pages.get(Integer.valueOf(position));
//            if (viewHolder != null) {
//                ((ViewPager) container).removeView(viewHolder.rootView);
//            }
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            ViewHolder viewHolder = null;
//            if (mCurrentEmoticon == EMOTICON) {
//                viewHolder = mEmoticonPages.get(Integer.valueOf(position));
//                if (viewHolder == null) {
//                    viewHolder = instanceView(position, EMOTICON);
//                    mEmoticonPages.put(Integer.valueOf(position), viewHolder);
//                }
//
//                int offset = position * 8;
//                int i;
//                for (i = offset; i < mEmoticons.size() && i < (position + 1) * 8; i++) {
//                    ImageButton button = viewHolder.emotionViews.get(i - offset);
//                    button.setVisibility(View.VISIBLE);
//                    button.setImageDrawable(mContext.getResources().getDrawable(mEmoticons.get(i)));
//                }
//
//                for (; i < (position + 1) * 8; i++) {
//                    ImageButton button = viewHolder.emotionViews.get(i - offset);
//                    button.setVisibility(View.INVISIBLE);
//                }
//            } else {
//                viewHolder = mEmoticonClassicalPages.get(Integer.valueOf(position));
//                if (viewHolder == null) {
//                    viewHolder = instanceView(position, EMOTICON_CLASSICAL);
//                    mEmoticonClassicalPages.put(Integer.valueOf(position), viewHolder);
//                }
//
//                int offset = position * 21;
//                for (int i = offset; i < mEmoticonClassicals.length && i < (position + 1) * 21; i++) {
//                    ImageButton button = viewHolder.emotionViews.get(i - offset);
//
//                    button.setImageDrawable(mContext.getResources().getDrawable(mEmoticonClassicalStatics[i]));
//                }
//            }
//
//            ((ViewPager) container).addView(viewHolder.rootView);
//            return viewHolder.rootView;
//        }
//
//    }
//
//    public class NativeOnPageChangeListener implements OnPageChangeListener {
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//
//        }
//
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//        }
//
//        @Override
//        public void onPageSelected(int position) {
//
//            ImageView nowImageView = null;
//            ImageView beforeImageView = null;
//            ImageView afterImageView = null;
//            if (mCurrentEmoticon == EMOTICON) {
//                if (position < 0 || position > mEmoticonIndicatorImageViews.size() - 1) {
//                    return;
//                }
//                mCurrentEmoticonPage = position;
//                nowImageView = mEmoticonIndicatorImageViews.get(position);
//                beforeImageView = position > 0 ? mEmoticonIndicatorImageViews.get(position - 1) : null;
//                afterImageView = position < (mEmoticonIndicatorImageViews.size() - 1) ? mEmoticonIndicatorImageViews.get(position + 1) : null;
//            } else {
//                if (position < 0 || position > mEmoticonClassicalIndicatorImageViews.size() - 1) {
//                    return;
//                }
//                mCurrentClassicalEmoticonPage = position;
//                nowImageView = mEmoticonClassicalIndicatorImageViews.get(position);
//                beforeImageView = position > 0 ? mEmoticonClassicalIndicatorImageViews.get(position - 1) : null;
//                afterImageView = position < (mEmoticonClassicalIndicatorImageViews.size() - 1) ? mEmoticonClassicalIndicatorImageViews.get(position + 1) : null;
//            }
//
//            nowImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.selector_indicator_view_focused));
//
//            if (beforeImageView != null) {
//                beforeImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.selector_indicator_view_normal));
//            }
//
//            if (afterImageView != null) {
//                afterImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.selector_indicator_view_normal));
//            }
//        }
//    }
//
//    private class ViewHolder {
//        View rootView;
//        ArrayList<ImageButton> emotionViews;
//    }
//
//    public static int getEmotionResourceId(String message) {
//        if (message.startsWith(":emo:")) {
//            for (int i = 0; i < mEmoticonDescList.size(); i++) {
//                if (mEmoticonDescList.get(i).equals(message)) {
//                    return mEmoticons.get(i);
//                }
//            }
//        } else {
//            for (int i = 0; i < mClassicalEmotionDescs.length; i++) {
//                if (mClassicalEmotionDescs[i].equals(message)) {
//                    return mEmoticonClassicals[i];
//                }
//            }
//        }
//        return -1;
//    }
//
//    public static int getEmotionStaticResourceId(String message) {
//        if (message.startsWith(":emo:")) {
//            for (int i = 0; i < mEmoticonDescList.size(); i++) {
//                if (mEmoticonDescList.get(i).equals(message)) {
//                    return mEmoticons.get(i);
//                }
//            }
//        } else {
//            for (int i = 0; i < mClassicalEmotionDescs.length; i++) {
//                if (mClassicalEmotionDescs[i].equals(message)) {
//                    return mEmoticonClassicalStatics[i];
//                }
//            }
//        }
//        return -1;
//    }
//
//    public static int[] getClassicalEmotions() {
//        return mEmoticonClassicals;
//    }
//
//    public static String[] getClassicalEmotionDescs() {
//        return mClassicalEmotionDescs;
//    }
//
//    @Override
//    public void onClick(View v) {
//        String tag = (String) v.getTag();
//        if (tag != null) {
//            try {
//                int index = Integer.parseInt(tag);
//                if (mCurrentEmoticon == EMOTICON) {
//                    Message message = mHandler.obtainMessage(ChatFormActivity.MSG_SEND_EMOTICON, mEmoticonDescList.get(index));
//                    message.sendToTarget();
//                } else {
//                    Message message = mHandler.obtainMessage(ChatFormActivity.MSG_SEND_CLASSICAL_EMOTICON, mClassicalEmotionDescs[index]);
//                    message.sendToTarget();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
