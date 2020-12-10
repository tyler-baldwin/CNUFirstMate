package com.example.cnufirstmate.ui.workOrder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cnufirstmate.R;
import com.example.cnufirstmate.ui.Groups.Group;
import com.example.cnufirstmate.ui.Groups.GroupAdapter;

import java.util.List;

public class WorkOrderAdapter extends RecyclerView.Adapter<WorkOrderAdapter.WorkViewHolder> {
    private List<WorkOrder> workOrders;
    private WorkOrderAdapter.OnWorkOrderClickListener listener;

    //constructor
    public WorkOrderAdapter(List<WorkOrder> workOrders, WorkOrderAdapter.OnWorkOrderClickListener listener) {
        this.workOrders = workOrders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_work_order,
                parent,
                false
        );
        return new WorkOrderAdapter.WorkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkViewHolder holder, int position) {
        holder.bind(workOrders.get(position));
    }

    public interface OnWorkOrderClickListener {
        void onClick(WorkOrder workOrder);
    }

    @Override
    public int getItemCount() {
        return workOrders.size();
    }

    public class WorkViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        WorkOrder workOrder;

        public WorkViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.work_order_textView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(workOrder);
                }
            });
        }

        public void bind(WorkOrder workOrder) {
            this.workOrder = workOrder;
            name.setText(workOrder.getIssue());
        }
    }
}
