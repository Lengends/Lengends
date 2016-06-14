package com.org.lengend.pagedview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by wangyanfei on 2016/6/13.
 */
public class ScaleImageView extends ImageView{

    private int mScaleWidth = -1;
    private int mScaleHight = -1;

    public ScaleImageView(Context context) {
        this(context,null);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScaleImageView, defStyleAttr, 0);
        mScaleWidth = a.getInteger(R.styleable.ScaleImageView_scaleWidth, -1);
        mScaleHight = a.getInteger(R.styleable.ScaleImageView_scaleHight, -1);
        a.recycle();

    }

    public void setScale(int wScale,int hScale){
        this.mScaleWidth = wScale;
        this.mScaleHight = hScale;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mScaleWidth == -1 || mScaleHight == -1){
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        }else{
            //根据宽度设置宽高比例
            int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
            if(measureWidth != 0){
                setMeasuredDimension(measureWidth, measureWidth * mScaleHight / mScaleWidth);
            }else{
                super.onMeasure(widthMeasureSpec,heightMeasureSpec);
            }
        }
    }
}
