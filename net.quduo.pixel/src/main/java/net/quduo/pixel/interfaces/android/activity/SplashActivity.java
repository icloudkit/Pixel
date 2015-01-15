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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.widget.TextView;

import net.quduo.pixel.BuildConfig;
import net.quduo.pixel.R;

/**
 * SplashActivity
 *
 * @author hongquanli <hongquanli@qq.com>
 * @version 1.0 2015-01-13 17:6:02
 */
public class SplashActivity extends Activity {

    private static final String TAG = SplashActivity.class.getName();

    private static final boolean DEBUG = BuildConfig.DEBUG;

	// 延迟3秒 SPLASH_DISPLAY_LENGHT 
 	private static final long SPLASH_DELAY_MILLIS = 1000;
    
    private static final String SHARED_PREFERENCES_NAME = "first_pref";
    
	private static final int REDIRECT_GUIDE = 1000;
	private static final int REDIRECT_LOGIN = 1001;
	private static final int REDIRECT_HOME = 1002;
	
	// private final static String[] messages = new String[] { "不积跬步，无以至千里；不积小流，无以成江海。——《荀子劝学》", "反省不是去后悔，是为前进铺路。", "哭着流泪是怯懦的宣泄，笑着流泪是勇敢的宣言。", "路漫漫其修远兮，吾将上下而求索。——屈原《离骚》", "每一个成功者都有一个开始。勇于开始，才能找到成功的路。", "人生的目的不是为了活得长，是为了活得好。", "若不给自己设限，则人生中就没有限制你发挥的藩篱。", "所有的魅力在“习惯”面前都将无色。", " 重要的不是要得到什么，是珍重已经拥有的。", "天才就是回避艰苦工作的能力。——埃·哈伯德", "这世界没有未完的故事，只有未死的心。" };
    
    private boolean isFirstIn = false;
    
    // 跳转到不同界面
	private Handler redirectHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REDIRECT_HOME:
				// SystemClock.sleep(4000);      
                // splash.setVisibility(View.GONE);
				redirectHome();
				break;
			case REDIRECT_LOGIN:
//				redirectLogin();
				break;
			case REDIRECT_GUIDE:
				redirectGuide();
				break;
			}
			super.handleMessage(msg);
		}
	};

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // // 去掉标题栏 Flag for the "no title" feature, turning off the title at the top of the screen.
        // requestWindowFeature(Window.FEATURE_NO_TITLE);

        // // 屏幕全屏 Window flag: Hide all screen decorations (e.g.
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // // 屏幕没有边界限制(允许窗口扩展到屏幕外) Window flag: allow window to extend outside of the screen.
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        
        getWindow().setFormat(PixelFormat.RGBA_8888);
        setContentView(R.layout.activity_splash);
        
        // Display the current version number
		PackageManager pm = getPackageManager();
		try {
		    PackageInfo pi = pm.getPackageInfo("net.quduo.pixel", 0);
		    TextView versionNumber = (TextView) findViewById(R.id.versionNumber);
		    // versionNumber.setText("V " + pi.versionName);
		} catch (NameNotFoundException e) {
		    e.printStackTrace();
		}

        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/icon/iconfont.ttf");
        TextView textview = (TextView)findViewById(R.id.appicon);
        textview.setTypeface(typeface);
        // textview.setTextSize(40);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Toast.makeText(SplashActivity.this, "loading...", Toast.LENGTH_LONG).show();

        // 使用SharedPreferences来记录程序的是否使用
        // 读取SharedPreferences中需要的数据
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        // 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
        isFirstIn = preferences.getBoolean("is_first_in", true);

//        // Handler().postDelayed  是延迟指定的时间再执行
//        // Handler类主要可以使用如下3个方法来设置执行Runnable对象的时间：
//        //  立即执行Runnable对象
//        public final boolean post(Runnable r);
//        //  在指定的时间（uptimeMillis）执行Runnable对象
//        public final boolean postAtTime(Runnable r, long uptimeMillis);
//        //  在指定的时间间隔（delayMillis）执行Runnable对象
//        public final boolean postDelayed(Runnable r, long delayMillis);

//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
//                SplashActivity.this.startActivity(mainIntent);
//                SplashActivity.this.finish();
//            }
//
//        }, SPLASH_DISPLAY_LENGHT);

        // 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
        // sendMessageDelayed(msg, SPLASHTIME);
        if (!isFirstIn) {
            // 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity REDIRECT_HOME
            redirectHandler.sendEmptyMessageDelayed(REDIRECT_GUIDE, SPLASH_DELAY_MILLIS);
        } else {
            redirectHandler.sendEmptyMessageDelayed(REDIRECT_GUIDE, SPLASH_DELAY_MILLIS);
        }

//        // 设置已经引导过了，下次启动不用再次引导
//        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
//        Editor editor = preferences.edit();
//        // 存入数据
//        editor.putBoolean("is_first_in", false);
//        // 提交修改
//        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.splash, menu);
        return false;
    }
    
	private void redirectHome() {

//      	// Goto Main
//          Handler x = new Handler();
//          x.postDelayed(new Runnable() {
//              @Override
//              public void run() {
//              	
//              	// LoginActivity MainActivity
//              	// SplashActivity.this.startActivity(intent);
//                  startActivity(new Intent(getApplication(), LoginActivity.class));
//                  // SplashActivity.this.finish();
//                  finish();
//              }
//
//          }, SPLASH_DELAY_MILLIS);
          
        // Toast.makeText(this, "Login details are saved..", 3000).show();
        // Toast.makeText(this, "已加载完成...", Toast.LENGTH_LONG).show();
		
		Intent intent = new Intent(SplashActivity.this, WebViewActivity.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}

	private void redirectGuide() {
    	// Goto Guide
		// Intent intent = new Intent();
		// intent.setClass(getApplicationContext(), GuideActivity.class);
      
		Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
		// Intent intent = new Intent(getApplicationContext(), GuideActivity.class);
		
//		// 如果已经启动了四个Activity：A，B，C和D。在D Activity里，我们要跳到B Activity，同时希望C 也finish掉，可以在startActivity(intent)里的intent里添加flags标记，如下所示：
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		// 这样启动B Activity，就会把D，C都finished掉，如果你的B Activity的启动模式是默认的（multiple） ，则B Activity会finished掉，再启动一个新的Activity B。  如果不想重新再创建一个新的B Activity，则在上面的代码里再加上：
//		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}
	
//	private void redirectLogin(){
//		Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		SplashActivity.this.startActivity(intent);
//		SplashActivity.this.finish();
//	}

}
