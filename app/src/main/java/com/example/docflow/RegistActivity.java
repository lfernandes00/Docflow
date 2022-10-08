package com.example.docflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistActivity extends AppCompatActivity {
    TextInputEditText emailText, passwordText, nameText, workerNumberText;
    TextView loginText;
    Button registBtn;
    JSONObject user;
    JSONObject loginObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        loginText = (TextView) findViewById(R.id.loginText);
        registBtn = (Button) findViewById(R.id.registBtn);
        emailText = (TextInputEditText) findViewById(R.id.emailText);
        passwordText = (TextInputEditText) findViewById(R.id.passwordText);
        nameText = (TextInputEditText) findViewById(R.id.nameText);
        workerNumberText = (TextInputEditText) findViewById(R.id.workerNumberText);

        user = new JSONObject();
        registBtn.setOnClickListener(v-> {
            try {
                user.put("email", emailText.getText());
                user.put("password", passwordText.getText());
                user.put("name", nameText.getText());
                user.put("workerNumber", workerNumberText.getText());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            regist(UrlAPI.Url + "users/signup");
        });

        loginText.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void regist(String url){
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, user,
                response -> {
                    // response
                    Log.d("Response", String.valueOf(response));
                    loginObj = new JSONObject();
                    try {
                        loginObj.put("email", emailText.getText());
                        loginObj.put("password", passwordText.getText());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    login(UrlAPI.Url + "users/signin");
                },
                error -> {
                    // error
                    Log.d("Error.Response", String.valueOf(error));
                }
        ) {

        };

        queue.add(postRequest);
    }

    private void login(String url){
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, loginObj,
                response -> {
                    // response
                    Log.d("Response", String.valueOf(response));
                    try {
                        LoggedUser.id = response.getInt("id");
                        LoggedUser.token = response.getString("accessToken");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);

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
    protected void onResume() {
        super.onResume();
        hideActionBar();
    }

    public void hideActionBar() {
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}