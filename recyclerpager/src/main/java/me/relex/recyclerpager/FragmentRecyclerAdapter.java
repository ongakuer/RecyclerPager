package me.relex.recyclerpager;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public abstract class FragmentRecyclerAdapter
        extends PagerRecyclerAdapter<FragmentRecyclerAdapter.FragmentViewHolder> {

    private final int mBaseContainerId;
    private final FragmentManager mFragmentManager;

    public FragmentRecyclerAdapter(FragmentManager fm) {
        mFragmentManager = fm;
        mBaseContainerId = ViewCompat.generateViewId();
    }

    @NonNull @Override
    public FragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FrameLayout frameLayout = new FrameLayout(parent.getContext());
        frameLayout.setLayoutParams(
                new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.MATCH_PARENT));
        return new FragmentViewHolder(frameLayout);
    }

    /**
     * Set ContainerId
     */
    @Override public void onBindViewHolder(@NonNull FragmentViewHolder holder, int position) {
        holder.itemView.setId(mBaseContainerId + position);
    }

    /**
     * Attach Fragment && Fragment setUserVisibleHint true
     */
    @Override public void onViewAttachedToWindow(@NonNull FragmentViewHolder holder) {
        super.onViewAttachedToWindow(holder);

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

            fragmentTransaction.commitNowAllowingStateLoss();

            if (holder.currentFragment != null) {
                holder.currentFragment.setMenuVisibility(false);
                holder.currentFragment.setUserVisibleHint(false);
            }

            fragment.setMenuVisibility(true);
            fragment.setUserVisibleHint(true);
            holder.currentFragment = fragment;
        }
    }

    /**
     * Fragment setUserVisibleHint false
     */
    @Override public void onViewDetachedFromWindow(@NonNull FragmentViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder.currentFragment != null) {
            holder.currentFragment.setMenuVisibility(false);
            holder.currentFragment.setUserVisibleHint(false);
        }
    }

    /**
     * Detach Fragment
     */
    @Override public void onViewRecycled(@NonNull FragmentViewHolder holder) {
        super.onViewRecycled(holder);

        if (holder.currentFragment != null) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.detach(holder.currentFragment);
            fragmentTransaction.commitNowAllowingStateLoss();
        }
        holder.currentFragment = null;
    }

    private static String makeFragmentName(int viewId, int position) {
        return "fragment:adapter:" + viewId + ":" + position;
    }

    /**
     * Return the Fragment associated with a specified position.
     */
    public abstract Fragment getItem(int position);

    class FragmentViewHolder extends RecyclerView.ViewHolder {
        Fragment currentFragment;

        FragmentViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}