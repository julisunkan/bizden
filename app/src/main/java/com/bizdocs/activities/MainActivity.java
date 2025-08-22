
package com.bizdocs.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bizdocs.R;
import com.bizdocs.BizDocsApplication;
import com.bizdocs.adapters.DocumentAdapter;
import com.bizdocs.data.models.Document;
import com.bizdocs.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private DocumentAdapter adapter;
    private List<Document> documentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupRecyclerView();
        setupFAB();
        loadDocuments();
        seedDatabaseIfEmpty();
    }

    private void setupRecyclerView() {
        adapter = new DocumentAdapter(documentList, this::onDocumentClick, this::onDeleteClick);
        binding.recyclerViewDocuments.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewDocuments.setAdapter(adapter);
    }

    private void setupFAB() {
        binding.fabAddDocument.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateDocumentActivity.class);
            startActivity(intent);
        });
    }

    private void loadDocuments() {
        ((BizDocsApplication) getApplication()).getDatabase().documentDao()
                .getAllDocuments().observe(this, new Observer<List<Document>>() {
            @Override
            public void onChanged(List<Document> documents) {
                documentList.clear();
                documentList.addAll(documents);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void onDocumentClick(Document document) {
        Intent intent = new Intent(this, DocumentDetailActivity.class);
        intent.putExtra("document_id", document.getId());
        startActivity(intent);
    }

    private void onDeleteClick(Document document) {
        ((BizDocsApplication) getApplication()).getDatabase().documentDao()
                .deleteDocument(document);
    }

    private void seedDatabaseIfEmpty() {
        new Thread(() -> {
            int count = ((BizDocsApplication) getApplication()).getDatabase()
                    .documentDao().getDocumentCount();
            if (count == 0) {
                // Seed with demo document
                // Implementation will be added in utils
            }
        }).start();
    }
}
