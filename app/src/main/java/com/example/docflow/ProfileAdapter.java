package com.example.docflow;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.myViewHolder> {
    ArrayList<ProfileDataModel> dataholder;


    public ProfileAdapter(ArrayList<ProfileDataModel> dataholder) {
        this.dataholder = dataholder;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profilecard, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Picasso.get().load(dataholder.get(position).getPhoto()).into(holder.img);
        int documentId = dataholder.get(position).getId();
        holder.img.setOnClickListener( view -> {
            Log.d("id", String.valueOf(documentId));
            Bundle bundle = new Bundle();
            bundle.putString("documentId", String.valueOf(documentId));
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            DocumentFragment documentFragment = new DocumentFragment();
            documentFragment.setArguments(bundle);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, documentFragment).addToBackStack(null).commit();
        });
    }


    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img= itemView.findViewById(R.id.imageBtn);
        }
    }
}
