package com.example.mylaundry;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookItemAdapter extends RecyclerView.Adapter<BookItemAdapter.BookViewHolder> {

    private ArrayList<Booking> bookings;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder{

        public TextView dayView;
        public TextView monthView;
        public TextView timeView;
        public TextView washerView;

        public BookViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            dayView = itemView.findViewById(R.id.dayView);
            monthView = itemView.findViewById(R.id.monthView);
            timeView = itemView.findViewById(R.id.timeView);
            washerView = itemView.findViewById(R.id.washerView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public BookItemAdapter(ArrayList<Booking> bookings){
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookedbookinglistitem, parent, false);
        BookViewHolder bvh = new BookViewHolder(v, listener);
        return bvh;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.dayView.setText(booking.getDay());
        holder.monthView.setText(booking.getMonth());
        String time;
        String endtime;

        time = String.format("%02d:%02d", booking.getHour(), booking.getMinute());
        endtime = String.format("%02d:%02d", booking.getEndHr(), booking.getMinute());

        String displayTime = time + " to " + endtime;
        holder.timeView.setText(displayTime);
        holder.washerView.setText("Washer #" + booking.getWasher());
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

}
