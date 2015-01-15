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

package net.quduo.pixel.interfaces.android;

import android.app.Application;
import android.graphics.Bitmap;

import net.quduo.pixel.BuildConfig;

/**
 * MainApplication
 *
 * @author hongquanli <hongquanli@qq.com>
 * @version 1.0 2013年11月20日 上午11:38:34
 */
public class MainApplication extends Application implements Thread.UncaughtExceptionHandler {

    private static final String TAG = MainApplication.class.getName();
    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static int DISK_IMAGECACHE_SIZE = 1024 * 1024 * 10;
    private static Bitmap.CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;
    // PNG is lossless so quality is ignored but must be provided
    private static int DISK_IMAGECACHE_QUALITY = 100;

    private static final String VALUE = "Harvey";

    private String value;

    @Override
    public void onCreate() {
        super.onCreate();
        setValue(VALUE); // 初始化全局变量
    }

//    /**
//     * Intialize the request manager and the image cache
//     */
//    private void init() {
//        RequestManager.init(this);
//        createImageCache();
//    }
//
//    /**
//     * Create the image cache. Uses Memory Cache by default. Change to Disk for a Disk based LRU implementation.
//     */
//    private void createImageCache(){
//        ImageCacheManager.getInstance().init(this,
//                this.getPackageCodePath()
//                , DISK_IMAGECACHE_SIZE
//                , DISK_IMAGECACHE_COMPRESS_FORMAT
//                , DISK_IMAGECACHE_QUALITY
//                , CacheType.MEMORY);
//    }


    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        thread.setDefaultUncaughtExceptionHandler(this);
    }
}
