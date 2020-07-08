package com.example.mylaundry.ui.settings;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mylaundry.BookActivity;
import com.example.mylaundry.MainActivity;
import com.example.mylaundry.R;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private MainActivity main;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((Activity) root.getContext()).getWindow().setStatusBarColor(ContextCompat.getColor(root.getContext(), R.color.colorAccent));
        }
        return root;
    }
}