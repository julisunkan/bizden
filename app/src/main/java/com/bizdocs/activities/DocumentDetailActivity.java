
package com.bizdocs.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bizdocs.R;
import com.bizdocs.BizDocsApplication;
import com.bizdocs.adapters.DocumentItemDisplayAdapter;
import com.bizdocs.data.models.Document;
import com.bizdocs.data.models.DocumentItem;
import com.bizdocs.databinding.ActivityDocumentDetailBinding;
import com.bizdocs.utils.PdfUtil;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DocumentDetailActivity extends AppCompatActivity {
    private ActivityDocumentDetailBinding binding;
    private Document document;
    private List<DocumentItem> items;
    private int documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDocumentDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        documentId = getIntent().getIntExtra("document_id", -1);
        if (documentId == -1) {
            finish();
            return;
        }

        loadDocumentData();
        setupActionBar();
    }

    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Document Details");
        }
    }

    private void loadDocumentData() {
        new Thread(() -> {
            document = ((BizDocsApplication) getApplication()).getDatabase()
                    .documentDao().getDocumentById(documentId);
            items = ((BizDocsApplication) getApplication()).getDatabase()
                    .documentItemDao().getItemsByDocumentIdSync(documentId);

            runOnUiThread(this::displayDocumentData);
        }).start();
    }

    private void displayDocumentData() {
        if (document == null) return;

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        
        binding.textDocumentNumber.setText(document.getDocumentNumber());
        binding.textDocumentType.setText(document.getType().name());
        binding.textCustomerName.setText(document.getCustomerName());
        binding.textCurrency.setText(document.getCurrency());
        binding.textCreatedDate.setText(dateFormat.format(document.getCreatedDate()));
        
        if (document.getWatermark() != null && !document.getWatermark().isEmpty()) {
            binding.textWatermark.setText(document.getWatermark());
        } else {
            binding.textWatermark.setText("No watermark");
        }

        // Display totals
        binding.textSubtotal.setText(String.format("%.2f", document.getSubtotal()));
        binding.textTaxRate.setText(String.format("%.1f%%", document.getTaxRate()));
        binding.textTaxAmount.setText(String.format("%.2f", document.getTaxAmount()));
        binding.textDiscount.setText(String.format("%.2f", document.getDiscount()));
        binding.textTotal.setText(String.format("%.2f", document.getTotal()));

        // Setup items RecyclerView
        DocumentItemDisplayAdapter adapter = new DocumentItemDisplayAdapter(items);
        binding.recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewItems.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_document_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_generate_pdf) {
            generatePdf();
            return true;
        } else if (id == R.id.action_share) {
            shareDocument();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    private void generatePdf() {
        if (document == null || items == null) return;

        new Thread(() -> {
            try {
                String pdfPath = PdfUtil.generatePdf(this, document, items);
                runOnUiThread(() -> {
                    Toast.makeText(this, "PDF generated: " + pdfPath, Toast.LENGTH_LONG).show();
                    openPdfPreview(pdfPath);
                });
            } catch (Exception e) {
                runOnUiThread(() -> 
                    Toast.makeText(this, "Error generating PDF: " + e.getMessage(), 
                                 Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void openPdfPreview(String pdfPath) {
        Intent intent = new Intent(this, PdfPreviewActivity.class);
        intent.putExtra("pdf_path", pdfPath);
        startActivity(intent);
    }

    private void shareDocument() {
        // Generate PDF and share
        generatePdf();
    }
}
