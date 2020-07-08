package com.example.mylaundry.ui.calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mylaundry.BookActivity;
import com.example.mylaundry.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarFragment extends Fragment {

    CalendarView calendar;
    Button today;
    Button book;
    TimePickerDialog timePicker;
    String TODAY = "";
    private CalendarViewModel calendarViewModel;

    @RequiresApi(api = Build.VERSION_CODES.N)
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

        calendar = (CalendarView) root.findViewById(R.id.calendarView);
        calendar.setMinDate(System.currentTimeMillis() - 1000);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        TODAY = dateFormat.format(date);
        final TextView todayView = root.findViewById(R.id.temptext);

        todayView.setText(TODAY);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // TODO: we need to display the bookings for this day here by pulling from db..
                String temp = dayOfMonth + "/" + (month + 1) + "/" + year;
                todayView.setText(temp);
            }
        });

        today = (Button) root.findViewById(R.id.today);
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.setDate(System.currentTimeMillis());
                todayView.setText(TODAY);
                // TODO: need to update the bookings for today date on list now.
            }
        });

        book = (Button) root.findViewById(R.id.bookbtn);
        book.setOnClickListener(new View.OnClickListener() {
            Calendar timeNow = Calendar.getInstance();
            @Override
            public void onClick(View v) {
                TimePickerDialog timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String tempTime = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                        todayView.setText(tempTime);
                        // TODO: need to do booking stuff here.
                    }
                }, timeNow.get(Calendar.HOUR_OF_DAY), timeNow.get(Calendar.MINUTE), false);
                timePicker.show();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((Activity) root.getContext()).getWindow().setStatusBarColor(ContextCompat.getColor(root.getContext(), R.color.main));
        }

        return root;
    }
}