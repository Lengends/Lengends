package com.org.lengend.pagedview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.util.ArrayList;


/**
 * An abstraction of the original Workspace which supports browsing through a
 * sequential list of "pages"
 */
public class PagedView extends ViewGroup implements ViewGroup.OnHierarchyChangeListener {
    private static final String TAG = "PagedView";
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_LAYOUT = false;
    private static final boolean DEBUG_DRAW = false;
    private static final boolean DEBUG_MOTION = false;
    protected static final int INVALID_PAGE = -1;

    // the min drag distance for a fling to register, to prevent random page
    // shifts
    private static final int MIN_LENGTH_FOR_FLING = 25;

    protected static final int PAGE_SNAP_ANIMATION_DURATION = 550;
    protected static final float NANOTIME_DIV = 1000000000.0f;

    private static final float OVERSCROLL_ACCELERATE_FACTOR = 2;
    private static final float OVERSCROLL_DAMP_FACTOR = 0.14f;

    private static final float RETURN_TO_ORIGINAL_PAGE_THRESHOLD = 0.33f;
    // The page is moved more than halfway, automatically move to the next page
    // on touch up.
    private static final float SIGNIFICANT_MOVE_THRESHOLD = 0.4f;

    // The following constants need to be scaled based on density. The scaled
    // versions will be
    // assigned to the corresponding member variables below.
    private static final int FLING_THRESHOLD_VELOCITY = 500;
    private static final int MIN_SNAP_VELOCITY = 1500;
    private static final int MIN_FLING_VELOCITY = 250;

    static final int AUTOMATIC_PAGE_SPACING = -1;

    protected int mFlingThresholdVelocity;
    protected int mMinFlingVelocity;
    protected int mMinSnapVelocity;

    protected float mDensity;
    protected float mSmoothingTime;
    protected float mTouchX;

    protected boolean mFirstLayout = true;

    protected int mCurrentPage = 1;
    protected int currentIndex = 0;
    protected int mNextPage = INVALID_PAGE;
    protected int mMaxScrollX;
    protected Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    private float mDownMotionX;
    protected float mLastMotionX;
    protected float mLastMotionXRemainder;
    protected float mLastMotionY;
    protected float mTotalMotionX;
    //	private int mLastScreenCenter = -1;
    private int[] mChildOffsets;
    private int[] mChildRelativeOffsets;


    // 滑动结束状态
    protected final static int TOUCH_STATE_REST = 0;
    // 正在滑动
    protected final static int TOUCH_STATE_SCROLLING = 1;
    // 滑动到上一页
    protected final static int TOUCH_STATE_PREV_PAGE = 2;
    // 滑动到下一页
    protected final static int TOUCH_STATE_NEXT_PAGE = 3;

    protected int mTouchState = TOUCH_STATE_REST;
    protected boolean mForceScreenScrolled = false;

    protected OnLongClickListener mLongClickListener;

    protected boolean mAllowLongPress = true;

    protected int mTouchSlop;
    private int mPagingTouchSlop;
    private int mMaximumVelocity;
    protected int mPageSpacing;
    protected int mPageLayoutPaddingTop;
    protected int mPageLayoutPaddingBottom;
    protected int mPageLayoutPaddingLeft;
    protected int mPageLayoutPaddingRight;
    protected boolean mCenterPagesVertically;
    protected boolean mAllowOverScroll = true;
    protected int mUnboundedScrollX;

    // mOverScrollX is equal to getScrollX() when we're within the normal scroll
    // range. Otherwise
    // it is equal to the scaled overscroll position. We use a separate value so
    // as to prevent
    // the screens from continuing to translate beyond the normal bounds.
    protected int mOverScrollX;

    protected static final int INVALID_POINTER = -1;

    protected int mActivePointerId = INVALID_POINTER;

//	private PageSwitchListener mPageSwitchListener;

    protected ArrayList<Boolean> mDirtyPageContent;

    // If true, syncPages and syncPageItems will be called to refresh pages
    protected boolean mContentIsRefreshable = true;

    // If true, modify alpha of neighboring pages as user scrolls left/right
    protected boolean mFadeInAdjacentScreens = true;

    // It true, use a different slop parameter (pagingTouchSlop = 2 * touchSlop)
    // for deciding
    // to switch to a new page
    // Modified by yongan.qiu on 2013-2-19 for scroll slot begin.
    // protected boolean mUsePagingTouchSlop = true;
    protected boolean mUsePagingTouchSlop = false;
    // Modified by yongan.qiu end.

    // If true, the subclass should directly update scrollX itself in its
    // computeScroll method
    // (SmoothPagedView does this)
    protected boolean mDeferScrollUpdate = false;

    protected boolean mIsPageMoving = false;

    // All syncs and layout passes are deferred until data is ready.
    protected boolean mIsDataReady = false;

    // If set, will defer loading associated pages until the scrolling settles
    private boolean mDeferLoadAssociatedPagesUntilScrollCompletes;


    private int mScaleWidth = -1;
    private int mScaleHight = -1;


    private ArrayList<RecyclerView.ViewHolder> mChangedScrap = new ArrayList<>();
    private PagedViewAdapter adapter;

    public PagedView(Context context) {
        this(context, null);
    }

