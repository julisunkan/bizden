package com.bizdocs.utils;

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