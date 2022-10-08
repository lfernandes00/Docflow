package com.example.docflow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class FolderFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    ImageButton backBtn;
    ArrayList<DocumentDataModel> dataholder;
    RecyclerView recyclerView;
    JSONObject documentsObj;
    int folderId;
    String folderName;
    TextView title;
    EditText searchText;
    // TODO: Rename and change types of parameters

    public FolderFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FolderFragment newInstance(String param1, String param2) {
        FolderFragment fragment = new FolderFragment();
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
        View view = inflater.inflate(R.layout.fragment_folder, container, false);

        Bundle bundle = this.getArguments();
        Log.d("bundle", String.valueOf(bundle));
        folderId = Integer.parseInt(bundle.getString("folderId"));
        folderName = bundle.getString("folderName");

        searchText = (EditText) view.findViewById(R.id.edittext);
        backBtn = (ImageButton) view.findViewById(R.id.backBtn);
        title = (TextView) view.findViewById(R.id.title_text);
        title.setText(folderName.toUpperCase());
        backBtn.setOnClickListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getDocuments(UrlAPI.Url + "documents/folder/" + folderId);
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
                    getDocuments(UrlAPI.Url + "folders");
                }
                if (s.toString() != "") {
                    filter(s.toString());
                }
            }
        });

        return view;
    }

    private void filter(String text) {
        ArrayList<DocumentDataModel> dataFilter = new ArrayList<>();

        for (DocumentDataModel item : dataholder) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                dataFilter.add(item);
            }
        }

        if (LoggedUser.type == 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(new FolderAdminAdapter(dataFilter, getActivity()));
        }
        if (LoggedUser.type == 2) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(new FolderAdapter(dataFilter));
        }




    }

    private void getDocuments(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                documentsObj = response.getJSONObject(i);
                                Log.d("msg", String.valueOf(documentsObj));
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

                                int id = Integer.parseInt(documentsObj.getString("id"));
                                String name  = documentsObj.getString("name");
                                int userId = Integer.parseInt(documentsObj.getString("userId"));
                                String dataVencimento = documentsObj.getString("dataVencimento");
                                int typeId = Integer.parseInt(documentsObj.getString("typeId"));
                                int version = Integer.parseInt(documentsObj.getString("version"));
                                int pending = Integer.parseInt(documentsObj.getString("pending"));
                                int value = Integer.parseInt(documentsObj.getString("value"));
                                String description = documentsObj.getString("description");
                                int clientId = Integer.parseInt(documentsObj.getString("clientId"));
                                String extension = documentsObj.getString(("extension"));
                                int folderId = Integer.parseInt(documentsObj.getString("folderId"));
                                int deleted = Integer.parseInt(documentsObj.getString("deleted"));

                                if (pending == 1) {
                                    DocumentDataModel ob = new DocumentDataModel(id, name, userId, dataVencimento, typeId, version, pending, value, description, clientId, extension, folderId, deleted);
                                    dataholder.add(ob);
                                }
                            }


                            if (LoggedUser.type == 1) {
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(new FolderAdminAdapter(dataholder, getActivity()));
                            }
                            if (LoggedUser.type == 2) {
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(new FolderAdapter(dataholder));
                            }



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
            case R.id.cardBtn:
                openFragment(DocumentFragment.newInstance("", ""));
                break;
        }
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}