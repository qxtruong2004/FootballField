package com.example.football_field_management;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.football_field_management.adapter.BookingAdapter;
import com.example.football_field_management.database.DatabaseHelper;
import com.example.football_field_management.model.Booking;


import java.util.List;

public class BookingManagementActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private DatabaseHelper dbHelper;
    private Button addButton;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_management);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recycler_bookings);
        addButton = findViewById(R.id.button_add_booking);
        btnBack = findViewById(R.id.btnBackManagement);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Booking> bookings = dbHelper.getAllBookings();
        adapter = new BookingAdapter(bookings, dbHelper,
                booking -> {
                    Intent intent = new Intent(this, AddEditBookingActivity.class);
                    intent.putExtra("BOOKING_ID", booking.getId());
                    intent.putExtra("FIELD_ID", booking.getFieldId());
                    startActivity(intent);
                },
                booking -> {
                    new AlertDialog.Builder(this)
                            .setTitle("Xóa lịch đặt sân")
                            .setMessage("Bạn có chắc muốn xóa lịch đặt sân của " + booking.getCustomerName() + "?")
                            .setPositiveButton("Có", (dialog, which) -> {
                                dbHelper.deleteBooking(booking.getId());
                                updateBookingList();
                            })
                            .setNegativeButton("Không", null)
                            .show();
                },
                booking -> {
                    Intent intent = new Intent(this, BookingDetailActivity.class);
                    intent.putExtra("BOOKING_ID", booking.getId());
                    startActivity(intent);
                });
        recyclerView.setAdapter(adapter);

        addButton.setOnClickListener(v -> startActivity(new Intent(this, AddEditBookingActivity.class)));
        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBookingList();
    }

    private void updateBookingList() {
        List<Booking> bookings = dbHelper.getAllBookings();
        adapter.updateBookings(bookings);
    }
}