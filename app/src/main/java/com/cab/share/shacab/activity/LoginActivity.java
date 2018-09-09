package com.cab.share.shacab.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
            return;
        }

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        buttonLogin.setOnClickListener(this);

    }

    private void userLogin() throws JSONException {
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        progressDialog.show();


        JSONObject jsonBody = new JSONObject();
        jsonBody.put("username", username);
        jsonBody.put("password", password);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Constants.URL_LOGIN, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();

                        try {
                            if(response.getBoolean("error")){
                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .userLogin(
                                                1,
                                                username,
                                               username
                                        );
                                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                                finish();
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
                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
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
        if(view == buttonLogin){
            try {
                userLogin();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
