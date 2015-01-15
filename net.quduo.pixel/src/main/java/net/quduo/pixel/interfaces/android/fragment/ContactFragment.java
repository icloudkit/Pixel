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

package net.quduo.pixel.interfaces.android.fragment;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import net.quduo.pixel.BuildConfig;
import net.quduo.pixel.R;
import net.quduo.pixel.interfaces.android.common.CharacterParser;
import net.quduo.pixel.interfaces.android.common.ContactListAdapter;
import net.quduo.pixel.interfaces.android.model.ContactListDataModel;
import net.quduo.pixel.interfaces.android.widget.LetterQueryView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ContactFragment extends Fragment {

    private static final String TAG = ContactFragment.class.getName();

    private static final boolean DEBUG = BuildConfig.DEBUG;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private ListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ContactListAdapter mContactListAdapter;
    List<ContactListDataModel> mSourceDataList;

    private CharacterParser mCharacterParser;

    private LetterQueryView mLetterQueryView;
    private TextView mLetterTextDialog;

    /*
    // mGestureDetector = new GestureDetector(container.getContext(), mOnGestureListener);
    private GestureDetector mGestureDetector;
    private GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float x = e2.getX() - e1.getX();
            // float y = e2.getY() - e1.getY();

            if (x > 0 || x < 0) {
                mLetterQueryView.setVisibility(View.GONE);
            } else {
                mLetterQueryView.setVisibility(View.VISIBLE);
            }
            return true;
        }
    };
    */

    // TODO: Rename and change types of parameters
    public static ContactFragment newInstance(String param1, String param2) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ContactFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // 实例化汉字转拼音类
        mCharacterParser = CharacterParser.getInstance();

        // TODO: Change Adapter to display your content
        mSourceDataList = new ArrayList<ContactListDataModel>();
        ContactListDataModel sourceData = new ContactListDataModel();
        sourceData.setContactAvatar(R.drawable.default_avatar_04);
        sourceData.setContactName("新的朋友");
        sourceData.setCatalogLetter("↑");
        mSourceDataList.add(sourceData);

        ContactListDataModel sourceData1 = new ContactListDataModel();
        sourceData1.setContactAvatar(R.drawable.default_avatar_03);
        sourceData1.setContactName("群聊");
        sourceData1.setCatalogLetter("↑");
        mSourceDataList.add(sourceData1);

        ContactListDataModel sourceData2 = new ContactListDataModel();
        sourceData2.setContactAvatar(R.drawable.default_avatar_02);
        sourceData2.setContactName("标签");
        sourceData2.setCatalogLetter("↑");
        mSourceDataList.add(sourceData2);

        ContactListDataModel sourceData3 = new ContactListDataModel();
        sourceData3.setContactAvatar(R.drawable.default_avatar_01);
        sourceData3.setContactName("公众号");
        sourceData3.setCatalogLetter("↑");
        mSourceDataList.add(sourceData3);

        // mAdapter = new ArrayAdapter<DummyContent.DummyItem>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS);
        // mAdapter = new ArrayAdapter<DummyContent.DummyItem>(getActivity(), R.layout.list_item_contact, R.id.contact_name_text_view, DummyContent.ITEMS);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_contact, container, false);

        // TODO ContextMenu列表
        final ArrayList<HashMap<String, Object>> mPopupWindowListItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        // map1.put("item_image", R.drawable.icon_message);
        map1.put("item_icon", R.string.icon_chat_add_group);
        // map1.put("item_text", "这是第" + i + "行");
        map1.put("item_title", "发起群聊");
        mPopupWindowListItem.add(map1);
        HashMap<String, Object> map2 = new HashMap<String, Object>();
        // map2.put("item_image", R.drawable.icon_message);
        map2.put("item_icon", R.string.icon_chat_add_group);
        // map2.put("item_text", "这是第" + i + "行");
        map2.put("item_title", "删除");
        mPopupWindowListItem.add(map2);

        // Set the adapter
        mContactListAdapter = new ContactListAdapter(this.getActivity(), mSourceDataList);
        mListView = (ListView) view.findViewById(R.id.contact_list_view);
        mListView.setAdapter(mContactListAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    // mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
                }
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View v, int i, long l) {
                // Log.e(TAG, DummyContent.ITEMS.get(i).content);
                Log.e(TAG, mSourceDataList.get(i).getContactName());

                // 引入窗口配置文件 
                View contextMenuView = inflater.inflate(R.layout.layout_common_context_menu, null);
                // 创建PopupWindow对象 
                final PopupWindow popupWindow = new PopupWindow(contextMenuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
                // 需要设置一下此参数，点击外边可消失
                // new ColorDrawable(0) getResources().getDrawable(R.drawable.popup_window_background)
                popupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
                // 设置点击窗口外边窗口消失 
                // popupWindow.setOutsideTouchable(true);
                // 设置此参数获得焦点，否则无法点击 
                popupWindow.setFocusable(true);
                // 设置动画样式
                // popupWindow.setAnimationStyle(R.style.PopupAnimation);
                // 设置窗口消失事件
                // popupWindow.setOnDismissListenerd(new PopupWindow.OnDismissListener(){});
                /*
                // 点击PopupWindow区域外部,PopupWindow消失
                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                            popupWindow.dismiss();
                            return true;
                        }
                        return false;
                    }
                });
                */
                popupWindow.getContentView().setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
                if (popupWindow.isShowing()) {
                    // 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏 
                    popupWindow.dismiss();
                } else {
                    // SimpleAdapter mSimpleAdapter = new SimpleAdapter(getApplicationContext(), mPopupWindowListItem, R.layout.main_popup_menu_item, new String[]{"item_icon", "item_title"}, new int[]{R.id.popup_menu_item_icon, R.id.popup_menu_item_title});

                    BaseAdapter mSimpleAdapter = new BaseAdapter() {

                        @Override
                        public int getCount() {
                            return mPopupWindowListItem.size();
                        }

                        @Override
                        public Object getItem(int i) {
                            return null;
                        }

                        @Override
                        public long getItemId(int i) {
                            return 0;
                        }

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {

                            if (convertView == null) {
                                LayoutInflater inflater = LayoutInflater.from(getActivity());
                                convertView = inflater.inflate(R.layout.list_item_common_context_menu, parent, false);
                            }
                            // TextView popupMenuItemIcon = (TextView) convertView.findViewById(R.id.popup_menu_item_icon);
                            // if (DEBUG) Log.d(TAG, "item_icon:" + mPopupWindowListItem.get(position).get("item_icon"));
                            // popupMenuItemIcon.setTypeface(typeface);
                            // if (popupMenuItemIcon.getTypeface() != typeface) {
                            //    popupMenuItemIcon.setTypeface(typeface);
                            // }
                            // popupMenuItemIcon.setText(Integer.valueOf(mPopupWindowListItem.get(position).get("item_icon").toString()));

                            TextView popupMenuItemTitle = (TextView) convertView.findViewById(R.id.context_menu_action_text_view);
                            popupMenuItemTitle.setText(mPopupWindowListItem.get(position).get("item_title").toString());

                            return convertView;
                        }
                    };

                    TextView contextMenuTitle = (TextView) contextMenuView.findViewById(R.id.context_menu_title);
                    contextMenuTitle.setText(mSourceDataList.get(i).getContactName());

                    ListView mPopupMenuListView = (ListView) contextMenuView.findViewById(R.id.context_menu_list_view);
                    mPopupMenuListView.setAdapter(mSimpleAdapter);

                    // 显示窗口
                    // 设置显示PopupWindow的位置位于View的右下方，x,y表示坐标偏移量
                    // popupWindow.showAsDropDown(v, -460, 0);
                    // （以某个View为参考）,表示弹出窗口以parent组件为参考，位于左侧，偏移-90。
                    // Gravity.TOP|Gravity.LEFT, 0, 150
                    popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                    popupWindow.update();
                }

                return true;
            }
        });

        mLetterQueryView = (LetterQueryView) view.findViewById(R.id.letter_query_view);
        mLetterTextDialog = (TextView) view.findViewById(R.id.letter_text_dialog);
        mLetterQueryView.setTextView(mLetterTextDialog);

        // 设置右侧触摸监听
        mLetterQueryView.setOnTouchingLetterChangedListener(new LetterQueryView.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = mContactListAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }

            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public void onScrolled(boolean isScrolled) {
        if(isScrolled) {
            mLetterQueryView.setVisibility(View.GONE);
            mLetterTextDialog.setVisibility(View.GONE);
        } else {
            mLetterQueryView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
