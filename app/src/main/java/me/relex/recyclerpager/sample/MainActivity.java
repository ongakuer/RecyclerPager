package me.relex.recyclerpager.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import me.relex.recyclerpager.FragmentRecyclerAdapter;
import me.relex.recyclerpager.FragmentViewHolder;
import me.relex.recyclerpager.SnapPageScrollListener;
import me.relex.smarttablayout.SmartTabLayout2;

public class MainActivity extends AppCompatActivity {

    private TestAdapter mAdapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false) {

                    @Override public void stopIgnoringView(@NonNull View view) {
                        super.stopIgnoringView(view);
                    }

                    @Override public void ignoreView(@NonNull View view) {
                        super.ignoreView(view);
                    }
                };

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new TestAdapter(getSupportFragmentManager());
        recyclerView.setAdapter(mAdapter);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        SmartTabLayout2 smartTabLayout2 = findViewById(R.id.tab_layout);
        smartTabLayout2.attachToRecyclerView(recyclerView);
        mAdapter.registerAdapterDataObserver(smartTabLayout2.getAdapterDataObserver());

        recyclerView.addOnScrollListener(new SnapPageScrollListener() {
            @Override public void onPageScrolled(int position, float positionOffset,
                    int positionOffsetPixels) {
                Log.i("SnapPageScrollListener", "onPageScrolled = "
                        + position
                        + " positionOffset = "
                        + positionOffset
                        + " positionOffsetPixels = "
                        + positionOffsetPixels);
            }

            @Override public void onPageSelected(int position) {
                Log.w("SnapPageScrollListener", "onPageSelected = " + position);
            }
        });

        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mAdapter.add();
            }
        });

        findViewById(R.id.remove_button).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mAdapter.remove();
            }
        });
    }

    private class TestAdapter extends FragmentRecyclerAdapter {

        private int count;

        TestAdapter(FragmentManager fm) {
            super(fm);
            count = 3;
        }

        @NonNull @Override
        public FragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            FragmentViewHolder vh = super.onCreateViewHolder(parent, viewType);
            return vh;
        }

        @Override public Fragment getItem(int position) {
            return PageFragment.newInstance(position);
        }

        @Override public int getItemCount() {
            return count;
        }

        @Override public CharSequence getPageTitle(int position) {
            return "Title-" + position;
        }

        @Override public void onViewAttachedToWindow(@NonNull FragmentViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            Log.e("MainActivity", "onViewAttachedToWindow = " + holder.getLayoutPosition());
        }

        @Override public void onViewDetachedFromWindow(@NonNull FragmentViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
            Log.e("MainActivity", "onViewDetachedFromWindow " + holder.getLayoutPosition());
        }

        @Override public void onViewRecycled(@NonNull FragmentViewHolder holder) {
            super.onViewRecycled(holder);
            Log.e("MainActivity", "onViewRecycled = " + holder.getLayoutPosition());
        }

        public void add() {
            int position = count;
            count++;
            notifyItemInserted(position);
        }

        public void remove() {
            if (count == 0) {
                return;
            }
            count--;
            notifyItemRemoved(count);
        }
    }
}
