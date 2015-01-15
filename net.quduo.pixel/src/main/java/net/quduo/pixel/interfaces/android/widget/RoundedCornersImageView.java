package net.quduo.pixel.interfaces.android.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import net.quduo.pixel.R;

/**
 * 圆角图片显示
 *
 * @author hongquanli <hongquanli@qq.com>
 * @version 1.0 2013年11月20日 上午11:38:34
 */
public class RoundedCornersImageView extends MaskedImageView {
	private static final int DEFAULT_CORNER_RADIUS = 3;
	private int cornerRadius = DEFAULT_CORNER_RADIUS;

	public RoundedCornersImageView(Context paramContext) {
		super(paramContext);
		this.cornerRadius = 0;
		StringBuilder localStringBuilder = new StringBuilder().append("Radius size = ");
		int i = this.cornerRadius;

	}

	public RoundedCornersImageView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		int[] arrayOfInt = R.styleable.RoundedCornersImageView;
		int i = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt).getDimensionPixelSize(0, 0);
		this.cornerRadius = i;
		StringBuilder localStringBuilder = new StringBuilder().append("Radius size = ");
		int j = this.cornerRadius;
	
	}

    /*
	public RoundedCornersImage(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		int[] arrayOfInt = R.styleable.RoundedCornersImageView;
		int i = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt).getDimensionPixelSize(0, 0);
		this.cornerRadius = i;
		StringBuilder localStringBuilder = new StringBuilder().append("Radius size = ");

	}
    */

	public Bitmap createMask() {
		int i = getWidth();
		int j = getHeight();
		Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
		Bitmap localBitmap = Bitmap.createBitmap(i, j, localConfig);
		Canvas localCanvas = new Canvas(localBitmap);
		Paint localPaint = new Paint(1);
		localPaint.setColor(-16777216);
		float f1 = getWidth();
		float f2 = getHeight();
		RectF localRectF = new RectF(0.0F, 0.0F, f1, f2);
		float f3 = this.cornerRadius;
		float f4 = this.cornerRadius;
		localCanvas.drawRoundRect(localRectF, f3, f4, localPaint);
		return localBitmap;
	}
}