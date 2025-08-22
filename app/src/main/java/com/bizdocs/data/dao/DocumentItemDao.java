
package com.bizdocs.data.dao;

import androidx.room.*;
import androidx.lifecycle.LiveData;
import com.bizdocs.data.models.DocumentItem;
import java.util.List;

@Dao
public interface DocumentItemDao {
    @Query("SELECT * FROM document_items WHERE documentId = :documentId")
    LiveData<List<DocumentItem>> getItemsByDocumentId(int documentId);

    @Query("SELECT * FROM document_items WHERE documentId = :documentId")
    List<DocumentItem> getItemsByDocumentIdSync(int documentId);

    @Insert
    void insertItem(DocumentItem item);

    @Insert
    void insertItems(List<DocumentItem> items);

    @Update
    void updateItem(DocumentItem item);

    @Delete
    void deleteItem(DocumentItem item);

    @Query("DELETE FROM document_items WHERE documentId = :documentId")
    void deleteItemsByDocumentId(int documentId);
}
