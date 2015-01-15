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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.quduo.pixel.BuildConfig;
import net.quduo.pixel.R;
import net.quduo.pixel.interfaces.android.common.CommonConstant;
import net.quduo.pixel.interfaces.android.common.MobileTextWatcher;
import net.quduo.pixel.interfaces.android.model.CountryRegionDataModel;

/**
 * LoginActivity
 *
 * @author hongquanli <hongquanli@qq.com>
 * @version 1.0 2015-01-13 17:6:02
 */
public class LoginActivity extends Activity {

    private static final String TAG = LoginActivity.class.getName();

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private Button mBackActionItem;
    private ImageView mBackActionDivider;
    private TextView mActionTitle;
    private TextView mHelpActionItem;

    private LinearLayout mCountryRegionAreaLayout;
    private TextView mCountryRegionDisplayName;
    private EditText mCountryRegionEditText;
    private ImageView mCountryRegionEditTextLine;
    private LinearLayout mMobileAreaLayout;
    private EditText mMobileEditText;
    private ImageView mMobileEditTextLine;
    private TextView mCleanActionIcon;
    private LinearLayout mUsernameAreaLayout;
    private EditText mUsernameEditText;
    private ImageView mUsernameEditTextLine;
    // private LinearLayout mPasswordAreaLayout;
    private EditText mPasswordEditText;
    private ImageView mPasswordEditTextLine;
    private TextView mEyeViewActionIcon;

    private TextView mLoginButton;
    private TextView mRegisterLink;

    private TextView mOtherLoginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        mActionTitle.setText("使用手机号登录");

        mHelpActionItem = (Button) findViewById(R.id.help_action_item);
        mHelpActionItem.setVisibility(View.VISIBLE);
        mHelpActionItem.setTypeface(typeface);

        mCountryRegionAreaLayout = (LinearLayout) findViewById(R.id.country_region_area_layout);
        mCountryRegionAreaLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, CountryRegionActivity.class);
                /*
                // 以下二个为 Activity 传参数
                intent.putExtra("Name", "eboy");
                intent.putExtra("Age", 22);
                // 也可以使用 Bundle 来传参数
                Bundle bundle = new Bundle();
                bundle.putString("Name1", "eboy1");
                bundle.putInt("Age1", 23);
                intent.putExtras(bundle);
                */
                intent.putExtra(CommonConstant.SOURCE_TAG_KEY, TAG);
                LoginActivity.this.startActivityForResult(intent, 0);
                // LoginActivity.this.finish();
                // 设置切换动画
                overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_close_exit);
            }
        });
        mCountryRegionDisplayName = (TextView) findViewById(R.id.country_region_display_name);
        mCountryRegionEditText = (EditText) findViewById(R.id.country_region_edit_text);
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
        mCountryRegionEditTextLine = (ImageView) findViewById(R.id.country_region_edit_text_line);
        mMobileAreaLayout = (LinearLayout) findViewById(R.id.mobile_area_layout);
        mMobileEditText = (EditText) findViewById(R.id.mobile_edit_text);
        mCleanActionIcon = (TextView) findViewById(R.id.clean_action_icon);
        mCleanActionIcon.setTypeface(typeface);
        mMobileEditText.addTextChangedListener(new MobileTextWatcher(mMobileEditText, mCleanActionIcon));
        mCleanActionIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mMobileEditText.setText("");
            }
        });
        mMobileEditTextLine = (ImageView) findViewById(R.id.mobile_edit_text_line);
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
        mMobileEditText.requestFocus();
        mUsernameAreaLayout = (LinearLayout) findViewById(R.id.username_area_layout);
        mUsernameEditText = (EditText) findViewById(R.id.username_edit_text);
        mUsernameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b) {
                if (DEBUG) Log.d(TAG, "boolean:" + b);
                if (b) {
                    mUsernameEditTextLine.setImageResource(R.drawable.line_edit_text_pink);
                } else {
                    mUsernameEditTextLine.setImageResource(R.drawable.line_edit_text_grey);
                }
            }
        });
        mUsernameEditTextLine = (ImageView) findViewById(R.id.username_edit_text_line);
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

        mLoginButton = (TextView) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(intent);
                // 设置切换动画
                overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_close_exit);
                LoginActivity.this.finish();
            }
        });
        mLoginButton.setEnabled(true);
        mRegisterLink = (TextView) findViewById(R.id.register_link);
        mRegisterLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
                LoginActivity.this.finish();
                // 设置切换动画
                overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_close_exit);
            }
        });

        mOtherLoginLink = (TextView) findViewById(R.id.other_login_link);
        mOtherLoginLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (mCountryRegionAreaLayout.getVisibility() == View.GONE) {
                    mCountryRegionAreaLayout.setVisibility(View.VISIBLE);
                    mMobileAreaLayout.setVisibility(View.VISIBLE);
                    mMobileAreaLayout.requestFocus();
                    mUsernameAreaLayout.setVisibility(View.GONE);
                    mActionTitle.setText("使用手机号登录");
                } else {
                    mCountryRegionAreaLayout.setVisibility(View.GONE);
                    mMobileAreaLayout.setVisibility(View.GONE);
                    mUsernameAreaLayout.setVisibility(View.VISIBLE);
                    mUsernameAreaLayout.requestFocus();
                    mActionTitle.setText("登录彩信");
                }
            }
        });

        // 130 150 170
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

    /*
    // 保存数据 暂停：onStart()->onResume()->onPause()
    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Lifecycle_Activity1", "onPause()");

        //把数据保存到类似于Session之类的存储集合里面
        SharedPreferences.Editor saveData = getPreferences(0).edit();
        saveData.putString("value", et_string.getText().toString());
        saveData.commit();
    }

    // 提取数据重启：onStart()->onResume()
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Lifecycle_Activity1", "onResume()");

        //从共享数据存储对象中获取所需的数据
        SharedPreferences getData = getPreferences(0);
        String value = getData.getString("value", null);
        if(value != null) {
            et_string.setText(value);
        }
    }
    */

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginActivity.this, GuideActivity.class);
        LoginActivity.this.startActivity(intent);
        // 设置切换动画
        overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_close_exit);
        LoginActivity.this.finish();
    }
}
