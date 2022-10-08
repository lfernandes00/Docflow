package com.example.docflow;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.myViewHolder>{
    ArrayList<NotificationDataModel> dataholder;
    private ItemClickListener clickListener;

    public NotificationAdapter(ArrayList<NotificationDataModel> dataholder, ItemClickListener clickListener) {
        this.dataholder = dataholder;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notificationcard, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.desc.setText(dataholder.get(position).getDesc());
        holder.date.setText(dataholder.get(position).getDate());
        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(dataholder.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView desc, date;
        ImageButton removeBtn;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            desc = itemView.findViewById(R.id.descText);
            date = itemView.findViewById(R.id.dateText);
            removeBtn = itemView.findViewById(R.id.removeBtn);
        }
    }

    public interface ItemClickListener {
        void onItemClick(NotificationDataModel NotificationDataModel);
    }
}
