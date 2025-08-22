package com.example.football_field_management.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.football_field_management.R;
import com.example.football_field_management.utils.SessionManager;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout logoutLayout;
    private MaterialCardView serviceManagementLayout, bookingManagementLayout, fieldManagementLayout, statisticsLayout, helpLayout ; // Layout quản lý dịch vụ
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logoutLayout = findViewById(R.id.button_logout);
        serviceManagementLayout = findViewById(R.id.card_service_management);
        bookingManagementLayout = findViewById(R.id.card_booking_management);
        fieldManagementLayout = findViewById(R.id.card_field_management);
        statisticsLayout = findViewById(R.id.card_statistics);
        helpLayout = findViewById(R.id.card_help);
        TextView tvWelcome = findViewById(R.id.tvWelcome);

        SessionManager sessionManager = new SessionManager(this);
        String username = sessionManager.getSession();
        tvWelcome.setText("Xin chào, " + username);

        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị hộp thoại xác nhận
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Đăng xuất")
                        .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Xóa trạng thái đăng nhập trong SharedPreferences
                                SessionManager sessionManager = new SessionManager(getApplicationContext());
                                sessionManager.logout();

                                // Chuyển về màn hình đăng nhập
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();
            }
        });
        serviceManagementLayout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ServiceManagementActivity.class);
            startActivity(intent);
        });
        serviceManagementLayout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ServiceManagementActivity.class);
            startActivity(intent);
        });
        bookingManagementLayout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BookingManagementActivity.class);
            startActivity(intent);
        });
        fieldManagementLayout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FieldManagementActivity.class);
            startActivity(intent);
        });
        statisticsLayout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(intent);
        });
        helpLayout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HelpActivity.class);
            startActivity(intent);
        });


    }

}