package me.relex.recyclerpager.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import me.relex.recyclerpager.FragmentRecyclerAdapter;
import me.relex.recyclerpager.SnapPageScrollListener;
import me.relex.smarttablayout.SmartTabLayout2;

public class MainActivity extends AppCompatActivity {

    private TestAdapter mAdapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        mAdapter = new TestAdapter(getSupportFragmentManager());
        recyclerView.setAdapter(mAdapter);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        SmartTabLayout2 smartTabLayout2 = findViewById(R.id.tab_layout);
        smartTabLayout2.attachToRecyclerView(recyclerView, snapHelper);
        mAdapter.registerAdapterDataObserver(smartTabLayout2.getAdapterDataObserver());

        recyclerView.addOnScrollListener(new SnapPageScrollListener(snapHelper) {
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

        @Override public Fragment getItem(int position) {
            return PageFragment.newInstance(position);
        }

        @Override public int getItemCount() {
            return count;
        }

        @Override public CharSequence getPageTitle(int position) {
            return "Title-" + position;
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
