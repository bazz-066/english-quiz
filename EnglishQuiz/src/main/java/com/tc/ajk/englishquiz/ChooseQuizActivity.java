package com.tc.ajk.englishquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tc.ajk.englishquiz.model.Category;

import java.util.List;

public class ChooseQuizActivity extends Activity implements View.OnClickListener, ListView.OnItemSelectedListener {

    private ListView listQuiz;
    private TextView txtChooseQuiz;
    private AdapterView.OnItemSelectedListener itemSelectedListener;
    private List<Category> alCategory;
    private DatabaseManager dbHelper;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choosequiz);
        this.listQuiz = (ListView) this.findViewById(R.id.listQuiz);
        this.txtChooseQuiz = (TextView) this.findViewById(R.id.txtChooseQuiz);
        this.dbHelper = new DatabaseManager(this);

        this.dbHelper.createDatabase();
        this.dbHelper.openDatabase();

        this.alCategory = this.dbHelper.getCategories();

        CategoryAdapter adapter = new CategoryAdapter(this, R.layout.list_item, this.alCategory);
        this.listQuiz.setAdapter(adapter);
        this.context = this;

        adapter.notifyDataSetChanged();
        this.listQuiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //txtChooseQuiz.setText((CharSequence) adapterView.getItemAtPosition(position).toString());
                Intent intent = new Intent(context, ExecuteQuizActivity.class);
                Category selectedCategory = (Category) adapterView.getItemAtPosition(position);

                intent.putExtra(Category.KEY, selectedCategory);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.choose_quiz, menu);
        return true;
    }

    public void onClick(View v)
    {
        if(v.getId() == R.id.listQuiz)
        {
            this.txtChooseQuiz.setText(this.listQuiz.getSelectedItem().toString());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.txtChooseQuiz.setText(this.listQuiz.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
