package com.example.football_field_management.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.football_field_management.R;
import com.example.football_field_management.model.Service;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    private List<Service> services;
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;

    public interface OnItemClickListener {
        void onItemClick(Service service);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Service service);
    }

    public ServiceAdapter(List<Service> services, OnItemClickListener listener, OnItemLongClickListener longClickListener) {
        this.services = services;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = services.get(position);
        holder.nameTextView.setText(service.getName());
        holder.priceTextView.setText(String.format("%.2f", service.getPrice()));
        holder.itemView.setOnClickListener(v -> listener.onItemClick(service));
        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onItemLongClick(service);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public void updateServices(List<Service> newServices) {
        services.clear();
        services.addAll(newServices);
        notifyDataSetChanged();
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, priceTextView;

        ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_service_name);
            priceTextView = itemView.findViewById(R.id.text_service_price);
        }
    }
}