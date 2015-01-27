package com.tc.ajk.englishquiz.model;

import android.database.Cursor;

import java.util.List;
import java.util.Random;

/**
 * Created by baskoro on 1/27/15.
 */
public class Question {
    private long id;
    private long catId;
    private String questionText;
    private List<Answer> answers;
    private boolean isAnswered;
    private boolean isShowed;
    private int[] answerIndex;
    private int selectedAnswer;
    private String answerMessage;

    public Question() {
        this.isAnswered = false;
        this.isShowed = false;
        this.setAnswerIndex(new int[] {0, 1, 2, 3});
        this.shuffle();
        this.selectedAnswer = -1;
    }

    public void shuffle() {
        Random rnd = new Random();

        for (int i = this.answerIndex.length - 1; i > 0; i--)
        {
            int tmp = rnd.nextInt(i + 1);
            // Simple swap
            int a = this.answerIndex[tmp];
            this.answerIndex[tmp] = this.answerIndex[i];
            this.answerIndex[i] = a;
        }
    }

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

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    public boolean isShowed() {
        return isShowed;
    }

    public void setShowed(boolean showed) {
        isShowed = showed;
    }

    public int[] getAnswerIndex() {
        return answerIndex;
    }

    public void setAnswerIndex(int[] answerIndex) {
        this.answerIndex = answerIndex;
    }

    public int getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(int selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public String getAnswerMessage() {
        return answerMessage;
    }

    public void setAnswerMessage(String answerMessage) {
        this.answerMessage = answerMessage;
    }
}
