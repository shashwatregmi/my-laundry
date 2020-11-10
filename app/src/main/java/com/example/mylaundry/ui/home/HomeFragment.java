package com.example.mylaundry.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylaundry.BookActivity;
import com.example.mylaundry.MachineItemList;
import com.example.mylaundry.MachineListAdapter;
import com.example.mylaundry.MainActivity;
import com.example.mylaundry.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;

import javax.crypto.Mac;

public class HomeFragment extends Fragment {
    private RecyclerView view;
    private MachineListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<MachineItemList> machineItemList = new ArrayList<>();

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);



        machineItemList.add(new MachineItemList(R.drawable.washer, "Washer #1", "This washer is currently available!"));
        machineItemList.add(new MachineItemList(R.drawable.washer, "Washer #2", "This washer is currently available!"));

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this.getContext());
        if (signInAccount != null){
            System.out.println(signInAccount.getDisplayName());
            System.out.println(signInAccount);
        }

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        view = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        view.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new MachineListAdapter(machineItemList);

        view.setLayoutManager(layoutManager);
        view.setAdapter(adapter);

        adapter.setOnItemClickListener(new MachineListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), BookActivity.class);
                intent.putExtra("Machine", machineItemList.get(position));
                intent.putExtra("Number", position + 1);

                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((Activity) rootView.getContext()).getWindow().setStatusBarColor(ContextCompat.getColor(rootView.getContext(), R.color.main));
        }

        return rootView;
    }
}