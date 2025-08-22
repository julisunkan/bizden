
package com.bizdocs.activities;

import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bizdocs.R;
import com.bizdocs.databinding.ActivityPdfPreviewBinding;
import java.io.File;
import java.io.IOException;

public class PdfPreviewActivity extends AppCompatActivity {
    private ActivityPdfPreviewBinding binding;
    private PdfRenderer pdfRenderer;
    private PdfRenderer.Page currentPage;
    private ParcelFileDescriptor parcelFileDescriptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupActionBar();
        
        String pdfPath = getIntent().getStringExtra("pdf_path");
        if (pdfPath != null) {
            loadPdf(pdfPath);
        } else {
            Toast.makeText(this, "No PDF path provided", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("PDF Preview");
        }
    }

    private void loadPdf(String pdfPath) {
        try {
            File file = new File(pdfPath);
            if (!file.exists()) {
                Toast.makeText(this, "PDF file not found", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            pdfRenderer = new PdfRenderer(parcelFileDescriptor);
            
            if (pdfRenderer.getPageCount() > 0) {
                showPage(0);
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error loading PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showPage(int index) {
        if (pdfRenderer == null) return;
        
        if (currentPage != null) {
            currentPage.close();
        }
        
        currentPage = pdfRenderer.openPage(index);
        
        // For simplicity, we'll show a message instead of rendering the PDF
        binding.textPdfInfo.setText(String.format("PDF Preview\nPage %d of %d\nFile loaded successfully", 
                                                  index + 1, pdfRenderer.getPageCount()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (currentPage != null) {
                currentPage.close();
            }
            if (pdfRenderer != null) {
                pdfRenderer.close();
            }
            if (parcelFileDescriptor != null) {
                parcelFileDescriptor.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
