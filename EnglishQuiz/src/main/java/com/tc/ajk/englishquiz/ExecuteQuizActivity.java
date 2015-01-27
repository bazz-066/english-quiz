package com.tc.ajk.englishquiz;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tc.ajk.englishquiz.model.Category;
import com.tc.ajk.englishquiz.model.Question;

import java.util.List;

/**
 * Created by baskoro on 1/27/15.
 */
public class ExecuteQuizActivity extends Activity {
    private DatabaseManager dbHelper;
    private TextView txtQuestion;
    private RadioButton[] rbAnswer;
    private Category category;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_executequiz);

        this.rbAnswer = new RadioButton[4];
        this.txtQuestion = (TextView) this.findViewById(R.id.txtQuestion);
        this.rbAnswer[0] = (RadioButton) this.findViewById(R.id.rbAnswer1);
        this.rbAnswer[1] = (RadioButton) this.findViewById(R.id.rbAnswer2);
        this.rbAnswer[2] = (RadioButton) this.findViewById(R.id.rbAnswer3);
        this.rbAnswer[3] = (RadioButton) this.findViewById(R.id.rbAnswer4);

        this.dbHelper = new DatabaseManager(this);
        this.dbHelper.openDatabase();

        this.category = (Category) this.getIntent().getSerializableExtra(Category.KEY);
        this.dbHelper.fillQuestions(this.category);

        List<Question> questions = this.category.getQuestions();
        for(Question q: questions) {
            this.dbHelper.fillAnswers(q);
        }

        Question q = questions.get(0);
        this.txtQuestion.setText(q.getQuestionText());
        this.rbAnswer[0].setText(q.getAnswers().get(0).getAnswerText());
        this.rbAnswer[1].setText(q.getAnswers().get(1).getAnswerText());
        this.rbAnswer[2].setText(q.getAnswers().get(2).getAnswerText());
        this.rbAnswer[3].setText(q.getAnswers().get(3).getAnswerText());
    }
}