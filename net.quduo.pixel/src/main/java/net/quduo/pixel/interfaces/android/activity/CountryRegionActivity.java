package net.quduo.pixel.interfaces.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.quduo.pixel.BuildConfig;
import net.quduo.pixel.R;
import net.quduo.pixel.interfaces.android.common.CharacterParser;
import net.quduo.pixel.interfaces.android.common.CommonConstant;
import net.quduo.pixel.interfaces.android.common.CountryRegionAdapter;
import net.quduo.pixel.interfaces.android.model.CountryRegionDataModel;
import net.quduo.pixel.interfaces.android.widget.LetterQueryView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * CountryRegionActivity
 *
 * @author hongquanli <hongquanli@qq.com>
 * @version 1.0 2015-01-13 17:6:02
 */
public class CountryRegionActivity extends Activity {

    private static final String TAG = CountryRegionActivity.class.getName();

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private String target;

    private Button mBackActionItem;
    private ImageView mBackActionDivider;
    private TextView mActionTitle;
    private Button mSearchActionItem;

    private LinearLayout mSearchAreaLayout;
    private TextView mSearchIcon;
    private TextView mVoiceIcon;
    private RelativeLayout mVoiceLayout;
    private TextView mCleanIcon;
    private EditText mSearchEditText;

    private CountryRegionAdapter mCountryRegionAdapter;
    private List<CountryRegionDataModel> mSourceDataList;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser mCharacterParser;

    private ListView mCountryRegionListView;

    private LetterQueryView mLetterQueryView;
    private TextView mLetterTextDialog;

