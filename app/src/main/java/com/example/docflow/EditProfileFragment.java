package com.example.docflow;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class EditProfileFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private ImageButton backBtn, adminBtn;
    private ImageView imgView;
    private Button submitBtn;
    private TextView editPhoto;
    int workerNumber;
    String photo, email, name, password;
    JSONObject userObj;
    TextInputEditText emailText, nameText, passwordText, confirmPasswordText, workerNumberText;
    public static final int PICK_IMAGE = 1;
    // TODO: Rename and change types of parameters

    public EditProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        backBtn = (ImageButton) view.findViewById(R.id.backBtn);
        submitBtn = (Button) view.findViewById(R.id.submitBtn);
        editPhoto = (TextView) view.findViewById(R.id.editPhoto);
        imgView = (ImageView) view.findViewById(R.id.img);
        editPhoto.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        emailText = (TextInputEditText) view.findViewById(R.id.emailText);
        nameText = (TextInputEditText) view.findViewById(R.id.nameText);
        passwordText = (TextInputEditText) view.findViewById(R.id.passwordText);
        confirmPasswordText = (TextInputEditText) view.findViewById(R.id.confirmPasswordText);
        workerNumberText = (TextInputEditText) view.findViewById(R.id.workerNumberText);
        adminBtn = (ImageButton) view.findViewById(R.id.adminUserBtn);
        adminBtn.setOnClickListener(this);

        if (LoggedUser.type == 1) {
            adminBtn.setVisibility(View.VISIBLE);
        }
        if (LoggedUser.type == 2) {
            adminBtn.setVisibility(View.INVISIBLE);
        }


        Bundle bundle = this.getArguments();
        photo = bundle.getString("photo");
        name = bundle.getString("name");
        email = bundle.getString("email");
        workerNumber = bundle.getInt("workerNumber");

        emailText.setText(email);
        emailText.setEnabled(false);
        nameText.setText(name);
        workerNumberText.setText(String.valueOf(workerNumber));

        Picasso.get().load(photo).into(imgView);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.backBtn:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
                break;
            case R.id.submitBtn:
                password = String.valueOf(passwordText.getText());
                Log.d("msg", "submit");


                    try {
                        userObj = new JSONObject();
                        userObj.put("name", name);
                        userObj.put("password", String.valueOf(passwordText.getText()));
                        userObj.put("workerNumber", workerNumber);
                        updateUser(UrlAPI.Url + "users/" + LoggedUser.id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                openFragment(ProfileFragment.newInstance("", ""));
                break;
            case R.id.editPhoto:
                Log.d("msg", "working");
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                break;
            case R.id.adminUserBtn:
                openFragment(UserManagementFragment.newInstance("",""));
                break;
        }
    }

    private void updateUser(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, userObj,
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), data.getData());
                imgView.setImageBitmap(bitmap);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte [] b=baos.toByteArray();
                String imgText = Base64.encodeToString(b, Base64.DEFAULT);
                Log.d("msg", imgText);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}