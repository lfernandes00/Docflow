package com.example.docflow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserManagementAdapter extends RecyclerView.Adapter<UserManagementAdapter.myviewholder> {
    ArrayList<UserDataModel> dataholder;
    JSONObject userObj;
    Context c;

    public UserManagementAdapter(ArrayList<UserDataModel> dataholder, Context c) {
        this.dataholder = dataholder;
        this.c = c;
    }

    @NonNull
    @Override
    public UserManagementAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admincard, parent, false);
        return new UserManagementAdapter.myviewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull UserManagementAdapter.myviewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(dataholder.get(position).getName());
        Picasso.get().load(dataholder.get(position).getPhoto()).into(holder.img);
        holder.type.setText(String.valueOf(dataholder.get(position).getWorkerNumber()));
        int userId = dataholder.get(position).getId();
        holder.cardBtn.setOnClickListener(view -> {
            try {
                removeUser(UrlAPI.Url + "users/" + userId);
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
        ImageView img;
        ImageButton cardBtn;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.cardTitle);
            type = itemView.findViewById(R.id.cardType);
            cardBtn = itemView.findViewById(R.id.cardBtn);
            img = itemView.findViewById(R.id.img);
        }
    }

    private void removeUser(String url) throws JSONException {
        userObj = new JSONObject();
        userObj.put("deleted", 1);
        RequestQueue queue = Volley.newRequestQueue(c);
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PATCH, url, userObj,
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
