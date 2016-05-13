package com.org.lengend.recyclerhead;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by wangyanfei on 2016/5/12.
 */
public class RecyclerZoomHeadView extends FrameLayout {
    private View mHeader;

    private RecyclerView recyclerView;


    private int mPaddingLeft = 0;
    private int mPaddingTop = 0;
    private int mPaddingRight = 0;
    private int mPaddingBottom = 0;


    public RecyclerZoomHeadView(Context context) {
        this(context, null);
    }

    public RecyclerZoomHeadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    private int totalDy = 0;
    private int hight = 0;
    private float textSize = 0;
    public RecyclerZoomHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        recyclerView = new RecyclerView(context);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (mHeader != null) {
                    MarginLayoutParams lp = (MarginLayoutParams) mHeader.getLayoutParams();
                    int headerTop = lp.topMargin;
                    TextView tv = (TextView) mHeader.findViewById(R.id.textView);

                    if(hight == 0){
                        hight = mHeader.getMeasuredHeight();
                        textSize = tv.getTextSize();
                    }
                    totalDy -= dy;
//                    System.out.println("=====totalDy======"+totalDy);

                    int height = totalDy + headerTop + hight;
                    if(height < 150){
                        height = 150;
                    }


                    float tempTextSize = ((float) height) / hight * textSize;
//                    System.out.println("=======textSize========"+textSize);
//                    System.out.println("=======height========"+height);
//                    System.out.println("=======hight========"+hight);
//                    System.out.println("=======tempTextSize========"+tempTextSize);


//                    final ScaleAnimation animation =new ScaleAnimation(0.0f, 1.4f, 0.0f, 1.4f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                    animation.setDuration(20);//设置动画持续时间
//                    animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
//                    mHeader.setAnimation(animation);
//                    TypedValue.applyDimension()
//                    setRawTextSize(TypedValue.applyDimension(
//                            unit, size, r.getDisplayMetrics()));

//                    System.out.println("==========sp==========" + px2sp(getContext(), tempTextSize));
                    tv.setTextSize(px2sp(getContext(), tempTextSize));
                    lp.height = height;
//                    System.out.println("=====height======"+height);
//                    System.out.println("=====B========"+(headerTop + mHeader.getMeasuredHeight()));
                    mHeader.setLayoutParams(lp);

//                    measureHeader(mHeader);
//                    mHeader.layout(mPaddingLeft, headerTop, mHeader.getMeasuredWidth()
//                            + mPaddingLeft, height);
                }

            }
        });



        addView(recyclerView);

        View head = View.inflate(context, R.layout.headview,null);
        swapHeader(head);
        ensureHeaderHasCorrectLayoutParams(mHeader);
        measureHeader(mHeader);
//        recyclerView.setVisibility(View.GONE);



    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param fontScale
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static float px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (pxValue / fontScale + 0.5f);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager){
        recyclerView.setLayoutManager(layoutManager);
    }
    public void setHasFixedSize(boolean fixedSize){
        recyclerView.setHasFixedSize(fixedSize);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        recyclerView.layout(0, 0, recyclerView.getMeasuredWidth(), getHeight());
        System.out.println("===========onLayout============="+ bottom);

        if (mHeader != null) {

//            ensureHeaderHasCorrectLayoutParams(mHeader);

            MarginLayoutParams lp = (MarginLayoutParams) mHeader.getLayoutParams();
            int headerTop = lp.topMargin;
//            System.out.println("=====totalDy========"+totalDy);
//            System.out.println("=====L========"+mPaddingLeft);
//            System.out.println("=====T========"+headerTop);
//            System.out.println("=====R========"+mHeader.getMeasuredWidth());

//            lp.width = 1000;
//            lp.height = 500;
//            System.out.println("=====B========"+(headerTop + mHeader.getMeasuredHeight()));
//            mHeader.setLayoutParams(lp);
            mHeader.layout(mPaddingLeft, headerTop, mHeader.getMeasuredWidth()
                    + mPaddingLeft, headerTop + mHeader.getMeasuredHeight());
        }
    }

//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        // Only draw the list here.
//        // The header should be drawn right after the lists children are drawn.
//        // This is done so that the header is above the list items
//        // but below the list decorators (scroll bars etc).
//        if (recyclerView.getVisibility() == VISIBLE || recyclerView.getAnimation() != null) {
//            drawChild(canvas, recyclerView, 0);
//        }
//    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        System.out.println("======onMeasure======="+mHeader.getMeasuredHeight());
        measureHeader(mHeader);
    }

    private void ensureHeaderHasCorrectLayoutParams(View header) {
        ViewGroup.LayoutParams lp = header.getLayoutParams();
        if (lp == null) {
            lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            header.setLayoutParams(lp);
        } else if (lp.height == LayoutParams.MATCH_PARENT || lp.width == LayoutParams.WRAP_CONTENT) {
            lp.height = LayoutParams.WRAP_CONTENT;
            lp.width = LayoutParams.MATCH_PARENT;
            header.setLayoutParams(lp);
        }
    }

    private void measureHeader(View header) {

        if (header != null) {

            final int width = getMeasuredWidth() /*- mPaddingLeft - mPaddingRight*/;
            final int parentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    width, MeasureSpec.EXACTLY);
            final int parentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
            measureChild(header, parentWidthMeasureSpec,
                    parentHeightMeasureSpec);
        }
    }

    private void swapHeader(View newHeader) {
        if (mHeader != null) {
            removeView(mHeader);
        }
        mHeader = newHeader;
        addView(mHeader);
//        if (mOnHeaderClickListener != null) {
//            mHeader.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnHeaderClickListener.onHeaderClick(
//                            StickyListHeadersListView.this, mHeader,
//                            mHeaderPosition, mHeaderId, true);
//                }
//            });
//        }
        mHeader.setClickable(true);
    }
}
