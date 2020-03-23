package ru.tpu.courses.lab3.adapter;

import android.icu.text.StringSearch;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.tpu.courses.lab3.Category;
import ru.tpu.courses.lab3.Student;

/**
 * Задача Адаптера - управление View, которые содержатся в RecyclerView, с учётом его жизненного цикла.
 * Адаптер работает не с View, а с {@link RecyclerView.ViewHolder}. Этот класс содержит не только
 * View, которая будет показана на экране, но и дополнительную информацию, вроде позиции элемента
 * в списке.
 * <p>
 * Сначала мы переопределяем метод {@link #getItemCount()}. В нём необходимо вернуть количество
 * элементов в списке. В нашем случае это количество студентов помноженное на 2, т.к. на каждого
 * студента идёт 2 отдельных View. Одна - с номером студента, другая - с его ФИО.
 * <p>
 * Т.к. у нас идёт 2 разных типа View, то мы переопределяем метод {@link #getItemViewType(int)},
 * в котором должны вернуть номер типа View для переданной в методе позиции списка.
 * <p>
 * В методе {@link #onCreateViewHolder(ViewGroup, int)} мы создаём ViewHolder для
 * соответствующего типа View. Здесь мы производим инфлейт View из XML и ищем нужные нам View
 * в их иерархии.
 * <p>
 * В методе {@link #onBindViewHolder(RecyclerView.ViewHolder, int)} мы описываем заполнение
 * ViewHolder-а данными, соответствующими переданной нам позиции.
 * <p>
 * Когда мы только вызвали {@link RecyclerView#setAdapter(RecyclerView.Adapter)}, согласно алгоритму
 * лэйаута описанному в LayoutManager, RecyclerView начинает вызывать методы адаптера,
 * чтобы расположить столько элементов, сколько помещается на экране.
 * Для каждого такого элемента вызывается сначала getItemViewType, с полученным itemViewType
 * вызывается метод onCreateViewHolder и созданный viewHolder передаётся в onBindViewHolder для заполнения
 * данными. После этого измеряются размеры элемента и добавляются в RecyclerView. Как только мы вышли
 * за пределы размеров RecyclerView, процесс останавливается. При скролле списка вниз, верхние
 * ViewHolder, которые стали не видны, открепляются от RecyclerView и добавляются в
 * {@link RecyclerView.RecycledViewPool}. Снизу же, когда появляется пустое пространство, в
 * RecyclerViewPool ищется ViewHolder для viewType следующего элемента. Если такой найден, то для него
 * вызывается onBindViewHolder и ViewHolder добавляется снизу.
 * <p>
 * Для того, чтобы сказать RecyclerView, что список был обновлён, используются методы
 * {@link #notifyDataSetChanged()}, {@link #notifyItemInserted(int)} и т.д. notifyDataSetChanged
 * обновляет весь список, а остальные методы notify... говорят об изменении конкретного элемента и
 * что с ним произошло, что позволяет делать анимированные изменения в списке. При этом RecyclerView
 * всё также будет работать с теми же закэшированными ViewHolder и не будет пересоздавать все View.
 */
public class StudentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

//    public static final int TYPE_NUMBER = 0;
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
//            case TYPE_NUMBER:
//                NumberHolder numberHolder = (NumberHolder) holder;
//                // Высчитыванием номер студента начиная с 1
//                numberHolder.bind((position + 1) / 2);
//                break;
            case TYPE_CATEGORY:
                CategoryHolder categoryHolder = (CategoryHolder) holder;
                Category category = (Category) objects.get(position);
                categoryHolder.categoryName.setText(category.сategoryName);

                categoryHolder.itemView.setOnClickListener(v -> {
//                    // Get the current state of the item
//                    boolean expanded = movie.isExpanded();
//                    // Change the state
//                    movie.setExpanded(!expanded);
//                    // Notify the adapter that item has changed
//                    notifyItemChanged(position);
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

//    @Override
//    public int getItemCount() {
//        return students.size() * 2;
//    }

//    @Override
//    public int getItemViewType(int position) {
//        return position % 2 == 0 ? TYPE_NUMBER : TYPE_STUDENT;
//    }

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
        categoryType("no");
    }

//    public void setObjects(List<Object> objects) {
//        this.objects = objects;
//    }

    // сворачивание и разворачивание категории
    private void onHeaderClicked(Category header) {
        //int idx = objects.indexOf(header);
        int idx = objects.indexOf(header);
        if (header.isExpanded()) {
            header.collapse();
//            objects.remove(idx + 1);
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
