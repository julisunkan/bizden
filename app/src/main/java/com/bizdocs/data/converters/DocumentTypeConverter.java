package com.bizdocs.data.converters;

import androidx.room.TypeConverter;
import com.bizdocs.data.models.DocumentType;

public class DocumentTypeConverter {
    @TypeConverter
    public static String fromDocumentType(DocumentType type) {
        return type == null ? null : type.name();
    }

    @TypeConverter
    public static DocumentType toDocumentType(String type) {
        return type == null ? null : DocumentType.valueOf(type);
    }
}