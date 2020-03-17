package com.example.testproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "ticketDB";
    static final String TABLE_TICKETS = "tickets";
    static final String KEY_ID = "_id";
    static final String KEY_FROMPLACE = "from_place";
    static final String KEY_TOPLACE = "to_place";
    static final String KEY_DATE = "date";
    static final String KEY_PRICE= "price";
    static final String KEY_WANTPRICE = "want_price";
    static final String KEY_GATE= "gate";

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_TICKETS + "("+ KEY_ID +" integer primary key,"
                + KEY_FROMPLACE + " text, "
                + KEY_TOPLACE + " text, "
                + KEY_DATE + " text, "
                + KEY_GATE + " text, "
                + KEY_PRICE + " real, "
                + KEY_WANTPRICE + " real"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + TABLE_TICKETS);
        onCreate(db);
    }
}
