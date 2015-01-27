package com.tc.ajk.englishquiz.model;

import android.database.Cursor;

import java.util.List;

/**
 * Created by baskoro on 1/27/15.
 */
public class Question {
    private long id;
    private long catId;
    private String questionText;
    private List<Answer> answers;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCatId() {
        return catId;
    }

    public void setCatId(long catId) {
        this.catId = catId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public static Question fetchCursor(Cursor cursor) {
        Question question = new Question();
        question.setId(cursor.getLong(0));
        question.setCatId(cursor.getLong(1));
        question.setQuestionText(cursor.getString(2));

        return question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
