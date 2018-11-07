package me.relex.recyclerpager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;

public abstract class FragmentPageSnapAdapter extends FragmentRecyclerAdapter {

    @Nullable private SnapHelper mSnapHelper;
    private final InternalScrollListener mScrollListener;

    public FragmentPageSnapAdapter(FragmentManager fm) {
        super(fm);
        mScrollListener = new InternalScrollListener();
    }

    @Override public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.OnFlingListener flingListener = recyclerView.getOnFlingListener();
        if (flingListener instanceof SnapHelper) {
            mSnapHelper = (SnapHelper) flingListener;
        }
        recyclerView.addOnScrollListener(mScrollListener);
    }

    @Override public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        recyclerView.removeOnScrollListener(mScrollListener);
    }

    private class InternalScrollListener extends RecyclerView.OnScrollListener {

        @Nullable private Fragment mCurrentFragment;

        @Override public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            // scroll to position
            if (dx == 0 && dy == 0) {
                setPrimaryItem(recyclerView);
            }
        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                setPrimaryItem(recyclerView);
            }
        }

        private void setPrimaryItem(@NonNull RecyclerView recyclerView) {

            if (mSnapHelper == null) {
                RecyclerView.OnFlingListener flingListener = recyclerView.getOnFlingListener();
                if (flingListener instanceof SnapHelper) {
                    mSnapHelper = (SnapHelper) flingListener;
                }
            }

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager == null || mSnapHelper == null) {
                return;
            }

            View snapView = mSnapHelper.findSnapView(layoutManager);
            RecyclerView.ViewHolder viewHolder = null;
            Fragment fragment = null;
            if (snapView != null) {
                viewHolder = recyclerView.findContainingViewHolder(snapView);
            }
            if (viewHolder instanceof FragmentViewHolder) {
                fragment = ((FragmentViewHolder) viewHolder).currentFragment;
            }
            if (fragment == null) {
                return;
            }

            if (fragment != mCurrentFragment) {
                if (mCurrentFragment != null && mCurrentFragment.getUserVisibleHint()) {
                    mCurrentFragment.setMenuVisibility(false);
                    mCurrentFragment.setUserVisibleHint(false);
                }
                if (!fragment.getUserVisibleHint()) {
                    fragment.setMenuVisibility(true);
                    fragment.setUserVisibleHint(true);
                }
                mCurrentFragment = fragment;
            }
        }
    }

    /**
     * Attach Fragment,
     */
    @Override public void onViewAttachedToWindow(@NonNull FragmentViewHolder holder) {
        String name = makeFragmentName(holder.itemView.getId(), holder.getAdapterPosition());
        Fragment fragment = mFragmentManager.findFragmentByTag(name);

        if (fragment != null && fragment == holder.currentFragment) {
            // Nothing Changed
        } else {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

            if (fragment != null) {
                fragmentTransaction.attach(fragment);
            } else {
                fragment = getItem(holder.getAdapterPosition());
                fragmentTransaction.add(holder.itemView.getId(), fragment, name);
            }
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);

            fragmentTransaction.commitNowAllowingStateLoss();

            if (holder.currentFragment != null && holder.currentFragment.getUserVisibleHint()) {
                holder.currentFragment.setMenuVisibility(false);
                holder.currentFragment.setUserVisibleHint(false);
            }

            holder.currentFragment = fragment;
        }
    }
}