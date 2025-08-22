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
import com.example.football_field_management.adapter.ServiceAdapter;
import com.example.football_field_management.database.DatabaseHelper;
import com.example.football_field_management.model.Service;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ServiceManagementActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ServiceAdapter adapter;
    private DatabaseHelper dbHelper;
    private ImageButton btnBack;
    private MaterialButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_management);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recycler_services);
        addButton = findViewById(R.id.button_add_service);
        btnBack = findViewById(R.id.btnBack);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Service> services = dbHelper.getAllServices();
        adapter = new ServiceAdapter(services,
                service -> {
                    Intent intent = new Intent(this, AddEditServiceActivity.class);
                    intent.putExtra("SERVICE_ID", service.getId());
                    startActivity(intent);
                },
                service -> {
                    new AlertDialog.Builder(this)
                            .setTitle("Xóa dịch vụ")
                            .setMessage("Bạn có chắc muốn xóa dịch vụ " + service.getName() + "?")
                            .setPositiveButton("Có", (dialog, which) -> {
                                dbHelper.deleteService(service.getId());
                                updateServiceList();
                            })
                            .setNegativeButton("Không", null)
                            .show();
                });
        recyclerView.setAdapter(adapter);

        addButton.setOnClickListener(v -> startActivity(new Intent(this, AddEditServiceActivity.class)));
        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateServiceList();
    }

    private void updateServiceList() {
        List<Service> services = dbHelper.getAllServices();
        adapter.updateServices(services);
    }
}