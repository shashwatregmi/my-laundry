package com.example.mylaundry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class BookActivity extends AppCompatActivity {

    CalendarView calendar;
    ImageButton today;

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
        
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // we need to display the bookings for this day here by pulling from db..

                TextView temp = findViewById(R.id.temptext);
                temp.setText(dayOfMonth + "/" + month + "/" + year);
            }
        });

        today = (ImageButton) findViewById(R.id.todayButton);
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.setDate(System.currentTimeMillis());
                // need to update the bookings for today date on list now.
            }
        });
    }
}