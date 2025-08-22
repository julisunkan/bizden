
package com.bizdocs.data.dao;

import androidx.room.*;
import androidx.lifecycle.LiveData;
import com.bizdocs.data.models.Document;
import java.util.List;

@Dao
public interface DocumentDao {
    @Query("SELECT * FROM documents ORDER BY createdDate DESC")
    LiveData<List<Document>> getAllDocuments();

    @Query("SELECT * FROM documents WHERE id = :id")
    Document getDocumentById(int id);

    @Insert
    long insertDocument(Document document);

    @Update
    void updateDocument(Document document);

    @Delete
    void deleteDocument(Document document);

    @Query("SELECT COUNT(*) FROM documents")
    int getDocumentCount();
}
