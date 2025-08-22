
package com.bizdocs.data.models;

public enum DocumentType {
    INVOICE("INV"),
    RECEIPT("REC"),
    QUOTATION("QUO"),
    ESTIMATE("EST");

    private final String prefix;

    DocumentType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
