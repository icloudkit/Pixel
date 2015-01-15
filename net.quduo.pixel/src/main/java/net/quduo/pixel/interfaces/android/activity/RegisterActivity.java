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

package net.quduo.pixel.interfaces.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.quduo.pixel.BuildConfig;
import net.quduo.pixel.R;
import net.quduo.pixel.interfaces.android.common.CommonConstant;
import net.quduo.pixel.interfaces.android.common.MobileTextWatcher;
import net.quduo.pixel.interfaces.android.model.CountryRegionDataModel;

/**
 * RegisterActivity
 *
 * @author hongquanli <hongquanli@qq.com>
 * @version 1.0 2015-01-13 17:6:02
 */
public class RegisterActivity extends Activity {

    private static final String TAG = RegisterActivity.class.getName();

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private Button mBackActionItem;
    private ImageView mBackActionDivider;
    private TextView mActionTitle;

    // private LinearLayout mNicknameAreaLayout;
    private EditText mNicknameEditText;
    private ImageView mNicknameEditTextLine;
    private RelativeLayout mAvatarSelectAreaLayout;
    private TextView mAvatarSelectIcon;
    private ImageView mAvatarSelectBackground;
    private LinearLayout mCountryRegionAreaLayout;
    private TextView mCountryRegionDisplayName;
    private EditText mCountryRegionEditText;
    private ImageView mCountryRegionEditTextLine;
    // private LinearLayout mMobileAreaLayout;
    private EditText mMobileEditText;
    private ImageView mMobileEditTextLine;
    private TextView mCleanActionIcon;
    // private LinearLayout mPasswordAreaLayout;
    private EditText mPasswordEditText;
    private ImageView mPasswordEditTextLine;
    private TextView mEyeViewActionIcon;

    private TextView mRegisterButton;
    private TextView mLoginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
        mActionTitle.setText("填写手机号");

