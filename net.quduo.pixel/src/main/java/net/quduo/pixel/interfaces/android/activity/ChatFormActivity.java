package net.quduo.pixel.interfaces.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.quduo.pixel.BuildConfig;
import net.quduo.pixel.R;
import net.quduo.pixel.interfaces.android.common.SharedPreferencesHelper;

public class ChatFormActivity extends Activity {

    private static final String TAG = ChatFormActivity.class.getName();
    private static final boolean DEBUG = BuildConfig.DEBUG;

    private SharedPreferences preferences;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_form);

        preferences = SharedPreferencesHelper.getSharedPreferences(this);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/icon/iconfont.ttf");

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

        /*
        mDemoType.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 按住事件发生后执行代码的区域
                        Log.e(TAG, event.getX() + "-------------1");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 移动事件发生后执行代码的区域
                        Log.e(TAG, event.getX() + "-------------");
                        // v.setScrollX((int) event.getX());
                        break;
                    case MotionEvent.ACTION_UP:
                        // 松开事件发生后执行代码的区域
                        Log.e(TAG, event.getX() + "-------------2");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        */

        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

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
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

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

                    mMessageInputEditText.clearFocus();
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
        if(keyboardHeight > 0) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mMoreInputTypeLayout.getLayoutParams();
            params.height = keyboardHeight;
            mMoreInputTypeLayout.setLayoutParams(params);
        }

        mEmoticonInputLayout = (LinearLayout) findViewById(R.id.emoticon_input_layout);
        mOtherInputTypeLayout = (LinearLayout) findViewById(R.id.other_input_type_layout);


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
}

