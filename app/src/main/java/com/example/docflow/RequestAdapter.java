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

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.myViewHolder> {

    ArrayList<RequestDataModel> dataholder;

    public RequestAdapter(ArrayList<RequestDataModel> dataholder) {
        this.dataholder = dataholder;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.requestcard, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(dataholder.get(position).getName());
        int documentId = dataholder.get(position).getDocumentId();
        int type = dataholder.get(position).getType();

        if (type == 1) {
            holder.type.setText("Revisão");
        }
        if (type == 2) {
            holder.type.setText("Aprovação");
        }


        holder.cardBtn.setOnClickListener(v -> {
            Log.d("id", String.valueOf(documentId));
            Bundle bundle = new Bundle();
            bundle.putString("documentId", String.valueOf(documentId));
            bundle.putString("type", String.valueOf(type));
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            RequestInfoFragment requestInfoFragment = new RequestInfoFragment();
            requestInfoFragment.setArguments(bundle);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, requestInfoFragment).addToBackStack(null).commit();
        });
    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView title, type;
        ImageButton cardBtn;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.cardTitle);
            type = itemView.findViewById(R.id.cardType);
            cardBtn = itemView.findViewById(R.id.cardBtn);
        }
    }

}
