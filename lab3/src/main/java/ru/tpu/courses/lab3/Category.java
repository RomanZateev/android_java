package ru.tpu.courses.lab3;

import androidx.annotation.NonNull;

import java.util.List;

public class Category {
    @NonNull
    public String сategoryName;

    private List<Student> students;

    public Category() {
    }

    public Category(String categoryName, List<Student> students) {
        this.сategoryName = categoryName;
        this.students = students;
    }

    public Category(String categoryName) {
        this.сategoryName = categoryName;
    }

    public List<?> getChildItemList() {
        return students;
    }

    private boolean expanded = false;

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