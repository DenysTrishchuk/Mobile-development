package ua.kpi.comsys.iv8127.android_prog.ui.lab3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import ua.kpi.comsys.iv8127.android_prog.R;

public class Lab3Fragment extends Fragment {

    private Lab3ViewModel lab3ViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        lab3ViewModel =
                new ViewModelProvider(this).get(Lab3ViewModel.class);
        View root = inflater.inflate(R.layout.fragment_lab3, container, false);
        final TextView textView = root.findViewById(R.id.text_lab3);
        lab3ViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}