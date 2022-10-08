package com.example.docflow;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Base64;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class DocumentFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private ImageButton backBtn;
    TextView title, descText, userText, typeText, versionText, valueText, clientText, dateText;
    ImageView docImg;
    JSONObject typeObj;
    JSONObject userObj;
    JSONObject clientObj;
    int documentId;
    public DocumentFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DocumentFragment newInstance(String param1, String param2) {
        DocumentFragment fragment = new DocumentFragment();
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
        View view = inflater.inflate(R.layout.fragment_document, container, false);
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
        backBtn = (ImageButton) view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);

        getDocument(UrlAPI.Url + "documents/" + documentId);
        return view;
    }

    private void getDocument(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {
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

                        Log.d("img", String.valueOf(extension.length()));


                        title.setText(name.toUpperCase());
                        Picasso.get().load(extension).into(docImg);
                        descText.setText(description);
                        userText.setText(user);
                        typeText.setText(type);
                        versionText.setText(String.valueOf(version));
                        valueText.setText(String.valueOf(value));
                        clientText.setText(client);
                        dateText.setText(separated[0]);
                        Log.d("msg", String.valueOf(typeObj));

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
        }
    }
}