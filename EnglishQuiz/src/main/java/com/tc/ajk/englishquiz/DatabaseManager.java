package com.tc.ajk.englishquiz;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tc.ajk.englishquiz.model.Answer;
import com.tc.ajk.englishquiz.model.Category;
import com.tc.ajk.englishquiz.model.Question;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        this.myDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
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

    public void saveScore(int value, String username, Date startTime) {
        ContentValues cv = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        cv.put("test_date", sdf.format(date));
        cv.put("value", value);
        cv.put("username", username);
        cv.put("start_time", sdf.format(startTime));
        this.myDB.insert("score", null, cv);

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://batik.if.its.ac.id/ielts/inscore.php");

        try {
            List<NameValuePair> postdata = new ArrayList<NameValuePair>();
            postdata.add(new BasicNameValuePair("nrp", username));
            postdata.add(new BasicNameValuePair("datetime", sdf.format(date)));
            postdata.add(new BasicNameValuePair("score", Integer.toString(value)));
            postdata.add(new BasicNameValuePair("starttime", sdf.format(startTime)));

            post.setEntity(new UrlEncodedFormEntity(postdata));
            HttpResponse response = client.execute(post);
            long respLength = response.getEntity().getContentLength();
            BufferedInputStream bis = new BufferedInputStream(response.getEntity().getContent());

            int count = 0;
            StringBuffer strResp = new StringBuffer();
            byte[] buffer = new byte[1024];

            while(count < respLength) {
                count += bis.read(buffer);
                strResp.append(new String(buffer));
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
            builder.setCancelable(true);
            builder.setTitle("Information");
            builder.setMessage("Score uploaded : " + strResp.toString());
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ExecuteQuizActivity activity = (ExecuteQuizActivity) DatabaseManager.this.context;
                    activity.finish();
                }
            });


            AlertDialog alert = builder.create();
            alert.show();
        }
        catch (IOException e) {
            Log.d("ERROR", e.getMessage());
            AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
            builder.setTitle("Error");
            builder.setCancelable(true);
            builder.setMessage(e.getMessage());
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ExecuteQuizActivity activity = (ExecuteQuizActivity) DatabaseManager.this.context;
                    activity.finish();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }

        return;
    }
}
