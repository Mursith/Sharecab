package com.cab.share.shacab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cab.share.shacab.R;
import com.cab.share.shacab.util.SharedPrefManager;

public class ProfileActivity extends AppCompatActivity {


    private TextView textViewUsername, textViewUserEmail;
    private Button buttonlogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        textViewUsername = (TextView) findViewById(R.id.textViewUsername);
        textViewUserEmail = (TextView) findViewById(R.id.textViewUseremail);
        Button button = (Button) findViewById(R.id.logout);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        textViewUserEmail.setText(SharedPrefManager.getInstance(this).getUserEmail());
        textViewUsername.setText(SharedPrefManager.getInstance(this).getUsername());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuLogout:
                SharedPrefManager.getInstance(this).logout();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.menuSettings:
                Toast.makeText(this, "You clicked settings", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }
}
