package ru.tpu.courses.lab4.db;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Data access object (DAO), содержит методы доступа к данным. В нашим случае - SQL запросы к БД.
 * По аналогии с @Database классом, в случае работы Room мы описываем только сами SQL запросы, а маппинг
 * результатов выполнения запросов в сами объекты (например в методе {@link #getAll()}) выполняется
 * за нас библиотекой. Подробнее о построении DAO можно прочитать в оффициальной документации:
 * https://developer.android.com/training/data-storage/room/accessing-data.html
 */
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
