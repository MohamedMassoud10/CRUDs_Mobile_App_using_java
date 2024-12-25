package com.example.myapplication;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    EditText etName, etEmail, etId;
    Button btnInsert, btnView, btnUpdate, btnDelete;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the user is logged in
        if (!isUserLoggedIn()) {
            // If not logged in, show the login screen
            showLoginScreen();
        } else {
            // If logged in, proceed with the main activity layout
            setContentView(R.layout.activity_main);
            initializeViews();
            setButtonListeners();
        }
    }

    // Check if the user is logged in using SharedPreferences
    private boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    // Save the user's login status
    private void saveUserLoginStatus(boolean status) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", status);
        editor.apply();
    }

    // Show the login screen
    private void showLoginScreen() {
        setContentView(R.layout.activity_login); // Show login layout
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            // Hardcoded credentials
            if (email.equals("Massoud@admin.com") && password.equals("Massoud")) {
                Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                saveUserLoginStatus(true); // Save login status
                // Restart the activity to show the main layout
                recreate(); // Recreate the activity after successful login
            } else {
                Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Initialize the views
    private void initializeViews() {
        dbHelper = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etId = findViewById(R.id.etId);
        btnInsert = findViewById(R.id.btnInsert);
        btnView = findViewById(R.id.btnView);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        tvResult = findViewById(R.id.tvResult);
    }

    // Set the button listeners for Insert, View, Update, Delete
    private void setButtonListeners() {
        btnInsert.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            boolean isInserted = dbHelper.insertData(name, email);
            showToast(isInserted ? getString(R.string.data_inserted) : getString(R.string.insert_failed));
            // Immediately update the displayed data after insertion
            showData();
        });

        btnView.setOnClickListener(v -> {
            showData(); // Show data immediately after clicking "View All"
        });

        btnUpdate.setOnClickListener(v -> {
            String id = etId.getText().toString();
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();

            if (id.isEmpty()) {
                showToast("Please enter an ID to update");
                return;
            }

            boolean isUpdated = dbHelper.updateData(id, name, email);
            showToast(isUpdated ? getString(R.string.data_updated) : getString(R.string.update_failed));
            // Immediately update the displayed data after update
            showData();
        });

        btnDelete.setOnClickListener(v -> {
            String id = etId.getText().toString();

            if (id.isEmpty()) {
                showToast("Please enter an ID to delete");
                return;
            }

            int deletedRows = dbHelper.deleteData(id);
            showToast(deletedRows > 0 ? getString(R.string.data_deleted) : getString(R.string.delete_failed));
            // Immediately update the displayed data after deletion
            showData();
        });
    }

    // Show data immediately
    private void showData() {
        Cursor cursor = dbHelper.getAllData();
        if (cursor.getCount() == 0) {
            tvResult.setText(getString(R.string.no_data_found));
            return;
        }
        StringBuilder sb = new StringBuilder();
        while (cursor.moveToNext()) {
            sb.append("ID: ").append(cursor.getString(0)).append("\n")
                    .append("Name: ").append(cursor.getString(1)).append("\n")
                    .append("Email: ").append(cursor.getString(2)).append("\n\n");
        }
        tvResult.setText(sb.toString());
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
