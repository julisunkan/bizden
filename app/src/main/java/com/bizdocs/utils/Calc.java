
package com.bizdocs.utils;

import com.bizdocs.data.models.DocumentItem;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Calc {
    private static final int DECIMAL_PLACES = 2;
    
    public static double calculateSubtotal(List<DocumentItem> items) {
        double subtotal = 0.0;
        for (DocumentItem item : items) {
            subtotal += item.getTotal();
        }
        return roundToTwoDecimals(subtotal);
    }
    
    public static double calculateTax(double subtotal, double taxRate) {
        double tax = subtotal * (taxRate / 100.0);
        return roundToTwoDecimals(tax);
    }
    
    public static double calculateTotal(double subtotal, double tax, double discount) {
        double total = subtotal + tax - discount;
        return roundToTwoDecimals(total);
    }
    
    public static double calculateDiscount(double subtotal, double discountRate) {
        double discount = subtotal * (discountRate / 100.0);
        return roundToTwoDecimals(discount);
    }
    
    public static double applyDiscount(double amount, double discount) {
        return roundToTwoDecimals(amount - discount);
    }
    
    private static double roundToTwoDecimals(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(DECIMAL_PLACES, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static boolean isValidAmount(String amount) {
        try {
            double value = Double.parseDouble(amount);
            return value >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean isValidQuantity(String quantity) {
        try {
            int value = Integer.parseInt(quantity);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

public class Calc {
    
    public static double calculateSubtotal(double[] itemTotals) {
        double subtotal = 0;
        for (double total : itemTotals) {
            subtotal += total;
        }
        return roundToTwoDecimals(subtotal);
    }
    
    public static double calculateTaxAmount(double subtotal, double taxRate) {
        double taxAmount = subtotal * (taxRate / 100);
        return roundToTwoDecimals(taxAmount);
    }
    
    public static double calculateTotal(double subtotal, double taxAmount, double discount) {
        double total = subtotal + taxAmount - discount;
        return roundToTwoDecimals(Math.max(0, total)); // Ensure total is not negative
    }
    
    public static double calculateItemTotal(int quantity, double price) {
        return roundToTwoDecimals(quantity * price);
    }
    
    public static double roundToTwoDecimals(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
    
    public static boolean isValidNumber(String numberStr) {
        try {
            Double.parseDouble(numberStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean isValidInteger(String numberStr) {
        try {
            Integer.parseInt(numberStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
