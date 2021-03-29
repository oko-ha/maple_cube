package com.example.maple_cube.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class BonusDB {
    SQLiteDatabase db;
    Context ctx;
    BonusDBHelper mHelper;

    public BonusDB(Context ctx) {
        this.ctx = ctx;
        mHelper = new BonusDBHelper(ctx);

    }

    // id에 해당하는 option 값 찾기
    public String getOption(String table_name, int id) {
        String option;
        String query;
        Cursor cursor;
        db = mHelper.getReadableDatabase();

        query = ("SELECT * FROM " + table_name + " WHERE id=" + id);
        cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
            option = cursor.getString(cursor.getColumnIndex("option"));
        else
            option = null;
        cursor.close();
        mHelper.close();
        return option;
    }

    // category에 해당하는 id, weight, exception 값 찾기
    public ArrayList<Integer[]> getList(String table_name, int[] category) {
        ArrayList<Integer[]> list = new ArrayList<>();
        StringBuilder query;
        Cursor cursor;
        db = mHelper.getReadableDatabase();

        query = new StringBuilder("SELECT * FROM " + table_name + " WHERE category=" + category[0]);
        for(int i = 1; i < category.length; i++){
            query.append(" OR category=").append(category[i]);
        }
        cursor = db.rawQuery(query.toString(), null);

        if (cursor.moveToFirst()) {
            do {
                list.add(new Integer[]{cursor.getInt(cursor.getColumnIndex("id")), cursor.getInt(cursor.getColumnIndex("weight")), cursor.getInt(cursor.getColumnIndex("exception"))});
            } while (cursor.moveToNext());
        } else {
            list = null;
        }
        cursor.close();
        mHelper.close();
        return list;
    }

    // id에 해당하는 exception 값 찾기
    public int getException(String table_name, int id) {
        int exception;
        String query;
        Cursor cursor;
        db = mHelper.getReadableDatabase();

        query = ("SELECT * FROM " + table_name + " WHERE id=" + id);
        cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            exception = cursor.getInt(cursor.getColumnIndex("exception"));
        } else {
            exception = 0;
        }
        cursor.close();
        mHelper.close();
        return exception;
    }

    // id에 해당하는 auto_option 값 찾기
    public int getAutoOption(String table_name, int id) {
        int auto_option;
        String query;
        Cursor cursor;
        db = mHelper.getReadableDatabase();

        query = ("SELECT * FROM " + table_name + " WHERE id=" + id);
        cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            auto_option = cursor.getInt(cursor.getColumnIndex("auto_option"));
        } else {
            auto_option = 0;
        }
        cursor.close();
        mHelper.close();
        return auto_option;
    }

}

class BonusDBHelper extends SQLiteOpenHelper {
    Context mContext;

    public BonusDBHelper(Context context) {
        super(context, "bonus.db", null, 1);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //
    }

}