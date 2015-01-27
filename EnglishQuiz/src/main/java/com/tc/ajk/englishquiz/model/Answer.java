package com.tc.ajk.englishquiz.model;

import android.database.Cursor;

/**
 * Created by baskoro on 1/27/15.
 */
public class Answer {
    private long id;
    private long questionId;
    private String answerText;
    private int isTrue;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public int isTrue() {
        return isTrue;
    }

    public void setTrue(int aTrue) {
        isTrue = aTrue;
    }

    public static Answer fetchCursor(Cursor cursor) {
        Answer answer = new Answer();
        answer.setId(cursor.getLong(0));
        answer.setQuestionId(cursor.getLong(1));
        answer.setAnswerText(cursor.getString(2));
        answer.setTrue(cursor.getInt(3));

        return answer;
    }
}