    private Bundle mCountryRegionBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_region);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/icon/iconfont.ttf");

        // 接收来自 Activity 的参数
        Intent intent = getIntent();
        target = intent.getStringExtra(CommonConstant.SOURCE_TAG_KEY);
        /*
        String Name = intent.getStringExtra("Name");
        int Age = intent.getIntExtra("Age", 0);
        Bundle bundle = intent.getExtras();
        String Name1 = bundle.getString("Name1");
        int Age1 = bundle.getInt("Age1");
        Log.e(TAG, Name + " : " + Age + "/" + Name1 + " : " + Age1);
        */

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

        mSearchActionItem = (Button) findViewById(R.id.search_action_item);
        mSearchActionItem.setTypeface(typeface);
        mSearchActionItem.setVisibility(View.VISIBLE);
        mSearchActionItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mSearchAreaLayout.setVisibility(View.VISIBLE);
                mActionTitle.setVisibility(View.GONE);
                mSearchActionItem.setVisibility(View.GONE);
                mSearchEditText.requestFocus();
            }
        });

        mActionTitle = (TextView) findViewById(R.id.action_title);
        mActionTitle.setVisibility(View.VISIBLE);
        mActionTitle.setText("选择国家和地区代码");

        mSearchAreaLayout = (LinearLayout) findViewById(R.id.search_area_layout);
        mSearchIcon = (TextView) findViewById(R.id.search_icon);
        mSearchIcon.setTypeface(typeface);
        mVoiceLayout = (RelativeLayout) findViewById(R.id.voice_layout);
        mVoiceIcon = (TextView) findViewById(R.id.voice_icon);
        mVoiceIcon.setTypeface(typeface);
        mCleanIcon = (TextView) findViewById(R.id.clean_icon);
        mCleanIcon.setTypeface(typeface);
        mCleanIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mSearchEditText.setText("");
            }
        });

        mSearchEditText = (EditText) findViewById(R.id.search_edit_text);
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

                List<CountryRegionDataModel> filterDateList = new ArrayList<CountryRegionDataModel>();
                String keywords = charSequence.toString();
                if (charSequence.toString().equals("")) {
                    filterDateList = mSourceDataList;
                } else {
                    filterDateList.clear();
                    for (CountryRegionDataModel dataModel : mSourceDataList) {
                        String name = dataModel.getDataTitle();
                        if (name.indexOf(keywords.toString()) != -1 || mCharacterParser.getSelling(name).startsWith(keywords.toString())) {
                            filterDateList.add(dataModel);
                        }
                    }
                }

                // TODO 根据a-z进行排序
                Collections.sort(filterDateList, new Comparator<CountryRegionDataModel>() {

                    public int compare(CountryRegionDataModel o1, CountryRegionDataModel o2) {
                        if (o1.getCatalogLetter().equals("@") || o2.getCatalogLetter().equals("#")) {
                            return -1;
                        } else if (o1.getCatalogLetter().equals("#") || o2.getCatalogLetter().equals("@")) {
                            return 1;
                        } else {
                            return o1.getCatalogLetter().compareTo(o2.getCatalogLetter());
                        }
                    }

                });
                mCountryRegionAdapter.updateListView(filterDateList);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mLetterQueryView = (LetterQueryView) findViewById(R.id.letter_query_view);
        mLetterTextDialog = (TextView) findViewById(R.id.letter_text_dialog);
        mLetterQueryView.setTextView(mLetterTextDialog);

        // 设置右侧触摸监听
        mLetterQueryView.setOnTouchingLetterChangedListener(new LetterQueryView.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = mCountryRegionAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mCountryRegionListView.setSelection(position);
                }

            }
        });

        mCountryRegionListView = (ListView) findViewById(R.id.country_region_list_view);
        mCountryRegionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // TODO adapter.getItem(position)获取当前position所对应的对象
                // Toast.makeText(getApplication(), ((SortDataModel) mCountryRegionAdapter.getItem(position)).getDataTitle(), Toast.LENGTH_SHORT).show();
                mCountryRegionBundle = new Bundle();
                mCountryRegionBundle.putSerializable("result", (CountryRegionDataModel) mCountryRegionAdapter.getItem(position));

                Intent intent = null;
                if (target != null) {
                    if (target.equals(LoginActivity.class.getName())) {
                        intent = new Intent(CountryRegionActivity.this, LoginActivity.class);
                        intent.putExtras(mCountryRegionBundle);
                    }
                    if (target.equals(RegisterActivity.class.getName())) {
                        intent = new Intent(CountryRegionActivity.this, RegisterActivity.class);
                        intent.putExtras(mCountryRegionBundle);
                    }
                    CountryRegionActivity.this.setResult(RESULT_OK, intent);
                    CountryRegionActivity.this.finish();
                    // 设置切换动画
                    overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_close_exit);
                }

            }
        });

        // 实例化汉字转拼音类
        mCharacterParser = CharacterParser.getInstance();

        Locale[] locales = Locale.getAvailableLocales();
        mSourceDataList = new ArrayList<CountryRegionDataModel>();

        for (int i = 0; i < locales.length; i++) {

            if (DEBUG)
                Log.d(TAG, "Country:" + locales[i].getCountry() + " Language:" + locales[i].getLanguage() + " DisplayName:" + locales[i].getDisplayName() + " DisplayCountry:" + locales[i].getDisplayCountry() + " DisplayVariant:" + locales[i].getDisplayVariant());

            String displayCountry = locales[i].getDisplayCountry();
            if (!displayCountry.equals("") && locales[i].getCountry().length() == 2) {
                // Log.d(TAG, locales[i].getCountry() + "-");
                CountryRegionDataModel dataModel = new CountryRegionDataModel();
                dataModel.setDataTitle(displayCountry);

                //汉字转换成拼音
                String spell = mCharacterParser.getSelling(displayCountry);
                String sortString = spell.substring(0, 1).toUpperCase();

                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]")) {
                    dataModel.setCatalogLetter(sortString.toUpperCase());
                } else {
                    dataModel.setCatalogLetter("#");
                }

                mSourceDataList.add(dataModel);
            }
        }

        // 根据a-z进行排序源数据
        Collections.sort(mSourceDataList, new Comparator<CountryRegionDataModel>() {

            public int compare(CountryRegionDataModel o1, CountryRegionDataModel o2) {
                if (o1.getCatalogLetter().equals("@") || o2.getCatalogLetter().equals("#")) {
                    return -1;
                } else if (o1.getCatalogLetter().equals("#") || o2.getCatalogLetter().equals("@")) {
                    return 1;
                } else {
                    return o1.getCatalogLetter().compareTo(o2.getCatalogLetter());
                }
            }

        });
        mCountryRegionAdapter = new CountryRegionAdapter(this, mSourceDataList);
        mCountryRegionListView.setAdapter(mCountryRegionAdapter);
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

        CountryRegionActivity.this.finish();
    }
}
