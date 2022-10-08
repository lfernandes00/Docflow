package com.example.docflow;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePageFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private ImageButton list1Btn, list2Btn, optionsBtn, folder, notificationBtn;
    private TextView folderTitle, folderItems;
    private EditText editText;
    RecyclerView recyclerView;
    ArrayList<FolderDataModel> dataholder;
    JSONObject foldersObj;
    int interfaceType;
    CardView adminCard;
    ImageView adminCardImg;
    TextView adminCardText;
    JSONObject folderObj;
    // TODO: Rename and change types of parameters

    public HomePageFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomePageFragment newInstance(String param1, String param2) {
        HomePageFragment fragment = new HomePageFragment();
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
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        list1Btn = (ImageButton) view.findViewById(R.id.list1);
        list2Btn = (ImageButton) view.findViewById(R.id.list2);
        optionsBtn = (ImageButton) view.findViewById(R.id.folderOptions);
        folder = (ImageButton) view.findViewById(R.id.folderBtn);
        folderTitle = (TextView) view.findViewById(R.id.folderTitle);
        folderItems = (TextView) view.findViewById(R.id.folderItems);
        notificationBtn = (ImageButton) view.findViewById(R.id.notificationBtn);
        recyclerView = view.findViewById(R.id.recyclerView);
        adminCard = (CardView) view.findViewById(R.id.card_view_admin);
        adminCardImg = (ImageView) view.findViewById(R.id.adminCardImg);
        adminCardText = (TextView) view.findViewById(R.id.adminCardText);
        adminCard.setOnClickListener(this);

        folderObj = new JSONObject();

        if (LoggedUser.type == 2) {
            adminCard.setVisibility(View.INVISIBLE);
            adminCardImg.setVisibility(View.INVISIBLE);
            adminCardText.setVisibility(View.INVISIBLE);
        }
        if (LoggedUser.type == 1) {
            adminCard.setVisibility(View.VISIBLE);
            adminCardImg.setVisibility(View.VISIBLE);
            adminCardText.setVisibility(View.VISIBLE);
        }


        getFolders(UrlAPI.Url + "folders");
        dataholder = new ArrayList<>();


        list1Btn.getBackground().setAlpha(100);
        list1Btn.setOnClickListener(this);
        list2Btn.setOnClickListener(this);
        notificationBtn.setOnClickListener(this);
        interfaceType = 1;

        editText = (EditText) view.findViewById(R.id.edittext);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString() == "") {
                    getFolders(UrlAPI.Url + "folders");
                }
                if (s.toString() != "") {
                    filter(s.toString());
                }

            }
        });
        return view;
    }

    private void getFolders(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                foldersObj = response.getJSONObject(i);

                                int folderId = Integer.parseInt(foldersObj.getString("id"));
                                String folderName  = foldersObj.getString("name");

                                FolderDataModel ob = new FolderDataModel(folderId, folderName);
                                dataholder.add(ob);
                            }



                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(gridLayoutManager);
                            recyclerView.setAdapter(new HomePageAdapter(dataholder));

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

    private void filter(String text) {
        ArrayList<FolderDataModel> dataFilter = new ArrayList<>();

        for (FolderDataModel item : dataholder) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                dataFilter.add(item);
            }
        }

        if (interfaceType == 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(new HomePageAdapter(dataFilter));
        }
        if (interfaceType == 2) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(new HomePageAdapter2(dataFilter));
        }



    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.list1:
                interfaceType = 2;
                list1Btn.getBackground().setAlpha(255);
                list2Btn.getBackground().setAlpha(100);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(new HomePageAdapter2(dataholder));
                break;
            case R.id.list2:
                interfaceType = 1;
                list1Btn.getBackground().setAlpha(100);
                list2Btn.getBackground().setAlpha(255);
                GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(gridLayoutManager2);
                recyclerView.setAdapter(new HomePageAdapter(dataholder));
                break;
            case R.id.notificationBtn:
                openFragment(NotificationFragment.newInstance("",""));
                break;
            case R.id.card_view_admin:
                showCreateFolderDialog();
                break;
        }
    }

    void showCreateFolderDialog() {
        double wPixel = Resources.getSystem().getDisplayMetrics().widthPixels * 0.6;
        int width = (int) wPixel;
        double hPixel = Resources.getSystem().getDisplayMetrics().heightPixels * 0.33;
        int height = (int) hPixel;
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.createfolderdialog);
        dialog.getWindow().setLayout(width , height);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageButton crossBtn = (ImageButton) dialog.findViewById(R.id.cross);
        crossBtn.setOnClickListener(v -> dialog.dismiss());

        TextInputEditText nameText = (TextInputEditText) dialog.findViewById(R.id.nameText);
        Button submitBtn = (Button) dialog.findViewById(R.id.submitBtn);

        dialog.show();

        submitBtn.setOnClickListener(v -> {
            try {
                folderObj.put("name", String.valueOf(nameText.getText()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            createFolder(UrlAPI.Url + "folders");
            dialog.dismiss();
        });
    }

    private void createFolder(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, folderObj,
                response -> {
                    // response
                    Log.d("Response", String.valueOf(response));
                    dataholder = new ArrayList<>();
                    getFolders(UrlAPI.Url + "folders");
                },
                error -> {
                    // error
                    Log.d("Error.Response", String.valueOf(error));
                }
        ) {

        };

        queue.add(postRequest);
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}