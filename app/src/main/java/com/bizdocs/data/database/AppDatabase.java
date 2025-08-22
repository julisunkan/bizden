
package com.bizdocs.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.bizdocs.data.dao.DocumentDao;
import com.bizdocs.data.dao.DocumentItemDao;
import com.bizdocs.data.models.Document;
import com.bizdocs.data.models.DocumentItem;

@Database(
    entities = {Document.class, DocumentItem.class},
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DocumentDao documentDao();
    public abstract DocumentItemDao documentItemDao();
}
