package ru.tpu.courses.lab3;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.core.util.ObjectsCompat;

import java.util.Objects;

public class Student implements Parcelable {

	@NonNull
	public String firstName;
	@NonNull
	public String secondName;
	@NonNull
	public String lastName;
	@NonNull
	public String groupNumber;
	@NonNull
	public String sex;

	public Student(@NonNull String firstName, @NonNull String secondName, @NonNull String lastName, @NonNull String groupNumber, @NonNull String sex) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.secondName = secondName;
		this.groupNumber = groupNumber;
		this.sex = sex;
	}

	private Student(Parcel in) {
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

	@Override
	public int hashCode() {
		return ObjectsCompat.hash(lastName, firstName, secondName, groupNumber, sex);
	}
}
