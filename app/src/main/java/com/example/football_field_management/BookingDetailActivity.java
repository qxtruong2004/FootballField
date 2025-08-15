package com.example.football_field_management;


import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.football_field_management.database.DatabaseHelper;
import com.example.football_field_management.model.Booking;
import com.example.football_field_management.model.ServiceUsage;

import java.util.List;

public class BookingDetailActivity extends AppCompatActivity {
    private TextView fieldTextView, customerTextView, phoneTextView, dateTextView, timeTextView, totalTextView;
    private RecyclerView serviceRecyclerView;
    private Button payButton;
    private DatabaseHelper dbHelper;
    private Booking booking;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        dbHelper = new DatabaseHelper(this);
        fieldTextView = findViewById(R.id.text_field);
        customerTextView = findViewById(R.id.text_customer);
        phoneTextView = findViewById(R.id.text_phone);
//        dateTextView = findViewBy guardianshipId(R.id.text_date);
        dateTextView = findViewById(R.id.text_date);
        timeTextView = findViewById(R.id.text_time);
        totalTextView = findViewById(R.id.text_total);
        serviceRecyclerView = findViewById(R.id.recycler_services);
        payButton = findViewById(R.id.button_pay);
        btnBack = findViewById(R.id.btnBackDetail);

        int bookingId = getIntent().getIntExtra("BOOKING_ID", -1);
        if (bookingId != -1) {
            for (Booking b : dbHelper.getAllBookings()) {
                if (b.getId() == bookingId) {
                    booking = b;
                    break;
                }
            }
            if (booking != null) {
                fieldTextView.setText(dbHelper.getFieldNameById(booking.getFieldId()));
                customerTextView.setText(booking.getCustomerName());
                phoneTextView.setText(booking.getPhone());
                dateTextView.setText(booking.getDate());
                timeTextView.setText(booking.getTime());

                double total = dbHelper.getFieldPriceById(booking.getFieldId());
                List<ServiceUsage> usages = dbHelper.getServiceUsagesForBooking(bookingId);
                for (ServiceUsage usage : usages) {
                    double servicePrice = dbHelper.getServicePriceById(usage.getServiceId());
                    total += servicePrice * usage.getQuantity();
                }
                totalTextView.setText(String.format("%.2f", total));

                serviceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                // Assuming a simple TextView-based adapter for services
                serviceRecyclerView.setAdapter(new RecyclerView.Adapter() {
                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        TextView textView = new TextView(parent.getContext());
                        textView.setPadding(16, 8, 16, 8);
                        return new RecyclerView.ViewHolder(textView) {};
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        ServiceUsage usage = usages.get(position);
                        String text = dbHelper.getServiceNameById(usage.getServiceId()) + ": " + usage.getQuantity();
                        ((TextView) holder.itemView).setText(text);
                    }

                    @Override
                    public int getItemCount() {
                        return usages.size();
                    }
                });

                if (booking.isPaid()) {
                    payButton.setEnabled(false);
                    payButton.setText("Đã thanh toán");
                }
            }
        }

        payButton.setOnClickListener(v -> {
            if (booking != null && !booking.isPaid()) {
                dbHelper.markBookingAsPaid(booking.getId());
                Toast.makeText(this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}