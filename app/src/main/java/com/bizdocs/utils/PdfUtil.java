package com.bizdocs.utils;

import android.content.Context;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import android.os.Environment;
import com.bizdocs.data.models.Document;
import com.bizdocs.data.models.DocumentItem;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PdfUtil {
    private static final int PAGE_WIDTH = 595; // A4 width in points
    private static final int PAGE_HEIGHT = 842; // A4 height in points
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    public static String generatePdf(Context context, Document document, List<DocumentItem> items) {
        try {
            PdfDocument pdfDocument = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);

            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();

            // Draw document content
            drawDocumentContent(canvas, paint, document, items);

            pdfDocument.finishPage(page);

            // Save PDF to external storage
            String fileName = document.getDocumentNumber() + ".pdf";
            File pdfDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "BizDocs");
            if (!pdfDir.exists()) {
                pdfDir.mkdirs();
            }

            File pdfFile = new File(pdfDir, fileName);
            FileOutputStream fos = new FileOutputStream(pdfFile);
            pdfDocument.writeTo(fos);
            pdfDocument.close();
            fos.close();

            return pdfFile.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    private static void drawDocumentContent(Canvas canvas, Paint paint, Document document, List<DocumentItem> items) {
        int yPosition = 50;

        // Set text properties
        paint.setColor(Color.BLACK);
        paint.setTextSize(24);
        paint.setTextAlign(Paint.Align.LEFT);

        // Document header
        canvas.drawText(document.getDocumentNumber(), 50, yPosition, paint);
        yPosition += 40;

        paint.setTextSize(18);
        canvas.drawText(document.getType().name(), 50, yPosition, paint);
        yPosition += 30;

        canvas.drawText("Customer: " + document.getCustomerName(), 50, yPosition, paint);
        yPosition += 30;

        canvas.drawText("Date: " + DATE_FORMAT.format(document.getCreatedDate()), 50, yPosition, paint);
        yPosition += 40;

        // Items header
        paint.setTextSize(16);
        canvas.drawText("Item", 50, yPosition, paint);
        canvas.drawText("Qty", 200, yPosition, paint);
        canvas.drawText("Price", 280, yPosition, paint);
        canvas.drawText("Total", 380, yPosition, paint);
        yPosition += 25;

        // Draw line
        paint.setStrokeWidth(2);
        canvas.drawLine(50, yPosition, 450, yPosition, paint);
        yPosition += 20;

        // Items
        paint.setTextSize(14);
        for (DocumentItem item : items) {
            canvas.drawText(item.getName(), 50, yPosition, paint);
            canvas.drawText(String.valueOf(item.getQuantity()), 200, yPosition, paint);
            canvas.drawText(String.format(Locale.getDefault(), "%.2f", item.getPrice()), 280, yPosition, paint);
            canvas.drawText(String.format(Locale.getDefault(), "%.2f", item.getTotal()), 380, yPosition, paint);
            yPosition += 20;
        }

        yPosition += 20;

        // Totals section
        paint.setTextSize(16);
        canvas.drawText("Subtotal: " + document.getCurrency() + " " + String.format(Locale.getDefault(), "%.2f", document.getSubtotal()), 280, yPosition, paint);
        yPosition += 25;

        if (document.getTaxRate() > 0) {
            canvas.drawText("Tax (" + document.getTaxRate() + "%): " + document.getCurrency() + " " + String.format(Locale.getDefault(), "%.2f", document.getTaxAmount()), 280, yPosition, paint);
            yPosition += 25;
        }

        if (document.getDiscount() > 0) {
            canvas.drawText("Discount: " + document.getCurrency() + " " + String.format(Locale.getDefault(), "%.2f", document.getDiscount()), 280, yPosition, paint);
            yPosition += 25;
        }

        paint.setTextSize(18);
        paint.setFakeBoldText(true);
        canvas.drawText("Total: " + document.getCurrency() + " " + String.format(Locale.getDefault(), "%.2f", document.getTotal()), 280, yPosition, paint);

        // Add watermark if present
        if (document.getWatermark() != null && !document.getWatermark().isEmpty()) {
            paint.setTextSize(48);
            paint.setColor(Color.LTGRAY);
            paint.setAlpha(100);
            canvas.save();
            canvas.rotate(-45, PAGE_WIDTH/2, PAGE_HEIGHT/2);
            canvas.drawText(document.getWatermark(), PAGE_WIDTH/2 - 100, PAGE_HEIGHT/2, paint);
            canvas.restore();
        }
    }

    public static Bitmap generateSignature(String text) {
        Bitmap bitmap = Bitmap.createBitmap(300, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        paint.setColor(Color.BLUE);
        paint.setTextSize(24);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);

        canvas.drawColor(Color.WHITE);
        canvas.drawText(text, 20, 60, paint);

        return bitmap;
    }
}