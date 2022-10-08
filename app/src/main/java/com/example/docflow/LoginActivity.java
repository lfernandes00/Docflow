package com.example.docflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText emailText, passwordText;
    TextView registerText;
    Button loginBtn;
    JSONObject user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = (TextInputEditText) findViewById(R.id.emailText);
        passwordText = (TextInputEditText) findViewById(R.id.passwordText);

        registerText = (TextView) findViewById(R.id.registerText);
        registerText.setOnClickListener(v ->{
            Intent intent = new Intent(this, RegistActivity.class);
            startActivity(intent);
        });

        loginBtn = (Button) findViewById(R.id.loginBtn);

        user = new JSONObject();
        loginBtn.setOnClickListener(v -> {

            try {
                user.put("email", emailText.getText());
                user.put("password", passwordText.getText());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            login(UrlAPI.Url + "users/signin");
        });


    }

    private void login(String url){
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, user,
                response -> {
                    // response
                    Log.d("Response", String.valueOf(response));
                    try {
                        LoggedUser.id = response.getInt("id");
                        LoggedUser.token = response.getString("accessToken");
                        LoggedUser.name = response.getString("name");
                        LoggedUser.type = response.getInt("typeId");
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