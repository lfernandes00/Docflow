package com.example.docflow;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestInfoFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private ImageButton backBtn, acceptBtn, rejectBtn;
    TextView title, descText, userText, typeText, versionText, valueText, clientText, dateText;
    ImageView docImg;
    int documentId, type,  reviewCount, approvalCount;
    JSONObject typeObj;
    JSONObject userObj;
    JSONObject clientObj;
    JSONArray requestObj;
    JSONObject bodyRequest;
    JSONObject bodyDocument;
    JSONObject bodyUser;
    ArrayList<Integer> avaliations;
    int number = 0;
    // TODO: Rename and change types of parameters

    public RequestInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment requestInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestInfoFragment newInstance(String param1, String param2) {
        RequestInfoFragment fragment = new RequestInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_request_info, container, false);
        backBtn = (ImageButton) view.findViewById(R.id.backBtn);
        acceptBtn = (ImageButton) view.findViewById(R.id.acceptBtn);
        rejectBtn = (ImageButton) view.findViewById(R.id.rejectBtn);
        backBtn.setOnClickListener(this);
        acceptBtn.setOnClickListener(this);
        rejectBtn.setOnClickListener(this);

        title = (TextView) view.findViewById(R.id.title_text);
        docImg = (ImageView) view.findViewById(R.id.docImg);
        descText = (TextView) view.findViewById(R.id.descText);
        userText = (TextView) view.findViewById(R.id.userText);
        typeText = (TextView) view.findViewById(R.id.typeText);
        versionText = (TextView) view.findViewById(R.id.versionText);
        valueText = (TextView) view.findViewById(R.id.valueText);
        clientText = (TextView) view.findViewById(R.id.clientText);
        dateText = (TextView) view.findViewById(R.id.dateText);

        Bundle bundle = this.getArguments();
        Log.d("bundle", String.valueOf(bundle));
        documentId = Integer.parseInt(bundle.getString("documentId"));
        type = Integer.parseInt(bundle.getString("type"));


        avaliations = new ArrayList<Integer>();

        getUser(UrlAPI.Url + "users/" + LoggedUser.id);

        getDocument(UrlAPI.Url + "documents/" + documentId);

        return view;
    }

    private void getDocument(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {
                        Log.d("response", String.valueOf(response));
                        String dataVencimento = response.getString("dataVencimento");
                        String[] separated = dataVencimento.split("T");

                        String name = response.getString("name");

                        int version = Integer.parseInt(response.getString("version"));
                        int value = Integer.parseInt(response.getString("value"));
                        String description = response.getString("description");
                        String extension = response.getString("extension");
                        typeObj = response.getJSONObject("documentType");
                        String type = typeObj.getString("name");
                        userObj = response.getJSONObject("user");
                        String user = userObj.getString("name");
                        clientObj = response.getJSONObject("client");
                        String client = clientObj.getString("name");
                        requestObj = response.getJSONArray("Request");

                        for (int i = 0; i < requestObj.length(); i++) {
                            JSONObject obj = requestObj.getJSONObject(i);
                            JSONObject objRequest = obj.getJSONObject("request");
                            int pending = Integer.parseInt(objRequest.getString("pending"));
                            int avaliation = Integer.parseInt(objRequest.getString("avaliation"));

                            avaliations.add(avaliation);

                            if (pending == 0) {
                                number++;
                            }
                        }

                        title.setText(name.toUpperCase());
                        Picasso.get().load(extension).into(docImg);
                        descText.setText(description);
                        userText.setText(user);
                        typeText.setText(type);
                        versionText.setText(String.valueOf(version));
                        valueText.setText(String.valueOf(value));
                        clientText.setText(client);
                        dateText.setText(separated[0]);

                    } catch (JSONException e){
                        Log.d("VolleyDebug", e.getMessage());
                        e.printStackTrace();
                    }
                }, error -> Log.d("VolleyDebug", error.getMessage()));

        queue.add(request);
    }

    private void getUser(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {

                        reviewCount = Integer.parseInt(response.getString("reviewCount"));
                        approvalCount = Integer.parseInt(response.getString("aprovedCount"));

                    } catch (JSONException e){
                        Log.d("VolleyDebug", e.getMessage());
                        e.printStackTrace();
                    }
                }, error -> Log.d("VolleyDebug", error.getMessage()));

        queue.add(request);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.backBtn:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
                break;
            case R.id.acceptBtn:
                int place = avaliations.indexOf(0);
                if (place != -1) {
                    Log.d("troca", "troca");
                    avaliations.set(place, 1);
                }
                bodyRequest = new JSONObject();
                bodyDocument = new JSONObject();
                try {
                    bodyRequest.put("pending", 1);
                    bodyRequest.put("avaliation", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                updateRequest(UrlAPI.Url + "requests/" + documentId + "/" + LoggedUser.id);

                break;
            case R.id.rejectBtn:
                bodyRequest = new JSONObject();
                bodyDocument = new JSONObject();
                try {
                    bodyRequest.put("pending", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                updateRequest(UrlAPI.Url + "requests/" + documentId + "/" + LoggedUser.id);
                openFragment(RequestFragment.newInstance("", ""));
                break;
        }
    }

    private void updateRequest(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PATCH, url, bodyRequest,
                response -> {
                    // response
                    Log.d("Response", String.valueOf(response));

                    bodyUser = new JSONObject();

                    if (type == 1) {
                        try {
                            bodyUser.put("reviewCount", reviewCount + 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (type == 2) {
                        try {
                            bodyUser.put("aprovedCount", approvalCount + 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    updateUser(UrlAPI.Url + "users/" + LoggedUser.id);

                    Log.d("avaliations", String.valueOf(avaliations));
                    int avaliation = 0;
                    try {
                        avaliation = Integer.parseInt(bodyRequest.getString("avaliation"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    int occurrences = Collections.frequency(avaliations, 0);

                    Log.d("occurrences" , String.valueOf(occurrences));
                    Log.d("avaliation", String.valueOf(avaliation));
                    Log.d("number", String.valueOf(number));
                    if (number == 1) {

                        if (occurrences == 0 && avaliation == 1) {
                            try {
                                bodyDocument.put("pending", 1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            updateDocument(UrlAPI.Url + "documents/" + documentId + "/requests");
                        }
                    }

                    openFragment(RequestFragment.newInstance("", ""));
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

    private void updateDocument(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PATCH, url, bodyDocument,
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

    private void updateUser(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PUT, url, bodyUser,
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

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}