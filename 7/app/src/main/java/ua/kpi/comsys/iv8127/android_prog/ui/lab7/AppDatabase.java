package ua.kpi.comsys.iv8127.android_prog.ui.lab7;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {BooksTable.class, SearchTable.class, GalleryEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BooksTableDao bookDao();
    public abstract SearchTableDao searchTableDao();
    public abstract GalleryDao galleryDao();
}
