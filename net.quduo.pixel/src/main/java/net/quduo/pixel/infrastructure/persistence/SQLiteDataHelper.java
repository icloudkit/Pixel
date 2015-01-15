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

package net.quduo.pixel.infrastructure.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.quduo.pixel.BuildConfig;

public class SQLiteDataHelper extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHelper.class.getName();
    private static final boolean DEBUG = BuildConfig.DEBUG;
	
	private final static String DATABASE_NAME = "BOOKS.db";
	private final static int DATABASE_VERSION = 1;

	private final static String TABLE_NAME = "books_table";
    private final static String BOOK_ID = "book_id";
    private final static String BOOK_NAME = "book_name";
    private final static String BOOK_AUTHOR = "book_author";

	public SQLiteDataHelper(Context context) {
		// TODO Auto-generated constructor stub
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建数据库后，对数据库的操作
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BOOK_NAME + " text, " + BOOK_AUTHOR + " text);";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 更改数据库版本的操作
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
		// 每次成功打开数据库后首先被执行
	}

	public Cursor select() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}

	/**
	 * 增加操作
	 * 
	 * @param bookname
	 * @param author
	 * @return
	 */
	public long insert(String bookname, String author) {
		SQLiteDatabase db = this.getWritableDatabase();
		/* ContentValues */
		ContentValues cv = new ContentValues();
		cv.put(BOOK_NAME, bookname);
		cv.put(BOOK_AUTHOR, author);
		long row = db.insert(TABLE_NAME, null, cv);
		return row;
	}

	/**
	 * 删除操作
	 * 
	 * @param id
	 */
	public void delete(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = BOOK_ID + " = ?";
		String[] whereValue = { Integer.toString(id) };
		db.delete(TABLE_NAME, where, whereValue);
	}

	/**
	 * 修改操作
	 * 
	 * @param id
	 * @param bookname
	 * @param author
	 */
	public void update(int id, String bookname, String author) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = BOOK_ID + " = ?";
		String[] whereValue = { Integer.toString(id) };

		ContentValues cv = new ContentValues();
		cv.put(BOOK_NAME, bookname);
		cv.put(BOOK_AUTHOR, author);
		db.update(TABLE_NAME, cv, where, whereValue);
	}
}