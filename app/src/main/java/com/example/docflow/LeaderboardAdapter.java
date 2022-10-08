package com.example.docflow;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.myviewholder> {
    ArrayList<UserDataModel> dataholder;
    Context c;
    int dataType;

    public LeaderboardAdapter(ArrayList<UserDataModel> dataholder, Context c, int dataType) {
        this.dataholder = dataholder;
        this.c = c;
        this.dataType = dataType;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboardcard, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, @SuppressLint("RecyclerView") int position) {
        Picasso.get().load(dataholder.get(position).getPhoto()).into(holder.img);
        holder.name.setText(dataholder.get(position).getName());
        holder.number.setText(String.valueOf(position + 4) + "º");
        holder.infoBtn.setOnClickListener(v -> {
            showLeaderboardDialog(position);
        });
    }

    public void showLeaderboardDialog(int position) {
        double wPixel = Resources.getSystem().getDisplayMetrics().widthPixels * 0.8;
        int width = (int) wPixel;
        double hPixel = Resources.getSystem().getDisplayMetrics().heightPixels * 0.3;
        int height = (int) hPixel;
        final Dialog dialog = new Dialog(c);
        dialog.setContentView(R.layout.leaderboard_dialog);
        dialog.getWindow().setLayout(width , height);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        ImageButton crossBtn = (ImageButton) dialog.findViewById(R.id.cross);
        TextView dialogName = (TextView) dialog.findViewById(R.id.dialogName);
        dialogName.setText(dataholder.get(position).getName());
        TextView dialogNumber = (TextView) dialog.findViewById(R.id.dialogNumber);
        TextView dialogTime = (TextView) dialog.findViewById(R.id.dialogTime);
        ImageView dialogImage = (ImageView) dialog.findViewById(R.id.dialogPhoto);
        Picasso.get().load(dataholder.get(position).getPhoto()).into(dialogImage);

        if (dataType == 1) {
            dialogNumber.setText(String.valueOf(dataholder.get(position).getReviewCount()));
            dialogTime.setText(String.valueOf(dataholder.get(position).getTimeReview()));
        } else if (dataType == 2) {
            dialogNumber.setText(String.valueOf(dataholder.get(position).getApprovalCount()));
            dialogTime.setText(String.valueOf(dataholder.get(position).getTimeApproval()));
        }



        Log.d("timeReview", String.valueOf(dataholder.get(position).getPhoto()));

        //dialogTime.setText(dataholder.get(position).getTimeReview());
        crossBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    class myviewholder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name, number;
        ImageButton infoBtn;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.cardPhoto);
            name = itemView.findViewById(R.id.cardName);
            number = itemView.findViewById(R.id.cardPlace);
            infoBtn = itemView.findViewById(R.id.infoBtn);
        }
    }


}
