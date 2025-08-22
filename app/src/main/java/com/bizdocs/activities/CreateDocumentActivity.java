
package com.bizdocs.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bizdocs.R;
import com.bizdocs.BizDocsApplication;
import com.bizdocs.adapters.DocumentItemAdapter;
import com.bizdocs.data.models.Document;
import com.bizdocs.data.models.DocumentItem;
import com.bizdocs.data.models.DocumentType;
import com.bizdocs.databinding.ActivityCreateDocumentBinding;
import com.bizdocs.utils.Calc;
import com.bizdocs.utils.Numbering;
import java.util.ArrayList;
import java.util.List;

public class CreateDocumentActivity extends AppCompatActivity {
    private ActivityCreateDocumentBinding binding;
    private DocumentItemAdapter itemAdapter;
    private List<DocumentItem> itemList = new ArrayList<>();
    private Document currentDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateDocumentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupUI();
        setupRecyclerView();
        setupListeners();
    }

    private void setupUI() {
        // Setup document type spinner
        ArrayAdapter<DocumentType> typeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, DocumentType.values());
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDocumentType.setAdapter(typeAdapter);

        // Setup currency spinner
        String[] currencies = {"USD", "EUR", "GBP", "JPY", "INR", "CAD", "AUD"};
        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, currencies);
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCurrency.setAdapter(currencyAdapter);

        // Set default values
        binding.editTextTaxRate.setText("10");
        binding.editTextDiscount.setText("0");
    }

    private void setupRecyclerView() {
        itemAdapter = new DocumentItemAdapter(itemList, this::onItemChanged, this::onItemRemoved);
        binding.recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewItems.setAdapter(itemAdapter);
    }

    private void setupListeners() {
        binding.buttonAddItem.setOnClickListener(v -> addNewItem());
        binding.buttonSaveDocument.setOnClickListener(v -> saveDocument());
        binding.buttonCalculateTotal.setOnClickListener(v -> calculateTotals());
        
        // Back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void addNewItem() {
        String itemName = binding.editTextItemName.getText().toString().trim();
        String quantityStr = binding.editTextQuantity.getText().toString().trim();
        String priceStr = binding.editTextPrice.getText().toString().trim();

        if (itemName.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Please fill all item fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityStr);
            double price = Double.parseDouble(priceStr);

            DocumentItem item = new DocumentItem(0, itemName, quantity, price);
            itemList.add(item);
            itemAdapter.notifyItemInserted(itemList.size() - 1);

            // Clear input fields
            binding.editTextItemName.setText("");
            binding.editTextQuantity.setText("");
            binding.editTextPrice.setText("");

            calculateTotals();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid quantity or price", Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateTotals() {
        double subtotal = Calc.calculateSubtotal(itemList);
        double taxRate = 0;
        double discount = 0;

        try {
            taxRate = Double.parseDouble(binding.editTextTaxRate.getText().toString());
            discount = Double.parseDouble(binding.editTextDiscount.getText().toString());
        } catch (NumberFormatException e) {
            // Use default values
        }

        double taxAmount = Calc.calculateTax(subtotal, taxRate);
        double total = Calc.calculateTotal(subtotal, taxAmount, discount);

        binding.textViewSubtotal.setText(String.format("%.2f", subtotal));
        binding.textViewTaxAmount.setText(String.format("%.2f", taxAmount));
        binding.textViewTotal.setText(String.format("%.2f", total));
    }

    private void saveDocument() {
        String customerName = binding.editTextCustomerName.getText().toString().trim();
        String watermark = binding.editTextWatermark.getText().toString().trim();

        if (customerName.isEmpty()) {
            Toast.makeText(this, "Please enter customer name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (itemList.isEmpty()) {
            Toast.makeText(this, "Please add at least one item", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create document
        DocumentType type = (DocumentType) binding.spinnerDocumentType.getSelectedItem();
        String currency = (String) binding.spinnerCurrency.getSelectedItem();
        String documentNumber = Numbering.generateDocumentNumber(type);

        Document document = new Document(documentNumber, type, customerName, currency);
        document.setWatermark(watermark);

        // Set calculated values
        double subtotal = Calc.calculateSubtotal(itemList);
        double taxRate = Double.parseDouble(binding.editTextTaxRate.getText().toString());
        double discount = Double.parseDouble(binding.editTextDiscount.getText().toString());
        double taxAmount = Calc.calculateTax(subtotal, taxRate);
        double total = Calc.calculateTotal(subtotal, taxAmount, discount);

        document.setSubtotal(subtotal);
        document.setTaxRate(taxRate);
        document.setTaxAmount(taxAmount);
        document.setDiscount(discount);
        document.setTotal(total);

        // Save to database
        new Thread(() -> {
            long documentId = ((BizDocsApplication) getApplication()).getDatabase()
                    .documentDao().insertDocument(document);

            // Save items
            for (DocumentItem item : itemList) {
                item.setDocumentId((int) documentId);
                ((BizDocsApplication) getApplication()).getDatabase()
                        .documentItemDao().insertItem(item);
            }

            runOnUiThread(() -> {
                Toast.makeText(this, "Document saved successfully", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }

    private void onItemChanged(DocumentItem item) {
        calculateTotals();
    }

    private void onItemRemoved(DocumentItem item) {
        itemList.remove(item);
        itemAdapter.notifyDataSetChanged();
        calculateTotals();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
