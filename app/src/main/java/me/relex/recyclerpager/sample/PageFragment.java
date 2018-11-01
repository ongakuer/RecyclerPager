package me.relex.recyclerpager.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Random;

public class PageFragment extends Fragment {

    public static PageFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("position", position);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
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
    }
}
