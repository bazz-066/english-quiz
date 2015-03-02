package com.tc.ajk.englishquiz;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tc.ajk.englishquiz.model.Category;
import com.tc.ajk.englishquiz.model.Question;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by baskoro on 1/27/15.
 */
public class ExecuteQuizActivity extends Activity {
    private DatabaseManager dbHelper;
    private TextView txtQuestion, txtMessage, txtScore;
    private RadioButton[] rbAnswer;
    private Button btnNext, btnFinish;
    private Category category;
    private int index, numOfQuestions, score;
    private String username;
    private RadioGroup rgAnswer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_executequiz);

        this.rbAnswer = new RadioButton[4];
        this.txtQuestion = (TextView) this.findViewById(R.id.txtQuestion);
        this.txtMessage = (TextView) this.findViewById(R.id.txtMessage);
        this.txtScore = (TextView) this.findViewById(R.id.txtScore);
        this.rbAnswer[0] = (RadioButton) this.findViewById(R.id.rbAnswer1);
        this.rbAnswer[1] = (RadioButton) this.findViewById(R.id.rbAnswer2);
        this.rbAnswer[2] = (RadioButton) this.findViewById(R.id.rbAnswer3);
        this.rbAnswer[3] = (RadioButton) this.findViewById(R.id.rbAnswer4);
        this.rgAnswer = (RadioGroup) this.findViewById(R.id.rgAnswer);
        this.btnNext = (Button) this.findViewById(R.id.btnNext);
        this.btnFinish = (Button) this.findViewById(R.id.btnFinish);

        this.dbHelper = new DatabaseManager(this);
        this.dbHelper.openDatabase();

        this.category = (Category) this.getIntent().getSerializableExtra(Category.KEY);
        this.username = this.getIntent().getStringExtra(LoginActivity.KEYUSERNAME);
        this.dbHelper.fillQuestions(this.category);

        long seed = System.nanoTime();
        List<Question> questions = this.category.getQuestions();
        Collections.shuffle(questions);

        for(Question q: questions) {
            this.dbHelper.fillAnswers(q);
        }

        this.numOfQuestions = questions.size();
        this.index = 0;
        this.score = 0;
        this.showQuestion();
        this.btnFinish.setEnabled(false);
        this.category.resetQuestions();

        this.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index++;
                btnNext.setEnabled(true);

                if (index >= numOfQuestions)
                    index--;
                if (index + 1 == numOfQuestions)
                {
                    btnNext.setEnabled(false);
                    btnFinish.setEnabled(true);
                }
                //btnFinish.setEnabled(true);
                showQuestion();
            }
        });

        this.btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseManager dbHelper = new DatabaseManager(ExecuteQuizActivity.this);
                dbHelper.openDatabase();
                dbHelper.saveScore(ExecuteQuizActivity.this.score, ExecuteQuizActivity.this.username);
                dbHelper.close();

                //ExecuteQuizActivity.this.finish();
            }
        });

        CompoundButton.OnCheckedChangeListener rbListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            Question q = category.getQuestions().get(index);
            if(compoundButton.isChecked()) {
                if(q.getAnswers().get(compoundButton.getId()).isTrue() == 1) {
                    q.setAnswerMessage("You're right !!!");
                    txtMessage.setText("You're right !!!");
                    String question = (String) txtQuestion.getText();
                    question = question.replaceFirst("___", q.getAnswers().get(compoundButton.getId()).getAnswerText());
                    txtQuestion.setText(question);

                    if(!q.isAnswered())
                        score++;
                }
                else {
                    q.setAnswerMessage("Wrong");
                    txtMessage.setText("Wrong");
                }

                compoundButton.setChecked(true);
                compoundButton.setSelected(true);
                q.setSelectedAnswer(compoundButton.getId());
                q.setAnswered(true);
                txtScore.setText("Score : " + score + "/" + category.getQuestions().size());
            }
            }
        };

        this.rbAnswer[0].setOnCheckedChangeListener(rbListener);
        this.rbAnswer[1].setOnCheckedChangeListener(rbListener);
        this.rbAnswer[2].setOnCheckedChangeListener(rbListener);
        this.rbAnswer[3].setOnCheckedChangeListener(rbListener);
    }

    public void resetButton() {
        for(RadioButton rb : this.rbAnswer) {
            rb.setChecked(false);
        }
        this.rgAnswer.clearCheck();
        this.txtMessage.setText("");
    }

    public void showQuestion() {
        Question q = this.category.getQuestions().get(index);
        this.txtQuestion.setText((this.index + 1) + ". " + q.getQuestionText());
        this.rbAnswer[0].setId(q.getAnswerIndex()[0]);
        this.rbAnswer[1].setId(q.getAnswerIndex()[1]);
        this.rbAnswer[2].setId(q.getAnswerIndex()[2]);
        this.rbAnswer[3].setId(q.getAnswerIndex()[3]);
        this.rbAnswer[0].setText(q.getAnswers().get(q.getAnswerIndex()[0]).getAnswerText());
        this.rbAnswer[1].setText(q.getAnswers().get(q.getAnswerIndex()[1]).getAnswerText());
        this.rbAnswer[2].setText(q.getAnswers().get(q.getAnswerIndex()[2]).getAnswerText());
        this.rbAnswer[3].setText(q.getAnswers().get(q.getAnswerIndex()[3]).getAnswerText());
        this.resetButton();
        q.setShowed(true);
    }



}