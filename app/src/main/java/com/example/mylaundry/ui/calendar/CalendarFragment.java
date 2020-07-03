package com.example.mylaundry.ui.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mylaundry.R;

import java.util.ArrayList;

public class CalendarFragment extends Fragment {

    private CalendarViewModel calendarViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel =
                ViewModelProviders.of(this).get(CalendarViewModel.class);
        ArrayList<String> machineList = new ArrayList<>();

        machineList.add("Washer #1");
        machineList.add("Washer #2");

        //TODO: pull data from DB and place into this array list..

        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        Spinner spinner = (Spinner) root.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinnerlayout,
                machineList);
        adapter.setDropDownViewResource(R.layout.spinnerdropdownlayout);
        spinner.setAdapter(adapter);
        return root;
    }
}