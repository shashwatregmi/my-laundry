package com.example.mylaundry;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

public class BookActivity extends AppCompatActivity {

    CalendarView calendar;
    Button today;
    String TODAY = "";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Intent intent = getIntent();
        MachineItemList machine = intent.getParcelableExtra("Machine");
        String title = machine.getTitle();

        TextView txtview = findViewById(R.id.textView);
        txtview.setText(title);
        calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setMinDate(System.currentTimeMillis() - 1000);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        TODAY = dateFormat.format(date);
        final TextView todayView = findViewById(R.id.temptext);

        todayView.setText(TODAY);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // we need to display the bookings for this day here by pulling from db..
                String temp = dayOfMonth + "/" + (month + 1) + "/" + year;
                todayView.setText(temp);
            }
        });

        today = (Button) findViewById(R.id.today);
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.setDate(System.currentTimeMillis());
                todayView.setText(TODAY);
                // need to update the bookings for today date on list now.
            }
        });
    }
}