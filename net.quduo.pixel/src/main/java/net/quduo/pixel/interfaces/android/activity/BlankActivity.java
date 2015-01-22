package net.quduo.pixel.interfaces.android.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.quduo.pixel.BuildConfig;
import net.quduo.pixel.R;

import java.util.ArrayList;
import java.util.List;

public class BlankActivity extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private static final String TAG = BlankActivity.class.getName();
    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static final int GRID_VIEW_COUNT = 21;
    private List<Integer> mSourceDataList = new ArrayList<Integer>();
    private List<MyGridViewAdapter> mGridViewAdapters = new ArrayList<MyGridViewAdapter>();
    private ViewPager mExpressionViewPager;
    private List<View> mViews = new ArrayList<View>();
    private MyViewPagerAdapter mViewPagerAdapter;
    private Context mContext;

    private ImageView mIndicatorImageView;
    private ImageView[] mIndicatorImageViews;
    private LinearLayout mExpressionIndicatorLayout;

    private int mPageCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);
        mContext = getApplicationContext();

        mExpressionViewPager = (ViewPager) findViewById(R.id.expression_view_pager);
        mExpressionIndicatorLayout = (LinearLayout) findViewById(R.id.expression_indicator_layout);

        for (int i = 0; i < 23; i++) {
            mSourceDataList.add(i);
        }
        loadViews();
    }

    private void loadViews() {

        // 包含删除键后的页面大小
        mPageCount = (mSourceDataList.size() + GRID_VIEW_COUNT - 2) / (GRID_VIEW_COUNT - 1);
        if (DEBUG) Log.d(TAG, "Page count:" + mPageCount);

        mGridViewAdapters.clear();
        mViews.clear();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < mPageCount; i++) {
            View view = inflater.inflate(R.layout.layout_emoticon_view, null);
            GridView mGridView = (GridView) view.findViewById(R.id.emoticon_grid_view);
            MyGridViewAdapter adapter = new MyGridViewAdapter(mContext, i);
            mGridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            mGridViewAdapters.add(adapter);
            mViews.add(view);
        }

        mIndicatorImageViews = new ImageView[mPageCount];
        for (int i = 0; i < mPageCount; i++) {
            mIndicatorImageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
            layoutParams.setMargins(8, 0, 8, 20);
            mIndicatorImageView.setLayoutParams(layoutParams);
            mIndicatorImageViews[i] = mIndicatorImageView;
            mIndicatorImageView.setBackgroundResource(R.drawable.selector_indicator_view);
            mIndicatorImageView.setEnabled(false);
            if (i == 0) {
                mIndicatorImageView.setEnabled(true);
            }
            mExpressionIndicatorLayout.addView(mIndicatorImageView);
        }

        mViewPagerAdapter = new MyViewPagerAdapter();
        mExpressionViewPager.setAdapter(mViewPagerAdapter);
        mExpressionViewPager.setOnPageChangeListener(this);
        mExpressionViewPager.setCurrentItem(0);
        mViewPagerAdapter.notifyDataSetChanged();
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
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        if (DEBUG) Log.d(TAG, "你当前选择的是:" + position);
        for (int i = 0; i < mIndicatorImageViews.length; i++) {
            if (i == position) {
                mIndicatorImageViews[i].setEnabled(true);
            } else {
                mIndicatorImageViews[i].setEnabled(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.emoticon_item_layout:
                int id = (Integer) v.getTag();
                // Toast.makeText(mContext, "position： " + id, Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    private class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            mExpressionViewPager.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViews.get(position), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return mViews.get(position);
        }
    }

    private class MyGridViewAdapter extends BaseAdapter {

        private Context mContext;
        private int mPagePosition;
        private LayoutInflater mInflater;

        private MyGridViewAdapter(Context context, int pagePosition) {
            this.mContext = context;
            this.mPagePosition = pagePosition;
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            int size = (mSourceDataList == null ? 0 : (mSourceDataList.size() + mPageCount * 1) - GRID_VIEW_COUNT * mPagePosition);
            Log.d(TAG, "size:" + size);
            if (size > GRID_VIEW_COUNT) {
                return GRID_VIEW_COUNT;
            } else {
                return size > 0 ? size : 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            int nowPosition = GRID_VIEW_COUNT * mPagePosition + position;

            ViewHolder viewHolder = null;
            if (convertView != null) {
                viewHolder = (ViewHolder) convertView.getTag();
            } else {
                convertView = mInflater.inflate(R.layout.list_item_emoticon_view, null);

                viewHolder = new ViewHolder();
                viewHolder.layout = (RelativeLayout) convertView.findViewById(R.id.emoticon_item_layout);
                viewHolder.name = (TextView) convertView.findViewById(R.id.emoticon_name);
                viewHolder.icon = (ImageView) convertView.findViewById(R.id.emoticon_icon);
                convertView.setTag(viewHolder);
            }
            viewHolder.layout.setTag(nowPosition);
            viewHolder.layout.setOnClickListener(BlankActivity.this);

            if (((nowPosition + 1) % GRID_VIEW_COUNT == 0) || ((nowPosition + 1) == (mSourceDataList.size() + mPageCount * 1))) {
                viewHolder.name.setText("DEL");
            } else {
                viewHolder.name.setText("" + nowPosition);
            }
            return convertView;
        }
    }

    static class ViewHolder {
        RelativeLayout layout;
        ImageView icon;
        TextView name;
    }

}
