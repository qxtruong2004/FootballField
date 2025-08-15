package com.example.football_field_management.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.football_field_management.R;
import com.example.football_field_management.model.Field;

import java.util.List;

public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.FieldViewHolder> {
    private List<Field> fields;
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;

    public interface OnItemClickListener {
        void onItemClick(Field field);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Field field);
    }

    public FieldAdapter(List<Field> fields, OnItemClickListener listener, OnItemLongClickListener longClickListener) {
        this.fields = fields;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public FieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_field, parent, false);
        return new FieldViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FieldViewHolder holder, int position) {
        Field field = fields.get(position);
        holder.nameTextView.setText(field.getName());
        holder.priceTextView.setText(String.format("%.2f", field.getPrice()));
        holder.itemView.setOnClickListener(v -> listener.onItemClick(field));
        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onItemLongClick(field);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return fields.size();
    }

    public void updateFields(List<Field> newFields) {
        fields.clear();
        fields.addAll(newFields);
        notifyDataSetChanged();
    }

    static class FieldViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, priceTextView;

        FieldViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_field_name);
            priceTextView = itemView.findViewById(R.id.text_field_price);
        }
    }
}