package com.cab.share.shacab.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.cab.share.shacab.R;
import com.cab.share.shacab.service.RequestHandler;
import com.cab.share.shacab.util.Constants;
import com.cab.share.shacab.util.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText editTextUsername, editTextPhone, editTextPassword;
    private Button buttonRegister;
    private ProgressDialog progressDialog;

    private TextView textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
            return;
        }

        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        textViewLogin = (TextView) findViewById(R.id.textViewLogin);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        progressDialog = new ProgressDialog(this);

        buttonRegister.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);
    }

    private void registerUser() throws JSONException {
        final String phone = editTextPhone.getText().toString().trim();
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("userName", username);
        jsonBody.put("phoneNumber", phone);
        jsonBody.put("password", password);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Constants.URL_REGISTER, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();

                        try {
                            if(!response.getBoolean("error")){
                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Already registered", Toast.LENGTH_LONG).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) { //no semicolon or coma
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };




        RequestHandler.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonRegister) {
            try {
                registerUser();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(view == textViewLogin)
            startActivity(new Intent(this, LoginActivity.class));
    }
}
