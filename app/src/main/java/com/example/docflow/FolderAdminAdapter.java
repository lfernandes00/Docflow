package com.example.docflow;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FolderAdminAdapter extends RecyclerView.Adapter<FolderAdminAdapter.myviewholder>{

    ArrayList<DocumentDataModel> dataholder;
    JSONObject documentObj;
    Context c;

    public FolderAdminAdapter(ArrayList<DocumentDataModel> dataholder, Context c) {
        this.dataholder = dataholder;
        this.c = c;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adminfoldercard, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(dataholder.get(position).getName());
        int documentId = dataholder.get(position).getId();
        holder.cardBtn.setOnClickListener(view -> {
            try {
                removeDocument(UrlAPI.Url + "documents/" + documentId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

    private void removeDocument(String url) throws JSONException {
        documentObj = new JSONObject();
        documentObj.put("deleted", 1);
        RequestQueue queue = Volley.newRequestQueue(c);
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PATCH, url, documentObj,
                response -> {
                    // response
                    Log.d("Response", String.valueOf(response));
                },
                error -> {
                    // error
                    Log.d("Error.Response", String.valueOf(error));
                }
        ) {



            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", "Bearer " + LoggedUser.token);
                return headerMap;
            }

        };

        queue.add(postRequest);
    }


}