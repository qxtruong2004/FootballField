package com.example.football_field_management.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.football_field_management.R;
import com.example.football_field_management.database.DatabaseHelper;
import com.example.football_field_management.model.Service;
import com.google.android.material.button.MaterialButton;

public class AddEditServiceActivity extends AppCompatActivity {
    private EditText nameEditText, priceEditText;

    private DatabaseHelper dbHelper;
    private MaterialButton btnSave;
    private ImageButton btnBack;
    private Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_service);

        dbHelper = new DatabaseHelper(this);
        nameEditText = findViewById(R.id.edit_service_name);
        priceEditText = findViewById(R.id.edit_service_price);
        btnSave = findViewById(R.id.button_save);
        btnBack = findViewById(R.id.btnBack);

        int serviceId = getIntent().getIntExtra("SERVICE_ID", -1);
        if (serviceId != -1) {
            for (Service s : dbHelper.getAllServices()) {
                if (s.getId() == serviceId) {
                    service = s;
                    break;
                }
            }
            if (service != null) {
                nameEditText.setText(service.getName());
                priceEditText.setText(String.valueOf(service.getPrice()));
            }
        }

        btnSave.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String priceStr = priceEditText.getText().toString().trim();

            if (name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (service == null) {
                service = new Service();
                service.setName(name);
                service.setPrice(price);
                if (dbHelper.addService(service)) {
                    Toast.makeText(this, "Thêm dịch vụ thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Thêm dịch vụ thất bại", Toast.LENGTH_SHORT).show();
                }
            } else {
                service.setName(name);
                service.setPrice(price);
                if (dbHelper.updateService(service)) {
                    Toast.makeText(this, "Cập nhật dịch vụ thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Cập nhật dịch vụ thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

}