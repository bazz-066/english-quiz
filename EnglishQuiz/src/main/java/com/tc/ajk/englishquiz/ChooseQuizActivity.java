package com.tc.ajk.englishquiz;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChooseQuizActivity extends Activity implements View.OnClickListener, ListView.OnItemSelectedListener {

    private ListView listQuiz;
    private TextView txtChooseQuiz;
    private ArrayList<String> alListItem;
    private AdapterView.OnItemSelectedListener itemSelectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choosequiz);
        this.listQuiz = (ListView) this.findViewById(R.id.listQuiz);
        this.txtChooseQuiz = (TextView) this.findViewById(R.id.txtChooseQuiz);
        this.alListItem = new ArrayList<String>();
        this.alListItem.add("Quiz 1");
        this.alListItem.add("Quiz 2");
        this.alListItem.add("Quiz 3");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.alListItem);
        this.listQuiz.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        this.listQuiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                txtChooseQuiz.setText((CharSequence) adapterView.getItemAtPosition(position));
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
