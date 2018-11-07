package me.relex.recyclerpager;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

class FragmentViewHolder extends RecyclerView.ViewHolder {
    Fragment currentFragment;

    static FragmentViewHolder createViewHolder(@NonNull ViewGroup parent) {
        FrameLayout frameLayout = new FrameLayout(parent.getContext());
        frameLayout.setLayoutParams(
                new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.MATCH_PARENT));
        return new FragmentViewHolder(frameLayout);
    }

    private FragmentViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}