package com.example.testproject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class TicketFounded {
    private static String fromIATA;
    private static String toIATA;
    private static String fromSTROKA;
    private static String toSTROKA;
    private static String currency="rub";
    private static String fromDATE;
    private static String gate;
    private static Double value;
    private static Double wantvalue;
    private static Integer range=0;
    static DBHelper dbHelper=MainActivity.dbHelper;

    public static String tostring() {
        StringBuilder result= new StringBuilder();
        return result
                .append(getFromSTROKA().split(" ")[1]).append(" --> ")
                .append(getToSTROKA().split(" ")[1]).append("\n")
                .append(getFromDATE()).append("  in  ")
                .append(getGate()).append("\n")
                .append(Math.round(getValue())).append(" руб ( Желаемая: ")
                .append(Math.round(getWantvalue())+" )")
                .toString();
    }

    public static void toBase() {
        SQLiteDatabase database= dbHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DBHelper.KEY_TOPLACE, getToSTROKA());
        contentValues.put(DBHelper.KEY_FROMPLACE, getFromSTROKA());
        contentValues.put(DBHelper.KEY_DATE, getFromDATE());
        contentValues.put(DBHelper.KEY_GATE, getGate());
        contentValues.put(DBHelper.KEY_PRICE, getValue());
        contentValues.put(DBHelper.KEY_WANTPRICE, getWantvalue());
        database.insert(DBHelper.TABLE_TICKETS, null, contentValues);
    }


    public static void setFromIATA(String iata){
        fromIATA=iata;
    }

    public static String getFromIATA(){
        return fromIATA;
    }

    public static void setToIATA(String iata){
        toIATA=iata;
    }

    public static String getToIATA(){
        return toIATA;
    }

    public static void setFromSTROKA(String stroka){
        fromSTROKA=stroka;
    }

    public static String getFromSTROKA(){
        return fromSTROKA;
    }

    public static void setToSTROKA(String stroka){
        toSTROKA=stroka;
    }

    public static String getToSTROKA(){
        return toSTROKA;
    }

    public static void setRange(Integer count){
        range=count;
    }

    public static Integer getRange(){
        return range;
    }

    public static void setCurrency(String curr){currency=curr;}

    public static String getCurrency(){ return currency; }


    public static void setFromDATE(String date){ fromDATE=date; }

    public static String getFromDATE(){ return fromDATE; }

    public static void setGate(String gat) { gate = gat; }

    public static String getGate() {
        return gate;
    }

    public static void setValue(Double val) {
        value = val;
    }

    public static Double getValue() {
        return value;
    }

    public static Double getWantvalue() {
        return wantvalue;
    }

    public static void setWantvalue(Double wantvalue) {
        TicketFounded.wantvalue = wantvalue;
    }
}
