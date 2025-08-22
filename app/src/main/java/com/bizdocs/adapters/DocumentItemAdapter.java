
package com.bizdocs.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bizdocs.R;
import com.bizdocs.data.models.DocumentItem;
import java.util.List;

public class DocumentItemAdapter extends RecyclerView.Adapter<DocumentItemAdapter.ItemViewHolder> {
    private List<DocumentItem> items;
    private OnItemChangedListener onItemChangedListener;
    private OnItemRemovedListener onItemRemovedListener;

    public interface OnItemChangedListener {
        void onItemChanged(DocumentItem item);
    }

    public interface OnItemRemovedListener {
        void onItemRemoved(DocumentItem item);
    }

    public DocumentItemAdapter(List<DocumentItem> items, OnItemChangedListener changedListener,
                              OnItemRemovedListener removedListener) {
        this.items = items;
        this.onItemChangedListener = changedListener;
        this.onItemRemovedListener = removedListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_document_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        DocumentItem item = items.get(position);
        holder.bind(item, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private EditText editTextName;
        private EditText editTextQuantity;
        private EditText editTextPrice;
        private TextView textViewTotal;
        private ImageButton buttonRemove;
        private DocumentItem currentItem;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            editTextName = itemView.findViewById(R.id.editTextItemName);
            editTextQuantity = itemView.findViewById(R.id.editTextQuantity);
            editTextPrice = itemView.findViewById(R.id.editTextPrice);
            textViewTotal = itemView.findViewById(R.id.textViewTotal);
            buttonRemove = itemView.findViewById(R.id.buttonRemove);

            setupTextWatchers();
        }

        private void setupTextWatchers() {
            editTextName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (currentItem != null) {
                        currentItem.setName(s.toString());
                        notifyItemChanged();
                    }
                }
            });

            editTextQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (currentItem != null) {
                        try {
                            int quantity = Integer.parseInt(s.toString());
                            currentItem.setQuantity(quantity);
                            updateTotal();
                            notifyItemChanged();
                        } catch (NumberFormatException e) {
                            // Handle invalid input
                        }
                    }
                }
            });

            editTextPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (currentItem != null) {
                        try {
                            double price = Double.parseDouble(s.toString());
                            currentItem.setPrice(price);
                            updateTotal();
                            notifyItemChanged();
                        } catch (NumberFormatException e) {
                            // Handle invalid input
                        }
                    }
                }
            });
        }

        public void bind(DocumentItem item, int position) {
            currentItem = item;
            
            editTextName.setText(item.getName());
            editTextQuantity.setText(String.valueOf(item.getQuantity()));
            editTextPrice.setText(String.valueOf(item.getPrice()));
            updateTotal();

            buttonRemove.setOnClickListener(v -> {
                if (onItemRemovedListener != null) {
                    onItemRemovedListener.onItemRemoved(item);
                }
            });
        }

        private void updateTotal() {
            if (currentItem != null) {
                double total = currentItem.getQuantity() * currentItem.getPrice();
                currentItem.setTotal(total);
                textViewTotal.setText(String.format("%.2f", total));
            }
        }

        private void notifyItemChanged() {
            if (onItemChangedListener != null) {
                onItemChangedListener.onItemChanged(currentItem);
            }
        }
    }
}
