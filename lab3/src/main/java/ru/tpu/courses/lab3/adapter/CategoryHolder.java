package ru.tpu.courses.lab3.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.tpu.courses.lab3.R;

import androidx.recyclerview.widget.RecyclerView;

class CategoryHolder extends  RecyclerView.ViewHolder{

    public final TextView categoryName;

    CategoryHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.lab3_item_category, parent, false));
        categoryName = itemView.findViewById(R.id.categoryName);
    }
}