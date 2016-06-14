package com.org.lengend.pagedview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;


public class PageIndicatorView extends View implements PageIndicator{

	/**
	 * 圆点总数量
	 */
	private int count = 0;
	/**
	 * 当前处于焦点状态圆点的索引
	 */
	private int currentIndex = 0;
	private Paint normalPaint;
	private Paint pressPaint;
	private Paint docBgPaint;
	
	/**
	 * 圆点的直径
	 */
	private float DIAMETER = 7;

	/**
	 * 圆点之间的间隔
	 */
	private float INTERVAL = 7;

	public PageIndicatorView(Context context) {
		this(context,null);
	}

	public PageIndicatorView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public PageIndicatorView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.init();
	}

	/**
	 * 初始化数据
	 */
	private void init() {
		Context c = getContext();
        Resources r;

        if (c == null)
            r = Resources.getSystem();
        else
            r = c.getResources();

        DIAMETER = TypedValue.applyDimension(
        		TypedValue.COMPLEX_UNIT_SP, DIAMETER, r.getDisplayMetrics());
        INTERVAL = TypedValue.applyDimension(
        		TypedValue.COMPLEX_UNIT_SP, INTERVAL, r.getDisplayMetrics());
		normalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		pressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		docBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		//目前画笔的颜色是写死的
		normalPaint.setColor(Color.rgb( 0xbb, 0xbb, 0xbb));
		pressPaint.setColor(Color.rgb( 0x47, 0xfa, 0x65));
		docBgPaint.setColor(Color.argb(0x4d, 0xa9, 0xa9, 0xa9));
		
		normalPaint.setAntiAlias(true);// 设置画笔的锯齿效果
		pressPaint.setAntiAlias(true);// 设置画笔的锯齿效果
		docBgPaint.setAntiAlias(true);// 设置画笔的锯齿效果
	}
	
	/**
	 * 设置当前选中状态索引
	 * @param currentIndex  选中状态索引
	 */
	@Override
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
		invalidate();
	}
	
	public void toNext() {
		if(currentIndex < count - 1){
			currentIndex++;
		}else{
			currentIndex = 0;
		}
		invalidate();
	}
	public void toPre() {
		if(currentIndex > 0){
			currentIndex--;
		}else{
			currentIndex = count - 1;
		}
		invalidate();
	}
	
	/**
	 * 设置页数总数
	 * @param count 总数
	 */
	@Override
	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(count <= 1){
			return;
		}
		drawDocBg(canvas);
		drawDoc(canvas);
	}
	
	/**
	 * 画圆点
	 * @param canvas
	 */
	private void drawDoc(Canvas canvas) {
		RectF rect = new RectF();
		
		rect.bottom = getHeight() - INTERVAL + 2;
		rect.left = (getWidth() - computeWidth()) / 2;
		rect.top = rect.bottom - 2;
		rect.right = rect.left + INTERVAL;
		
		for (int i = 0; i < count; i++) {
			if (i == currentIndex) {// 画当前获取焦点的圆点
				canvas.drawCircle(rect.right - (rect.right - rect.left)/2, rect.bottom - (rect.bottom - rect.top)/2, DIAMETER/2, pressPaint);
			} else {
				canvas.drawCircle(rect.right - (rect.right - rect.left)/2, rect.bottom - (rect.bottom - rect.top)/2, DIAMETER/2, normalPaint);
				canvas.drawRect(rect, normalPaint);
			}
			rect.left = rect.right + INTERVAL;
			rect.right = rect.left + DIAMETER;
		}
	}

	/**
	 * 画圆点背景
	 */
	private void drawDocBg(Canvas canvas) {
//		RectF rect = new RectF();
//		rect.bottom = getHeight() - INTERVAL + 2;
//		rect.top = rect.bottom - INTERVAL - 4;
//		rect.left = (getWidth() - computeWidth()) / 2 - INTERVAL;
//		rect.right = rect.left + computeWidth() + INTERVAL * 2;
//		final float rad = (rect.bottom - rect.top) / 2; // 圆角弧度
//      canvas.drawRoundRect(rect, rad, rad, docBgPaint);//第二个参数是x半径，第三个参数是y半径
	}

	/**
	 * 画.9图片
	 * @param c
	 * @param bmp
	 * @param r1
	 */
//	private void drawNinepath(Canvas c, Bitmap bmp, Rect r1){
//        NinePatch patch = new NinePatch(bmp, bmp.getNinePatchChunk(), null);
//        patch.draw(c, r1);
//    }
	
	/**
	 * 计算所有圆点宽度之和
	 * 
	 * @return
	 */
	private float computeWidth() {
		float width = 0;
		if (count > 0) {
			width = DIAMETER * count;
			if (count > 1) {
				width += (count - 1) * INTERVAL;
			}
		}
		return width;
	}
}
