package com.example.football_field_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.football_field_management.R;
import com.example.football_field_management.database.DatabaseHelper;
import com.example.football_field_management.model.Booking;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private List<Booking> bookings;
    private DatabaseHelper dbHelper;
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;
    private OnCalculateClickListener calculateListener;

    public interface OnItemClickListener {
        void onItemClick(Booking booking);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Booking booking);
    }

    public interface OnCalculateClickListener {
        void onCalculateClick(Booking booking);
    }

    public BookingAdapter(List<Booking> bookings, DatabaseHelper dbHelper,
                          OnItemClickListener clickListener, OnItemLongClickListener longClickListener,
                          OnCalculateClickListener calculateListener) {
        this.bookings = bookings;
        this.dbHelper = dbHelper;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
        this.calculateListener = calculateListener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        Context context = holder.itemView.getContext();

        String fieldName = dbHelper.getFieldNameById(booking.getFieldId());
        holder.fieldTextView.setText(fieldName);
        holder.customerTextView.setText(booking.getCustomerName());
        holder.dateTextView.setText(booking.getDate());

        // Cập nhật trạng thái và màu sắc
        if (booking.isPaid()) {
            holder.statusTextView.setText("Đã thanh toán");
            holder.statusTextView.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
            holder.calculateButton.setEnabled(false);
            holder.calculateButton.setText("ĐÃ THANH TOÁN");
            holder.calculateButton.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            holder.calculateButton.setAlpha(0.5f); // Làm mờ button
            holder.calculateButton.setOnClickListener(null); // Không xử lý click
        } else {
            holder.statusTextView.setText("Chưa thanh toán");
            holder.statusTextView.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));

            // Enable nút tính tiền khi chưa thanh toán
            holder.calculateButton.setEnabled(true);
            holder.calculateButton.setText("TÍNH TIỀN");
            holder.calculateButton.setAlpha(1.0f); // Hiển thị bình thường
            holder.calculateButton.setOnClickListener(v -> calculateListener.onCalculateClick(booking));
        }

        // Click listeners cho item
        holder.itemView.setOnClickListener(v -> clickListener.onItemClick(booking));
        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onItemLongClick(booking);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public void updateBookings(List<Booking> newBookings) {
        bookings.clear();
        bookings.addAll(newBookings);
        notifyDataSetChanged();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView fieldTextView, customerTextView, dateTextView, statusTextView;
        Button calculateButton;

        BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            fieldTextView = itemView.findViewById(R.id.text_booking_field);
            customerTextView = itemView.findViewById(R.id.text_booking_customer);
            dateTextView = itemView.findViewById(R.id.text_booking_date);
            statusTextView = itemView.findViewById(R.id.text_booking_status);
            calculateButton = itemView.findViewById(R.id.button_calculate);
        }
    }
}