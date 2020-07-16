package com.example.mylaundry;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookActivity extends AppCompatActivity {

    CalendarView calendar;
    Button today;
    Button book;
    TimePickerDialog timePicker;
    String TODAY = "";
    FirebaseFirestore db;
    private RecyclerView bookingRecyclerView;
    private RecyclerView.Adapter bookingAdapter;
    private RecyclerView.LayoutManager bookingLayoutManager;
    private ArrayList<Booking> dbBookingList = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        TODAY = dateFormat.format(date);
        final Intent intent = getIntent();
        final int washerNumber = intent.getIntExtra("Number", 0);

        //TODO: add database pulled bookings here....
        // will need to filter by current date and machine...
        // repeat this process when date changes onClick below...
        db.collection("bookings").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> tempBookings = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d: tempBookings){
                                Booking pulledBooking = d.toObject(Booking.class);
                                if (pulledBooking.getDate().equals(TODAY) && pulledBooking.getWasher() == washerNumber){
                                    dbBookingList.add(pulledBooking);
                                }
                            }
                            bookingAdapter.notifyDataSetChanged();
                        }
                    }
                });

        bookingRecyclerView = findViewById(R.id.bookings);
        bookingLayoutManager = new LinearLayoutManager(this);
        bookingAdapter = new BookItemAdapter(dbBookingList);

        bookingRecyclerView.setLayoutManager(bookingLayoutManager);
        bookingRecyclerView.setAdapter(bookingAdapter);

        MachineItemList machine = intent.getParcelableExtra("Machine");
        assert machine != null;
        String title = machine.getTitle();

        TextView txtview = findViewById(R.id.textView);
        txtview.setText(title);
        calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setMinDate(System.currentTimeMillis() - 1000);


        final TextView todayView = findViewById(R.id.temptext);

        todayView.setText(TODAY);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // TODO: we need to display the bookings for this day here by pulling from db..
                String temp = "";
                if (dayOfMonth >= 1 && dayOfMonth <= 9){
                    temp = "0" + dayOfMonth;
                } else {
                    temp = String.valueOf(dayOfMonth);
                }

                if (month >= 0 && month < 9){
                    temp = temp + "/0" + (month + 1) + "/" + year;
                } else{
                    temp = temp + "/" + (month + 1) + "/" + year;
                }
                todayView.setText(temp);
            }
        });

        today = (Button) findViewById(R.id.today);
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.setDate(System.currentTimeMillis());
                todayView.setText(TODAY);
                // TODO: need to update the bookings for today date on list now.
            }
        });

        book = (Button) findViewById(R.id.bookbtn);
        book.setOnClickListener(new View.OnClickListener() {
            Calendar timeNow = Calendar.getInstance();
            @Override
            public void onClick(View v) {
                TimePickerDialog timePicker = new TimePickerDialog(BookActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        CollectionReference dbBooking = db.collection("bookings");
                        int bookingEnd = 0;
                        if (hourOfDay != 24){
                            bookingEnd = hourOfDay+1;
                        }
                        Booking booking = new Booking(washerNumber, hourOfDay, minute, String.valueOf(todayView.getText()), bookingEnd);
                        Boolean flagConflict = false;
                        for (Booking b : dbBookingList){
                            if ((b.getEndHr() == hourOfDay && b.getMinute() >= minute) || b.getHour() == hourOfDay){
                                flagConflict = true;
                            }
                        }

                        if (!flagConflict){
                            dbBooking.add(booking)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(BookActivity.this, "Code will be provided here", Toast.LENGTH_LONG).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(BookActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                            dbBookingList.add(booking);
                            bookingAdapter.notifyDataSetChanged();
                        } else {
                            TimeConflictDialog conflictDialog = new TimeConflictDialog();
                            conflictDialog.show(getSupportFragmentManager(), "Time Conflict Error");
                            book.callOnClick();
                        }

                    }
                }, timeNow.get(Calendar.HOUR_OF_DAY), timeNow.get(Calendar.MINUTE), false);
                timePicker.show();
            }
        });
    }
}