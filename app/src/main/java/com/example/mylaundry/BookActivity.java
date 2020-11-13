package com.example.mylaundry;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.SecureRandom;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

@RequiresApi(api = Build.VERSION_CODES.N)
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
    private Date currentTime = null;
    private DateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private GoogleSignInAccount signInAccount;


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
        getCurrentTime();
        signInAccount = GoogleSignIn.getLastSignedInAccount(BookActivity.this);

        //TODO: add database pulled bookings here....
        // will need to filter by current date and machine...
        // repeat this process when date changes onClick below...
        pullData(TODAY, washerNumber);
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
                dbBookingList.clear();
                bookingAdapter.notifyDataSetChanged();
                pullData(temp, washerNumber);
            }
        });

        today = (Button) findViewById(R.id.today);
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.setDate(System.currentTimeMillis());
                todayView.setText(TODAY);
                dbBookingList.clear();
                bookingAdapter.notifyDataSetChanged();
                pullData(TODAY, washerNumber);
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
                        final int pinCode;
                        CollectionReference dbBooking = db.collection("bookings");
                        int bookingEnd = 0;
                        if (hourOfDay != 24){
                            bookingEnd = hourOfDay+1;
                        }
                        SecureRandom random = new SecureRandom();
                        pinCode = random.nextInt(100000);

                        Boolean earlyTimeError = false;
                        Date date = new Date();
                        Calendar calendar = GregorianCalendar.getInstance();
                        calendar.setTime(date);
                        int currHour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
                        int currMin = calendar.get(Calendar.MINUTE);

                        final Booking booking = new Booking(washerNumber, hourOfDay, minute,
                                String.valueOf(todayView.getText()), bookingEnd, pinCode, signInAccount.getId());
                        boolean flagConflict = false;
                        for (Booking b : dbBookingList){
                            if ((b.getEndHr() == hourOfDay && b.getMinute() >= minute) || b.getHour() == hourOfDay) {
                                flagConflict = true;
                                break;
                            }
                        }

                        if ((hourOfDay < currHour && todayView.getText() == TODAY) ||
                                (hourOfDay == currHour && minute < currMin && todayView.getText() == TODAY)){
                            earlyTimeError = true;
                        }

                        if (!flagConflict && !earlyTimeError){
                            dbBooking.add(booking)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @SuppressLint("DefaultLocale")
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(BookActivity.this);
                                            builder.setCancelable(false);
                                            builder.setMessage("Washer #" + booking.getWasher() + " has been successfully booked for 1 hour starting " +
                                                    String.format("%02d:%02d", booking.getHour(), booking.getMinute()) + " on " + booking.getDate() +
                                                    ".\n\n" + "Your pin-code is " + pinCode);
                                            builder.setTitle("Successfully Booked!");
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                            builder.show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(BookActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                            dbBookingList.add(booking);
                            sort();
                            bookingAdapter.notifyDataSetChanged();
                        } else if (earlyTimeError) {
                            EarlyTimeError conflictDialog = new EarlyTimeError();
                            conflictDialog.show(getSupportFragmentManager(), "Time Conflict Error");
                            book.callOnClick();
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

    private void getCurrentTime(){
        currentTime = Calendar.getInstance().getTime();
        String temp = timeFormat.format(currentTime);
        try {
            currentTime = timeFormat.parse(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void sort(){
        Collections.sort(dbBookingList, new Comparator<Booking>(){
            public int compare(Booking b1, Booking b2)
            {
                if (b1.getHour() > (b2.getHour())) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    private void pullData(final String date, final int washerNumber){
        db.collection("bookings").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> tempBookings = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d: tempBookings){
                                Booking pulledBooking = d.toObject(Booking.class);
                                if (pulledBooking.getDate().equals(date) && pulledBooking.getWasher() == washerNumber  &&
                                        pulledBooking.getUserCode().equals(signInAccount.getId())){
                                    Date bookingTime = null;
                                    try {
                                        bookingTime = timeFormat.parse(pulledBooking.getEndHr() + ":" + pulledBooking.getMinute());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if (bookingTime.after(currentTime)){
                                        dbBookingList.add(pulledBooking);
                                    }
                                }
                            }
                            sort();
                            bookingAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}