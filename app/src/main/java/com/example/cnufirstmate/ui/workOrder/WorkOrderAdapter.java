package com.example.cnufirstmate.ui.workOrder;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WorkOrderAdapter extends RecyclerView.Adapter<WorkOrderAdapter.WorkViewHolder> {
    @NonNull
    @Override
    public WorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull WorkViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class WorkViewHolder extends RecyclerView.ViewHolder {

        public WorkViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
