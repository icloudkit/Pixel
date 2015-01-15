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

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.quduo.pixel.BuildConfig;
import net.quduo.pixel.R;

/**
 * SearchActivity
 *
 * @author hongquanli <hongquanli@qq.com>
 * @version 1.0 2015-01-13 17:6:02
 */
public class SearchActivity extends Activity {

    private static final String TAG = SearchActivity.class.getName();

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private ActionBar mActionBar;

    private Button mBackActionItem;
    private ImageView mBackActionDivider;

    private LinearLayout mSearchAreaLayout;
    private TextView mSearchIcon;
    private TextView mVoiceIcon;
    private RelativeLayout mVoiceLayout;
    private TextView mCleanIcon;
    private EditText mSearchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 去标题栏
        // getWindow().requestFeature(Window.FEATURE_PROGRESS);

        // setupActionBar();

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

        mSearchAreaLayout = (LinearLayout) findViewById(R.id.search_area_layout);
        mSearchAreaLayout.setVisibility(View.VISIBLE);
        mSearchIcon = (TextView) findViewById(R.id.search_icon);
        mVoiceLayout = (RelativeLayout) findViewById(R.id.voice_layout);
        mVoiceLayout.setVisibility(View.VISIBLE);
        mVoiceIcon = (TextView) findViewById(R.id.voice_icon);
        mCleanIcon = (TextView) findViewById(R.id.clean_icon);
        mCleanIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mSearchEditText.setText("");
            }
        });
        mSearchIcon.setTypeface(typeface);
        mVoiceIcon.setTypeface(typeface);
        mCleanIcon.setTypeface(typeface);

        mSearchEditText = (EditText) findViewById(R.id.search_edit_text);
        // mSearchEditText.setFocusable(true);
        // mSearchEditText.setFocusableInTouchMode(true);
        // mSearchEditText.clearFocus();
        // mSearchEditText.setSelected(true);
        // mSearchEditText.setSelectAllOnFocus(true);
        // mSearchEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        // mSearchEditText.requestFocus();

        // mSearchEditText.setShowSoftInputOnFocus(true);
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                Log.e(TAG, mSearchEditText.getText().toString());
                if (!mSearchEditText.getText().toString().trim().equals("")) {
                    mVoiceLayout.setVisibility(View.GONE);
                    mCleanIcon.setVisibility(View.VISIBLE);
                } else {
                    mVoiceLayout.setVisibility(View.VISIBLE);
                    mCleanIcon.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /*
        // ActionMode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            mSearchEditText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }
            });

            ActionMode.Callback mCallback = new ActionMode.Callback() {

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    // TODO Auto-generated method stub
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.menu_global, menu);

                    return true;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    boolean ret = false;
                    if (item.getItemId() == R.id.action_settings) {
                        mode.finish();
                        ret = true;
                    }
                    return ret;
                }
            };

            mSearchEditText.setOnLongClickListener(new View.OnLongClickListener() {
                // Called when the user long-clicks on someView
                public boolean onLongClick(View view) {
                    if (mActionMode != null) {
                        return false;
                    }

                    // Start the CAB using the ActionMode.Callback defined above
                    mActionMode = getActivity().startActionMode(mActionModeCallback);
                    view.setSelected(true);
                    return true;
                }
            });
        }
        */

        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // InputMethodManager inputMethodManager = (InputMethodManager) mSearchEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        // ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(mSearchEditText, 0);
        // InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        //隐藏软键盘
        // inputMethodManager.hideSoftInputFromWindow(editView.getWindowToken(), 0);
        //显示软键盘
        // inputMethodManager.showSoftInputFromInputMethod(mSearchEditText.getWindowToken(), 0);
        //切换软键盘的显示与隐藏
        // inputMethodManager.toggleSoftInputFromWindow(editView.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /*
    private void setupActionBar() {
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayUseLogoEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(false);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        View view = LayoutInflater.from(this).inflate(R.layout.search_action, null);
        mActionBar.setCustomView(view, params);
        // mActionBar.setCustomView(R.layout.search_action);

        //*
        RelativeLayout relative = new RelativeLayout(mActionBar.getThemedContext());
        SearchView mSearchView = new SearchView(mActionBar.getThemedContext());
        mSearchView.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // mSearchView.setIconifiedByDefault(false);
        mSearchView.setGravity(Gravity.LEFT);
        relative.addView(mSearchView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(relative);
        //*

        // Log.e(TAG, findViewById(android.R.id.home) + "---");
    }
    */

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_global, menu);
        // Log.e(TAG, "" + menu.findItem(android.R.id.home));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // Log.e(TAG, "" + item.getItemId());
        //noinspection SimplifiableIfStatement

        // Respond to the action bar's Up/Home button
        if (id == android.R.id.home) {

            // onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
        SearchActivity.this.startActivity(intent);
        // 设置切换动画
        overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_close_exit);
        SearchActivity.this.finish();
    }
}
