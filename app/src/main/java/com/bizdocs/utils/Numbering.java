
package com.bizdocs.utils;

import com.bizdocs.data.models.DocumentType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Numbering {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HHmmss", Locale.getDefault());
    
    public static String generateDocumentNumber(DocumentType type) {
        String prefix = type.getPrefix();
        String date = DATE_FORMAT.format(new Date());
        String time = TIME_FORMAT.format(new Date());
        
        return prefix + "-" + date + "-" + time;
    }
    
    public static String generateDocumentNumber(DocumentType type, int sequenceNumber) {
        String prefix = type.getPrefix();
        String date = DATE_FORMAT.format(new Date());
        String sequence = String.format("%04d", sequenceNumber);
        
        return prefix + "-" + date + "-" + sequence;
    }
    
    public static boolean isValidDocumentNumber(String documentNumber) {
        if (documentNumber == null || documentNumber.trim().isEmpty()) {
            return false;
        }
        
        String[] parts = documentNumber.split("-");
        return parts.length >= 2 && parts[0].length() == 3;
    }
}
