package ru.tpu.courses.lab4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.tpu.courses.lab4.adapter.StudentsAdapter;
import ru.tpu.courses.lab4.db.CategoryDao;
import ru.tpu.courses.lab4.db.Lab4Database;
import ru.tpu.courses.lab4.db.Student;
import ru.tpu.courses.lab4.db.StudentDao;

public class Lab4Activity extends AppCompatActivity {

    private static final int REQUEST_STUDENT_ADD = 1;

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, Lab4Activity.class);
    }

    private StudentDao studentDao;
    private CategoryDao categoryDao;
    private RecyclerView list;
    private StudentsAdapter studentsAdapter;
    private String categoryName = "no";

    public void clearCategories(){
        categoryDao.deleteAll();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        studentDao = Lab4Database.getInstance(this).studentDao();
        categoryDao = Lab4Database.getInstance(this).categoryDao();

        setTitle("Lab 4");

        setContentView(R.layout.lab3_activity);
        list = findViewById(android.R.id.list);

        FloatingActionButton fab = findViewById(R.id.fab);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        list.setAdapter(studentsAdapter = new StudentsAdapter());
        studentsAdapter.setCategoryDao(categoryDao);

        loadCategoryName();

        studentsAdapter.setCategoryToDBS(categoryDao.getAll());
        studentsAdapter.setStudents(studentDao.getAll());
        studentsAdapter.categoryType(categoryName);

        fab.setOnClickListener(
                v -> startActivityForResult(
                        AddStudentActivity.newIntent(this),
                        REQUEST_STUDENT_ADD
                )
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_STUDENT_ADD && resultCode == RESULT_OK) {
            Student student = AddStudentActivity.getResultStudent(data);

            studentDao.insert(student);

            studentsAdapter.setStudents(studentDao.getAll());
            studentsAdapter.categoryType(categoryName);
            studentsAdapter.notifyDataSetChanged();
            list.scrollToPosition(studentsAdapter.getItemCount() - 1);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean onOptionsItemSelected(MenuItem item) {
        // Если пользователь нажал "Фильтр"
        if (item.getItemId() == R.id.action_filter) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.lab3_filter)
                    .setPositiveButton(R.string.lab3_filter_group, (dialog, id) -> {
                        studentsAdapter.categoryType("groupNumber");
                        studentsAdapter.notifyDataSetChanged();
                        categoryName = "groupNumber";
                        clearCategories();
                    })
                    .setNegativeButton(R.string.lab3_filter_sex, (dialog, id) -> {
                        studentsAdapter.categoryType("sex");
                        studentsAdapter.notifyDataSetChanged();
                        setTitle(getString(R.string.lab3_sexes));
                        categoryName = "sex";
                        clearCategories();
                    })
                    .setNeutralButton(R.string.lab3_filter_cancel, (dialog, id) -> {
                        studentsAdapter.categoryType("no");
                        studentsAdapter.notifyDataSetChanged();
                        categoryName = "no";
                        clearCategories();
                    });
            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lab3_filter_students, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveCategoryName();
    }

    SharedPreferences sPref;
    final String SAVED_CATEGORY = "saved_category";

    void saveCategoryName() {
        sPref = getPreferences(MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.putString(SAVED_CATEGORY, categoryName);
        ed.apply();
    }

    void loadCategoryName() {
        sPref = getPreferences(MODE_PRIVATE);
        String loadedCategoryName = sPref.getString(SAVED_CATEGORY, "");

        assert loadedCategoryName != null;
        if (!loadedCategoryName.equals("")){
            categoryName = loadedCategoryName;
        }
    }
}
