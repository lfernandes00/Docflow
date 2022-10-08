package com.example.docflow;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class LeaderboardFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    private Button approvalBtn, reviewBtn;
    private ImageView firstPlaceBtn, secondPlaceBtn, thirdPlaceBtn;
    RecyclerView recyclerView;
    ArrayList<UserDataModel> dataholder;
    ArrayList<UserDataModel> dataholder2;
    JSONObject usersObj;
    JSONArray requestObj;
    ArrayList<String> reviewTime, approvalTime;
    int dataType;
    public LeaderboardFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LeaderboardFragment newInstance(String param1, String param2) {
        LeaderboardFragment fragment = new LeaderboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        approvalBtn = (Button) view.findViewById(R.id.approvalButton);
        reviewBtn = (Button) view.findViewById(R.id.reviewButton);
        firstPlaceBtn = (ImageView) view.findViewById(R.id.firstPlaceBtn);
        secondPlaceBtn = (ImageView) view.findViewById(R.id.secondPlaceBtn);
        thirdPlaceBtn = (ImageView) view.findViewById(R.id.thirdPlaceBtn);
        approvalBtn.setOnClickListener(this);
        reviewBtn.setOnClickListener(this);
        firstPlaceBtn.setOnClickListener(this);
        secondPlaceBtn.setOnClickListener(this);
        thirdPlaceBtn.setOnClickListener(this);

        recyclerView = view.findViewById(R.id.recyclerView);
        dataholder = new ArrayList<>();

        dataholder2 = new ArrayList<>();
        dataType = 1;



        getUsers(UrlAPI.Url + "users");

        return view;
    }

    private void getUsers(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                usersObj = response.getJSONObject(i);
                                reviewTime = new ArrayList<>();
                                approvalTime = new ArrayList<>();

                                int id = Integer.parseInt(usersObj.getString("id"));
                                String email = usersObj.getString("email");
                                String password = usersObj.getString("password");
                                String name = usersObj.getString("name");
                                int typeId = Integer.parseInt(usersObj.getString("typeId"));
                                int reviewCount = Integer.parseInt(usersObj.getString("reviewCount"));
                                int approvalCount = Integer.parseInt(usersObj.getString("aprovedCount"));
                                int workerNumber = Integer.parseInt(usersObj.getString("workerNumber"));
                                int uploadCount = Integer.parseInt(usersObj.getString("uploadCount"));
                                String photo = usersObj.getString("photo");
                                requestObj = usersObj.getJSONArray("Request");

                                Log.d("array", String.valueOf(requestObj));

                                String timeReview = "";
                                String timeApproval = "";

                                if (requestObj.length() != 0) {
                                    for (int j = 0; j < requestObj.length(); j++) {
                                        JSONObject obj = requestObj.getJSONObject(j);
                                        JSONObject objRequest = obj.getJSONObject("request");

                                        Log.d("obj", String.valueOf(obj));

                                        int type = Integer.parseInt(objRequest.getString("type"));
                                        String time = objRequest.getString("time");

                                        //Log.d("type", String.valueOf(type));
                                        //Log.d("time", String.valueOf(time));


                                        if (type == 1) {
                                            reviewTime.add(time);
                                        }
                                        if (type == 2) {
                                            approvalTime.add(time);
                                        }
                                    }


                                    Collections.sort(approvalTime);
                                    Collections.sort(reviewTime);

                                    Log.d("approval", String.valueOf(approvalTime));
                                    Log.d("review", String.valueOf(reviewTime));

                                    if (reviewTime.size() != 0) {
                                        timeReview = reviewTime.get(0);
                                    }
                                    if (approvalTime.size() != 0) {
                                        timeApproval = approvalTime.get(0);
                                    }

                                }

                                if (typeId == 2) {
                                    UserDataModel ob = new UserDataModel(id, email, password, name, typeId, reviewCount, approvalCount, workerNumber, uploadCount, photo, timeReview, timeApproval);
                                    dataholder.add(ob);
                                }

                            }

                            Collections.sort(dataholder, Comparator.comparing(UserDataModel::getReviewCount));
                            Collections.reverse(dataholder);

                            for (int i = 0; i<dataholder.size();i++) {
                                Log.d("count", String.valueOf(dataholder.get(i).getTimeReview()));
                            }

                            Picasso.get().load(dataholder.get(0).getPhoto()).into(firstPlaceBtn);
                            Picasso.get().load(dataholder.get(1).getPhoto()).into(secondPlaceBtn);
                            Picasso.get().load(dataholder.get(2).getPhoto()).into(thirdPlaceBtn);

                            //firstPlaceBtn.setImageResource(dataholder.get(0).getPhoto());
                            //secondPlaceBtn.setImageResource(dataholder.get(1).getPhoto());
                            //thirdPlaceBtn.setImageResource(dataholder.get(2).getPhoto());


                            for (int i = 3; i < dataholder.size() ; i++) {
                                dataholder2.add(dataholder.get(i));
                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(new LeaderboardAdapter(dataholder2, getActivity(), dataType));

                        } catch (JSONException e){
                            Log.d("VolleyDebug", e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d("VolleyDebug", error.getMessage());
                    }
                });

        queue.add(request);
    }

    @SuppressLint({"ResourceAsColor", "NonConstantResourceId"})
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.approvalButton:
                dataType = 2;
                reviewBtn.setBackgroundTintList(getResources().getColorStateList(R.color.frejenRed));
                reviewBtn.setTextColor(getResources().getColorStateList(R.color.white));
                approvalBtn.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                approvalBtn.setTextColor(getResources().getColorStateList(R.color.black));
                Collections.sort(dataholder, Comparator.comparing(UserDataModel::getApprovalCount));
                Collections.reverse(dataholder);

                Picasso.get().load(dataholder.get(0).getPhoto()).into(firstPlaceBtn);
                Picasso.get().load(dataholder.get(1).getPhoto()).into(secondPlaceBtn);
                Picasso.get().load(dataholder.get(2).getPhoto()).into(thirdPlaceBtn);

                //firstPlaceBtn.setImageResource(dataholder.get(0).getImage());
                //secondPlaceBtn.setImageResource(dataholder.get(1).getImage());
                //thirdPlaceBtn.setImageResource(dataholder.get(2).getImage());

                dataholder2 = new ArrayList<>();

                for (int i = 3; i < dataholder.size() ; i++) {
                    dataholder2.add(dataholder.get(i));
                }

                recyclerView.setAdapter(new LeaderboardAdapter(dataholder2, getActivity(), dataType));
                break;
            case R.id.reviewButton:
                dataType = 1;
                approvalBtn.setBackgroundTintList(getResources().getColorStateList(R.color.frejenRed));
                approvalBtn.setTextColor(getResources().getColorStateList(R.color.white));
                reviewBtn.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                reviewBtn.setTextColor(getResources().getColorStateList(R.color.black));
                Collections.sort(dataholder, Comparator.comparing(UserDataModel::getReviewCount));
                Collections.reverse(dataholder);

                Picasso.get().load(dataholder.get(0).getPhoto()).into(firstPlaceBtn);
                Picasso.get().load(dataholder.get(1).getPhoto()).into(secondPlaceBtn);
                Picasso.get().load(dataholder.get(2).getPhoto()).into(thirdPlaceBtn);

                //firstPlaceBtn.setImageResource(dataholder.get(0).getImage());
                //secondPlaceBtn.setImageResource(dataholder.get(1).getImage());
                //thirdPlaceBtn.setImageResource(dataholder.get(2).getImage());

                dataholder2 = new ArrayList<>();


                for (int i = 3; i < dataholder.size() ; i++) {
                    dataholder2.add(dataholder.get(i));
                }

                recyclerView.setAdapter(new LeaderboardAdapter(dataholder2, getActivity(), dataType));
                break;
            case R.id.firstPlaceBtn:
                showLeaderboardDialog(0);
                break;
            case R.id.secondPlaceBtn:
                showLeaderboardDialog(1);
                break;
            case R.id.thirdPlaceBtn:
                showLeaderboardDialog(2);
                break;
        }
    }

    public void showLeaderboardDialog(int position) {
        double wPixel = Resources.getSystem().getDisplayMetrics().widthPixels * 0.8;
        int width = (int) wPixel;
        double hPixel = Resources.getSystem().getDisplayMetrics().heightPixels * 0.3;
        int height = (int) hPixel;
        final Dialog dialog = new Dialog(getActivity());
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
            dialogTime.setText("");
        } else if (dataType == 2) {
            dialogNumber.setText(String.valueOf(dataholder.get(position).getApprovalCount()));
            dialogTime.setText("");
        }



        Log.d("timeReview", String.valueOf(dataholder.get(position).getTimeReview()));

        //dialogTime.setText(dataholder.get(position).getTimeReview());
        crossBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


}