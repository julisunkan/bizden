
package com.bizdocs.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bizdocs.R;
import com.bizdocs.data.models.Document;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {
    private List<Document> documents;
    private OnDocumentClickListener onDocumentClickListener;
    private OnDeleteClickListener onDeleteClickListener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    public interface OnDocumentClickListener {
        void onDocumentClick(Document document);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Document document);
    }

    public DocumentAdapter(List<Document> documents, OnDocumentClickListener clickListener, 
                          OnDeleteClickListener deleteListener) {
        this.documents = documents;
        this.onDocumentClickListener = clickListener;
        this.onDeleteClickListener = deleteListener;
    }

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_document, parent, false);
        return new DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {
        Document document = documents.get(position);
        holder.bind(document);
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    public class DocumentViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView textDocumentNumber;
        private TextView textDocumentType;
        private TextView textCustomerName;
        private TextView textTotal;
        private TextView textDate;
        private ImageButton buttonDelete;

        public DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardViewDocument);
            textDocumentNumber = itemView.findViewById(R.id.textDocumentNumber);
            textDocumentType = itemView.findViewById(R.id.textDocumentType);
            textCustomerName = itemView.findViewById(R.id.textCustomerName);
            textTotal = itemView.findViewById(R.id.textTotal);
            textDate = itemView.findViewById(R.id.textDate);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }

        public void bind(Document document) {
            textDocumentNumber.setText(document.getDocumentNumber());
            textDocumentType.setText(document.getType().name());
            textCustomerName.setText(document.getCustomerName());
            textTotal.setText(String.format("%.2f %s", document.getTotal(), document.getCurrency()));
            textDate.setText(dateFormat.format(document.getCreatedDate()));

            cardView.setOnClickListener(v -> {
                if (onDocumentClickListener != null) {
                    onDocumentClickListener.onDocumentClick(document);
                }
            });

            buttonDelete.setOnClickListener(v -> {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(document);
                }
            });
        }
    }
}
