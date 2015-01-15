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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.quduo.pixel.BuildConfig;
import net.quduo.pixel.R;

/**
 * WhatsNewActivity
 *
 * @author hongquanli <hongquanli@qq.com>
 * @version 1.0 2015-01-13 17:6:02
 */
public class WebViewActivity extends Activity {

    private static final String TAG = WebViewActivity.class.getName();

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private Button mBackActionItem;
    private ImageView mBackActionDivider;
    private TextView mActionTitle;

	private WebView mWebView;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		// 声明使用自定义标题
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_web_view);
//		// 自定义布局赋值
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_toolbar);


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
        mActionTitle.setText("帮助");

        mWebView = (WebView) findViewById(R.id.web_view);
		
		// 设置WebView属性，能够执行Javascript脚本
		// 如果访问的页面中有Javascript，则webview必须设置支持Javascript
		mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);

        mWebView.setBackgroundColor(Color.TRANSPARENT);
		
		
//		private TextView textView;
//		textView.setMovementMethod(new LinkMovementMethod());
		
		
//		// private Handler mHandler = new Handler();
//		mWebView.addJavascriptInterface(new Object() {
//            public void clickOnAndroid() {
//                mHandler.post(new Runnable() {
//                    public void run() {
//                        mWebView.loadUrl("javascript:wave()");
//                    }
//                });
//            }
//        }, "demo");

		
		// 加载需要显示的网页
		// webview.loadUrl(getResources().openRawResource(R.raw.whatsnew).toString());
		// 指定BaseURL為file:///android_res/raw/，如此一來，html的圖檔，才能正確顯示
		// getResources().openRawResource(R.raw.whatsnew).toString()
		
		// webview.loadDataWithBaseURL("file:///android_res/raw/", readTextFromResource(R.raw.whatsnew), "text/html", "UTF-8", null);
		
		// Load html files from raw folder in web view
		// File lFile = new File(Environment.getExternalStorageDirectory() + "<FOLDER_PATH_TO_FILE>/<FILE_NAME>");
		// lWebView.loadUrl("file:///" + lFile.getAbsolutePath());
		// mWebView.loadUrl("file:///android_res/raw/whatsnew.html");

		// // 打开本包内asset目录下的index.html文件
		// mWebView.loadurl("file:///android_asset/www/index.html");

		// // 打开本地sd卡内的index.html文件
		// mWebView.loadUrl("content://com.snippets.snail/sdcard/index.html");

		// // 打开指定URL的html文件
		// mWebView.loadUrl("http://www.google.com/");
		mWebView.loadUrl("http://m.so.com/");

        // mWebView.loadUrl("file:///android_asset/html/default/whatsnew.html");
		
		// 设置Web视图
		// 如果页面中链接，如果希望点击链接继续在当前browser中响应，而不是新开Android的系统browser中响应该链接，必须覆盖 webview的WebViewClient对象.
		// new CustomWebViewClient()
		mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                // super.shouldOverrideUrlLoading(view, url)
                return true;
            }
        });
	}

//	/**
//	 * 从raw 读取 raw file，以支持多国语言
//	 */
//	private String readTextFromResource(int resourceID) {
//		InputStream raw = getResources().openRawResource(resourceID);
//		ByteArrayOutputStream stream = new ByteArrayOutputStream();
//		int i;
//		try {
//			i = raw.read();
//			while (i != -1) {
//				stream.write(i);
//				i = raw.read();
//			}
//			raw.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return stream.toString();
//	}

	/**
	 * 回退MainActivity
	 */
	@Override
	public void onBackPressed() {
		// Intent intent = new Intent();
		// intent.setClass(WhatsNewActivity.this, MainActivity.class);
		
		Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
		startActivity(intent);
		// 如果不关闭当前的会出现好多个页面
		WebViewActivity.this.finish();
        overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_close_exit);
	}

	/**
	 * 设置回退,覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			// goBack()表示返回WebView的上一页面
			mWebView.goBack();
			return true;
		} else {
			onBackPressed();
		}
		return false;
	}

}
