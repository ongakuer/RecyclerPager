package me.relex.recyclerpager.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Random;

public class PageFragment extends Fragment {
    private ItemAdapter mItemAdapter;

    public static PageFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("position", position);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItemAdapter = new ItemAdapter();
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        int position = 0;
        if (bundle != null) {
            position = bundle.getInt("position");
        }
        Random random = new Random(position);
        TextView textView = view.findViewById(R.id.text_view);
        textView.setText(String.valueOf(position));
        view.setBackgroundColor(0xff000000 | random.nextInt(0x00ffffff));

        initRecyclerView(view);
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mItemAdapter);
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

        @NonNull @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
            return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(android.R.layout.simple_list_item_1, viewGroup, false));
        }

        @Override public void onBindViewHolder(@NonNull ItemViewHolder viewHolder, int position) {
            ((TextView) viewHolder.itemView).setText("position : " + position);
        }

        @Override public int getItemCount() {
            return 20;
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            ItemViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}
