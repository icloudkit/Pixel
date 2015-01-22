package net.quduo.pixel.interfaces.android.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.quduo.pixel.BuildConfig;
import net.quduo.pixel.R;
import net.quduo.pixel.interfaces.android.common.SharedPreferencesHelper;
import net.quduo.pixel.interfaces.android.model.EmoticonDataModel;

import java.util.ArrayList;
import java.util.List;

public class ChatFormActivity extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private static final String TAG = ChatFormActivity.class.getName();
    private static final boolean DEBUG = BuildConfig.DEBUG;

    private SharedPreferences preferences;
    private InputMethodManager inputMethodManager;

    private View mActionBarContainer;
    private Button mBackActionItem;
    private ImageView mBackActionDivider;
    private TextView mActionTitle;
    private Button mUserInfoActionItem;

    private LinearLayout mChatFormInputLayout;

    private boolean isEmoticonSwitched = false;
    private boolean isOtherInputTypeSwitched = false;

    private Button mSpeakSwitchButton;
    private Button mKeyboardSwitchButton;
    private LinearLayout mPressedSpeakButton;
    private Button mMorePlusSwitchButton;
    private Button mSendMessageButton;

    private RelativeLayout mMessageInputLayout;
    private EditText mMessageInputEditText;
    private ImageView mEmoticonSwitchButton;

    private LinearLayout mMoreInputTypeLayout;
    private LinearLayout mEmoticonInputLayout;
    private LinearLayout mOtherInputTypeLayout;

    private static final int GRID_VIEW_COUNT = 21;
    private List<EmoticonDataModel> mSourceDataList = new ArrayList<EmoticonDataModel>();
    private List<MyGridViewAdapter> mGridViewAdapters = new ArrayList<MyGridViewAdapter>();
    private ViewPager mExpressionViewPager;
    private List<View> mViews = new ArrayList<View>();
    private MyViewPagerAdapter mViewPagerAdapter;
    private Context mContext;

    private ImageView mIndicatorImageView;
    private ImageView[] mIndicatorImageViews;
    private LinearLayout mExpressionIndicatorLayout;

    /*
    private DeleteCharacterThread deleteCharacterThread = null;
    private boolean isOnLongClick = false;
    */

    private int mPageCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_form);

        preferences = SharedPreferencesHelper.getSharedPreferences(this);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/icon/iconfont.ttf");

        mActionBarContainer = findViewById(R.id.action_bar_container);
        mBackActionItem = (Button) findViewById(R.id.back_action_item);
        mBackActionItem.setVisibility(View.VISIBLE);
        mBackActionItem.setTypeface(typeface);
        mBackActionItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mBackActionDivider = (ImageView) findViewById(R.id.back_action_divider);
        mBackActionDivider.setVisibility(View.VISIBLE);

        mActionTitle = (TextView) findViewById(R.id.action_title);
        mActionTitle.setVisibility(View.VISIBLE);
        mActionTitle.setText("我的朋友");

        mUserInfoActionItem = (Button) findViewById(R.id.user_info_action_item);
        mUserInfoActionItem.setTypeface(typeface);
        mUserInfoActionItem.setVisibility(View.VISIBLE);

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        mSpeakSwitchButton = (Button) findViewById(R.id.speak_switch_button);
        mSpeakSwitchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mSpeakSwitchButton.setVisibility(View.GONE);
                mMessageInputLayout.setVisibility(View.GONE);
                mKeyboardSwitchButton.setVisibility(View.VISIBLE);
                mPressedSpeakButton.setVisibility(View.VISIBLE);

                mMoreInputTypeLayout.setVisibility(View.GONE);

                mMessageInputEditText.clearFocus();

                // inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                inputMethodManager.hideSoftInputFromWindow(mMessageInputEditText.getWindowToken(), 0);

                isOtherInputTypeSwitched = false;
                mMoreInputTypeLayout.setVisibility(View.GONE);
            }
        });
        mKeyboardSwitchButton = (Button) findViewById(R.id.keyboard_switch_button);
        mKeyboardSwitchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                mSpeakSwitchButton.setVisibility(View.VISIBLE);
                mMessageInputLayout.setVisibility(View.VISIBLE);
                mKeyboardSwitchButton.setVisibility(View.GONE);
                mPressedSpeakButton.setVisibility(View.GONE);

                mMessageInputEditText.requestFocus();

                // inputMethodManager.showSoftInputFromInputMethod();
                // inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                inputMethodManager.showSoftInput(mMessageInputEditText, InputMethodManager.SHOW_FORCED);

                isEmoticonSwitched = false;
                isOtherInputTypeSwitched = false;

                mEmoticonSwitchButton.setImageResource(R.drawable.a7a);
                mMoreInputTypeLayout.setVisibility(View.GONE);
            }
        });
        mPressedSpeakButton = (LinearLayout) findViewById(R.id.pressed_speak_botton);
        mMorePlusSwitchButton = (Button) findViewById(R.id.more_plus_switch_button);
        mMorePlusSwitchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

                mEmoticonSwitchButton.setImageResource(R.drawable.a7a);
                mSpeakSwitchButton.setVisibility(View.VISIBLE);
                mMessageInputLayout.setVisibility(View.VISIBLE);
                mKeyboardSwitchButton.setVisibility(View.GONE);
                mPressedSpeakButton.setVisibility(View.GONE);

                if (!isOtherInputTypeSwitched) {
                    isOtherInputTypeSwitched = true;

                    // mMessageInputEditText.clearFocus();
                    mMessageInputEditText.clearComposingText();

                    inputMethodManager.hideSoftInputFromWindow(mMessageInputEditText.getWindowToken(), 0);

                    mMoreInputTypeLayout.setVisibility(View.VISIBLE);
                    mEmoticonInputLayout.setVisibility(View.GONE);
                    mOtherInputTypeLayout.setVisibility(View.VISIBLE);
                } else {
                    isEmoticonSwitched = false;
                    isOtherInputTypeSwitched = false;

                    // mMoreInputTypeLayout.setVisibility(View.GONE);
                    // mEmoticonInputLayout.setVisibility(View.GONE);
                    // mOtherInputTypeLayout.setVisibility(View.GONE);

                    mMessageInputEditText.requestFocus();
                    inputMethodManager.showSoftInput(mMessageInputEditText, InputMethodManager.SHOW_FORCED);

                }
            }
        });
        mSendMessageButton = (Button) findViewById(R.id.send_message_button);
        mSendMessageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        mMessageInputLayout = (RelativeLayout) findViewById(R.id.message_input_layout);
        mMessageInputEditText = (EditText) findViewById(R.id.message_input_edit_text);
        mMessageInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mMessageInputLayout.setBackgroundResource(R.drawable.line_edit_text_pink);
                } else {
                    mMessageInputLayout.setBackgroundResource(R.drawable.line_edit_text_grey);
                }
            }
        });
        mMessageInputEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mMessageInputEditText.getText().toString().trim().length() > 0) {
                    mMorePlusSwitchButton.setVisibility(View.GONE);
                    mSendMessageButton.setVisibility(View.VISIBLE);
                } else {
                    mMorePlusSwitchButton.setVisibility(View.VISIBLE);
                    mSendMessageButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEmoticonSwitchButton = (ImageView) findViewById(R.id.emoticon_switch_button);
        mEmoticonSwitchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

                if (!isEmoticonSwitched) {
                    isEmoticonSwitched = true;

                    inputMethodManager.hideSoftInputFromWindow(mMessageInputEditText.getWindowToken(), 0);

                    mMoreInputTypeLayout.setVisibility(View.VISIBLE);
                    mEmoticonInputLayout.setVisibility(View.VISIBLE);
                    mOtherInputTypeLayout.setVisibility(View.GONE);

                    mEmoticonSwitchButton.setImageResource(R.drawable.a7b);

                } else {
                    isEmoticonSwitched = false;
                    isOtherInputTypeSwitched = false;

                    // mMoreInputTypeLayout.setVisibility(View.GONE);
                    // mEmoticonInputLayout.setVisibility(View.GONE);
                    // mOtherInputTypeLayout.setVisibility(View.GONE);

                    mEmoticonSwitchButton.setImageResource(R.drawable.a7a);

                    inputMethodManager.showSoftInput(mMessageInputEditText, InputMethodManager.SHOW_FORCED);
                }
            }
        });

        mMoreInputTypeLayout = (LinearLayout) findViewById(R.id.more_input_type_layout);
        int keyboardHeight = preferences.getInt(SharedPreferencesHelper.KEYBOARD_HEIGHT_KEY, 0);
        if (keyboardHeight > 0) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mMoreInputTypeLayout.getLayoutParams();
            params.height = keyboardHeight;
            mMoreInputTypeLayout.setLayoutParams(params);
        }

        mEmoticonInputLayout = (LinearLayout) findViewById(R.id.emoticon_input_layout);
        mOtherInputTypeLayout = (LinearLayout) findViewById(R.id.other_input_type_layout);


        mContext = getApplicationContext();

        mExpressionViewPager = (ViewPager) findViewById(R.id.expression_view_pager);
        mExpressionIndicatorLayout = (LinearLayout) findViewById(R.id.expression_indicator_layout);

        for (int i = 0; i < 40; i++) {
            EmoticonDataModel emoticonDataModel = new EmoticonDataModel();
            emoticonDataModel.setName("微" + i);
            mSourceDataList.add(emoticonDataModel);
        }

        loadEmoticonViews();
    }

    private void loadEmoticonViews() {

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
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(12, 12);
            layoutParams.setMargins(16, 0, 16, 0);
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onActionModeStarted(ActionMode mode) {
        super.onActionModeStarted(mode);
        mActionBarContainer.setVisibility(View.GONE);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
        mActionBarContainer.setVisibility(View.VISIBLE);
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
        Intent intent = new Intent(ChatFormActivity.this, MainActivity.class);
        ChatFormActivity.this.startActivity(intent);
        ChatFormActivity.this.finish();
        // 设置切换动画
        ChatFormActivity.this.overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_close_exit);
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
                if (v.getTag() instanceof EmoticonDataModel) {
                    EmoticonDataModel emoticonDataModel = (EmoticonDataModel) v.getTag();

                    int index = mMessageInputEditText.getSelectionStart();
                    Editable editable = mMessageInputEditText.getEditableText();
                    if (index < 0 || index >= editable.length()) {
                        editable.append(emoticonDataModel.getName());
                        // Toast.makeText(mContext, "position： " + id, Toast.LENGTH_SHORT).show();
                    } else {
                        editable.insert(index, emoticonDataModel.getName());
                    }
                } else {
                    // 删除键
                    KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL);
                    KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL);
                    mMessageInputEditText.onKeyDown(KeyEvent.KEYCODE_DEL, keyEventDown);
                    mMessageInputEditText.onKeyUp(KeyEvent.KEYCODE_DEL, keyEventUp);
                }
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
            if (DEBUG) Log.d(TAG, "size:" + size);
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

            viewHolder.layout.setOnClickListener(ChatFormActivity.this);

            if (((nowPosition + 1) % GRID_VIEW_COUNT == 0) || ((nowPosition + 1) == (mSourceDataList.size() + mPageCount * 1))) {
                // viewHolder.name.setText("DEL");
                viewHolder.icon.setImageResource(R.drawable.selector_emoticon_delete);
                // setBackgroundDrawable(new ColorDrawable(0x00000000));
                viewHolder.layout.setBackgroundColor(Color.parseColor("#00000000"));
                viewHolder.layout.setTag(nowPosition);

                /*
                // 长按删除 setOnLongClickListener
                viewHolder.layout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                // 按住事件发生后执行代码的区域
                                deleteCharacterThread = new DeleteCharacterThread();
                                isOnLongClick = true;
                                deleteCharacterThread.start();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                // 移动事件发生后执行代码的区域
                                if (deleteCharacterThread != null) {
                                    isOnLongClick = true;
                                }
                                break;
                            case MotionEvent.ACTION_UP:
                                // 松开事件发生后执行代码的区域
                                if (deleteCharacterThread != null) {
                                    isOnLongClick = false;
                                }
                                break;
                        }
                        return true;
                    }
                });
                */

            } else {


                // mSourceDataList.get(nowPosition).getName()
                if (mSourceDataList.get(nowPosition - mPagePosition) != null) {
                    // Log.e(TAG, mSourceDataList.get(nowPosition - mPagePosition).getName() + " " + nowPosition);
                    // viewHolder.name.setText(mSourceDataList.get(nowPosition - mPagePosition).getName());
                    viewHolder.layout.setTag(mSourceDataList.get(nowPosition - mPagePosition));
                }
            }
            return convertView;
        }
    }

    static class ViewHolder {
        RelativeLayout layout;
        ImageView icon;
        TextView name;
    }

    /*
    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (mMessageInputEditText.getText().length() > 0) {
                        mMessageInputEditText.setText(mMessageInputEditText.getText().toString().substring(0, mMessageInputEditText.getText().length() - 1));
                        mMessageInputEditText.setSelection(mMessageInputEditText.getText().length());
                    }
                    break;
            }
        }
    };

    class DeleteCharacterThread extends Thread {

        @Override
        public void run() {
            while (isOnLongClick) {
                try {
                    Thread.sleep(100);
                    myHandler.sendEmptyMessage(1);
                    super.run();
                } catch (InterruptedException e) {

                }
            }
        }
    }
    */

}

