package com.example.docflow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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

public class RequestFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private RecyclerView recyclerView;
    ArrayList<RequestDataModel> dataholder;
    JSONObject requestsObj;
    JSONObject request2Obj;
    // TODO: Rename and change types of parameters

    public RequestFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RequestFragment newInstance(String param1, String param2) {
        RequestFragment fragment = new RequestFragment();
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
        View view = inflater.inflate(R.layout.fragment_request, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        dataholder = new ArrayList<>();
        getRequests(UrlAPI.Url + "users/" + LoggedUser.id + "/requests");

        return view;
    }

    private void getRequests(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {

                        for (int i = 0; i < response.getJSONArray("Request").length(); i++) {
                            requestsObj = response.getJSONArray("Request").getJSONObject(i);

                            request2Obj = requestsObj.getJSONObject("request");

                            int documentId = Integer.parseInt(request2Obj.getString("documentId"));
                            int type = Integer.parseInt(request2Obj.getString("type"));
                            String name = requestsObj.getString("name");
                            int pending = Integer.parseInt(request2Obj.getString("pending"));

                            Log.d("msg", String.valueOf(request2Obj));

                            if (pending == 0) {
                                RequestDataModel ob = new RequestDataModel(documentId, type, name);
                                dataholder.add(ob);
                            }
                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(new RequestAdapter(dataholder));

                    } catch (JSONException e){
                        Log.d("VolleyDebug", e.getMessage());
                        e.printStackTrace();
                    }
                }, error -> Log.d("VolleyDebug", error.getMessage()));

        queue.add(request);
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }



}