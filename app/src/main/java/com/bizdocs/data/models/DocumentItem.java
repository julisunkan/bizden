
package com.bizdocs.data.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "document_items",
        foreignKeys = @ForeignKey(entity = Document.class,
                                parentColumns = "id",
                                childColumns = "documentId",
                                onDelete = ForeignKey.CASCADE))
public class DocumentItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private int documentId;
    private String name;
    private int quantity;
    private double price;
    private double total;

    public DocumentItem() {}

    public DocumentItem(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.total = quantity * price;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getDocumentId() { return documentId; }
    public void setDocumentId(int documentId) { this.documentId = documentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { 
        this.quantity = quantity;
        this.total = quantity * price;
    }

    public double getPrice() { return price; }
    public void setPrice(double price) { 
        this.price = price;
        this.total = quantity * price;
    }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
