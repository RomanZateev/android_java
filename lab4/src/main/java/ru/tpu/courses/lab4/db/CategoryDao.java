package ru.tpu.courses.lab4.db;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface CategoryDao {
    @Query("SELECT * FROM categorytodb")
    List<CategoryToDB> getAll();

    @Insert
    void insert(@NonNull CategoryToDB categoryToDB);

    @Query(
            "SELECT COUNT(*) FROM categorytodb WHERE " +
                    "category_name = :categoryName"
    )
    int expand(@NonNull String categoryName);

    @Query(
            "DELETE FROM categorytodb WHERE " +
                    "category_name = :categoryName"
    )
    void delete(@NonNull String categoryName);

    @Query(
            "DELETE FROM categorytodb"
    )
    void deleteAll();
}
