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

package net.quduo.pixel.interfaces.android.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import net.quduo.pixel.BuildConfig;
import net.quduo.pixel.R;
import net.quduo.pixel.interfaces.android.common.ImageLoader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义的ScrollView，在其中动态地对图片进行添加。
 */
public class WaterfallFixedHeightScrollView extends ScrollView implements View.OnTouchListener {

    private static final String TAG = WaterfallFixedHeightScrollView.class.getName();

    private static final boolean DEBUG = BuildConfig.DEBUG;

    public final static String[] imageUrls = new String[]{
            "http://img1.gtimg.com/digi/pics/hv1/11/113/1126/73246976.jpg",
            "http://img1.gtimg.com/digi/pics/hv1/13/113/1126/73246978.jpg",
            "http://img1.gtimg.com/digi/pics/hv1/14/113/1126/73246979.jpg",
            "http://img1.gtimg.com/digi/pics/hv1/15/113/1126/73246980.jpg",
            "http://img1.gtimg.com/digi/pics/hv1/16/113/1126/73246981.jpg",
            "http://img1.gtimg.com/digi/pics/hv1/17/113/1126/73246982.jpg"};

    /**
     * 每页要加载的图片数量
     */
    public static final int PAGE_SIZE = 15;

    /**
     * 记录当前已加载到第几页
     */
    private int pageNumber;

    /**
     * WaterfallFixedHeightScrollView下的布局。
     */
    private static View scrollLayout;

    /**
     * WaterfallFixedHeightScrollView 宽度。
     */
    private static int scrollViewWidth;

    /**
     * WaterfallFixedHeightScrollView 高度。
     */
    private static int scrollViewHeight;

    /**
     * 记录上垂直方向的滚动距离。
     */
    private static int lastScrollY = -1;

    /**
     * 已加载图片位置
     */
    private static int imagesTotalPosition = 0;

    /**
     * 是否已加载过一次layout，这里onLayout中的初始化只需加载一次
     */
    private boolean isLoaded;

    /**
     * 对图片进行管理的工具类
     */
    private ImageLoader imageLoader;

    /**
     * 记录所有界面上的图片，用以可以随时控制对图片的释放。
     */
    private List<ImageView> imageViewList = new ArrayList<ImageView>();

    /**
     * 记录所有正在下载或等待下载的任务。
     */
    private static Set<LoadImageTask> tasks;

    /**
     * 在Handler中进行图片可见性检查的判断，以及加载更多图片的操作。
     */
    private static Handler handler = new Handler() {

        public void handleMessage(Message message) {
            WaterfallFixedHeightScrollView waterfallFixedHeightScrollView = (WaterfallFixedHeightScrollView) message.obj;

            int scrollY = waterfallFixedHeightScrollView.getScrollY();
            // 如果当前的滚动位置和上次相同，表示已停止滚动
            if (scrollY == lastScrollY) {

                if (DEBUG)
                    Log.d(WaterfallFixedHeightScrollView.class.getName(), "height:" + scrollLayout.getHeight());
                // 当滚动的最底部，并且当前没有正在下载的任务时，开始加载下一页的图片
                if (scrollViewHeight + scrollY >= scrollLayout.getHeight() && tasks.isEmpty()) {
                    waterfallFixedHeightScrollView.loadImages();
                }

                waterfallFixedHeightScrollView.reload();
            } else {
                lastScrollY = scrollY;

                message = new Message();
                message.obj = waterfallFixedHeightScrollView;

                // 5毫秒后再次对滚动位置进行判断
                handler.sendMessageDelayed(message, 5);
            }
        }
    };

    public WaterfallFixedHeightScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        tasks = new HashSet<LoadImageTask>();
        imageLoader = ImageLoader.getInstance();

