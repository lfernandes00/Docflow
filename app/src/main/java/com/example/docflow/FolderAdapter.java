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

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.myviewholder>{

    ArrayList<DocumentDataModel> dataholder;

    public FolderAdapter(ArrayList<DocumentDataModel> dataholder) {
        this.dataholder = dataholder;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foldercard, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(dataholder.get(position).getName());
        int documentId = dataholder.get(position).getId();
        holder.cardBtn.setOnClickListener(view -> {
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

    class myviewholder extends RecyclerView.ViewHolder {

        TextView title, type;
        ImageButton cardBtn;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.cardTitle);
            type = itemView.findViewById(R.id.cardType);
            cardBtn = itemView.findViewById(R.id.cardBtn);
        }
    }


}