        // mNicknameAreaLayout = (LinearLayout) findViewById(R.id.nickname_area_layout);
        mNicknameEditText = (EditText) findViewById(R.id.nickname_edit_text);
        mNicknameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b) {
                if (DEBUG) Log.d(TAG, "boolean:" + b);
                if (b) {
                    mNicknameEditTextLine.setImageResource(R.drawable.line_edit_text_pink);
                } else {
                    mNicknameEditTextLine.setImageResource(R.drawable.line_edit_text_grey);
                }
            }
        });
        mNicknameEditTextLine = (ImageView) findViewById(R.id.nickname_edit_text_line);
        mAvatarSelectAreaLayout = (RelativeLayout) findViewById(R.id.avatar_select_area_layout);
        mAvatarSelectIcon = (TextView) findViewById(R.id.avatar_select_icon);
        mAvatarSelectIcon.setTypeface(typeface);
        mAvatarSelectBackground = (ImageView) findViewById(R.id.avatar_select_background);
        mAvatarSelectAreaLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.parseColor("#ffaed3a1"));
                        mAvatarSelectBackground.setImageResource(R.drawable.decorator_box_rb_pressed);
                        break;
                    default:
                        v.setBackgroundColor(Color.parseColor("#ffdcdcdc"));
                        mAvatarSelectBackground.setImageResource(R.drawable.decorator_box_rb_normal);

                }
                return true;
            }
        });
        mAvatarSelectAreaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 选择头像
            }
        });
        mCountryRegionAreaLayout = (LinearLayout) findViewById(R.id.country_region_area_layout);
        mCountryRegionAreaLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RegisterActivity.this, CountryRegionActivity.class);
                intent.putExtra(CommonConstant.SOURCE_TAG_KEY, TAG);
                RegisterActivity.this.startActivityForResult(intent, 0);
                // RegisterActivity.this.finish();
                // 设置切换动画
                overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_close_exit);
            }
        });
        mCountryRegionDisplayName = (TextView) findViewById(R.id.country_region_display_name);
        mCountryRegionEditText = (EditText) findViewById(R.id.country_region_edit_text);
        mCountryRegionEditTextLine = (ImageView) findViewById(R.id.country_region_edit_text_line);
        // mMobileAreaLayout = (LinearLayout) findViewById(R.id.mobile_area_layout);
        mMobileEditText = (EditText) findViewById(R.id.mobile_edit_text);
        mCleanActionIcon = (TextView) findViewById(R.id.clean_action_icon);
        mCleanActionIcon.setTypeface(typeface);
        mMobileEditText.addTextChangedListener(new MobileTextWatcher(mMobileEditText, mCleanActionIcon));
        mMobileEditTextLine = (ImageView) findViewById(R.id.mobile_edit_text_line);
        mCleanActionIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mMobileEditText.setText("");
            }
        });
        mMobileEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b) {
                if (DEBUG) Log.d(TAG, "boolean:" + b);
                if (b) {
                    mCountryRegionEditTextLine.setImageResource(R.drawable.line_edit_text_pink);
                    mMobileEditTextLine.setImageResource(R.drawable.line_edit_text_left_open_pink);
                } else {
                    mCountryRegionEditTextLine.setImageResource(R.drawable.line_edit_text_grey);
                    mMobileEditTextLine.setImageResource(R.drawable.line_edit_text_left_open_grey);
                }
            }
        });
        mCountryRegionEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b) {
                if (DEBUG) Log.d(TAG, "boolean:" + b);
                if (b) {
                    mCountryRegionEditTextLine.setImageResource(R.drawable.line_edit_text_pink);
                    mMobileEditTextLine.setImageResource(R.drawable.line_edit_text_left_open_pink);
                } else {
                    mCountryRegionEditTextLine.setImageResource(R.drawable.line_edit_text_grey);
                    mMobileEditTextLine.setImageResource(R.drawable.line_edit_text_left_open_grey);
                }
            }
        });
        // mPasswordAreaLayout = (LinearLayout) findViewById(R.id.password_area_layout);
        mPasswordEditText = (EditText) findViewById(R.id.password_edit_text);
        mPasswordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b) {
                if (DEBUG) Log.d(TAG, "boolean:" + b);
                if (b) {
                    mPasswordEditTextLine.setImageResource(R.drawable.line_edit_text_pink);
                } else {
                    mPasswordEditTextLine.setImageResource(R.drawable.line_edit_text_grey);
                }
            }
        });
        mPasswordEditTextLine = (ImageView) findViewById(R.id.password_edit_text_line);
        mEyeViewActionIcon = (TextView) findViewById(R.id.eye_view_action_icon);
        mEyeViewActionIcon.setTypeface(typeface);
        mEyeViewActionIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mPasswordEditText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {

                    mPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mEyeViewActionIcon.setTextColor(getResources().getColor(R.color.grey_500));
                } else {

                    mPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mEyeViewActionIcon.setTextColor(Color.parseColor("#ffaed3a1"));
                }
                mPasswordEditText.setSelection(mPasswordEditText.getText().length());
            }
        });

        mRegisterButton = (TextView) findViewById(R.id.register_button);
        mRegisterButton.setEnabled(true);
        mLoginLink = (TextView) findViewById(R.id.login_link);
        mLoginLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(intent);
                // 设置切换动画
                overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_close_exit);
                RegisterActivity.this.finish();
            }
        });
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 根据状态码，处理返回结果
        switch (resultCode) {
            case RESULT_OK:
                // 获取intent里面的bundle对象
                Bundle bundle = data.getExtras();
                CountryRegionDataModel result = (CountryRegionDataModel) bundle.getSerializable("result");
                Log.e(TAG, "result:" + result.getDataTitle());
                mCountryRegionDisplayName.setText(result.getDataTitle());
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(RegisterActivity.this, GuideActivity.class);
        RegisterActivity.this.startActivity(intent);
        // 设置切换动画
        overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_close_exit);
        RegisterActivity.this.finish();
    }
}
