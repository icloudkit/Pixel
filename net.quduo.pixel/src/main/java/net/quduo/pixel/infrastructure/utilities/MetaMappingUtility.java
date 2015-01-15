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

package net.quduo.pixel.infrastructure.utilities;


/**
 * MetaMappingUtility
 *
 * @author hongquanli <hongquanli@qq.com>
 * @version 1.0 2015-01-13 17:6:02
 */
public class MetaMappingUtility {

    /**
     * 跟据表名转换实体名. Translates table name into the class name of relative entity. For example: Input the parameter 'COMPANY_NAME', this method will return the result 'companyName'.
     *
     * @param tableName the table name in database
     * @return the class name of BaseEntity
     */
    public static String getEntityName(String tableName) {

        StringBuffer buff = new StringBuffer(tableName.toLowerCase());
        // the first character of class name is upper case
        buff.replace(0, 1, String.valueOf(Character.toUpperCase(tableName.charAt(0))));
        // delete character '_', and convert the next character to uppercase
        for (int i = 1, length = buff.length(); i < length; ) {
            char lastCh = buff.charAt(i - 1);// the last character
            char ch = buff.charAt(i); // the current character
            // if this character is a letter, and the last character is '_'
            if (lastCh == '_') {
                buff.replace(i - 1, i, String.valueOf(Character.toUpperCase(ch)));
                buff.deleteCharAt(i);
                length--;
            } else {
                i++;
            }
        }
        return buff.toString();
    }

    /**
     * 根据列名转换实体属性名
     *
     * Translates column name into the field name in relative entity class.
     * For example: Input the parameter 'COMPANY_NAME', this method will return the result 'companyName'.
     *
     * @param columnName the column name in database
     * @return the field name of BaseEntity Class
     */
    public static String getFieldName(String columnName) {
        StringBuffer buff = new StringBuffer(columnName.toLowerCase());
        // delete character '_', and convert the next character to uppercase
        for (int i = 1, length = buff.length(); i < length; ) {
            char lastCh = buff.charAt(i - 1);// the last character
            char ch = buff.charAt(i); // the current character
            // if this character is a letter, and the last character is '_'
            if (lastCh == '_') {
                buff.replace(i - 1, i, String.valueOf(Character.toUpperCase(ch)));
                buff.deleteCharAt(i);
                length--;
            } else {
                i++;
            }
        }

        return buff.toString();
    }

    /**
     * 根据实体名转换表名
     *
     * Parses text from the given string to produce a table name that dababase understands.
     * For example: Input the parameter 'com.walker.entity.CompanyInfo', this method will return the result 'COMPANY_INFO'.
     *
     * @param entityClassName the String object, the name of Entity Class
     * @return the relative table name in dababase.
     */
    public static String getTableName(String entityClassName) {
        int index = entityClassName.lastIndexOf('.');
        if (index >= 0) {
            entityClassName = entityClassName.substring(index + 1);
        }
        StringBuffer buff = new StringBuffer(entityClassName);
        for (int i = 1, length = buff.length(); i < length; i++) {
            // insert character '_' before uppercase
            if (Character.isUpperCase(buff.charAt(i))) {
                buff.insert(i++, '_');
                length++;
            }
        }

        return buff.toString().toUpperCase();
    }

    /**
     * 根据属性名转换列名
     *
     * Parses text from the given string to produce a column name that dababase understands.
     * For example: Input the parameter 'companyName', this method will return the result 'COMPANY_NAME'.
     *
     * @param fieldName the String object, the field name in entity class.
     * @return the column name that the dababase understands.
     */
    public static String getColumnName(String fieldName) {
        StringBuffer buff = new StringBuffer(fieldName);
        for (int i = 1, length = buff.length(); i < length; i++) {
            // insert character '_' before uppercase or digit
            if (Character.isUpperCase(buff.charAt(i))) {
                buff.insert(i++, '_');
                length++;
            }
        }
        return buff.toString().toUpperCase();
    }
}
