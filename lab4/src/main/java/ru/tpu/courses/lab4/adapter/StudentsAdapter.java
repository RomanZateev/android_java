package ru.tpu.courses.lab4.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.tpu.courses.lab4.db.Category;
import ru.tpu.courses.lab4.db.CategoryDao;
import ru.tpu.courses.lab4.db.CategoryToDB;
import ru.tpu.courses.lab4.db.Student;

public class StudentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_STUDENT = 1;
    private static final int TYPE_CATEGORY = 2;

    private List<Object> objects = new ArrayList<>();

    private CategoryDao categoryDao;

    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }
    // массив студентов
    private List<Student> students = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void categoryType(String categoryType) {
        switch (categoryType) {
            case "no":
                objects.clear();
                objects.addAll(students);
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
            case TYPE_CATEGORY:
                return new CategoryHolder(parent);
            case TYPE_STUDENT:
                return new StudentHolder(parent);
        }
        throw new IllegalArgumentException("unknown viewType = " + viewType);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
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

    private Object getListItem(int position) {
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
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onHeaderClicked(Category header) {
        int idx = objects.indexOf(header);
        if (header.isExpanded()) {
            header.collapse();
            saveExpanded(header);
            objects = getFlatItemsList();
            notifyItemRangeRemoved(idx + 1, header.getChildItemList().size());
        } else {
            header.expand();
            saveExpanded(header);
            objects = getFlatItemsList();
            notifyItemRangeInserted(idx + 1, header.getChildItemList().size());
        }
    }

    private void saveExpanded(Category category) {
        if (categoryDao.expand(category.сategoryName) == 0){
            categoryDao.insert(new CategoryToDB(category.сategoryName, category.isExpanded()));
        } else{
            categoryDao.delete(category.сategoryName);
        }
    }

    private List<Category> categories = new ArrayList<>();

    //создание категорий
    private void SetStudentsToGroups(List<Student> students) {
        List<String> studentCategoriesNames = new ArrayList<>();

        for (Student student: students){
            if (!studentCategoriesNames.contains(student.groupNumber)){
                studentCategoriesNames.add(student.groupNumber);
            }
        }

        List<Category> studentCategories = new ArrayList<>();
        for (String studentCategoryName: studentCategoriesNames) {
            List<Student> studentsInCategory = new ArrayList<>();
            for (Student student: students){
                if (studentCategoryName.equals(student.groupNumber)){
                    studentsInCategory.add(student);
                }
            }

            studentCategories.add(new Category(studentCategoryName, studentsInCategory));
        }
        categories = studentCategories;
    }

    // Создает полный список всех групп {@link Category} и студентов по порядку.
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void generateCategoryStudentItemList() {
        objects.clear();
        objects = getFlatItemsList();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void generateSexStudentItemList() {
        objects.clear();
        objects = getFlatItemsList();
    }

    private Student changeSexLanguage(Student student) {

        if (Locale.getDefault().toString().equals("English")){
            if (student.sex.equals("Мужской")){
                student.sex = "Man";
            } else if (student.sex.equals("Женский")){
                student.sex = "Woman";
            }
        } else{
            if (student.sex.equals("Man")){
                student.sex = "Мужской";
            } else if (student.sex.equals("Woman")){
                student.sex = "Женский";
            }
        }
        return student;
    }

    private void SetStudentsToSexes(List<Student> students) {

        List<String> studentCategoriesNames = new ArrayList<>();

        for (Student student: students){

            student = changeSexLanguage(student);

            if (!studentCategoriesNames.contains(student.sex)){
                studentCategoriesNames.add(student.sex);
            }
        }

        List<Category> studentCategories = new ArrayList<>();
        for (String studentCategoryName: studentCategoriesNames) {
            List<Student> studentsInCategory = new ArrayList<>();
            for (Student student: students){
                if (studentCategoryName.equals(student.sex)){
                    studentsInCategory.add(student);
                }
            }

            studentCategories.add(new Category(studentCategoryName, studentsInCategory));
        }
        categories = studentCategories;
    }

    private List<CategoryToDB> categoryToDBS = new ArrayList<>();

    public void setCategoryToDBS(List<CategoryToDB> categoryToDBS) {
        this.categoryToDBS = categoryToDBS;
    }

    // преобразование в лист
    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<Object> getFlatItemsList() {
        List<Object> items = new ArrayList<>();
        for (Category category : categories) {
            if (containsName(categoryDao.getAll(), category.сategoryName)){
                category.expand();
            }
            items.add(category);
            if (category.isExpanded()) {
                items.addAll(category.getChildItemList());
            }
        }
        return items;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean containsName(final List<CategoryToDB> list, final String name){
        return list.stream().anyMatch(o -> o.getCategoryName().equals(name));
    }
}
