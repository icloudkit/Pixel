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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.util.Log;

import net.quduo.pixel.BuildConfig;

/**
 * 对图片进行管理的工具类。
 */
public class ImageLoader {

    private static final String TAG = ImageLoader.class.getName();

    private static final boolean DEBUG = BuildConfig.DEBUG;

    /**
     * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
     */
    private static LruCache<String, Bitmap> mMemoryCache;

    /**
     * ImageLoader的实例。
     */
    private static ImageLoader mImageLoader;

    private ImageLoader() {

        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();

        // 设置图片缓存大小为程序最大可用内存的1/8
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {

            @Override
            protected int sizeOf(String key, Bitmap bitmap) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                    return bitmap.getByteCount();
                } else {
                    return getByteCount(bitmap);
                }
            }
        };
    }

    /**
     * 获取ImageLoader的实例。
     *
     * @return ImageLoader的实例。
     */
    public static ImageLoader getInstance() {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader();
        }
        return mImageLoader;
    }

    /**
     * 将一张图片存储到LruCache中。
     *
     * @param key    LruCache的键，这里传入图片的URL地址。
     * @param bitmap LruCache的键，这里传入从网络上下载的Bitmap对象。
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        // TODO bitmap is null NullPointerException: key == null || value == null
        if (getBitmapFromMemoryCache(key) == null && bitmap != null) {
            // Log.e(TAG, "KEY:" + key + "VALUE:" + bitmap);
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 从LruCache中获取一张图片，如果不存在就返回null。
     *
     * @param key LruCache的键，这里传入图片的URL地址。
     * @return 对应传入键的Bitmap对象，或者null。
     */
    public Bitmap getBitmapFromMemoryCache(String key) {

        return mMemoryCache.get(key);
    }

    public static Bitmap decodeSampledBitmapFromResource(String pathName) {

        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // options.inJustDecodeBounds = true;
        // BitmapFactory.decodeFile(pathName, options);

        // 调用上面定义的方法计算inSampleSize值
        // options.inSampleSize = calculateInSampleSize(options, targetWidth);

        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }

    /**
     * 相当于Bitmap.getByteCount()方法，获取Bitmap存储用的最小字节数量
     *
     * @param bitmap 用于计算的Bitmap
     * @return Bitmap存储用的最小字节数量
     */
    public static int getByteCount(Bitmap bitmap) {

        Bitmap.Config config = bitmap.getConfig();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pixelByte = 4;

        // 大多数图片默认Config.ARGB_8888
        if (config == Bitmap.Config.ARGB_8888) {
            pixelByte = 4;
        } else if (config == Bitmap.Config.ALPHA_8) {
            pixelByte = 1;
        } else if (config == Bitmap.Config.ARGB_4444) {
            pixelByte = 2;
        } else if (config == Bitmap.Config.RGB_565) {
            pixelByte = 2;
        }
        return width * height * pixelByte;
    }

    /*
    public static int calculateInSampleSize(BitmapFactory.Options options, int targetWidth) {

        // 源图片的宽度
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (width > targetWidth) {

            // 计算出实际宽度和目标宽度的比率
            final int widthRatio = Math.round((float) width / (float) targetWidth);
            inSampleSize = widthRatio;
        }
        return inSampleSize;
    }

    private int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math. round((float)height / ( float)reqHeight);
            } else {
                inSampleSize = Math. round((float)width / ( float)reqWidth);
            }
        }

        return inSampleSize;
    }
    */

}
