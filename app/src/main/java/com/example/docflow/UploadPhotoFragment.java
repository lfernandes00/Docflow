package com.example.docflow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class UploadPhotoFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private ImageView imgView;
    private ImageButton backBtn;
    private Button submitBtn, cancelBtn;
    String name, dataVencimento, description, imgText, extension;
    int typeId, version, value, clientId,  folderId, reviewId, approvalId;
    int documentId;
    JSONObject newDocument, reviewRequest, approvalRequest;
    public UploadPhotoFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static UploadPhotoFragment newInstance(String param1, String param2) {
        UploadPhotoFragment fragment = new UploadPhotoFragment();
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
        View view =  inflater.inflate(R.layout.fragment_upload_photo, container, false);
        imgView = (ImageView) view.findViewById(R.id.img);
        backBtn = (ImageButton) view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        cancelBtn = (Button) view.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(this);
        submitBtn = (Button) view.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(this);


        Bundle bundle = this.getArguments();
        name = bundle.getString("name");
        dataVencimento = bundle.getString("dataVencimento");
        typeId = bundle.getInt("typeId");
        version = bundle.getInt("version");
        value = bundle.getInt("value");
        description = bundle.getString("description");
        clientId = bundle.getInt("clientId");
        imgText = bundle.getString("img");
        folderId = bundle.getInt("folderId");
        reviewId = bundle.getInt("reviewId");
        approvalId = bundle.getInt("approvalId");
        Log.d("size", String.valueOf(imgText.length()));
        byte [] encodeByte = Base64.decode(imgText, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        imgView.setImageBitmap(bitmap);
        approvalRequest = new JSONObject();
        reviewRequest = new JSONObject();

        newDocument = new JSONObject();
        try {
            newDocument.put("name", name);
            newDocument.put("dataVencimento", dataVencimento);
            newDocument.put("typeId", typeId);
            newDocument.put("version", version);
            newDocument.put("value", value);
            newDocument.put("description", description);
            newDocument.put("clientId", clientId);
            newDocument.put("extension", "https://img.r7.com/images/documentos-vazados-eua-ovnis-12022019171625471");
            newDocument.put("folderId", folderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    private void createDocument(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, newDocument,
                response -> {
                    // response
                    try {
                        Log.d("Response", String.valueOf(response.getInt("location")));
                        documentId = response.getInt("location");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        reviewRequest.put("userId", reviewId);
                        reviewRequest.put("documentId", documentId);
                        reviewRequest.put("type", 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        approvalRequest.put("userId", approvalId);
                        approvalRequest.put("documentId", documentId);
                        approvalRequest.put("type", 2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    createReviewRequest(UrlAPI.Url + "requests");
                    createApprovalRequest(UrlAPI.Url + "requests");

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

    private void createReviewRequest(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, reviewRequest,
                response -> {
                    // response
                    Log.d("Response", String.valueOf(response));
                },
                error -> {
                    // error
                    Log.d("Error.Response", String.valueOf(error));
                }
        ) {

        };

        queue.add(postRequest);
    }

    private void createApprovalRequest(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, approvalRequest,
                response -> {
                    // response
                    Log.d("Response", String.valueOf(response));
                },
                error -> {
                    // error
                    Log.d("Error.Response", String.valueOf(error));
                }
        ) {

        };

        queue.add(postRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
            case R.id.cancelBtn:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
                break;
            case R.id.submitBtn:
                createDocument(UrlAPI.Url + "documents");
                openFragment(HomePageFragment.newInstance("", ""));
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