package com.tc.ajk.englishquiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by baskoro on 1/25/15.
 */
public class DatabaseManager extends SQLiteOpenHelper {
    public DatabaseManager(Context context) {
        super(context, "quiz.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {

    }
}
