package com.tc.ajk.englishquiz.model;

import android.database.Cursor;
import java.io.Serializable;
import java.util.List;

/**
 * Created by baskoro on 1/27/15.
 */
public class Category implements Serializable {
    public static final String KEY = "CATEGORY";

    private long id;
    private String name;
    private List<Question> questions;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public static Category fetchCursor(Cursor cursor) {
        Category category = new Category();
        category.setId(cursor.getLong(0));
        category.setName(cursor.getString(1));

        return category;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void resetQuestions() {
        for(Question q : this.questions) {
            q.setAnswered(false);
        }
    }
}
