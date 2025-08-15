package com.example.football_field_management.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.football_field_management.R;
import com.example.football_field_management.database.DatabaseHelper;
import com.example.football_field_management.model.Field;

public class AddEditFieldActivity extends AppCompatActivity {
    private EditText nameEditText, priceEditText;
    private Button saveButton;
    private ImageButton backButton;
    private DatabaseHelper dbHelper;
    private Field field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_field);

        dbHelper = new DatabaseHelper(this);
        nameEditText = findViewById(R.id.edit_field_name);
        priceEditText = findViewById(R.id.edit_field_price);
        saveButton = findViewById(R.id.button_save);
        backButton = findViewById(R.id.button_back_toolbar);

        int fieldId = getIntent().getIntExtra("FIELD_ID", -1);
        if (fieldId != -1) {
            for (Field f : dbHelper.getAllFields()) {
                if (f.getId() == fieldId) {
                    field = f;
                    break;
                }
            }
            if (field != null) {
                nameEditText.setText(field.getName());
                priceEditText.setText(String.valueOf(field.getPrice()));
            }
        }

        saveButton.setOnClickListener(v -> {
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

            if (field == null) {
                field = new Field();
                field.setName(name);
                field.setPrice(price);
                if (dbHelper.addField(field)) {
                    Toast.makeText(this, "Thêm sân bóng thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Thêm sân bóng thất bại", Toast.LENGTH_SHORT).show();
                }
            } else {
                field.setName(name);
                field.setPrice(price);
                if (dbHelper.updateField(field)) {
                    Toast.makeText(this, "Cập nhật sân bóng thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Cập nhật sân bóng thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        backButton.setOnClickListener(v -> finish());


    }
}