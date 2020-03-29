package ru.tpu.courses.lab3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.tpu.courses.lab3.adapter.StudentsAdapter;

public class Lab3Activity extends AppCompatActivity {

    private static final int REQUEST_STUDENT_ADD = 1;

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, Lab3Activity.class);
    }

    private final StudentsCache studentsCache = StudentsCache.getInstance();

    private RecyclerView list;

    private StudentsAdapter studentsAdapter;

    private String categoryName = "no";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.lab3_students));

        setContentView(R.layout.lab3_activity);
        list = findViewById(android.R.id.list);

        FloatingActionButton fab = findViewById(R.id.fab);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);

        list.setAdapter(studentsAdapter = new StudentsAdapter());

        studentsAdapter.setStudents(studentsCache.getStudents());

        studentsAdapter.categoryType(categoryName);

        fab.setOnClickListener(
                v -> startActivityForResult(
                        AddStudentActivity.newIntent(this),
                        REQUEST_STUDENT_ADD
                )
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_STUDENT_ADD && resultCode == RESULT_OK) {
            Student student = AddStudentActivity.getResultStudent(data);

            studentsCache.addStudent(student);

            studentsAdapter.setStudents(studentsCache.getStudents());
            studentsAdapter.categoryType(categoryName);

            studentsAdapter.notifyDataSetChanged();
            list.scrollToPosition(studentsAdapter.getItemCount() - 1);
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.lab3_filter)
                    .setPositiveButton(R.string.lab3_filter_group, (dialog, id) -> {
                        studentsAdapter.categoryType("groupNumber");
                        studentsAdapter.notifyDataSetChanged();
                        setTitle(getString(R.string.lab3_groups));

                        categoryName = "groupNumber";
                    })
                    .setNegativeButton(R.string.lab3_filter_sex, (dialog, id) -> {
                        studentsAdapter.categoryType("sex");
                        studentsAdapter.notifyDataSetChanged();
                        setTitle(getString(R.string.lab3_sexes));

                        categoryName = "sex";
                    })
                    .setNeutralButton(R.string.lab3_filter_cancel, (dialog, id) -> {
                        studentsAdapter.categoryType("no");
                        studentsAdapter.notifyDataSetChanged();
                        setTitle(getString(R.string.lab3_students));

                        categoryName = "no";
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
}
