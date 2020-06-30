package com.example.mylaundry;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.crypto.Mac;

public class MachineListAdapter extends RecyclerView.Adapter<MachineListAdapter.MachineViewHolder> {
    private ArrayList<MachineItemList> machineList = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public static class MachineViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgView;
        public TextView titleTxtView;
        public TextView descTxtView;

        public MachineViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imageView);
            titleTxtView = itemView.findViewById(R.id.titleView);
            descTxtView = itemView.findViewById(R.id.descView);

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

    public MachineListAdapter(ArrayList<MachineItemList> array){
        machineList = array;
    }

    @NonNull
    @Override
    public MachineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.machine_item_list, parent, false);
        MachineViewHolder machViewHolder = new MachineViewHolder(view, listener);
        return machViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MachineViewHolder holder, int position) {
        MachineItemList current = machineList.get(position);
        holder.imgView.setImageResource(current.getImg());
        holder.titleTxtView.setText(current.getTitle());
        holder.descTxtView.setText(current.getDesc());

    }

    @Override
    public int getItemCount() {
        return machineList.size();
    }

}
