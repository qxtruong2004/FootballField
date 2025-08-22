package com.example.football_field_management.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.football_field_management.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MomoQrActivity extends AppCompatActivity {

    private ImageView imgQRCode;
    private TextView tvAmount;
    private Button btnBack;

    // Số MoMo nhận
    private final String momoPhone = "0987654321";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_momo_qr);

        imgQRCode = findViewById(R.id.imgQRCode);
        tvAmount = findViewById(R.id.tvAmount);
        btnBack = findViewById(R.id.btnBack);

        // Nhận số tiền từ Intent
        Intent intent = getIntent();
        int amount = intent.getIntExtra("amount", 0);
        String message = intent.getStringExtra("message");

        // Hiển thị số tiền
        tvAmount.setText("Số tiền cần thanh toán: " + amount + " VND");

        // Tạo link MoMo
        String momoUrl = "https://nhantien.momo.vn/" + momoPhone +
                "?amount=" + amount +
                "&message=" + message;

        // Sinh QR từ link
        try {
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix bitMatrix = writer.encode(momoUrl, BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(bitMatrix);
            imgQRCode.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Quay lại
        btnBack.setOnClickListener(v -> finish());
    }
}
