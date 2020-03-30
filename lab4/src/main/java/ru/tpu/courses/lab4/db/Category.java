package ru.tpu.courses.lab4.db;

import androidx.annotation.NonNull;
import java.util.List;

public class Category {

    @NonNull
    public String сategoryName;

    @NonNull
    private boolean expanded  = false;

    public Category(String categoryName, List<Student> students) {
        this.сategoryName = categoryName;
        this.students = students;
        this.expanded = false;
    }

    public List<Student> students;

    public List<?> getChildItemList() {
        return students;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void expand() {
        expanded = true;
    }

    public void collapse() {
        expanded = false;
    }
}