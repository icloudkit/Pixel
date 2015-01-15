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

package net.quduo.pixel.interfaces.android.common;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import net.quduo.pixel.R;

/**
 * 自定义一个视窗操作器，实现构造函数和onCreateActionView即可
 */
public class MenuActionProvider extends android.support.v4.view.ActionProvider {

    private Context context;
    private LayoutInflater inflater;
    private View view;

    private TextView mSearchActionitem;
    private TextView mPlusActionItem;

    public MenuActionProvider(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.include_action_menu_main, null);
    }

    @Override
    public View onCreateActionView() {

        try {
            mSearchActionitem = (TextView) view.findViewById(R.id.search_action_item);
            mPlusActionItem = (TextView) view.findViewById(R.id.plus_action_item);
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "font/icon/iconfont.ttf");
            mSearchActionitem.setTypeface(typeface);
            mPlusActionItem.setTypeface(typeface);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO Auto-generated method stub
//        button = (ImageView) view.findViewById(R.id.action_bar);
//        button.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Toast.makeText(context, "是我，没错", Toast.LENGTH_SHORT).show();
//            }
//        });
        return view;
    }

}

