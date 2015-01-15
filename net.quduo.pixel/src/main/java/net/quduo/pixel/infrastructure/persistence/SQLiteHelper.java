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

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.quduo.pixel.BuildConfig;
import net.quduo.pixel.domain.model.User;
import net.quduo.pixel.infrastructure.utilities.MetaMappingUtility;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLiteHelper
 *
 * @author hongquanli <hongquanli@qq.com>
 * @version 1.0 2015-01-13 17:6:02
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHelper.class.getName();
    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static final String DATABASE_NAME = "test.db";
    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(Context context) {
        // CursorFactory set to null, use the default values
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * The database is created for the first time the onCreate will be invoked
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String table = createTable(User.class);
        db.execSQL(table);
    }

    /**
     * Create table by class.
     *
     * @param clazz the model class.
     * @return SQL string.
     */
    private String createTable(Class<?> clazz) {
        StringBuffer tableBuffer = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
        // Table name.
        Table table = clazz.getAnnotation(Table.class);
        tableBuffer.append(table.name());
        tableBuffer.append("(");
        // Other field.
        Field[] fieldList = clazz.getDeclaredFields();
        for (int i = 0; i < fieldList.length; i++) {
            Field fld = fieldList[i];

            Column element = fld.getAnnotation(Column.class);
            tableBuffer.append(element.name());
            tableBuffer.append(" ");
            if (fld.getType() == int.class) {
                tableBuffer.append("INTEGER");
            } else if (fld.getType() == String.class) {
                tableBuffer.append("TEXT");
            } else if (fld.getType() == Double.class || fld.getType() == Float.class) {
                tableBuffer.append("REAL");
            }
            tableBuffer.append(", ");
        }
        tableBuffer.delete(tableBuffer.length() - 2, tableBuffer.length());
        tableBuffer.append(")");

        return tableBuffer.toString();
    }

    /**
     * If the DATABASE_VERSION value is changed to 2, the system found an existing database version, namely onUpgrade will call
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // db.execSQL("ALTER TABLE person ADD COLUMN other STRING");
    }

    /**
     * Add a new Object.
     *
     * @param obj
     * @param db
     */
    public void add(Object obj, SQLiteDatabase db) {
        try {
            Table table = obj.getClass().getAnnotation(Table.class);

            StringBuffer sqlBuffer = new StringBuffer();
            sqlBuffer.append("INSERT INTO ");
            sqlBuffer.append(table.name());
            sqlBuffer.append("(");
            Field[] fieldList = obj.getClass().getDeclaredFields();
            StringBuffer vals = new StringBuffer(" VALUES(");
            Object[] objs = new Object[fieldList.length];
            for (int i = 0; i < fieldList.length; i++) {
                Field fld = fieldList[i];
                Column element = fld.getAnnotation(Column.class);

                sqlBuffer.append(element.name());
                sqlBuffer.append(", ");
                vals.append("?, ");
                fld.setAccessible(true);
                objs[i] = fld.get(obj);
            }
            sqlBuffer.delete(sqlBuffer.length() - 2, sqlBuffer.length());
            sqlBuffer.append(")");
            vals.delete(vals.length() - 2, vals.length());
            vals.append(")");
            sqlBuffer.append(vals);

            db.beginTransaction();
            db.execSQL(sqlBuffer.toString(), objs);
            db.setTransactionSuccessful();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (db.inTransaction()) {
                db.endTransaction();
            }
        }
    }

    /**
     * Delete object by ID.
     *
     * @param obj
     * @param db
     */
    public void delete(Object obj, SQLiteDatabase db) {
        try {
            Field primaryKey = obj.getClass().getDeclaredField("id");
            primaryKey.setAccessible(true);
            String id = (String) primaryKey.get(obj);
            Table table = obj.getClass().getAnnotation(Table.class);
            String sql = "DELETE FROM " + table.name() + " WHERE id='" + id + "'";
            db.execSQL(sql);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update a Object by ID.
     *
     * @param obj
     * @param db
     */
    public void update(Object obj, SQLiteDatabase db) {
        try {
            Field primaryKey = obj.getClass().getDeclaredField("id");
            primaryKey.setAccessible(true);
            String id = (String) primaryKey.get(obj);
            Table table = obj.getClass().getAnnotation(Table.class);

            StringBuffer sqlBuffer = new StringBuffer();
            sqlBuffer.append("UPDATE ");
            sqlBuffer.append(table.name());
            sqlBuffer.append(" SET ");
            Field[] fieldList = obj.getClass().getDeclaredFields();
            for (int i = 0; i < fieldList.length; i++) {
                Field fld = fieldList[i];
                Column element = fld.getAnnotation(Column.class);

                fld.setAccessible(true);
                if (fld.get(obj) != null) {
                    sqlBuffer.append(element.name());
                    sqlBuffer.append("='");
                    sqlBuffer.append(fld.get(obj));
                    sqlBuffer.append("',");
                }
            }
            sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
            sqlBuffer.append(" WHERE id='");
            sqlBuffer.append(id);
            sqlBuffer.append("'");
            db.execSQL(sqlBuffer.toString());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * Get Object by ID.
     *
     * @param clazz
     * @param db
     * @param id
     * @return
     */
    public Object get(Class<?> clazz, SQLiteDatabase db, String id) {
        Table table = clazz.getAnnotation(Table.class);
        Cursor c = db.rawQuery("SELECT * FROM " + table.name() + " WHERE id=?", new String[]{id});
        try {
            if (c != null && c.moveToFirst()) {
                return cursorToObject(clazz, c);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return null;
    }

    /**
     * Get all object for the class.
     *
     * @param clazz class type.
     * @param db    SQLiteDatabase.
     * @return all object.
     */
    public List<Object> getAll(Class<?> clazz, SQLiteDatabase db) {

        Table table = clazz.getAnnotation(Table.class);
        List<Object> list = new ArrayList<Object>();
        Cursor c = db.rawQuery("SELECT * FROM " + table.name(), null);
        try {
            while (c != null && c.moveToNext()) {
                list.add(cursorToObject(clazz, c));
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return list;
    }

    /**
     * Convert cursor to Object.
     *
     * @param clazz
     * @param cursor
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    private Object cursorToObject(Class<?> clazz, Cursor cursor) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        String[] columnNames = cursor.getColumnNames();
        Object obj = clazz.newInstance();
        for (int i = 0; i < columnNames.length; i++) {

            String columnName = columnNames[i];
            int columnIndex = cursor.getColumnIndex(columnName);
            Field field = clazz.getDeclaredField(MetaMappingUtility.getFieldName(columnName));
            field.setAccessible(true);

            Class<?> fieldType = field.getType();
            Object val = null;
            if (fieldType == String.class) {
                val = cursor.getString(columnIndex);
            } else if (fieldType == Double.class) {
                val = cursor.getDouble(columnIndex);
            } else if (fieldType == Float.class) {
                val = cursor.getFloat(columnIndex);
            } else if (fieldType == Integer.class) {
                val = cursor.getInt(columnIndex);
            }

            /*
            int type = cursor.getType(columnIndex);
            switch (type) {
                case Cursor.FIELD_TYPE_BLOB:
                    break;
                case Cursor.FIELD_TYPE_FLOAT:
                    val = cursor.getDouble(columnIndex);
                    break;
                case Cursor.FIELD_TYPE_INTEGER:
                    val = cursor.getInt(columnIndex);
                    break;
                case Cursor.FIELD_TYPE_NULL:
                    break;
                case Cursor.FIELD_TYPE_STRING:
                    val = cursor.getString(columnIndex);
                    break;
                default:
                    break;
            }
            */
            field.set(obj, val);
        }
        return obj;
    }
}
