package com.example.mylaundry.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylaundry.MachineItemList;
import com.example.mylaundry.MachineListAdapter;
import com.example.mylaundry.R;

import java.util.ArrayList;

import javax.crypto.Mac;

public class HomeFragment extends Fragment {
    private RecyclerView view;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);


        ArrayList<MachineItemList> machineItemList = new ArrayList<>();

        machineItemList.add(new MachineItemList(R.drawable.washer, "Washer #1", "This washer is currently available!"));
        machineItemList.add(new MachineItemList(R.drawable.washer, "Washer #2", "This washer is currently available!"));


        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        view = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        view.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new MachineListAdapter(machineItemList);

        view.setLayoutManager(layoutManager);
        view.setAdapter(adapter);


        return rootView;
    }
}