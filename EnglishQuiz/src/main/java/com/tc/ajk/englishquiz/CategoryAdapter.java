package com.tc.ajk.englishquiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tc.ajk.englishquiz.model.Category;

import java.util.List;

/**
 * Created by baskoro on 1/27/15.
 */
public class CategoryAdapter extends ArrayAdapter<Category> {
    private List<Category> objects;

    public CategoryAdapter(Context context, int textViewResourceId, List<Category> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(v == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);
        }

        Category category = objects.get(position);

        if(category != null) {
            TextView txtId = (TextView) v.findViewById(R.id.txtIdCategory);
            TextView txtCategory = (TextView) v.findViewById(R.id.txtNameCategory);

            if(txtId != null) {
                txtId.setText("ID : " + category.getId());
            }

            if(txtCategory != null) {
                txtCategory.setText(category.getName());
            }
        }

        return v;
    }
}
