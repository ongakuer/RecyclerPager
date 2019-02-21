package me.relex.recyclerpager;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.view.ViewGroup;

public abstract class FragmentRecyclerAdapter extends PageRecyclerAdapter<FragmentViewHolder> {
    protected final FragmentManager mFragmentManager;
    private final int mBaseContainerId;

    public FragmentRecyclerAdapter(FragmentManager fm) {
        mFragmentManager = fm;
        mBaseContainerId = ViewCompat.generateViewId();
    }

    @NonNull @Override
    public FragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return FragmentViewHolder.createViewHolder(parent);
    }

    /**
     * Set ContainerId
     */
    @Override public void onBindViewHolder(@NonNull FragmentViewHolder holder, int position) {
        holder.itemView.setId(mBaseContainerId + position);
    }

    /**
     * Attach Fragment
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
            fragmentTransaction.commitNowAllowingStateLoss();

            if (holder.currentFragment != null && holder.currentFragment.getUserVisibleHint()) {
                holder.currentFragment.setMenuVisibility(false);
                holder.currentFragment.setUserVisibleHint(false);
            }

            holder.currentFragment = fragment;
            if (!fragment.getUserVisibleHint()) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
        }
    }

    /**
     * Detach Fragment
     */
    @Override public void onViewDetachedFromWindow(@NonNull FragmentViewHolder holder) {
        if (holder.currentFragment != null) {
            if (holder.currentFragment.getUserVisibleHint()) {
                holder.currentFragment.setMenuVisibility(false);
                holder.currentFragment.setUserVisibleHint(false);
            }
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.detach(holder.currentFragment);
            fragmentTransaction.commitNowAllowingStateLoss();
        }
        holder.currentFragment = null;
    }

    /**
     * Detach Fragment
     */
    @Override public void onViewRecycled(@NonNull FragmentViewHolder holder) {
        if (holder.currentFragment != null) {
            if (holder.currentFragment.getUserVisibleHint()) {
                holder.currentFragment.setMenuVisibility(false);
                holder.currentFragment.setUserVisibleHint(false);
            }
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.detach(holder.currentFragment);
            fragmentTransaction.commitNowAllowingStateLoss();
        }
        holder.currentFragment = null;
    }

    static String makeFragmentName(int viewId, int position) {
        return "fragment:adapter:" + viewId + ":" + position;
    }

    /**
     * @param position item position
     * @return Return the Fragment associated with a specified position.
     */
    public abstract Fragment getItem(int position);
}