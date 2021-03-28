package ua.kpi.comsys.iv8127.android_prog.ui.lab7;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BooksTableDao {
    @Query("SELECT * FROM BooksTable")
    List<BooksTable> getAll();

    @Query("SELECT * FROM BooksTable WHERE isbn13 = :isbn13")
    BooksTable getByIsbn13(long isbn13);

    @Query("UPDATE BooksTable " +
            "SET authors = :authors, description = :desc, pages = :pages, " +
            "publisher = :publisher, rating = :rating, year = :year " +
            "WHERE isbn13 = :isbn13")
    void setInfoByIsbn13(long isbn13, String authors, String desc, long pages, String publisher, String rating, long year);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(BooksTable booksTable);

    @Update
    void update(BooksTable booksTable);

    @Delete
    void delete(BooksTable booksTable);
}
