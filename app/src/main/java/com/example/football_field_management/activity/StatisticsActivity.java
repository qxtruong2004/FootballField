package com.example.football_field_management.activity;


import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.football_field_management.R;
import com.example.football_field_management.database.DatabaseHelper;


public class StatisticsActivity extends AppCompatActivity {
    private TextView fieldCountTextView, serviceCountTextView, bookingCountTextView, totalPaidTextView;
    private ImageButton backButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        dbHelper = new DatabaseHelper(this);
        fieldCountTextView = findViewById(R.id.text_field_count);
        serviceCountTextView = findViewById(R.id.text_service_count);
        bookingCountTextView = findViewById(R.id.text_booking_count);
        totalPaidTextView = findViewById(R.id.text_total_paid);
        backButton = findViewById(R.id.btnBack);

        fieldCountTextView.setText(String.valueOf(dbHelper.getAllFields().size()));
        serviceCountTextView.setText(String.valueOf(dbHelper.getAllServices().size()));
        bookingCountTextView.setText(String.valueOf(dbHelper.getAllBookings().size()));
        totalPaidTextView.setText(String.format("%.2f", dbHelper.getTotalPaidAmount()));

        backButton.setOnClickListener(v -> finish());
    }
}
