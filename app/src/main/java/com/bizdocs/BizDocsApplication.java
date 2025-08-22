
package com.bizdocs;

import android.app.Application;
import androidx.room.Room;
import com.bizdocs.data.database.AppDatabase;

public class BizDocsApplication extends Application {
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        
        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "bizdocs_database")
                .allowMainThreadQueries() // For simplicity in demo
                .addTypeConverter(new com.bizdocs.data.converters.DocumentTypeConverter())
                .addTypeConverter(new com.bizdocs.data.converters.DateConverter())
                .build();
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
