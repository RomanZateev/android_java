package ru.tpu.courses.lab3.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.tpu.courses.lab3.Category;
import ru.tpu.courses.lab3.Student;

public class StudentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_STUDENT = 1;
    public static final int TYPE_CATEGORY = 2;

    private static List<Object> objects = new ArrayList<>();

    // массив студентов
    private static List<Student> students = new ArrayList<>();

    public void categoryType(String categoryType)
    {
        switch (categoryType) {
            case "no":
                objects.clear();
                for (Student student: students)
                    objects.add(student);
                break;
            case "sex":
                SetStudentsToSexes(students);
                generateSexStudentItemList();
                break;
            case "groupNumber":
                SetStudentsToGroups(students);
                generateCategoryStudentItemList();
        }
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
//            case TYPE_NUMBER:
//                return new NumberHolder(parent);
            case TYPE_CATEGORY:
                return new CategoryHolder(parent);
            case TYPE_STUDENT:
                return new StudentHolder(parent);
        }
        throw new IllegalArgumentException("unknown viewType = " + viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_CATEGORY:
                CategoryHolder categoryHolder = (CategoryHolder) holder;
                Category category = (Category) objects.get(position);
                categoryHolder.categoryName.setText(category.сategoryName);

                categoryHolder.itemView.setOnClickListener(v -> {

                    onHeaderClicked(category);
                });
                break;
            case TYPE_STUDENT:
                StudentHolder studentHolder = (StudentHolder) holder;
                Student student = (Student) objects.get(position);
                studentHolder.student.setText(
                        "Студент: " +  student.lastName + " " + student.firstName + " " + student.secondName
                );
                break;
        }
    }
    @Override
    public int getItemCount() {
        return objects.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object listItem = getListItem(position);
        if (listItem instanceof Category) {
            return TYPE_CATEGORY;
        } else if (listItem == null) {
            throw new IllegalStateException("Null object added");
        } else {
            return TYPE_STUDENT;
        }
    }

    protected Object getListItem(int position) {
        boolean indexInRange = position >= 0 && position < objects.size();
        if (indexInRange) {
            return objects.get(position);
        } else {
            return null;
        }
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    // сворачивание и разворачивание категории
    private void onHeaderClicked(Category header) {
        int idx = objects.indexOf(header);
        if (header.isExpanded()) {
            header.collapse();
            objects = getFlatItemsList();
            notifyItemRangeRemoved(idx + 1, header.getChildItemList().size());
        } else {
            header.expand();
            objects = getFlatItemsList();
            notifyItemRangeInserted(idx + 1, header.getChildItemList().size());
        }
    }


    private static List<Category> categories = new ArrayList<>();

    //создание категорий
    private void SetStudentsToGroups(List<Student> students)
    {
        List<String> studentCategoriesNames = new ArrayList<>();

        for (Student student: students)
            if (!studentCategoriesNames.contains(student.groupNumber))
                studentCategoriesNames.add(student.groupNumber);

        List<Category> studentCategories = new ArrayList<>();
        for (String studentCategoryName: studentCategoriesNames)
        {
            List<Student> studentsInCategory = new ArrayList<>();

            for (Student student: students)
                if (studentCategoryName.equals(student.groupNumber))
                    studentsInCategory.add(student);

            studentCategories.add(new Category(studentCategoryName, studentsInCategory));
        }

        categories = studentCategories;
    }

    // Создает полный список всех групп {@link Category} и студентов по порядку.
    public void generateCategoryStudentItemList() {

        objects.clear();

        objects = getFlatItemsList();
    }

    public void generateSexStudentItemList()
    {
        objects.clear();

        objects = getFlatItemsList();
    }

    private void SetStudentsToSexes(List<Student> students)
    {
        List<String> studentCategoriesNames = new ArrayList<>();

        for (Student student: students)
            if (!studentCategoriesNames.contains(student.sex))
                studentCategoriesNames.add(student.sex);

        List<Category> studentCategories = new ArrayList<>();
        for (String studentCategoryName: studentCategoriesNames)
        {
            List<Student> studentsInCategory = new ArrayList<>();

            for (Student student: students)
                if (studentCategoryName.equals(student.sex))
                    studentsInCategory.add(student);

            studentCategories.add(new Category(studentCategoryName, studentsInCategory));
        }

        categories = studentCategories;
    }

    // преобразование в лист
    private List<Object> getFlatItemsList() {
        List<Object> items = new ArrayList<>();
        for (Category category : categories) {
            items.add(category);
            if (category.isExpanded()) {
                items.addAll(category.getChildItemList());
            }
        }
        return items;
    }
}
