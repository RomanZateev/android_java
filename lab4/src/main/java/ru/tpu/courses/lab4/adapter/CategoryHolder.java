package ru.tpu.courses.lab4.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ru.tpu.courses.lab4.R;

class CategoryHolder extends  RecyclerView.ViewHolder{

    public final TextView categoryName;

    CategoryHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.lab3_item_category, parent, false));
        categoryName = itemView.findViewById(R.id.categoryName);
    }
}