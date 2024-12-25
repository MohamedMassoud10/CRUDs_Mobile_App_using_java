package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    EditText etName, etEmail, etId;
    Button btnInsert, btnView, btnUpdate, btnDelete;
    ListView listView;
    ArrayList<String> userList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setButtonListeners();
    }

    private void initializeViews() {
        dbHelper = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etId = findViewById(R.id.etId);
        btnInsert = findViewById(R.id.btnInsert);
        btnView = findViewById(R.id.btnView);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        listView = findViewById(R.id.listView);

        userList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        listView.setAdapter(adapter);

        // Add ListView item click listener
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = userList.get(position);

            // Parse the selected item string to get individual values
            String[] parts = selectedItem.split(",");
            String userId = parts[0].split(":")[1].trim();
            String userName = parts[1].split(":")[1].trim();
            String userEmail = parts[2].split(":")[1].trim();

            // Create intent and pass data
            Intent intent = new Intent(MainActivity.this, UserDetailsActivity.class);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("USER_NAME", userName);
            intent.putExtra("USER_EMAIL", userEmail);
            startActivity(intent);
        });
    }

    private void setButtonListeners() {
        btnInsert.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isInserted = dbHelper.insertData(name, email);
            if (isInserted) {
                Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(this, "Insertion Failed", Toast.LENGTH_SHORT).show();
            }
        });

        btnView.setOnClickListener(v -> {
            Cursor cursor = dbHelper.getAllData();
            if (cursor.getCount() == 0) {
                Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
                return;
            }

            userList.clear();
            while (cursor.moveToNext()) {
                String user = "ID: " + cursor.getString(0) +
                        ", Name: " + cursor.getString(1) +
                        ", Email: " + cursor.getString(2);
                userList.add(user);
            }
            cursor.close();
            adapter.notifyDataSetChanged();
        });

        btnUpdate.setOnClickListener(v -> {
            String id = etId.getText().toString();
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();

            if (id.isEmpty() || name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isUpdated = dbHelper.updateData(id, name, email);
            if (isUpdated) {
                Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(v -> {
            String id = etId.getText().toString();

            if (id.isEmpty()) {
                Toast.makeText(this, "Please provide an ID", Toast.LENGTH_SHORT).show();
                return;
            }

            int rowsDeleted = dbHelper.deleteData(id);
            if (rowsDeleted > 0) {
                Toast.makeText(this, "Data Deleted", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(this, "Deletion Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        etName.setText("");
        etEmail.setText("");
        etId.setText("");
    }
}