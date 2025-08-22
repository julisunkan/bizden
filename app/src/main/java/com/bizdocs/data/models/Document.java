
package com.bizdocs.data.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.bizdocs.data.converters.DocumentTypeConverter;
import com.bizdocs.data.converters.DateConverter;
import java.util.Date;

@Entity(tableName = "documents")
@TypeConverters({DocumentTypeConverter.class, DateConverter.class})
public class Document {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String documentNumber;
    private DocumentType type;
    private String customerName;
    private String currency;
    private double subtotal;
    private double taxRate;
    private double taxAmount;
    private double discount;
    private double total;
    private String watermark;
    private String signaturePath;
    private Date createdDate;

    // Constructors
    public Document() {
        this.createdDate = new Date();
    }

    public Document(String documentNumber, DocumentType type, String customerName, String currency) {
        this();
        this.documentNumber = documentNumber;
        this.type = type;
        this.customerName = customerName;
        this.currency = currency;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDocumentNumber() { return documentNumber; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }

    public DocumentType getType() { return type; }
    public void setType(DocumentType type) { this.type = type; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getTaxRate() { return taxRate; }
    public void setTaxRate(double taxRate) { this.taxRate = taxRate; }

    public double getTaxAmount() { return taxAmount; }
    public void setTaxAmount(double taxAmount) { this.taxAmount = taxAmount; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getWatermark() { return watermark; }
    public void setWatermark(String watermark) { this.watermark = watermark; }

    public String getSignaturePath() { return signaturePath; }
    public void setSignaturePath(String signaturePath) { this.signaturePath = signaturePath; }

    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
}
