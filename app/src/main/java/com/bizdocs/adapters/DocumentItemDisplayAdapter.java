
package com.bizdocs.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bizdocs.R;
import com.bizdocs.data.models.DocumentItem;
import java.util.List;

public class DocumentItemDisplayAdapter extends RecyclerView.Adapter<DocumentItemDisplayAdapter.ItemViewHolder> {
    private List<DocumentItem> items;

    public DocumentItemDisplayAdapter(List<DocumentItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_document_display, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        DocumentItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView textName;
        private TextView textQuantity;
        private TextView textPrice;
        private TextView textTotal;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textPrice = itemView.findViewById(R.id.textPrice);
            textTotal = itemView.findViewById(R.id.textTotal);
        }

        public void bind(DocumentItem item) {
            textName.setText(item.getName());
            textQuantity.setText(String.valueOf(item.getQuantity()));
            textPrice.setText(String.format("%.2f", item.getPrice()));
            textTotal.setText(String.format("%.2f", item.getTotal()));
        }
    }
}

public class DocumentItemDisplayAdapter extends RecyclerView.Adapter<DocumentItemDisplayAdapter.ViewHolder> {
    private List<DocumentItem> items;

    public DocumentItemDisplayAdapter(List<DocumentItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_document_display, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DocumentItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textItemName;
        private TextView textQuantity;
        private TextView textPrice;
        private TextView textTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textItemName = itemView.findViewById(R.id.textItemName);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textPrice = itemView.findViewById(R.id.textPrice);
            textTotal = itemView.findViewById(R.id.textTotal);
        }

        public void bind(DocumentItem item) {
            textItemName.setText(item.getName());
            textQuantity.setText(String.valueOf(item.getQuantity()));
            textPrice.setText(String.format("%.2f", item.getPrice()));
            textTotal.setText(String.format("%.2f", item.getTotal()));
        }
    }
}
