package me.relex.smarttablayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.ogaclejapan.smarttablayout.BaseSmartTabLayout;
import me.relex.recyclerpager.PageRecyclerAdapter;
import me.relex.recyclerpager.SnapPageScrollListener;

/**
 * To be used with RecyclerView to provide a tab indicator component which give constant feedback as
 * to
 * the user's scroll progress.
 * <p>
 * To use the component, simply add it to your view hierarchy. Then in your
 * {@link android.app.Activity} or {@link android.app.Fragment}, {@link
 * android.support.v4.app.Fragment} call
 * {@link #attachToRecyclerView(RecyclerView, SnapHelper)} providing it the ViewPager this layout
 * is being used for.
 * <p>
 * The colors can be customized in two ways. The first and simplest is to provide an array of
 * colors
 * via {@link #setSelectedIndicatorColors(int...)} and {@link #setDividerColors(int...)}. The
 * alternative is via the {@link TabColorizer} interface which provides you complete control over
 * which color is used for any individual position.
 * <p>
 * The views used as tabs can be customized by calling {@link #setCustomTabView(int, int)},
 * providing the layout ID of your custom layout.
 * <p>
 * Forked from Google Samples &gt; SlidingTabsBasic &gt;
 * <a href="https://developer.android.com/samples/SlidingTabsBasic/src/com.example.android.common/view/SlidingTabLayout.html">SlidingTabLayout</a>
 */
public class SmartTabLayout2 extends BaseSmartTabLayout {

    private InternalOnScrollListener mInternalOnScrollListener;
    private RecyclerView mRecyclerView;
    private SnapHelper mSnapHelper;

    public SmartTabLayout2(Context context) {
        super(context);
    }

    public SmartTabLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartTabLayout2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void attachToRecyclerView(@NonNull RecyclerView recyclerView,
            @NonNull SnapHelper snapHelper) {
        mRecyclerView = recyclerView;
        mSnapHelper = snapHelper;
        mInternalOnScrollListener = new InternalOnScrollListener(snapHelper);
        mRecyclerView.addOnScrollListener(mInternalOnScrollListener);
        populateTabStrip();
    }

    public int getSnapPosition(@Nullable RecyclerView.LayoutManager layoutManager) {
        if (layoutManager == null) {
            return RecyclerView.NO_POSITION;
        }
        View snapView = mSnapHelper.findSnapView(layoutManager);
        if (snapView == null) {
            return RecyclerView.NO_POSITION;
        }
        return layoutManager.getPosition(snapView);
    }

    @Override protected void populateTabStrip() {
        tabStrip.removeAllViews();
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (!(adapter instanceof PageRecyclerAdapter)) {
            return;
        }

        int count = adapter.getItemCount();

        for (int i = 0; i < count; i++) {
            CharSequence title = ((PageRecyclerAdapter) adapter).getPageTitle(i);

            final View tabView = (tabProvider == null) ? createDefaultTabView(title)
                    : tabProvider.createTabView(tabStrip, i, title);

            if (tabView == null) {
                throw new IllegalStateException("tabView is null.");
            }

            if (distributeEvenly) {
                LinearLayout.LayoutParams lp =
                        (LinearLayout.LayoutParams) tabView.getLayoutParams();
                lp.width = 0;
                lp.weight = 1;
            }

            if (internalTabClickListener != null) {
                tabView.setOnClickListener(internalTabClickListener);
            }

            tabStrip.addView(tabView);

            if (i == getCurrentItem()) {
                tabView.setSelected(true);
            }
        }
    }

    @Override protected int getCurrentItem() {
        if (mRecyclerView == null) {
            return RecyclerView.NO_POSITION;
        }
        return getSnapPosition(mRecyclerView.getLayoutManager());
    }

    @Override protected void setCurrentItem(int position) {
        if (mRecyclerView != null) {
            mRecyclerView.scrollToPosition(position);
        }
    }

    private class InternalOnScrollListener extends SnapPageScrollListener {

        InternalOnScrollListener(SnapHelper snapHelper) {
            super(snapHelper);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            tabStrip.onViewPagerPageChanged(position, positionOffset);
            scrollToTab(position, positionOffset);
        }

        @Override public void onPageSelected(int position) {
            super.onPageSelected(position);

            tabStrip.onViewPagerPageChanged(position, 0f);
            scrollToTab(position, 0);
        }
    }

    private final RecyclerView.AdapterDataObserver mAdapterDataObserver =
            new RecyclerView.AdapterDataObserver() {
                @Override public void onChanged() {
                    super.onChanged();
                    if (mRecyclerView == null) {
                        return;
                    }

                    RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
                    int newCount = adapter != null ? adapter.getItemCount() : 0;
                    int currentCount = tabStrip.getChildCount();
                    if (newCount == currentCount) {
                        // No change
                        return;
                    } else if (mInternalOnScrollListener.currentPosition < newCount) {
                        mInternalOnScrollListener.currentPosition =
                                getSnapPosition(mRecyclerView.getLayoutManager());
                    } else {
                        mInternalOnScrollListener.currentPosition = RecyclerView.NO_POSITION;
                    }
                    populateTabStrip();
                }

                @Override public void onItemRangeChanged(int positionStart, int itemCount) {
                    super.onItemRangeChanged(positionStart, itemCount);
                    onChanged();
                }

                @Override public void onItemRangeChanged(int positionStart, int itemCount,
                        @Nullable Object payload) {
                    super.onItemRangeChanged(positionStart, itemCount, payload);
                    onChanged();
                }

                @Override public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    onChanged();
                }

                @Override public void onItemRangeRemoved(int positionStart, int itemCount) {
                    super.onItemRangeRemoved(positionStart, itemCount);
                    onChanged();
                }

                @Override
                public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                    super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                    onChanged();
                }
            };

    public RecyclerView.AdapterDataObserver getAdapterDataObserver() {
        return mAdapterDataObserver;
    }
}
