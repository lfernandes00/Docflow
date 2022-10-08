package com.example.docflow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class UserManagementFragment extends Fragment implements View.OnClickListener {
    ArrayList<UserDataModel> dataholder;
    ImageButton backBtn;
    EditText searchText;
    RecyclerView recyclerView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    // TODO: Rename and change types of parameters

    public UserManagementFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static UserManagementFragment newInstance(String param1, String param2) {
        UserManagementFragment fragment = new UserManagementFragment();
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_management, container, false);
        backBtn = (ImageButton) view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        searchText = (EditText) view.findViewById(R.id.edittext);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        getUsers(UrlAPI.Url + "users");
        dataholder = new ArrayList<>();

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() == "") {
                    getUsers(UrlAPI.Url + "folders");
                }
                if (s.toString() != "") {
                    filter(s.toString());
                }
            }
        });

        return view;
    }

    private void filter(String text) {
        ArrayList<UserDataModel> dataFilter = new ArrayList<>();

        for (UserDataModel item : dataholder) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                dataFilter.add(item);
            }
        }

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(new UserManagementAdapter(dataFilter, getActivity()));





    }

    private void getUsers(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject usersObj = response.getJSONObject(i);

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


                                String timeReview = "";
                                String timeApproval = "";



                                if (typeId == 2) {
                                    UserDataModel ob = new UserDataModel(id, email, password, name, typeId, reviewCount, approvalCount, workerNumber, uploadCount, photo, timeReview, timeApproval);
                                    dataholder.add(ob);
                                }

                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(new UserManagementAdapter(dataholder, getActivity()));

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

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.backBtn:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
                break;
        }
    }
}