    public PagedView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagedView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.PagedView, defStyle, 0);
        setPageSpacing(a.getDimensionPixelSize(
                R.styleable.PagedView_pageSpacing, 0));
        mPageLayoutPaddingTop = a.getDimensionPixelSize(
                R.styleable.PagedView_pageLayoutPaddingTop, 0);
        mPageLayoutPaddingBottom = a.getDimensionPixelSize(
                R.styleable.PagedView_pageLayoutPaddingBottom, 0);
        mPageLayoutPaddingLeft = a.getDimensionPixelSize(
                R.styleable.PagedView_pageLayoutPaddingLeft, 0);
        mPageLayoutPaddingRight = a.getDimensionPixelSize(
                R.styleable.PagedView_pageLayoutPaddingRight, 0);

        mScaleWidth = a.getInteger(R.styleable.PagedView_pageScaleWidth, -1);
        mScaleHight = a.getInteger(R.styleable.PagedView_pageScaleHight, -1);

        a.recycle();

        setHapticFeedbackEnabled(false);
        init();
        mContentIsRefreshable = false;
        setDataIsReady();
        mFadeInAdjacentScreens = false;
    }

    /**
     * Initializes various states for this workspace.
     */
    protected void init() {
        mDirtyPageContent = new ArrayList<Boolean>();
        mDirtyPageContent.ensureCapacity(32);
        mScroller = new Scroller(getContext(), new ScrollInterpolator());
        mCenterPagesVertically = true;

        final ViewConfiguration configuration = ViewConfiguration
                .get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mPagingTouchSlop = configuration.getScaledPagingTouchSlop();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mDensity = getResources().getDisplayMetrics().density;

        mFlingThresholdVelocity = (int) (FLING_THRESHOLD_VELOCITY * mDensity);
        mMinFlingVelocity = (int) (MIN_FLING_VELOCITY * mDensity);
        mMinSnapVelocity = (int) (MIN_SNAP_VELOCITY * mDensity);
        setOnHierarchyChangeListener(this);
    }

    /**
     * Called by subclasses to mark that data is ready, and that we can begin
     * loading and laying out pages.
     */
    protected void setDataIsReady() {
        mIsDataReady = true;
    }

    protected boolean isDataReady() {
        return mIsDataReady;
    }

    /**
     * Returns the index of the currently displayed page.
     *
     * @return The index of the currently displayed page.
     */
    private int getCurrentPage() {
        return mCurrentPage;
    }


    private int getPageCount() {
        return getChildCount();
    }

    View getPageAt(int index) {
        return getChildAt(index);
    }

    protected int indexToPage(int index) {
        return index;
    }

    /**
     * Updates the scroll of the current page immediately to its final scroll
     * position. We use this in CustomizePagedView to allow tabs to share the
     * same PagedView while resetting the scroll of the previous tab page.
     */
    protected void updateCurrentPageScroll() {
        int offset = getChildOffset(mCurrentPage);
        int relOffset = getRelativeChildOffset(mCurrentPage);
        int newX = offset - relOffset;
        scrollTo(newX, 0);
        mScroller.setFinalX(newX);
        mScroller.forceFinished(true);
    }

    /**
     * Sets the current page.
     */
    private void setCurrentPage(int currentPage) {
        if (DEBUG) {
            Log.d(TAG, "setCurrentPage: currentPage = " + currentPage
                    + ", mCurrentPage = " + mCurrentPage + ", mScrollX = "
                    + getScrollX() + ", this = " + this);
        }

        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
        // don't introduce any checks like mCurrentPage == currentPage here-- if
        // we change the
        // the default
        if (getChildCount() == 0) {
            return;
        }

        mCurrentPage = Math.max(0, Math.min(currentPage, getPageCount() - 1));
        updateCurrentPageScroll();
        updateScrollingIndicator();
        notifyDataSetChanged();
        invalidate();
    }

    /**
     * 通过当前的索引获取图片的索引
     *
     * @param index 当前的索引
     * @return 图片的索引
     */
    private int getViewPosition(int index) {
        int position;
        final int count = adapter.getCount();
        if (count == 0) {
            return 0;
        }
        if (index < 0) {
            position = (count - Math.abs(index) % count) % count;
        } else {
            position = index % count;
        }
        return position;
    }

    protected void notifyDataSetChanged() {
        if (adapter != null) {
            if (mCurrentPage == 0) {
                currentIndex--;
            } else if (mCurrentPage == 2) {
                currentIndex++;
            }

            if (getCurrentPage() != 1) {
                setCurrentPage(1);
            } else {
                if (mPageIndicator != null) {
                    int count = adapter.getCount();
                    mPageIndicator.setCount(count);
                    int index = getViewPosition(currentIndex);
                    mPageIndicator.setCurrentIndex(index == 0 ? 0 : index % count);
                }
                for (int i = 0; i < mChangedScrap.size(); i++) {
                    RecyclerView.ViewHolder holder = mChangedScrap.get(i);
                    int index = getViewPosition(currentIndex + i - 1);
                    if(adapter.getCount() > index){
                        adapter.onBindViewHolder(holder, index);
                    }

                }
            }
        }
    }

    protected void pageBeginMoving() {
        if (!mIsPageMoving) {
            mIsPageMoving = true;
            onPageBeginMoving();
        }
    }

    protected void pageEndMoving() {
        if (mIsPageMoving) {
            mIsPageMoving = false;
            onPageEndMoving();
        }
    }


    // a method that subclasses can override to add behavior
    protected void onPageBeginMoving() {
    }

    // a method that subclasses can override to add behavior
    protected void onPageEndMoving() {
    }

    /**
     * Registers the specified listener on each page contained in this
     * workspace.
     *
     * @param l The listener used to respond to long clicks.
     */
    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        mLongClickListener = l;
        final int count = getPageCount();
        for (int i = 0; i < count; i++) {
            getPageAt(i).setOnLongClickListener(l);
        }
    }

    @Override
    public void scrollBy(int x, int y) {
        scrollTo(mUnboundedScrollX + x, getScrollY() + y);
    }

    @Override
    public void scrollTo(int x, int y) {
        mUnboundedScrollX = x;

        if (x < 0) {
            super.scrollTo(0, y);
            if (mAllowOverScroll) {
                overScroll(x);
            }
        } else if (x > mMaxScrollX) {
            super.scrollTo(mMaxScrollX, y);
            if (mAllowOverScroll) {
                overScroll(x - mMaxScrollX);
            }
        } else {
            mOverScrollX = x;
            super.scrollTo(x, y);
        }

        mTouchX = x;
        mSmoothingTime = System.nanoTime() / NANOTIME_DIV;
    }

    // we moved this functionality to a helper function so SmoothPagedView can
    // reuse it
    protected boolean computeScrollHelper() {
        if (mScroller.computeScrollOffset()) {
            // Don't bother scrolling if the page does not need to be moved
            if (getScrollX() != mScroller.getCurrX()
                    || getScrollY() != mScroller.getCurrY()
                    || mOverScrollX != mScroller.getCurrX()) {
                scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            }
            postInvalidate();
            return true;
        } else if (mNextPage != INVALID_PAGE) {
            mCurrentPage = Math.max(0, Math.min(mNextPage, getPageCount() - 1));
            mNextPage = INVALID_PAGE;
            notifyDataSetChanged();

            // Load the associated pages if necessary
            if (mDeferLoadAssociatedPagesUntilScrollCompletes) {
                loadAssociatedPages(mCurrentPage);
                mDeferLoadAssociatedPagesUntilScrollCompletes = false;
            }

            // We don't want to trigger a page end moving unless the page has
            // settled
            // and the user has stopped scrolling
            if (mTouchState == TOUCH_STATE_REST) {
                pageEndMoving();
            }
            return true;
        }
        return false;
    }



    @Override
    public void computeScroll() {
        computeScrollHelper();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!mIsDataReady) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(mScaleHight != -1 && mScaleWidth != -1){
            heightSize = widthSize * mScaleHight / mScaleWidth;
        }





        if (widthMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException(
                    "Workspace can only be used in EXACTLY mode.");
        }
        // Return early if we aren't given a proper dimension
        if (widthSize <= 0 || heightSize <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

		/*
         * Allow the height to be set as WRAP_CONTENT. This allows the
		 * particular case of the All apps view on XLarge displays to not take
		 * up more space then it needs. Width is still not allowed to be set as
		 * WRAP_CONTENT since many parts of the code expect each page to have
		 * the same width.
		 */
        int maxChildHeight = 0;

        final int verticalPadding = getPaddingTop() + getPaddingBottom();
        final int horizontalPadding = getPaddingLeft() + getPaddingRight();

        // The children are given the same width and height as the workspace
        // unless they were set to WRAP_CONTENT
        if (DEBUG) {
            Log.d(TAG, "PagedView.onMeasure(): " + widthSize + ", "
                    + heightSize);
        }

        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            // disallowing padding in paged view (just pass 0)
            final View child = getPageAt(i);
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int childWidthMode;
            if (lp.width == LayoutParams.WRAP_CONTENT) {
                childWidthMode = MeasureSpec.AT_MOST;
            } else {
                childWidthMode = MeasureSpec.EXACTLY;
            }

            int childHeightMode;
            if (lp.height == LayoutParams.WRAP_CONTENT) {
                childHeightMode = MeasureSpec.AT_MOST;
            } else {
                childHeightMode = MeasureSpec.EXACTLY;
            }

            final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    widthSize - horizontalPadding, childWidthMode);
            final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    heightSize - verticalPadding, childHeightMode);

            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            maxChildHeight = Math
                    .max(maxChildHeight, child.getMeasuredHeight());
            if (DEBUG_LAYOUT) {
                Log.d(TAG, "measure-child " + i + ": child = " + child
                        + ", childWidthMode = " + childWidthMode
                        + ", childHeightMode = " + childHeightMode
                        + ", this = " + this);
            }
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = maxChildHeight + verticalPadding;
        }

        setMeasuredDimension(widthSize, heightSize);


        // We can't call getChildOffset/getRelativeChildOffset until we set the
        // measured dimensions.
        // We also wait until we set the measured dimensions before flushing the
        // cache as well, to
        // ensure that the cache is filled with good values.
        invalidateCachedOffsets();

        if (childCount > 0) {
            if (DEBUG)
                Log.d(TAG, "getRelativeChildOffset(): " + getMeasuredWidth()
                        + ", " + getChildWidth(0));

            // Calculate the variable page spacing if necessary
            if (mPageSpacing == AUTOMATIC_PAGE_SPACING) {
                // The gap between pages in the PagedView should be equal to the
                // gap from the page
                // to the edge of the screen (so it is not visible in the
                // current screen). To
                // account for unequal padding on each side of the paged view,
                // we take the maximum
                // of the left/right gap and use that as the gap between each
                // page.
                int offset = getRelativeChildOffset(0);
                int spacing = Math.max(offset,
                        widthSize - offset - getChildAt(0).getMeasuredWidth());
                setPageSpacing(spacing);
            }
        }

        updateScrollingIndicatorPosition();

        if (childCount > 0) {
            mMaxScrollX = getChildOffset(childCount - 1)
                    - getRelativeChildOffset(childCount - 1);
        } else {
            mMaxScrollX = 0;
        }

    }

    public void setPageSpacing(int pageSpacing) {
        mPageSpacing = pageSpacing;
        invalidateCachedOffsets();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        if (!mIsDataReady) {
            return;
        }

        if (DEBUG)
            Log.d(TAG, "PagedView.onLayout()");
        final int verticalPadding = getPaddingTop() + getPaddingBottom();
        final int childCount = getChildCount();
        int childLeft = getRelativeChildOffset(0);

        for (int i = 0; i < childCount; i++) {
            final View child = getPageAt(i);
            if (child.getVisibility() != View.GONE) {
                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();
                int childTop = getPaddingTop();
                if (mCenterPagesVertically) {
                    childTop += ((getMeasuredHeight() - verticalPadding) - childHeight) / 2;
                }

                if (DEBUG)
                    Log.d(TAG, "\tlayout-child" + i + ": " + childLeft + ", "
                            + childTop);
                child.layout(childLeft, childTop,
                        childLeft + child.getMeasuredWidth(), childTop
                                + childHeight);
                childLeft += childWidth + mPageSpacing;
            }
        }

        if (mFirstLayout && mCurrentPage >= 0 && mCurrentPage < getChildCount()) {
            setHorizontalScrollBarEnabled(false);
            updateCurrentPageScroll();
            setHorizontalScrollBarEnabled(true);
            mFirstLayout = false;
        }
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        // This ensures that when children are added, they get the correct
        // transforms / alphas
        // in accordance with any scroll effects.
        mForceScreenScrolled = true;
        invalidate();
        invalidateCachedOffsets();
    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
    }

    protected void invalidateCachedOffsets() {
        int count = getChildCount();
        if (count == 0) {
            mChildOffsets = null;
            mChildRelativeOffsets = null;
            return;
        }

        mChildOffsets = new int[count];
        mChildRelativeOffsets = new int[count];
        for (int i = 0; i < count; i++) {
            mChildOffsets[i] = -1;
            mChildRelativeOffsets[i] = -1;
        }
    }

    protected int getChildOffset(int index) {
        int[] childOffsets = mChildOffsets;

        if (childOffsets != null && childOffsets[index] != -1) {
            return childOffsets[index];
        } else {
            if (getChildCount() == 0)
                return 0;

            int offset = getRelativeChildOffset(0);
            for (int i = 0; i < index; ++i) {
                offset += getScaledMeasuredWidth(getPageAt(i)) + mPageSpacing;
            }
            if (childOffsets != null) {
                childOffsets[index] = offset;
            }
            return offset;
        }
    }

    protected int getRelativeChildOffset(int index) {
        if (mChildRelativeOffsets != null && mChildRelativeOffsets[index] != -1) {
            if (DEBUG_DRAW) {
                Log.d(TAG, "getRelativeChildOffset 1: index = " + index
                        + ", mChildRelativeOffsets[" + index + "] = "
                        + mChildRelativeOffsets[index] + ", this = " + this);
            }
            return mChildRelativeOffsets[index];
        } else {
            final int padding = getPaddingLeft() + getPaddingRight();
            final int offset = getPaddingLeft()
                    + (getMeasuredWidth() - padding - getChildWidth(index)) / 2;
            if (mChildRelativeOffsets != null) {
                mChildRelativeOffsets[index] = offset;
            }
            if (DEBUG_DRAW) {
                Log.d(TAG, "getRelativeChildOffset 2: index = " + index
                        + ", mPaddingLeft = " + getPaddingLeft()
                        + ", mPaddingRight = " + getPaddingRight()
                        + ", padding = " + padding + ", offset = " + offset
                        + ", measure width = " + getMeasuredWidth()
                        + ", this = " + this);
            }
            return offset;
        }
    }

    // TODO Remove this method!
    protected int getScaledMeasuredWidth(View child) {
        return (int) (child.getMeasuredWidth());
    }


    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rectangle,
                                                 boolean immediate) {
        int page = indexToPage(indexOfChild(child));
        if (page != mCurrentPage || !mScroller.isFinished()) {
            snapToPage(page);
            return true;
        }
        return false;
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction,
                                                  Rect previouslyFocusedRect) {
        int focusablePage;
        if (mNextPage != INVALID_PAGE) {
            focusablePage = mNextPage;
        } else {
            focusablePage = mCurrentPage;
        }
        View v = getPageAt(focusablePage);
        if (v != null) {
            return v.requestFocus(direction, previouslyFocusedRect);
        }
        return false;
    }

    @Override
    public boolean dispatchUnhandledMove(View focused, int direction) {
        if (direction == View.FOCUS_LEFT) {
            if (getCurrentPage() > 0) {
                snapToPage(getCurrentPage() - 1);
                return true;
            }
        } else if (direction == View.FOCUS_RIGHT) {
            if (getCurrentPage() < getPageCount() - 1) {
                snapToPage(getCurrentPage() + 1);
                return true;
            }
        }
        return super.dispatchUnhandledMove(focused, direction);
    }

    @Override
    public void addFocusables(ArrayList<View> views, int direction,
                              int focusableMode) {
        if (mCurrentPage >= 0 && mCurrentPage < getPageCount()) {
            getPageAt(mCurrentPage).addFocusables(views, direction,
                    focusableMode);
        }
        if (direction == View.FOCUS_LEFT) {
            if (mCurrentPage > 0) {
                getPageAt(mCurrentPage - 1).addFocusables(views, direction,
                        focusableMode);
            }
        } else if (direction == View.FOCUS_RIGHT) {
            if (mCurrentPage < getPageCount() - 1) {
                getPageAt(mCurrentPage + 1).addFocusables(views, direction,
                        focusableMode);
            }
        }
    }

    /**
     * If one of our descendant views decides that it could be focused now, only
     * pass that along if it's on the current page.
     * <p/>
     * This happens when live folders requery, and if they're off page, they end
     * up calling requestFocus, which pulls it on page.
     */
    @Override
    public void focusableViewAvailable(View focused) {
        View current = getPageAt(mCurrentPage);
        View v = focused;
        while (true) {
            if (v == current) {
                super.focusableViewAvailable(focused);
                return;
            }
            if (v == this) {
                return;
            }
            ViewParent parent = v.getParent();
            if (parent instanceof View) {
                v = (View) v.getParent();
            } else {
                return;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (disallowIntercept) {
            // We need to make sure to cancel our long press if
            // a scrollable widget takes over touch events
            final View currentPage = getPageAt(mCurrentPage);
            currentPage.cancelLongPress();
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    /**
     * Return true if a tap at (x, y) should trigger a flip to the previous
     * page.
     */
    protected boolean hitsPreviousPage(float x, float y) {
        return (x < getRelativeChildOffset(mCurrentPage) - mPageSpacing);
    }

    /**
     * Return true if a tap at (x, y) should trigger a flip to the next page.
     */
    protected boolean hitsNextPage(float x, float y) {
        return (x > (getMeasuredWidth() - getRelativeChildOffset(mCurrentPage) + mPageSpacing));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (DEBUG_MOTION) {
            Log.d(TAG, "onInterceptTouchEvent: ev = " + ev + ", mScrollX = "
                    + getScrollX() + ", this = " + this);
        }
		/*
		 * This method JUST determines whether we want to intercept the motion.
		 * If we return true, onTouchEvent will be called and we do the actual
		 * scrolling there.
		 */
        acquireVelocityTrackerAndAddMovement(ev);

        // Skip touch handling if there are no pages to swipe
        if (getChildCount() <= 0) {
            Log.d(TAG, "There are no pages to swipe, page count = "
                    + getChildCount());
            return super.onInterceptTouchEvent(ev);
        }

		/*
		 * Shortcut the most recurring case: the user is in the dragging state
		 * and he is moving his finger. We want to intercept this motion.
		 */
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE)
                && (mTouchState == TOUCH_STATE_SCROLLING)) {
            Log.d(TAG, "onInterceptTouchEvent: touch move during scrolling.");
            return true;
        }

        if (mTouchState != TOUCH_STATE_REST) {
            return true;
        }

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {
			/*
			 * mIsBeingDragged == false, otherwise the shortcut would have
			 * caught it. Check whether the user has moved far enough from his
			 * original down touch.
			 */
                if (mActivePointerId != INVALID_POINTER) {
                    determineScrollingStart(ev);
                    break;
                }
                // if mActivePointerId is INVALID_POINTER, then we must have missed
                // an ACTION_DOWN
                // event. in that case, treat the first occurence of a move event as
                // a ACTION_DOWN
                // i.e. fall through to the next case (don't break)
                // (We sometimes miss ACTION_DOWN events in Workspace because it
                // ignores all events
                // while it's small- this was causing a crash before we checked for
                // INVALID_POINTER)
            }

            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();
                // Remember location of down touch
                mDownMotionX = x;
                mLastMotionX = x;
                mLastMotionY = y;
                mLastMotionXRemainder = 0;
                mTotalMotionX = 0;
                mActivePointerId = ev.getPointerId(0);
                mAllowLongPress = true;

			/*
			 * If being flinged and user touches the screen, initiate drag;
			 * otherwise don't. mScroller.isFinished should be false when being
			 * flinged.
			 */
                final int xDist = Math.abs(mScroller.getFinalX()
                        - mScroller.getCurrX());
                final boolean finishedScrolling = (mScroller.isFinished() || xDist < mTouchSlop);
                if (finishedScrolling) {
                    mTouchState = TOUCH_STATE_REST;
                    mScroller.abortAnimation();
                } else {
                    mTouchState = TOUCH_STATE_SCROLLING;
                }

                // check if this can be the beginning of a tap on the side of the
                // pages
                // to scroll the current page
                if (mTouchState != TOUCH_STATE_PREV_PAGE
                        && mTouchState != TOUCH_STATE_NEXT_PAGE) {
                    if (getChildCount() > 0) {
                        if (hitsPreviousPage(x, y)) {
                            mTouchState = TOUCH_STATE_PREV_PAGE;
                        } else if (hitsNextPage(x, y)) {
                            mTouchState = TOUCH_STATE_NEXT_PAGE;
                        }
                    }
                }
                if (DEBUG) {
                    Log.d(TAG,
                            "onInterceptTouchEvent touch down: finishedScrolling = "
                                    + finishedScrolling + ", mScrollX = "
                                    + getScrollX() + ", xDist = " + xDist
                                    + ", mTouchState = " + mTouchState
                                    + ", this = " + this);
                }
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                requestDisallowInterceptTouchEvent(false);
			/*
			 * It means the workspace is in the middle if the scrollX can not be
			 * divided by the width of its child, need to snap to edge.
			 */
                snapToDestination();
                mTouchState = TOUCH_STATE_REST;
                mAllowLongPress = false;
                mActivePointerId = INVALID_POINTER;
                releaseVelocityTracker();
                break;

            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                releaseVelocityTracker();
                break;
        }

		/*
		 * The only time we want to intercept motion events is if we are in the
		 * drag mode.
		 */
        return mTouchState != TOUCH_STATE_REST;
    }

    protected void determineScrollingStart(MotionEvent ev) {
        // Modified by yongan.qiu on 2013-2-19 for scroll slot begin.
        // determineScrollingStart(ev, 1.0f);
        determineScrollingStart(ev, 0.35f);
        // Modified by yongan.qiu end.
    }

    /*
     * Determines if we should change the touch state to start scrolling after
     * the user moves their touch point too far.
     */
    protected void determineScrollingStart(MotionEvent ev, float touchSlopScale) {
		/*
		 * Locally do absolute value. mLastMotionX is set to the y value of the
		 * down event.
		 */
        final int pointerIndex = ev.findPointerIndex(mActivePointerId);
        if (pointerIndex == -1) {
            Log.d(TAG, "determineScrollingStart pointerIndex == -1.");
            return;
        }
        final float x = ev.getX(pointerIndex);
        final float y = ev.getY(pointerIndex);
        final int xDiff = (int) Math.abs(x - mLastMotionX);
        final int yDiff = (int) Math.abs(y - mLastMotionY);

        final int touchSlop = Math.round(touchSlopScale * mTouchSlop);
        boolean xPaged = xDiff > mPagingTouchSlop;
        boolean xMoved = xDiff > touchSlop;
        boolean yMoved = yDiff > touchSlop;

        if (xMoved || xPaged || yMoved) {
            if (mUsePagingTouchSlop ? xPaged : xMoved) {
                // Scroll if the user moved far enough along the X axis
                mTouchState = TOUCH_STATE_SCROLLING;
                mTotalMotionX += Math.abs(mLastMotionX - x);
                mLastMotionX = x;
                mLastMotionXRemainder = 0;
                mTouchX = getScrollX();
                mSmoothingTime = System.nanoTime() / NANOTIME_DIV;
                pageBeginMoving();
            }
            // Either way, cancel any pending longpress
            cancelCurrentPageLongPress();
        }
    }

    protected void cancelCurrentPageLongPress() {
        if (mAllowLongPress) {
            mAllowLongPress = false;
            // Try canceling the long press. It could also have been scheduled
            // by a distant descendant, so use the mAllowLongPress flag to block
            // everything
            final View currentPage = getPageAt(mCurrentPage);
            if (currentPage != null) {
                currentPage.cancelLongPress();
            }
        }
    }


    // This curve determines how the effect of scrolling over the limits of the
    // page dimishes
    // as the user pulls further and further from the bounds
    private float overScrollInfluenceCurve(float f) {
        f -= 1.0f;
        return f * f * f + 1.0f;
    }

    protected void acceleratedOverScroll(float amount) {
        int screenSize = getMeasuredWidth();

        // We want to reach the max over scroll effect when the user has
        // over scrolled half the size of the screen
        float f = OVERSCROLL_ACCELERATE_FACTOR * (amount / screenSize);

        if (f == 0)
            return;

        // Clamp this factor, f, to -1 < f < 1
        if (Math.abs(f) >= 1) {
            f /= Math.abs(f);
        }

        int overScrollAmount = Math.round(f * screenSize);
        if (amount < 0) {
            mOverScrollX = overScrollAmount;
            super.scrollTo(0, getScrollY());
        } else {
            mOverScrollX = mMaxScrollX + overScrollAmount;
            super.scrollTo(mMaxScrollX, getScrollY());
        }
        postInvalidate();
    }

    protected void dampedOverScroll(float amount) {
        int screenSize = getMeasuredWidth();

        float f = (amount / screenSize);

        if (f == 0)
            return;
        f = f / (Math.abs(f)) * (overScrollInfluenceCurve(Math.abs(f)));

        // Clamp this factor, f, to -1 < f < 1
        if (Math.abs(f) >= 1) {
            f /= Math.abs(f);
        }

        int overScrollAmount = (int) Math.round(OVERSCROLL_DAMP_FACTOR * f
                * screenSize);
        if (amount < 0) {
            mOverScrollX = overScrollAmount;
            super.scrollTo(0, getScrollY());
        } else {
            mOverScrollX = mMaxScrollX + overScrollAmount;
            super.scrollTo(mMaxScrollX, getScrollY());
        }
        postInvalidate();
    }

    protected void overScroll(float amount) {
        dampedOverScroll(amount);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        // Skip touch handling if there are no pages to swipe
        if (getChildCount() <= 0) {
            Log.d(TAG, "There is no child in PagedView, child count = "
                    + getChildCount());
            return super.onTouchEvent(ev);
        }

        acquireVelocityTrackerAndAddMovement(ev);

        final int action = ev.getAction();

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                // 停止自动轮播
                if (isAutoPage) {
                    stopTimer();
                }
			/*
			 * If being flinged and user touches, stop the fling. isFinished
			 * will be false if being flinged.
			 */
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                // Remember where the motion event started
                mDownMotionX = mLastMotionX = ev.getX();
                mLastMotionXRemainder = 0;
                mTotalMotionX = 0;
                mActivePointerId = ev.getPointerId(0);
                if (DEBUG) {
                    Log.d(TAG, "Touch down: mDownMotionX = " + mDownMotionX
                            + ", mTouchState = " + mTouchState
                            + ", mCurrentPage = " + mCurrentPage + ", mScrollX = "
                            + getScrollX() + ", this = " + this);
                }
                if (mTouchState == TOUCH_STATE_SCROLLING) {
                    pageBeginMoving();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (adapter == null || adapter.getCount() <= 1) {
                    mTouchState = TOUCH_STATE_REST;
                } else if (mTouchState == TOUCH_STATE_SCROLLING) {
                    requestDisallowInterceptTouchEvent(true);
                    // Scroll to follow the motion event
                    final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                    final float x = ev.getX(pointerIndex);
                    final float deltaX = mLastMotionX + mLastMotionXRemainder - x;

                    mTotalMotionX += Math.abs(deltaX);

                    // Only scroll and update mLastMotionX if we have moved some
                    // discrete amount. We
                    // keep the remainder because we are actually testing if we've
                    // moved from the last
                    // scrolled position (which is discrete).
                    if (Math.abs(deltaX) >= 1.0f) {
                        mTouchX += deltaX;
                        mSmoothingTime = System.nanoTime() / NANOTIME_DIV;
                        if (!mDeferScrollUpdate) {
                            scrollBy((int) deltaX, 0);
                            if (DEBUG)
                                Log.d(TAG, "onTouchEvent().Scrolling: " + deltaX);
                        } else {
                            invalidate();
                        }
                        mLastMotionX = x;
                        mLastMotionXRemainder = deltaX - (int) deltaX;
                    } else {
                        awakenScrollBars();
                    }

                    if (DEBUG) {
                        Log.d(TAG, "Touch move scroll: x = " + x + ", deltaX = "
                                + deltaX + ", mTotalMotionX = " + mTotalMotionX
                                + ", mLastMotionX = " + mLastMotionX
                                + ", mCurrentPage = " + mCurrentPage
                                + ",mTouchX = " + mTouchX + " ,mLastMotionX = "
                                + mLastMotionX + ", mScrollX = " + getScrollX());
                    }
                } else {
                    determineScrollingStart(ev);
                }
                break;

            case MotionEvent.ACTION_UP:
                requestDisallowInterceptTouchEvent(true);
                // 启动自动轮播
                if (isAutoPage) {
                    startTimer();
                }
                if (mTouchState == TOUCH_STATE_SCROLLING) {
                    final int activePointerId = mActivePointerId;
                    final int pointerIndex = ev.findPointerIndex(activePointerId);
                    final float x = ev.getX(pointerIndex);
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int velocityX = (int) velocityTracker
                            .getXVelocity(activePointerId);
                    final int deltaX = (int) (x - mDownMotionX);
                    final int pageWidth = getScaledMeasuredWidth(getPageAt(mCurrentPage));
                    boolean isSignificantMove = Math.abs(deltaX) > pageWidth
                            * SIGNIFICANT_MOVE_THRESHOLD;

                    mTotalMotionX += Math.abs(mLastMotionX + mLastMotionXRemainder
                            - x);

                    boolean isFling = mTotalMotionX > MIN_LENGTH_FOR_FLING
                            && Math.abs(velocityX) > mFlingThresholdVelocity;

                    if (DEBUG) {
                        Log.d(TAG, "Touch up scroll: x = " + x + ", deltaX = "
                                + deltaX + ", mTotalMotionX = " + mTotalMotionX
                                + ", mLastMotionX = " + mLastMotionX
                                + ", velocityX = " + velocityX
                                + ", mCurrentPage = " + mCurrentPage
                                + ", pageWidth = " + pageWidth + ", isFling = "
                                + isFling + ", isSignificantMove = "
                                + isSignificantMove + ", mScrollX = "
                                + getScrollX());
                    }
                    // In the case that the page is moved far to one direction and
                    // then is flung
                    // in the opposite direction, we use a threshold to determine
                    // whether we should
                    // just return to the starting page, or if we should skip one
                    // further.
                    boolean returnToOriginalPage = false;
                    if (Math.abs(deltaX) > pageWidth
                            * RETURN_TO_ORIGINAL_PAGE_THRESHOLD
                            && Math.signum(velocityX) != Math.signum(deltaX)
                            && isFling) {
                        if (DEBUG) {
                            Log.d(TAG, "Return to origin page: deltaX = " + deltaX
                                    + ", velocityX = " + velocityX + ", isFling = "
                                    + isFling);
                        }
                        returnToOriginalPage = true;
                    }

                    int finalPage;
                    // We give flings precedence over large moves, which is why we
                    // short-circuit our
                    // test for a large move if a fling has been registered. That
                    // is, a large
                    // move to the left and fling to the right will register as a
                    // fling to the right.
                    if (((isSignificantMove && deltaX > 0 && !isFling) || (isFling && velocityX > 0))
                            && mCurrentPage > 0) {
                        finalPage = returnToOriginalPage ? mCurrentPage
                                : mCurrentPage - 1;
                        if (DEBUG) {
                            Log.d(TAG, "Case 1: finalPage = " + finalPage
                                    + ", mCurrentPage = " + mCurrentPage
                                    + ", velocityX = " + velocityX);
                        }
                        snapToPageWithVelocity(finalPage, velocityX);
                    } else if (((isSignificantMove && deltaX < 0 && !isFling) || (isFling && velocityX < 0))
                            && mCurrentPage < getChildCount() - 1) {
                        finalPage = returnToOriginalPage ? mCurrentPage
                                : mCurrentPage + 1;
                        if (DEBUG) {
                            Log.d(TAG, "Case 2: finalPage = " + finalPage
                                    + ", mCurrentPage = " + mCurrentPage
                                    + ", velocityX = " + velocityX);
                        }
                        snapToPageWithVelocity(finalPage, velocityX);
                    } else {
                        if (DEBUG) {
                            Log.d(TAG, "Case 3: mCurrentPage = " + mCurrentPage
                                    + ", mScrollX = " + getScrollX());
                        }

                        snapToDestination();
                    }
                } else if (mTouchState == TOUCH_STATE_PREV_PAGE) {
                    // at this point we have not moved beyond the touch slop
                    // (otherwise mTouchState would be TOUCH_STATE_SCROLLING), so
                    // we can just page
                    int nextPage = Math.max(0, mCurrentPage - 1);
                    if (DEBUG) {
                        Log.d(TAG, "TOUCH_STATE_PREV_PAGE: mCurrentPage = "
                                + mCurrentPage + ", nextPage = " + nextPage
                                + ", this = " + this);
                    }
                    if (nextPage != mCurrentPage) {
                        snapToPage(nextPage);
                    } else {
                        snapToDestination();
                    }
                } else if (mTouchState == TOUCH_STATE_NEXT_PAGE) {
                    // at this point we have not moved beyond the touch slop
                    // (otherwise mTouchState would be TOUCH_STATE_SCROLLING), so
                    // we can just page
                    int nextPage = Math.min(getChildCount() - 1, mCurrentPage + 1);
                    if (DEBUG) {
                        Log.d(TAG, "TOUCH_STATE_NEXT_PAGE: mCurrentPage = "
                                + mCurrentPage + ", nextPage = " + nextPage
                                + ", this = " + this);
                    }
                    if (nextPage != mCurrentPage) {
                        snapToPage(nextPage);
                    } else {
                        snapToDestination();
                    }
                } else {
                    if (DEBUG) {
                        Log.d(TAG,
                                "[--Case Watcher--]Touch up unhandled: mCurrentPage = "
                                        + mCurrentPage + ", mTouchState = "
                                        + mTouchState + ", mScrollX = "
                                        + getScrollX() + ", this = " + this);
                    }
				/*
				 * M: Handle special wrong case, the child stop in the middle,
				 * we need to snap it to destination, but we have no efficient
				 * way to detect this case, so do the snap process all the way,
				 * this has no side effect because the distance will be 0 if it
				 * is a normal case.
				 */
                    snapToDestination();
                    onUnhandledTap(ev);
                }
                mTouchState = TOUCH_STATE_REST;
                mActivePointerId = INVALID_POINTER;
                releaseVelocityTracker();
                break;

            case MotionEvent.ACTION_CANCEL:
                if (DEBUG) {
                    Log.d(TAG, "Touch cancel: mCurrentPage = " + mCurrentPage
                            + ", mTouchState = " + mTouchState + ", mScrollX = "
                            + getScrollX() + ", this = " + this);
                }
                if (mTouchState == TOUCH_STATE_SCROLLING) {
                    snapToDestination();
                }
                mTouchState = TOUCH_STATE_REST;
                mActivePointerId = INVALID_POINTER;
                releaseVelocityTracker();
                break;

            case MotionEvent.ACTION_POINTER_UP:
                if (DEBUG) {
                    Log.d(TAG, "Touch ACTION_POINTER_UP: mCurrentPage = "
                            + mCurrentPage + ", mTouchState = " + mTouchState
                            + ", mActivePointerId = " + mActivePointerId
                            + ", this = " + this);
                }
                onSecondaryPointerUp(ev);
                break;
        }

        return true;
    }

    /**
     * 自动反向下一页
     */
    public void autoPage() {
        if (adapter == null || adapter.getCount() <= 1) {
            return;
        }
        int nextPage = Math.min(getChildCount() - 1, mCurrentPage + 1);
        if (nextPage != mCurrentPage) {
            snapToPage(nextPage);
        } else {
            snapToDestination();
        }

    }

    private void acquireVelocityTrackerAndAddMovement(MotionEvent ev) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
    }

    private void releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            // TODO: Make this decision more intelligent.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastMotionX = mDownMotionX = ev.getX(newPointerIndex);
            mLastMotionY = ev.getY(newPointerIndex);
            mLastMotionXRemainder = 0;
            mActivePointerId = ev.getPointerId(newPointerIndex);
            if (mVelocityTracker != null) {
                mVelocityTracker.clear();
            }
        }
    }

    protected void onUnhandledTap(MotionEvent ev) {
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        int page = indexToPage(indexOfChild(child));
        if (page >= 0 && page != getCurrentPage() && !isInTouchMode()) {
            snapToPage(page);
        }
    }

    protected int getChildIndexForRelativeOffset(int relativeOffset) {
        final int childCount = getChildCount();
        int left;
        int right;
        for (int i = 0; i < childCount; ++i) {
            left = getRelativeChildOffset(i);
            right = (left + getScaledMeasuredWidth(getPageAt(i)));
            if (left <= relativeOffset && relativeOffset <= right) {
                if (DEBUG) {
                    Log.d(TAG, "getChildIndexForRelativeOffset i = " + i);
                }
                return i;
            }
        }
        return -1;
    }

    protected int getChildWidth(int index) {
        // This functions are called enough times that it actually makes a
        // difference in the
        // profiler -- so just inline the max() here
        if (getPageAt(index) == null) return 0;
        final int measuredWidth = getPageAt(index).getMeasuredWidth();
        if (DEBUG_LAYOUT) {
            Log.d(TAG, "getChildWidth: index = " + index + ", child = "
                    + getPageAt(index) + ", measured width = " + measuredWidth);
        }
        return measuredWidth;
    }

    int getPageNearestToCenterOfScreen() {
        int minDistanceFromScreenCenter = Integer.MAX_VALUE;
        int minDistanceFromScreenCenterIndex = -1;
        int screenCenter = getScrollX() + (getMeasuredWidth() / 2);
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; ++i) {
            View layout = (View) getPageAt(i);
            int childWidth = getScaledMeasuredWidth(layout);
            int halfChildWidth = (childWidth / 2);
            int childCenter = getChildOffset(i) + halfChildWidth;
            int distanceFromScreenCenter = Math.abs(childCenter - screenCenter);
            if (distanceFromScreenCenter < minDistanceFromScreenCenter) {
                minDistanceFromScreenCenter = distanceFromScreenCenter;
                minDistanceFromScreenCenterIndex = i;
            }
        }
        if (DEBUG) {
            Log.d(TAG,
                    "getPageNearestToCenterOfScreen: minDistanceFromScreenCenterIndex = "
                            + minDistanceFromScreenCenterIndex
                            + ", mScrollX = " + getScrollX());
        }
        return minDistanceFromScreenCenterIndex;
    }

    protected void snapToDestination() {
        snapToPage(getPageNearestToCenterOfScreen(),
                PAGE_SNAP_ANIMATION_DURATION);
    }

    private static class ScrollInterpolator implements Interpolator {
        public ScrollInterpolator() {
        }

        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1;
        }
    }

    // We want the duration of the page snap animation to be influenced by the
    // distance that
    // the screen has to travel, however, we don't want this duration to be
    // effected in a
    // purely linear fashion. Instead, we use this method to moderate the effect
    // that the distance
    // of travel has on the overall snap duration.
    float distanceInfluenceForSnapDuration(float f) {
        f -= 0.5f; // center the values about 0.
        f *= 0.3f * Math.PI / 2.0f;
        return (float) Math.sin(f);
    }

    protected void snapToPageWithVelocity(int whichPage, int velocity) {
        whichPage = Math.max(0, Math.min(whichPage, getChildCount() - 1));
        int halfScreenSize = getMeasuredWidth() / 2;

        if (DEBUG)
            Log.d(TAG, "snapToPage.getChildOffset(): "
                    + getChildOffset(whichPage));
        if (DEBUG)
            Log.d(TAG, "snapToPageWithVelocity.getRelativeChildOffset(): "
                    + getMeasuredWidth() + ", " + getChildWidth(whichPage));
        final int newX = getChildOffset(whichPage)
                - getRelativeChildOffset(whichPage);
        int delta = newX - mUnboundedScrollX;
        int duration = 0;

        if (DEBUG) {
            Log.d(TAG, "snapToPageWithVelocity: getChildOffset() = "
                    + getChildOffset(whichPage) + ", measured width = "
                    + +getMeasuredWidth() + ", " + getChildWidth(whichPage)
                    + ", newX = " + newX + ", mUnboundedScrollX = "
                    + mUnboundedScrollX + ", halfScreenSize = "
                    + halfScreenSize);
        }

        if (Math.abs(velocity) < mMinFlingVelocity) {
            // If the velocity is low enough, then treat this more as an
            // automatic page advance
            // as opposed to an apparent physical response to flinging
            Log.i(TAG, "snapToPageWithVelocity: velocity = " + velocity
                    + ", whichPage = " + whichPage + ", MIN_FLING_VELOCITY = "
                    + MIN_FLING_VELOCITY + ", this = " + this);
            snapToPage(whichPage, PAGE_SNAP_ANIMATION_DURATION);
            return;
        }

        // Here we compute a "distance" that will be used in the computation of
        // the overall
        // snap duration. This is a function of the actual distance that needs
        // to be traveled;
        // we keep this value close to half screen size in order to reduce the
        // variance in snap
        // duration as a function of the distance the page needs to travel.
        float distanceRatio = Math.min(1f, 1.0f * Math.abs(delta)
                / (2 * halfScreenSize));
        float distance = halfScreenSize + halfScreenSize
                * distanceInfluenceForSnapDuration(distanceRatio);

        velocity = Math.abs(velocity);
        velocity = Math.max(mMinSnapVelocity, velocity);

        // we want the page's snap velocity to approximately match the velocity
        // at which the
        // user flings, so we scale the duration by a value near to the
        // derivative of the scroll
        // interpolator at zero, ie. 5. We use 4 to make it a little slower.
        duration = 4 * Math.round(1000 * Math.abs(distance / velocity));

        if (DEBUG) {
            Log.d(TAG, "snapToPageWithVelocity: velocity = " + velocity
                    + ", whichPage = " + whichPage + ", duration = " + duration
                    + ", delta = " + delta + ", mScrollX = " + getScrollX()
                    + ", mUnboundedScrollX = " + mUnboundedScrollX
                    + ", this = " + this);
        }
        snapToPage(whichPage, delta, duration);
    }

    protected void snapToPage(int whichPage) {
        snapToPage(whichPage, PAGE_SNAP_ANIMATION_DURATION);
    }

    protected void snapToPage(int whichPage, int duration) {
        whichPage = Math.max(0, Math.min(whichPage, getPageCount() - 1));

        if (DEBUG)
            Log.d(TAG, "snapToPage.getChildOffset(): "
                    + getChildOffset(whichPage));
        if (DEBUG)
            Log.d(TAG, "snapToPage.getRelativeChildOffset(): "
                    + getMeasuredWidth() + ", " + getChildWidth(whichPage));
        int newX = getChildOffset(whichPage)
                - getRelativeChildOffset(whichPage);
        final int sX = mUnboundedScrollX;
        final int delta = newX - sX;
        snapToPage(whichPage, delta, duration);
    }

    protected void snapToPage(int whichPage, int delta, int duration) {
        if (DEBUG) {
            Log.d(TAG, "(PagedView)snapToPage whichPage = " + whichPage
                    + ", delta = " + delta + ", duration = " + duration
                    + ", mNextPage = " + mNextPage + ", mUnboundedScrollX = "
                    + mUnboundedScrollX + ", mDeferScrollUpdate = "
                    + mDeferScrollUpdate + ", mScrollX = " + getScrollX()
                    + ", this = " + this);
        }

        mNextPage = whichPage;

        View focusedChild = getFocusedChild();
        if (focusedChild != null && whichPage != mCurrentPage
                && focusedChild == getPageAt(mCurrentPage)) {
            focusedChild.clearFocus();
        }

        pageBeginMoving();
        awakenScrollBars(duration);
        if (duration == 0) {
            duration = Math.abs(delta);
        }

        if (!mScroller.isFinished())
            mScroller.abortAnimation();
        mScroller.startScroll(mUnboundedScrollX, 0, delta, 0, duration);

        // Load associated pages immediately if someone else is handling the
        // scroll, otherwise defer
        // loading associated pages until the scroll settles
        if (mDeferScrollUpdate) {
            loadAssociatedPages(mNextPage);
        } else {
            mDeferLoadAssociatedPagesUntilScrollCompletes = true;
        }
        notifyDataSetChanged();
        postInvalidate();
    }

    public void scrollLeft() {
        if (mScroller.isFinished()) {
            if (mCurrentPage > 0)
                snapToPage(mCurrentPage - 1);
        } else {
            if (mNextPage > 0)
                snapToPage(mNextPage - 1);
        }
    }

    public void scrollRight() {
        if (mScroller.isFinished()) {
            if (mCurrentPage < getChildCount() - 1)
                snapToPage(mCurrentPage + 1);
        } else {
            if (mNextPage < getChildCount() - 1)
                snapToPage(mNextPage + 1);
        }
    }

    public int getPageForView(View v) {
        int result = -1;
        if (v != null) {
            ViewParent vp = v.getParent();
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                if (vp == getPageAt(i)) {
                    return i;
                }
            }
        }
        return result;
    }

    /**
     * @return True is long presses are still allowed for the current touch
     */
    public boolean allowLongPress() {
        return mAllowLongPress;
    }

    /**
     * Set true to allow long-press events to be triggered, usually checked by
     * {@link } to accept or block dpad-initiated long-presses.
     */
    public void setAllowLongPress(boolean allowLongPress) {
        mAllowLongPress = allowLongPress;
    }

    public static class SavedState extends BaseSavedState {
        int currentPage = -1;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPage = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(currentPage);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    protected void loadAssociatedPages(int page) {
        loadAssociatedPages(page, false);
    }

    protected void loadAssociatedPages(int page, boolean immediateAndOnly) {

        if (mContentIsRefreshable) {
            final int count = getChildCount();
            if (page < count) {
                int lowerPageBound = getAssociatedLowerPageBound(page);
                int upperPageBound = getAssociatedUpperPageBound(page);
                if (DEBUG) {
                    Log.d(TAG, "loadAssociatedPages: " + lowerPageBound + "/"
                            + upperPageBound + ", page = " + page
                            + ", count = " + count);
                }
                // First, clear any pages that should no longer be loaded
                for (int i = 0; i < count; ++i) {
                    Page layout = (Page) getPageAt(i);
                    if ((i < lowerPageBound) || (i > upperPageBound)) {
                        layout.removeAllViewsInPage();
                        mDirtyPageContent.set(i, true);
                    }
                }
                // Next, load any new pages
                for (int i = 0; i < count; ++i) {
                    if ((i != page) && immediateAndOnly) {
                        continue;
                    }
                    if (lowerPageBound <= i && i <= upperPageBound) {
                        if (mDirtyPageContent.get(i)) {
//							syncPageItems(i, (i == page) && immediateAndOnly);
                            mDirtyPageContent.set(i, false);
                        }
                    }
                }
            }
        }
    }

    protected int getAssociatedLowerPageBound(int page) {
        return Math.max(0, page - 1);
    }

    protected int getAssociatedUpperPageBound(int page) {
        final int count = getChildCount();
        return Math.min(page + 1, count - 1);
    }

    /**
     * This method is called ONLY to synchronize the number of pages that the
     * paged view has. To actually fill the pages with information, implement
     * syncPageItems() below. It is guaranteed that syncPageItems() will be
     * called for a particular page before it is shown, and therefore,
     * individual page items do not need to be updated in this method.
     */
//	public abstract void mainHandler();

    /**
     * This method is called to synchronize the items that are on a particular
     * page. If views on the page can be reused, then they should be updated
     * within this method.
     */
//	public abstract void syncPageItems(int page, boolean immediate);
    protected void invalidatePageData(int currentPage) {
        invalidatePageData(currentPage, false);
    }

    protected void invalidatePageData(int currentPage, boolean immediateAndOnly) {

        if (!mIsDataReady) {
            return;
        }

        if (mContentIsRefreshable) {
            // Force all scrolling-related behavior to end
            mScroller.forceFinished(true);
            mNextPage = INVALID_PAGE;

            // Update all the pages
//			syncPages();

            // We must force a measure after we've loaded the pages to update
            // the content width and
            // to determine the full scroll width
            measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(),
                    MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
                    getMeasuredHeight(), MeasureSpec.EXACTLY));

            // Set a new page as the current page if necessary
            if (currentPage > -1) {
                setCurrentPage(Math.min(getPageCount() - 1, currentPage));
            }

            // Mark each of the pages as dirty
            final int count = getChildCount();
            mDirtyPageContent.clear();
            for (int i = 0; i < count; ++i) {
                mDirtyPageContent.add(true);
            }

            // Load any pages that are necessary for the current window of views
            loadAssociatedPages(mCurrentPage, immediateAndOnly);
            if (DEBUG) {
                Log.d(TAG,
                        "[--Case Watcher--]invalidatePageData: currentPage = "
                                + currentPage + ", immediateAndOnly = "
                                + immediateAndOnly + ", mScrollX = "
                                + getScrollX());
            }
			/*
			 * M: The scroller is force finished at the very begin, sometimes
			 * the page will stop in the middle, we need to snap it to the right
			 * destination to make pages position to the bounds.
			 */
            snapToDestination();
            requestLayout();
        }
    }


    private void updateScrollingIndicator() {
        // TODO
    }

    private void updateScrollingIndicatorPosition() {
        // TODO
    }


    interface Page {
        void removeAllViewsInPage();
    }

    private PageIndicator mPageIndicator;

    public void setPageIndicator(PageIndicator mPageIndicator) {
        this.mPageIndicator = mPageIndicator;
        if (adapter != null && mPageIndicator != null) {
            int count = adapter.getCount();
            mPageIndicator.setCount(count);
            int index = getViewPosition(currentIndex);
            mPageIndicator.setCurrentIndex(index == 0 ? 0 : index % count);
        }
    }

    public void setCurrentIndex(int index) {
        if(adapter != null){
            if(index < 0){
                currentIndex = 0;
            }else if(index >= adapter.getCount()){
                currentIndex = adapter.getCount() - 1;
            }else{
                currentIndex = index;
            }
            if (mPageIndicator != null) {
                int count = adapter.getCount();
                mPageIndicator.setCount(count);
                int index1 = getViewPosition(currentIndex);
                mPageIndicator.setCurrentIndex(index1 == 0 ? 0 : index1 % count);
            }
            for (int i = 0; i < mChangedScrap.size(); i++) {
                RecyclerView.ViewHolder holder = mChangedScrap.get(i);
                int index1 = getViewPosition(currentIndex + i - 1);
                if(adapter.getCount() > index){
                    adapter.onBindViewHolder(holder, index1);
                }
            }
        }



//        notifyPageSwitchListener();
    }

    public int getCurrentIndex() {
        return getViewPosition(currentIndex);
    }

    public void setAdapter(PagedViewAdapter adapter) {
        removeAllViews();
        this.adapter = adapter;
        if (adapter != null) {
            adapter.setPagedView(this);
            for (int i = 0; i < 3; i++) {
                RecyclerView.ViewHolder holder = adapter.onCreateViewHolder(this, 0);
                mChangedScrap.add(holder);
                LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                addView(holder.itemView, layoutParams);
            }
        }

        if (adapter != null) {
            if (mCurrentPage == 0) {
                currentIndex--;
            } else if (mCurrentPage == 2) {
                currentIndex++;
            }
            if (getCurrentPage() != 1) {
                setCurrentPage(1);
            } else {
                if (mPageIndicator != null) {
                    int count = adapter.getCount();
                    mPageIndicator.setCount(count);
                    int index = getViewPosition(currentIndex);
                    mPageIndicator.setCurrentIndex(index == 0 ? 0 : index % count);
                }
                for (int i = 0; i < mChangedScrap.size(); i++) {
                    RecyclerView.ViewHolder holder = mChangedScrap.get(i);
                    int index = getViewPosition(currentIndex + i - 1);
                    if(adapter.getCount() > index){
                        adapter.onBindViewHolder(holder, index);
                    }
                }
            }
        }
    }

    public PagedViewAdapter getAdapter() {
        return adapter;
    }

    /**
     * 设置自动轮播
     *
     * @param isAutoPage
     */
    public void setAutoPage(boolean isAutoPage) {
        this.isAutoPage = isAutoPage;
        mainHandler = getHandler();
        if (mainHandler == null) {
            mainHandler = new Handler();
        }
        if (isAutoPage) {
            startTimer();
        } else {
            stopTimer();
        }
    }

    public void onResume() {
        if (isAutoPage) {
            startTimer();
        }
    }

    ;

    public void onPause() {
        if (isAutoPage) {
            stopTimer();
        }
    }

    /**************************************************** 计时器start *********************************************************/
    /**
     * 轮播时间间隔
     */
    private long STEP = 5000;
    private boolean isAutoPage = false;
    Handler mainHandler;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            autoPage();
            startTimer();
        }
    };

    /**
     * 设置轮播间隔时间
     *
     * @param step 毫秒数ms
     */
    public void setStepPageTime(long step) {
        STEP = step;
    }

    /**
     * 开始计时
     */
    public void startTimer() {
        if (isAutoPage) {
            stopTimer();
            if(mainHandler != null){
                mainHandler.postDelayed(runnable, STEP);
            }
        }
    }

    /**
     * 结束计时
     */
    public void stopTimer() {
        if(mainHandler != null){
            mainHandler.removeCallbacks(runnable);
        }

    }
    /**************************************************** 计时器 end *********************************************************/

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopTimer();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startTimer();
    }
}
