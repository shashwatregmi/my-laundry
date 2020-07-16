package com.example.mylaundry.ui.settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylaundry.BookActivity;
import com.example.mylaundry.BookItemAdapter;
import com.example.mylaundry.Booking;
import com.example.mylaundry.MainActivity;
import com.example.mylaundry.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class SettingsFragment extends Fragment {
    private RecyclerView bookingRecyclerView;
    private RecyclerView.Adapter bookingAdapter;
    private RecyclerView.LayoutManager bookingLayoutManager;
    private ArrayList<Booking> dbBookingList = new ArrayList<>();
    String TODAY = "";
    FirebaseFirestore db;


    private SettingsViewModel settingsViewModel;
    private MainActivity main;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        TODAY = dateFormat.format(date);
        db = FirebaseFirestore.getInstance();

        db.collection("bookings").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> tempBookings = queryDocumentSnapshots.getDocuments();
                            Date bookingDate = null;
                            Date todayDate = null;
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                            try {
                                todayDate = sdf.parse(TODAY);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            for (DocumentSnapshot d: tempBookings){
                                Booking pulledBooking = d.toObject(Booking.class);
                                try {
                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat bookFormat = new SimpleDateFormat("dd/MM/yyyy");
                                    bookingDate = bookFormat.parse(pulledBooking.getDate());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if (bookingDate != null && !(bookingDate.before(todayDate))){ // TODO: user check here after implementing user authent.
                                    dbBookingList.add(pulledBooking);
                                }                            }
                            bookingAdapter.notifyDataSetChanged();
                        }
                    }
                });


        bookingRecyclerView = root.findViewById(R.id.bookings);
        bookingLayoutManager = new LinearLayoutManager(getContext());
        bookingAdapter = new BookItemAdapter(dbBookingList);

        bookingRecyclerView.setLayoutManager(bookingLayoutManager);
        bookingRecyclerView.setAdapter(bookingAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((Activity) root.getContext()).getWindow().setStatusBarColor(ContextCompat.getColor(root.getContext(), R.color.colorAccent));
        }
        return root;
    }
}