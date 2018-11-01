package me.relex.recyclerpager.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import me.relex.recyclerpager.FragmentRecyclerAdapter;

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
