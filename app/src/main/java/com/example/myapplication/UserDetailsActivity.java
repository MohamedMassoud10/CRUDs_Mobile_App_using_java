package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class UserDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        TextView tvUserName = findViewById(R.id.tvUserName);
        TextView tvUserEmail = findViewById(R.id.tvUserEmail);
        TextView tvUserId = findViewById(R.id.tvUserId);

        // Get data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String id = extras.getString("USER_ID");
            String name = extras.getString("USER_NAME");
            String email = extras.getString("USER_EMAIL");

            tvUserId.setText("ID: " + id);
            tvUserName.setText("Name: " + name);
            tvUserEmail.setText("Email: " + email);
        }
    }
}