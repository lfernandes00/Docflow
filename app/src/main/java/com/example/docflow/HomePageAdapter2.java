package com.example.docflow;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomePageAdapter2 extends RecyclerView.Adapter<HomePageAdapter2.myViewHolder>{
    public ArrayList<FolderDataModel> dataholder;

    public HomePageAdapter2(ArrayList<FolderDataModel> dataholder) {
        this.dataholder = dataholder;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homepagecard2, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.text.setText(dataholder.get(position).getName());
        int folderId = dataholder.get(position).getId();
        String folderName = dataholder.get(position).getName();
        holder.cardBtn.setOnClickListener(view -> {
            Log.d("id", String.valueOf(folderId));
            Bundle bundle = new Bundle();
            bundle.putString("folderId", String.valueOf(folderId));
            bundle.putString("folderName", folderName);
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            FolderFragment folderFragment = new FolderFragment();
            folderFragment.setArguments(bundle);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, folderFragment).addToBackStack(null).commit();
        });
    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView text, items;
        ImageButton cardBtn;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.cardTitle);
            cardBtn = itemView.findViewById(R.id.cardBtn);
        }
    }

}