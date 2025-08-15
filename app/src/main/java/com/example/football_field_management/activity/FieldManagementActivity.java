package com.example.football_field_management.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.football_field_management.R;
import com.example.football_field_management.adapter.FieldAdapter;
import com.example.football_field_management.database.DatabaseHelper;
import com.example.football_field_management.model.Field;

import java.util.List;

public class FieldManagementActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FieldAdapter adapter;
    private DatabaseHelper dbHelper;
    private Button addButton;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_management);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recycler_fields);
        addButton = findViewById(R.id.button_add_field);
        backButton = findViewById(R.id.btnBackField);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Field> fields = dbHelper.getAllFields();
        adapter = new FieldAdapter(fields,
                field -> {
                    Intent intent = new Intent(this, com.example.football_field_management.activity.AddEditFieldActivity.class);
                    intent.putExtra("FIELD_ID", field.getId());
                    startActivity(intent);
                },
                field -> {
                    new AlertDialog.Builder(this)
                            .setTitle("Xóa sân bóng")
                            .setMessage("Bạn có chắc muốn xóa sân " + field.getName() + "?")
                            .setPositiveButton("Có", (dialog, which) -> {
                                dbHelper.deleteField(field.getId());
                                updateFieldList();
                            })
                            .setNegativeButton("Không", null)
                            .show();
                });
        recyclerView.setAdapter(adapter);

        addButton.setOnClickListener(v -> startActivity(new Intent(this,AddEditFieldActivity.class)));
        backButton.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFieldList();
    }

    private void updateFieldList() {
        List<Field> fields = dbHelper.getAllFields();
        adapter.updateFields(fields);
    }
}