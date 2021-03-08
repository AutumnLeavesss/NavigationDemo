package com.leaves.navigationdemo.ui.fourth;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.leaves.navigationannotation.FragmentDestination;
import com.leaves.navigationdemo.R;

@FragmentDestination(pageUrl = "main/tabs/fourth", asStart = false)
public class FourthFragment extends Fragment {

    private FourthViewModel fourthViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.e("Header-OnCreateView", "onCreate: This is Fourth!"  );

        fourthViewModel =
                new ViewModelProvider(this).get(FourthViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        fourthViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Header-OnCreate", "onCreate: This is Fourth!"  );
    }
}