package net.quduo.pixel.interfaces.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.quduo.pixel.BuildConfig;
import net.quduo.pixel.R;

public class ChatFormActivity extends Activity {

    private static final String TAG = ChatFormActivity.class.getName();
    private static final boolean DEBUG = BuildConfig.DEBUG;

    private Button mBackActionItem;
    private ImageView mBackActionDivider;
    private TextView mActionTitle;
    private Button mUserInfoActionItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_form);

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

