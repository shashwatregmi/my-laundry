package com.example.mylaundry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookItemAdapter extends RecyclerView.Adapter<BookItemAdapter.BookViewHolder> {

    private ArrayList<Booking> bookings;

    public static class BookViewHolder extends RecyclerView.ViewHolder{

        public TextView dayView;
        public TextView monthView;
        public TextView timeView;
        public TextView washerView;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            dayView = itemView.findViewById(R.id.dayView);
            monthView = itemView.findViewById(R.id.monthView);
            timeView = itemView.findViewById(R.id.timeView);
            washerView = itemView.findViewById(R.id.washerView);
        }
    }

    public BookItemAdapter(ArrayList<Booking> bookings){
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookedbookinglistitem, parent, false);
        BookViewHolder bvh = new BookViewHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.dayView.setText(booking.getDay());
        holder.monthView.setText(booking.getMonth());
        String time;
        if (booking.getMinute() == 0){
            time = booking.getHour() + ":" + "00";
        } else {
            time = booking.getHour() + ":" + booking.getMinute();
        }
        holder.timeView.setText(time);
        holder.washerView.setText("Washer #" + booking.getWasher());
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }
}
