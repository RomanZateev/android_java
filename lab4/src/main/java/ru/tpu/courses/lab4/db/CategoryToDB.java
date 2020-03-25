package ru.tpu.courses.lab4.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class CategoryToDB {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    @ColumnInfo(name = "category_name")
    public String сategoryName;

    @NonNull
    @ColumnInfo(name = "expanded")
    boolean expanded;

    CategoryToDB() {
    }

    public String getCategoryName(){
        return this.сategoryName;
    }

    public CategoryToDB(@NonNull String categoryName, boolean expanded) {
        this.сategoryName = categoryName;
        this.expanded = expanded;
    }
}