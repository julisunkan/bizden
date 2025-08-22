
package com.bizdocs.data.converters;

import androidx.room.TypeConverter;
import com.bizdocs.data.models.DocumentType;

public class DocumentTypeConverter {
    @TypeConverter
    public static DocumentType fromString(String value) {
        return value == null ? null : DocumentType.valueOf(value);
    }

    @TypeConverter
    public static String fromDocumentType(DocumentType type) {
        return type == null ? null : type.name();
    }
}
