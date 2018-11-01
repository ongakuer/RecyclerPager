package me.relex.recyclerpager;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

public abstract class PageRecyclerAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    @Nullable public CharSequence getPageTitle(int position) {
        return null;
    }
}