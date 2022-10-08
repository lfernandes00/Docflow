package com.example.docflow;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private ImageButton editBtn, logoutBtn;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    private ImageView img;
    Bundle bundle = new Bundle();
    private RecyclerView recyclerViewUpload, recyclerViewReview, recyclerViewApproval;
    private TextView nameText, workerNumberText, uploadCountText, reviewCountText, approvalCountText, uploadItems, reviewItems, approvalItems;
    JSONObject uploadDocumentsObj, requestsObj, request2Obj;
    ArrayList<ProfileDataModel> dataholder, dataReview, dataApproval;
    // TODO: Rename and change types of parameters

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        nameText = (TextView) view.findViewById(R.id.profileName);
        workerNumberText = (TextView) view.findViewById(R.id.profileWorkerNumber);
        reviewCountText = (TextView) view.findViewById(R.id.reviewNumber);
        uploadCountText = (TextView) view.findViewById(R.id.uploadNumber);
        approvalCountText = (TextView) view.findViewById(R.id.approvalNumber);
        editBtn = (ImageButton) view.findViewById(R.id.editBtn);
        recyclerViewUpload = (RecyclerView) view.findViewById(R.id.recyclerViewUpload);
        recyclerViewReview = (RecyclerView) view.findViewById(R.id.recyclerViewReview);
        recyclerViewApproval = (RecyclerView) view.findViewById(R.id.recyclerViewApproval);
        img = (ImageView) view.findViewById(R.id.imageView1);
        editBtn.setOnClickListener(this);
        recyclerViewUpload.setLayoutManager(RecyclerViewLayoutManager);
        recyclerViewReview.setLayoutManager(RecyclerViewLayoutManager);
        recyclerViewApproval.setLayoutManager(RecyclerViewLayoutManager);
        logoutBtn = (ImageButton) view.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(this);
        uploadItems = (TextView) view.findViewById(R.id.uploadItemsText);
        reviewItems = (TextView) view.findViewById(R.id.reviewItemsText);
        approvalItems = (TextView) view.findViewById(R.id.approvalItemsText);


        dataholder = new ArrayList<>();
        dataReview = new ArrayList<>();
        dataApproval = new ArrayList<>();
        getUser(UrlAPI.Url + "users/" + LoggedUser.id);

        return view;
    }

    private void getUser(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {

                        String name = response.getString("name");
                        String email = response.getString("email");
                        int workerNumber = Integer.parseInt(response.getString("workerNumber"));
                        int reviewCount = Integer.parseInt(response.getString("reviewCount"));
                        int approvalCount = Integer.parseInt(response.getString("aprovedCount"));
                        int uploadCount = Integer.parseInt(response.getString("uploadCount"));
                        String userPhoto = response.getString("photo");

                        if (response.getJSONArray("documents").length() != 0) {
                            for (int i = 0; i < response.getJSONArray("documents").length(); i++) {
                                uploadDocumentsObj = response.getJSONArray("documents").getJSONObject(i);

                                int documentId = Integer.parseInt(uploadDocumentsObj.getString("id"));
                                String photo = uploadDocumentsObj.getString("extension");


                                ProfileDataModel ob = new ProfileDataModel(documentId, photo);

                                dataholder.add(ob);
                            }
                        }

                        if (response.getJSONArray("Request").length() != 0) {
                            for (int i = 0; i < response.getJSONArray("Request").length(); i++) {
                                requestsObj = response.getJSONArray("Request").getJSONObject(i);

                                request2Obj = requestsObj.getJSONObject("request");

                                int documentId = Integer.parseInt(requestsObj.getString("id"));
                                String photo = requestsObj.getString("extension");
                                int type = Integer.parseInt(request2Obj.getString("type"));
                                int pending = Integer.parseInt(request2Obj.getString("pending"));

                                Log.d("msg", String.valueOf(request2Obj));

                                if (pending == 1 && type == 1) {
                                    ProfileDataModel ob = new ProfileDataModel(documentId, photo);
                                    dataReview.add(ob);
                                }

                                if (pending == 1 && type == 2) {
                                    ProfileDataModel ob = new ProfileDataModel(documentId, photo);
                                    dataApproval.add(ob);
                                }
                            }
                        }



                        bundle.putString("email", email);
                        bundle.putString("name", name);
                        bundle.putString("photo", userPhoto);
                        bundle.putInt("workerNumber", workerNumber);

                        Picasso.get().load(userPhoto).into(img);
                        nameText.setText(name);
                        workerNumberText.setText(String.valueOf(workerNumber));
                        uploadCountText.setText(String.valueOf(dataholder.size()));
                        reviewCountText.setText(String.valueOf(dataReview.size()));
                        approvalCountText.setText(String.valueOf(dataApproval.size()));

                        Log.d("msg", String.valueOf(dataholder));

                        uploadItems.setText(String.valueOf(dataholder.size() + " items"));
                        reviewItems.setText(String.valueOf(dataReview.size() + " items"));
                        approvalItems.setText(String.valueOf(dataApproval.size() + " items"));

                        recyclerViewUpload.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                        recyclerViewUpload.setAdapter(new ProfileAdapter(dataholder));

                        recyclerViewReview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                        recyclerViewReview.setAdapter(new ProfileAdapter(dataReview));

                        recyclerViewApproval.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                        recyclerViewApproval.setAdapter(new ProfileAdapter(dataApproval));

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
            case R.id.editBtn:
                openFragmentWithBundle(EditProfileFragment.newInstance("", ""),bundle);
                break;
            case R.id.logoutBtn:
                LoggedUser.id = 0;
                LoggedUser.token = "";
                LoggedUser.type = 0;
                LoggedUser.name = "";
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
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