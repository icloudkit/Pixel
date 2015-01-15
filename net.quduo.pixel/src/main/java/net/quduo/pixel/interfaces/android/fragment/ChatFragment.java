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

import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
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
import net.quduo.pixel.interfaces.android.common.ChatListAdapter;
import net.quduo.pixel.interfaces.android.model.ChatListDataModel;
import net.quduo.pixel.interfaces.android.widget.DragDropListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * ChatFragment
 *
 * @author hongquanli <hongquanli@qq.com>
 * @version 1.0 2015-01-13 17:6:02
 */
public class ChatFragment extends Fragment {

    private static final String TAG = ChatFragment.class.getName();

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private LayoutInflater inflater;

    private ListView mChatListView;
    private ChatListAdapter mChatListAdapter;
    List<ChatListDataModel> mSourceDataList;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        mChatListView = (ListView) v.findViewById(R.id.chat_list_view);

        // TODO: 会话列表
        mSourceDataList = new ArrayList<ChatListDataModel>();
        ChatListDataModel sourceData = new ChatListDataModel();
        sourceData.setChatAvatar(R.drawable.default_contact_action_01);
        sourceData.setChatTitle("订阅号");
        sourceData.setChatSummary("溜溜梅:今日小寒, 是时候给大头娘娘...");
        mSourceDataList.add(sourceData);

        ChatListDataModel sourceData1 = new ChatListDataModel();
        sourceData1.setChatAvatar(R.drawable.default_contact_action_04);
        sourceData1.setChatTitle("通知");
        sourceData1.setChatSummary("备份通讯录，保障你的通讯录安全");
        mSourceDataList.add(sourceData1);

        ChatListDataModel sourceData2 = new ChatListDataModel();
        sourceData2.setChatAvatar(R.drawable.default_contact_action_03);
        sourceData2.setChatTitle("微信团队");
        sourceData2.setChatSummary("欢迎你再次回到微信。如果你在使用...");
        mSourceDataList.add(sourceData2);

        ChatListDataModel sourceData3 = new ChatListDataModel();
        sourceData3.setChatAvatar(R.drawable.default_contact_action_02);
        sourceData3.setChatTitle("邮箱");
        sourceData3.setChatSummary("您有新的邮件，请注意查收");
        mSourceDataList.add(sourceData3);

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

        mChatListAdapter = new ChatListAdapter(this.getActivity(), mSourceDataList);
        mChatListView.setAdapter(mChatListAdapter);


        mChatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, mSourceDataList.get(i).getChatTitle());
            }
        });
        mChatListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View v, int i, long l) {
                Log.e(TAG, mSourceDataList.get(i).getChatTitle());

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
                    contextMenuTitle.setText(mSourceDataList.get(i).getChatTitle());

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
        // mChatListView.setOnItemSelectedListener();

        // #ContextMenu
        // this.unregisterForContextMenu(mChatListView);
        // mChatListView.setOnCreateContextMenuListener(null);
        // this.registerForContextMenu(mChatListView);
        return v;
    }

    /*
    // #ContextMenu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        // set context menu title
        // menu.setHeaderTitle("订阅号");
        menu.setHeaderView(inflater.inflate(R.layout.list_item_contact, null));

        // add context menu item
        // MenuInflater inflater = getActivity().getMenuInflater();
        // inflater.inflate(R.menu.menu_global, menu);

        // MENU_MSG_EDIT MENU_MSG_COPY MENU_MSG_DELETE MENU_MSG_MOVE
        menu.add(0, 1, Menu.NONE, "发送");
        menu.add(0, 2, Menu.NONE, "标记为重要");
        menu.add(0, 3, Menu.NONE, "重命名");
        menu.add(0, 4, Menu.NONE, "删除");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // 得到当前被选中的item信息
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (DEBUG) Log.d(TAG, "Context item seleted ID:" + menuInfo.id);

        switch (item.getItemId()) {
            case 1:
                // do something
                break;
            case 2:
                // do something
                break;
            case 3:
                // do something
                break;
            case 4:
                // do something
                int pos = (int) mChatListAdapter.getItemId(menuInfo.position);
                if (mSourceDataList.remove(pos) != null) {
                    // System.out.println("success");
                } else {
                    // System.out.println("failed");
                }
                mChatListAdapter.notifyDataSetChanged();
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }
    */


    /*
    // set drop down listener
    mChatListView.setOnDropDownListener(new DragDropListView.OnDropDownListener() {

        @Override
        public void onDropDown() {
            new GetDataTask(true).execute();
        }
    });
    // set on bottom listener
    mChatListView.setOnBottomListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            new GetDataTask(false).execute();
        }
    });

    private class GetDataTask extends AsyncTask<Void, Void, List<ChatListDataModel>> {

        private boolean isDropDown;

        public GetDataTask(boolean isDropDown) {
            this.isDropDown = isDropDown;
        }

        @Override
        protected List<ChatListDataModel> doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ;
            }
            return mSourceDataList;
        }

        @Override
        protected void onPostExecute(List<ChatListDataModel> result) {

            if (isDropDown) {
                // listItems.addFirst("Added after drop down");
                mChatListAdapter.notifyDataSetChanged();

                // should call onDropDownComplete function of DropDownListView at end of drop down complete.
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
                mChatListView.onDropDownComplete(dateFormat.format(new Date()));
            } else {
                // moreDataCount++;
                // listItems.add("Added after on bottom");
                ChatListDataModel sourceData = new ChatListDataModel();
                sourceData.setChatAvatar(R.drawable.default_contact_action_03);
                sourceData.setChatTitle("微信团队0");
                sourceData.setChatSummary("欢迎你再次回到微信。如果你在使用...");
                mSourceDataList.add(sourceData);

                mChatListAdapter.notifyDataSetChanged();

                // if (moreDataCount >= MORE_DATA_MAX_COUNT) {
                //    mChatListView.setHasMore(false);
                // }
                mChatListView.setHasMore(false);

                // should call onBottomComplete function of DragDropListView at end of on bottom complete.
                mChatListView.onBottomComplete();
            }

            super.onPostExecute(result);
        }
    }
    */

}
