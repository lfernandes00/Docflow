package com.example.docflow;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class UploadFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int PICK_IMAGE = 2;
    private Spinner typeSpinner, clientSpinner, reviewSpinner, approvalSpinner, folderSpinner;
    private ImageButton cameraBtn, galeryBtn;
    private TextView usernameText;
    private EditText nameText, dateText, descText, versionText, valueText;
    private Calendar calendar = Calendar.getInstance();
    Bundle bundle = new Bundle();
    DatePickerDialog datePickerDialog;
    private int day, month, year;
    private SpinAdapter adapterType, adapterClient, adapterFolder, adapterReview, adapterApproval;
    int typeId, clientId, folderId, reviewId, approvalId;
    // TODO: Rename and change types of parameters
    public UploadFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UploadFragment newInstance(String param1, String param2) {
        UploadFragment fragment = new UploadFragment();
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
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        cameraBtn = (ImageButton) view.findViewById(R.id.cameraBtn);
        galeryBtn = (ImageButton) view.findViewById(R.id.galeryBtn);
        dateText = (EditText) view.findViewById(R.id.dateText);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        cameraBtn.setOnClickListener(this);
        galeryBtn.setOnClickListener(this);
        dateText.setOnClickListener(this);
        clientSpinner = (Spinner) view.findViewById(R.id.clientSpinner);
        typeSpinner = (Spinner) view.findViewById(R.id.typeSpinner);
        reviewSpinner = (Spinner) view.findViewById(R.id.reviewSpinner);
        approvalSpinner = (Spinner) view.findViewById(R.id.approvalSpinner);
        folderSpinner = (Spinner) view.findViewById(R.id.folderSpinner);
        usernameText = (TextView) view.findViewById(R.id.usernameText);
        usernameText.setText(LoggedUser.name);
        nameText = (EditText) view.findViewById(R.id.nameText);
        descText = (EditText) view.findViewById(R.id.descText);
        versionText = (EditText) view.findViewById(R.id.versionText);
        valueText = (EditText) view.findViewById(R.id.valueText);


        getClients(UrlAPI.Url + "clients");
        getDocumentTypes(UrlAPI.Url + "documentTypes");
        getUsers(UrlAPI.Url + "users");
        getFolder(UrlAPI.Url + "folders");

        






        return view;
    }

    private void getClients(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Spin[] spinnerClient = new Spin[response.length()];
                            for (int i = 0; i < response.length(); i++) {
                                int clientId = Integer.parseInt(response.getJSONObject(i).getString("id"));
                                String clientName  = response.getJSONObject(i).getString("name");

                                spinnerClient[i] = new Spin();
                                spinnerClient[i].setId(clientId);
                                spinnerClient[i].setName(clientName);
                            }

                            adapterClient = new SpinAdapter(getActivity(),
                                    android.R.layout.simple_spinner_item,
                                    spinnerClient);
                            clientSpinner.setAdapter(adapterClient);
                            
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

    private void getDocumentTypes(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Spin[] spinnerType = new Spin[response.length()];
                            for (int i = 0; i < response.length(); i++) {
                                int typeId = Integer.parseInt(response.getJSONObject(i).getString("id"));
                                String typeName  = response.getJSONObject(i).getString("name");

                                spinnerType[i] = new Spin();
                                spinnerType[i].setId(typeId);
                                spinnerType[i].setName(typeName);
                            }

                            adapterType = new SpinAdapter(getActivity(),
                                    android.R.layout.simple_spinner_item,
                                    spinnerType);
                            typeSpinner.setAdapter(adapterType);

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

    private void getUsers(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Spin[] spinnerReview = new Spin[response.length()];
                            Spin[] spinnerApproval = new Spin[response.length()];
                            for (int i = 0; i < response.length(); i++) {
                                int userId = Integer.parseInt(response.getJSONObject(i).getString("id"));
                                String userName  = response.getJSONObject(i).getString("name");


                                spinnerReview[i] = new Spin();
                                spinnerReview[i].setId(userId);
                                spinnerReview[i].setName(userName);

                                spinnerApproval[i] = new Spin();
                                spinnerApproval[i].setId(userId);
                                spinnerApproval[i].setName(userName);


                            }

                            adapterReview = new SpinAdapter(getActivity(),
                                    android.R.layout.simple_spinner_item,
                                    spinnerReview);
                            reviewSpinner.setAdapter(adapterReview);

                            adapterApproval = new SpinAdapter(getActivity(),
                                    android.R.layout.simple_spinner_item,
                                    spinnerApproval);
                            approvalSpinner.setAdapter(adapterApproval);

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

    private void getFolder(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Spin[] spinnerFolder = new Spin[response.length()];
                            for (int i = 0; i < response.length(); i++) {
                                int folderId = Integer.parseInt(response.getJSONObject(i).getString("id"));
                                String folderName  = response.getJSONObject(i).getString("name");

                                spinnerFolder[i] = new Spin();
                                spinnerFolder[i].setId(folderId);
                                spinnerFolder[i].setName(folderName);
                            }

                            adapterFolder = new SpinAdapter(getActivity(),
                                    android.R.layout.simple_spinner_item,
                                    spinnerFolder);
                            folderSpinner.setAdapter(adapterFolder);

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
            case R.id.dateText:
                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateText.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },year, month, day);
                datePickerDialog.show();
                break;
            case R.id.cameraBtn:
                openCamera();
                break;
            case R.id.galeryBtn:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                break;
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onResume() {
        super.onResume();
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                Spin spin = adapterType.getItem(position);
                // Here you can do the action you want to...
                typeId = spin.getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        clientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                Spin spin = adapterClient.getItem(position);
                // Here you can do the action you want to...
                clientId = spin.getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        reviewSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                Spin spin = adapterReview.getItem(position);
                // Here you can do the action you want to...
                reviewId = spin.getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        approvalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                Spin spin = adapterApproval.getItem(position);
                // Here you can do the action you want to...
                approvalId = spin.getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        folderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                Spin spin = adapterFolder.getItem(position);
                // Here you can do the action you want to...
                folderId = spin.getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK)
        {
            Bitmap imgMap = (Bitmap) data.getExtras().get("data");



            ByteArrayOutputStream baos= new ByteArrayOutputStream();
            imgMap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte [] b=baos.toByteArray();
            String imgText = Base64.encodeToString(b, Base64.DEFAULT);
            int size = imgText.length();
            Log.d("length", String.valueOf(size));
            bundle.putString("name", String.valueOf(nameText.getText()));
            bundle.putString("dataVencimento", String.valueOf(dateText.getText()));
            bundle.putInt("typeId", typeId);
            bundle.putInt("version", Integer.parseInt(String.valueOf(versionText.getText())));
            bundle.putInt("value", Integer.parseInt(String.valueOf(valueText.getText())));
            bundle.putString("description", String.valueOf(descText.getText()));
            bundle.putInt("clientId", clientId);
            bundle.putString("img", imgText);
            bundle.putInt("folderId", folderId);
            bundle.putInt("reviewId", reviewId);
            bundle.putInt("approvalId", approvalId);
            openFragmentWithBundle(UploadPhotoFragment.newInstance("", ""),bundle);
        }

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap imgMap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), data.getData());


                ByteArrayOutputStream baos= new ByteArrayOutputStream();
                imgMap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte [] b=baos.toByteArray();
                String imgText = Base64.encodeToString(b, Base64.DEFAULT);
                int size = imgText.length();
                Log.d("length", String.valueOf(size));
                bundle.putString("name", String.valueOf(nameText.getText()));
                bundle.putString("dataVencimento", String.valueOf(dateText.getText()));
                bundle.putInt("typeId", typeId);
                bundle.putInt("version", Integer.parseInt(String.valueOf(versionText.getText())));
                bundle.putInt("value", Integer.parseInt(String.valueOf(valueText.getText())));
                bundle.putString("description", String.valueOf(descText.getText()));
                bundle.putInt("clientId", clientId);
                bundle.putString("img", imgText);
                bundle.putInt("folderId", folderId);
                bundle.putInt("reviewId", reviewId);
                bundle.putInt("approvalId", approvalId);
                openFragmentWithBundle(UploadPhotoFragment.newInstance("", ""),bundle);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
        }
    }



    public void openFragmentWithBundle(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}