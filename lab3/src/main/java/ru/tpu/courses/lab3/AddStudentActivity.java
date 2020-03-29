package ru.tpu.courses.lab3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class AddStudentActivity extends AppCompatActivity {

    private static final String EXTRA_STUDENT = "student";

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, AddStudentActivity.class);
    }

    public static Student getResultStudent(@NonNull Intent intent) {
        return intent.getParcelableExtra(EXTRA_STUDENT);
    }

    private final StudentsCache studentsCache = StudentsCache.getInstance();

    private EditText firstName;
    private EditText secondName;
    private EditText lastName;
    private EditText groupNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab3_activity_add_student);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        firstName = findViewById(R.id.first_name);
        secondName = findViewById(R.id.second_name);
        lastName = findViewById(R.id.last_name);
        groupNumber = findViewById(R.id.group_number);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lab3_add_student, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.action_save) {

            Spinner spinner = findViewById(R.id.sex);
            String sex = spinner.getSelectedItem().toString();

            Student student = new Student(
                    firstName.getText().toString(),
                    secondName.getText().toString(),
                    lastName.getText().toString(),
                    groupNumber.getText().toString(),
                    sex
            );

            if (TextUtils.isEmpty(student.firstName) ||
                    TextUtils.isEmpty(student.secondName) ||
                    TextUtils.isEmpty(student.lastName) ||
                    TextUtils.isEmpty(student.groupNumber)) {
                Toast.makeText(this, R.string.lab3_error_empty_fields, Toast.LENGTH_LONG).show();
                return true;
            }

            if (studentsCache.contains(student)) {
                Toast.makeText(this, R.string.lab3_error_already_exists, Toast.LENGTH_LONG).show();
                return true;
            }

            Intent data = new Intent();
            data.putExtra(EXTRA_STUDENT, student);
            setResult(RESULT_OK, data);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