        this.setOnTouchListener(this);
    }

    /**
     * 监听用户的触屏事件，如果用户手指离开屏幕则开始进行滚动检测。
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

            Message message = new Message();
            message.obj = this;
            handler.sendMessageDelayed(message, 5);
        }
        return false;
    }

    /**
     * 进行一些关键性的初始化操作，获取WaterfallFixedHeightScrollView的高度，以及得到第一列的宽度值。并在这里开始加载第一页的图片。
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !isLoaded) {
            scrollViewWidth = this.getWidth();
            scrollViewHeight = this.getHeight();
            scrollLayout = this.getChildAt(0);
            imagesTotalPosition = 0;
            isLoaded = true;
            loadImages();
        }
    }

    /**
     * 遍历imageViewList中的每张图片，对图片的可见性进行检查，如果图片已经离开屏幕可见范围，则将图片替换成一张空图。
     */
    public void reload() {
        for (int i = 0; i < imageViewList.size(); i++) {

            ImageView imageView = imageViewList.get(i);

            int borderTop = (Integer) imageView.getTag(R.string.border_top);
            int borderBottom = (Integer) imageView.getTag(R.string.border_bottom);

            if (borderBottom > this.getScrollY() && borderTop < this.getScrollY() + scrollViewHeight) {

                String imageUrl = (String) imageView.getTag(R.string.image_url);

                // 从缓存中加载图片
                Bitmap bitmap = imageLoader.getBitmapFromMemoryCache(imageUrl);

                // 如果缓存中为空，则新建加载任务
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    LoadImageTask task = new LoadImageTask(imageView);
                    task.execute(imageUrl);
                }
            } else {
                // 设置默认图片
                imageView.setImageResource(R.drawable.decorator_default_image);
            }
        }
    }


    /**
     * 开始加载下一页的图片，每张图片都会开启一个异步线程去下载。
     */
    public void loadImages() {
        int startIndex = pageNumber * PAGE_SIZE;
        int endIndex = startIndex + PAGE_SIZE;
        if (startIndex < imageUrls.length) {
            // Toast.makeText(getContext(), "正在加载...", Toast.LENGTH_SHORT).show();
            if (endIndex > imageUrls.length) {
                endIndex = imageUrls.length;
            }
            for (int i = startIndex; i < endIndex; i++) {
                LoadImageTask task = new LoadImageTask();
                tasks.add(task);
                task.execute(imageUrls[i]);
            }
            pageNumber++;
        } else {
            // Toast.makeText(getContext(), "已没有更多图片", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 异步下载图片的任务。
     */
    class LoadImageTask extends AsyncTask<String, Integer, Bitmap> {

        /**
         * 图片的URL地址
         */
        private String mImageUrl;

        /**
         * 可重复使用的ImageView
         */
        private ImageView mImageView;

        public LoadImageTask() {
        }

        /**
         * 将可重复使用的ImageView传入
         *
         * @param imageView
         */
        public LoadImageTask(ImageView imageView) {
            mImageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            mImageUrl = params[0];
            Bitmap imageBitmap = imageLoader.getBitmapFromMemoryCache(mImageUrl);
            if (imageBitmap == null) {
                imageBitmap = loadImage(mImageUrl);
            }
            return imageBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                // TODO 缩放图片
                double ratio = bitmap.getWidth() / (scrollViewWidth * 1.0);
                int scaledHeight = (int) (bitmap.getHeight() / ratio);
                this.addImage(bitmap, scrollViewWidth, scaledHeight);
            }
            tasks.remove(this);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // 任务启动，可以在这里显示一个对话框，这里简单处理
            // message.setText(R.string.task_started);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            // 更新进度
            // message.setText(values[0]);
        }

        /**
         * 根据传入的URL，对图片进行加载。如果这张图片已经存在于SD卡中，则直接从SD卡里读取，否则就从网络上下载。
         *
         * @param imageUrl 图片的URL地址
         * @return 加载到内存的图片。
         */
        private Bitmap loadImage(String imageUrl) {
            File imageFile = new File(getImagePath(imageUrl));
            if (!imageFile.exists()) {
                downloadImage(imageUrl);
            }

            Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(imageFile.getPath());
            imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
            return bitmap;

        }

        /**
         * 向ImageView中添加一张图片
         *
         * @param bitmap      待添加的图片
         * @param imageWidth  图片的宽度
         * @param imageHeight 图片的高度
         */
        private void addImage(Bitmap bitmap, int imageWidth, int imageHeight) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageWidth, imageHeight);
            if (mImageView != null) {
                mImageView.setImageBitmap(bitmap);
            } else {
                ImageView imageView = new ImageView(getContext());
                imageView.setLayoutParams(params);
                imageView.setImageBitmap(bitmap);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(5, 5, 5, 5);
                imageView.setTag(R.string.image_url, mImageUrl);

                // 添加图片
                LinearLayout linearLayout = (LinearLayout) scrollLayout;
                linearLayout.addView(imageView);
                imageView.setTag(R.string.border_top, imagesTotalPosition);
                imagesTotalPosition += imageHeight;
                imageView.setTag(R.string.border_bottom, imagesTotalPosition);

                imageViewList.add(imageView);
            }
        }

        /**
         * 将图片下载到SD卡缓存起来。
         *
         * @param imageUrl 图片的URL地址。
         */
        private void downloadImage(String imageUrl) {
            HttpURLConnection con = null;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            BufferedInputStream bis = null;
            File imageFile = null;
            try {
                URL url = new URL(imageUrl);
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(5 * 1000);
                con.setReadTimeout(15 * 1000);
                con.setDoInput(true);
                con.setDoOutput(true);
                bis = new BufferedInputStream(con.getInputStream());
                imageFile = new File(getImagePath(imageUrl));
                fos = new FileOutputStream(imageFile);
                bos = new BufferedOutputStream(fos);
                byte[] b = new byte[1024];
                int length;
                while ((length = bis.read(b)) != -1) {
                    bos.write(b, 0, length);
                    bos.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bis != null) {
                        bis.close();
                    }
                    if (bos != null) {
                        bos.close();
                    }
                    if (con != null) {
                        con.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (imageFile != null) {
                // TODO
                Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(imageFile.getPath());
                imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
            }
        }

        /**
         * 获取图片的本地存储路径。
         *
         * @param imageUrl 图片的URL地址。
         * @return 图片的本地存储路径。
         */
        private String getImagePath(String imageUrl) {
            int lastSlashIndex = imageUrl.lastIndexOf("/");
            String imageName = imageUrl.substring(lastSlashIndex + 1);
            String imageDir = Environment.getExternalStorageDirectory().getPath() + "/Pixel/temp/";
            File file = new File(imageDir);
            if (!file.exists()) {
                file.mkdirs();
            }
            String imagePath = imageDir + imageName;
            return imagePath;
        }
    }
}
