package net.quduo.pixel.interfaces.android.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import net.quduo.pixel.R;

import java.util.Random;

public class TouchRelativeLayout extends RelativeLayout {

    /*
    private int[] colors = {getResources().getColor(R.color.red_500),
            getResources().getColor(R.color.yellow_500),
            getResources().getColor(R.color.blue_500),
            getResources().getColor(R.color.purple_500),
            getResources().getColor(R.color.pink_500),
            getResources().getColor(R.color.green_500),
            getResources().getColor(R.color.blue_grey_500)};
    */

    private int touchStatus = 0;
    private Bitmap fingerprintBitmap;

    public TouchRelativeLayout(Context context) {
        super(context);
    }

    public TouchRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        fingerprintBitmap = scaleImage(BitmapFactory.decodeResource(getResources(), R.drawable.background_fingerprint), 127, 122);
    }

    public TouchRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TouchRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // canvas.drawColor(colors[new Random().nextInt(colors.length)]);
        // Paint paint = new Paint();
        // paint.setColor(Color.WHITE);
        // paint.setTextSize(24);

        // Matrix matrix = new Matrix();
        // matrix.postTranslate(this.getWidth() / 2 - home_flight.getWidth() / 2, this.getHeight() / 2 - home_flight.getHeight() / 2);
        // canvas.drawText(text, 10, 40, paint);
        // canvas.drawBitmap(home_flight, matrix, paint);

        if (touchStatus == 1) {
            Matrix matrix = new Matrix();
            matrix.postTranslate(this.getWidth() / 2 - fingerprintBitmap.getWidth() / 2, this.getHeight() / 2 - fingerprintBitmap.getHeight() / 2);
            canvas.drawBitmap(fingerprintBitmap, matrix, new Paint());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float start = 1.0f;
        float end = 0.95f;
        Animation scaleAnimation = new ScaleAnimation(start, end, start, end, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        Animation endAnimation = new ScaleAnimation(end, start, end, start, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(200);
        scaleAnimation.setFillAfter(true);
        endAnimation.setDuration(200);
        endAnimation.setFillAfter(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.startAnimation(scaleAnimation);
                touchStatus = 1;
                break;
            case MotionEvent.ACTION_UP:
                this.startAnimation(endAnimation);
                touchStatus = 0;
                break;
            case MotionEvent.ACTION_CANCEL:
                this.startAnimation(endAnimation);
                touchStatus = 0;
                break;
        }
        // invalidate();
        return true;
        // return super.onTouchEvent(event);
    }

    /***
     * 图片的缩放方法
     *
     * @param bitmapImage 源图片资源
     * @param newWidth 缩放后宽度
     * @param newHeight 缩放后高度
     * @return
     */
    public static Bitmap scaleImage(Bitmap bitmapImage, double newWidth, double newHeight) {
        // 获取图片的宽和高
        float width = bitmapImage.getWidth();
        float height = bitmapImage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bitmapImage, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }
}
