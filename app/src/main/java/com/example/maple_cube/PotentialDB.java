package com.example.maple_cube;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;

public class PotentialDB {
    SQLiteDatabase db;
    Context ctx;
    PotentialDBHelper mHelper;

    public PotentialDB(Context ctx) {
        this.ctx = ctx;
        mHelper = new PotentialDBHelper(ctx);

    }

    // id에 해당하는 option 값 찾기
    public String getOption(String table_name, int id) {
        String str;
        Cursor cursor = null;
        db = mHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + table_name + " WHERE id=" + id, null);
        if (cursor.moveToFirst())
            str = cursor.getString(cursor.getColumnIndex("option"));
        else
            str = null;
        cursor.close();
        mHelper.close();
        return str;
    }

    // category에 해당하는 id, weight 값 찾기
    public Map<Integer, Double> getList(String table_name, int[] category) {
        int i = 0;
        Map<Integer, Double> list = new HashMap<>();
        StringBuilder query;
        Cursor cursor = null;
        db = mHelper.getReadableDatabase();

        query = new StringBuilder("SELECT * FROM " + table_name + " WHERE category=" + category[0]);
        for(i = 1; i < category.length; i++){
            query.append(" OR category=").append(category[i]);
        }
        cursor = db.rawQuery(query.toString(), null);

        if (cursor.moveToFirst()) {
            i = 0;
            do {
                list.put(cursor.getInt(cursor.getColumnIndex("id")), (double)cursor.getInt(cursor.getColumnIndex("weight")));
                i++;
            } while (cursor.moveToNext());
        } else {
            list = null;
        }
        cursor.close();
        mHelper.close();
        return list;
    }

}

class PotentialDBHelper extends SQLiteOpenHelper {
    Context mContext;

    public PotentialDBHelper(Context context) {
        super(context, "potential.db", null, 1);
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