package com.example.mylaundry.ui.calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylaundry.BookActivity;
import com.example.mylaundry.BookItemAdapter;
import com.example.mylaundry.Booking;
import com.example.mylaundry.EarlyTimeError;
import com.example.mylaundry.R;
import com.example.mylaundry.TimeConflictDialog;
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

@RequiresApi(api = Build.VERSION_CODES.N)
public class CalendarFragment extends Fragment {

    CalendarView calendar;
    Button today;
    Button book;
    TimePickerDialog timePicker;
    String TODAY = "";
    private CalendarViewModel calendarViewModel;
    FirebaseFirestore db;
    Spinner dropdown;
    int spinnerposition;
    private RecyclerView bookingRecyclerView;
    private RecyclerView.Adapter bookingAdapter;
    private RecyclerView.LayoutManager bookingLayoutManager;
    private ArrayList<Booking> dbBookingList = new ArrayList<>();
    private Date currentTime = null;
    private DateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private GoogleSignInAccount signInAccount;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel =
                ViewModelProviders.of(this).get(CalendarViewModel.class);
        ArrayList<String> machineList = new ArrayList<>();

        machineList.add("Washer #1");
        machineList.add("Washer #2");
        db = FirebaseFirestore.getInstance();

        getCurrentTime();
        signInAccount = GoogleSignIn.getLastSignedInAccount(getContext());

        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        Spinner spinner = (Spinner) root.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinnerlayout,
                machineList);
        adapter.setDropDownViewResource(R.layout.spinnerdropdownlayout);
        spinner.setAdapter(adapter);

        calendar = (CalendarView) root.findViewById(R.id.calendarView);
        calendar.setMinDate(System.currentTimeMillis() - 1000);


        bookingRecyclerView = root.findViewById(R.id.bookings);
        bookingLayoutManager = new LinearLayoutManager(getContext());
        bookingAdapter = new BookItemAdapter(dbBookingList);

        bookingRecyclerView.setLayoutManager(bookingLayoutManager);
        bookingRecyclerView.setAdapter(bookingAdapter);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        TODAY = dateFormat.format(date);
        final TextView todayView = root.findViewById(R.id.temptext);

        todayView.setText(TODAY);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                spinnerposition = position;
                dbBookingList.clear();
                pullBookings(TODAY);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });



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
                dbBookingList.clear();
                bookingAdapter.notifyDataSetChanged();
                pullBookings(temp);
            }
        });

        today = (Button) root.findViewById(R.id.today);
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.setDate(System.currentTimeMillis());
                todayView.setText(TODAY);
                dbBookingList.clear();
                bookingAdapter.notifyDataSetChanged();
                pullBookings(TODAY);
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

                        final Booking booking = new Booking(spinnerposition + 1, hourOfDay, minute,
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
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                            dbBookingList.add(booking);
                            sort();
                            bookingAdapter.notifyDataSetChanged();
                        } else if (earlyTimeError) {
                            EarlyTimeError conflictDialog = new EarlyTimeError();
                            conflictDialog.show(getActivity().getSupportFragmentManager(), "Time Conflict Error");
                            book.callOnClick();
                        } else {
                            TimeConflictDialog conflictDialog = new TimeConflictDialog();
                            conflictDialog.show(getActivity().getSupportFragmentManager(), "Time Conflict Error");
                            book.callOnClick();
                        }
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

    private void getCurrentTime(){
        currentTime = Calendar.getInstance().getTime();
        String temp = timeFormat.format(currentTime);
        try {
            currentTime = timeFormat.parse(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void pullBookings(final String date){
        db.collection("bookings").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> tempBookings = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d: tempBookings) {
                                Booking pulledBooking = d.toObject(Booking.class);
                                if (pulledBooking.getDate().equals(date) && pulledBooking.getWasher() == spinnerposition + 1 &&
                                pulledBooking.getUserCode().equals(signInAccount.getId())) {
                                    Date bookingTime = null;
                                    try {
                                        bookingTime = timeFormat.parse(pulledBooking.getEndHr() + ":" + pulledBooking.getMinute());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if (bookingTime.after(currentTime)) {
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
}

