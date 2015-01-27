package com.tc.ajk.englishquiz;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.tc.ajk.englishquiz.model.Answer;
import com.tc.ajk.englishquiz.model.Category;
import com.tc.ajk.englishquiz.model.Question;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by baskoro on 1/25/15.
 */
public class DatabaseManager extends SQLiteOpenHelper {
    private static String DB_PATH = "/data/data/com.tc.ajk.englishquiz/databases/";
    private static String DB_NAME = "quiz.db";
    private SQLiteDatabase myDB;
    private final Context context;

    public DatabaseManager(Context context) {
        super(context, "quiz.db", null, 1);
        this.context = context;
    }

    public void createDatabase() {
        boolean isDBExist = this.checkDatabase();
        if(!isDBExist)
        {
            this.getReadableDatabase();
        }

        try {
            this.copyDatabase();
        } catch (IOException e) {
            throw new Error("Error copying database.");
        }
    }

    private boolean checkDatabase() {
        SQLiteDatabase checkDB = null;

        try {
            String path = DatabaseManager.DB_PATH + DatabaseManager.DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
            return true;
        }
        catch(SQLiteException e) {
            return false;
        }
    }

    private void copyDatabase() throws IOException {
        InputStream is = this.context.getAssets().open(DatabaseManager.DB_NAME);
        String outFilename = DatabaseManager.DB_PATH + DatabaseManager.DB_NAME;
        OutputStream os = new FileOutputStream(outFilename);

        byte[] buffer = new byte[1024];
        int length;

        while((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }

        os.flush();
        os.close();
        is.close();
    }

    public void openDatabase() {
        String path = DatabaseManager.DB_PATH + DatabaseManager.DB_NAME;
        this.myDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
    }

    public synchronized void close() {
        if(this.myDB != null)
        {
            this.myDB.close();
            super.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {

    }

    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<Category>();
        Cursor resultSet = this.myDB.rawQuery("SELECT * FROM category", null);
        resultSet.moveToFirst();

        while(!resultSet.isAfterLast()) {
            Category category = Category.fetchCursor(resultSet);
            categories.add(category);
            resultSet.moveToNext();
        }

        resultSet.close();
        return categories;
    }

    public void fillQuestions(Category category) {
        List<Question> questions = new ArrayList<Question>();
        String id = category.getId() + "";
        Cursor resultSet = this.myDB.rawQuery("SELECT * FROM question WHERE cat_id=?", new String[] { id });
        resultSet.moveToFirst();

        while(!resultSet.isAfterLast()) {
            Question question = Question.fetchCursor(resultSet);
            questions.add(question);
            resultSet.moveToNext();
        }

        resultSet.close();
        category.setQuestions(questions);
    }

    public void fillAnswers(Question question) {
        List<Answer> answers = new ArrayList<Answer>();
        String id = question.getId() + "";
        Cursor resultSet = this.myDB.rawQuery("SELECT * FROM answer WHERE question_id=?", new String[] { id });
        resultSet.moveToFirst();

        while(!resultSet.isAfterLast()) {
            Answer answer = Answer.fetchCursor(resultSet);
            answers.add(answer);
            resultSet.moveToNext();
        }

        resultSet.close();
        question.setAnswers(answers);
    }
}
