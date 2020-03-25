package ru.tpu.courses.lab4.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.core.util.ObjectsCompat;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Для передачи самописных объектов через {@link android.content.Intent} или
 * {@link android.os.Bundle}, необходимо реализовать интерфейс {@link Parcelable}. В нём описывается
 * как сохранить и восстановить объект используя примитивные свойства (String, int и т.д.).
 */
@Entity
public class Student implements Parcelable {

	@PrimaryKey(autoGenerate = true)
	public int id;
	@NonNull
	@ColumnInfo(name = "first_name")
	public String firstName;
	@NonNull
	@ColumnInfo(name = "second_name")
	public String secondName;
	@NonNull
	@ColumnInfo(name = "last_name")
	public String lastName;
	@NonNull
	@ColumnInfo(name = "group_number")
	public String groupNumber;
	@NonNull
	@ColumnInfo(name = "sex")
	public String sex;

	public Student(@NonNull String firstName, @NonNull String secondName, @NonNull String lastName, @NonNull String groupNumber, @NonNull String sex) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.secondName = secondName;
		this.groupNumber = groupNumber;
		this.sex = sex;
	}

	private Student(Parcel in) {
		id = in.readInt();
		firstName = Objects.requireNonNull(in.readString());
		lastName = Objects.requireNonNull(in.readString());
		secondName = Objects.requireNonNull(in.readString());
		groupNumber = Objects.requireNonNull(in.readString());
		sex = Objects.requireNonNull(in.readString());
	}

	public static final Creator<Student> CREATOR = new Creator<Student>() {
		@Override
		public Student createFromParcel(Parcel in) {
			return new Student(in);
		}

		@Override
		public Student[] newArray(int size) {
			return new Student[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(firstName);
		dest.writeString(lastName);
		dest.writeString(secondName);
		dest.writeString(groupNumber);
		dest.writeString(sex);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Student)) return false;
		Student student = (Student) o;
		return lastName.equals(student.lastName) &&
				firstName.equals(student.firstName) &&
				secondName.equals(student.secondName)&&
				groupNumber.equals(student.groupNumber)&&
				sex.equals(student.sex);
	}
}
