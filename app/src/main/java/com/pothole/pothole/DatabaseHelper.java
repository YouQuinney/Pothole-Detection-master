package com.pothole.pothole;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Potholes.db";
    public static final String TABLE_NAME = "student_table";
    public static final String USER_TABLE = "user_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "POTHOLE_NUMBER";
    public static final String COL_3 = "LATITUDE";
    public static final String COL_4 = "LONGITUDE";
    public static final String T2COL_1 = "USERNAME";
    public static final String T2COL_2 = "PASSWORD";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,POTHOLE_NUMBER TEXT,LATITUDE TEXT,LONGITUDE TEXT)");
        db.execSQL("create table " + USER_TABLE +" (USERNAME TEXT PRIMARY KEY,PASSWORD TEXT )");
    }

    //@Override
    public void createDB(SQLiteDatabase db){
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,POTHOLE_NUMBER TEXT,LATITUDE TEXT,LONGITUDE TEXT)");
        db.execSQL("create table " + USER_TABLE +" (USERNAME TEXT PRIMARY KEY,PASSWORD TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+USER_TABLE);
        onCreate(db);
    }

    public boolean isUsernameTaken(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE USERNAME = ?", new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }


    public boolean insertData(String pothole_number,String latitude,String longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,pothole_number);
        contentValues.put(COL_3,latitude);
        contentValues.put(COL_4,longitude);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertUser(String username, String password) {
        if (isUsernameTaken(username)) {
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(T2COL_1, username);
        contentValues.put(T2COL_2, password);
        long result = db.insert(USER_TABLE, null, contentValues);
        return result != -1;
    }


    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+USER_TABLE,null);
        return res;
    }

    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + USER_TABLE + " WHERE USERNAME = ? AND PASSWORD = ?",
                new String[]{username, password}
        );
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

}
