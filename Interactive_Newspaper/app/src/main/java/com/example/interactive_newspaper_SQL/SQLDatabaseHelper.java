package com.example.interactive_newspaper_SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
// Code learned from https://demonuts.com/sqlite-android/

public class SQLDatabaseHelper extends SQLiteOpenHelper {
    private Context context;

    private static final String DATABASE_NAME = "bookmarks";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "BOOKMARKS";
    private static final String UID = "_id";
    private static final String TITLE = "Title";
    private static final String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ TITLE + " TEXT);" ;
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    public SQLDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(CREATE_TABLE);
        }
        catch(Exception e){

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }
        catch(Exception e){

        }
    }

//    public long addDatabaseDetail(String title, String url){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues value = new ContentValues();
//        value.put(TITLE, title);
//        value.put(URL, url);
//        long insert = db.insert(TABLE_NAME, null, value);
//
//        return insert;
//    }
//    public void delDatabaseDetal(int index){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DELETE FROM "+TABLE_NAME+ " WHERE " + UID + " = "+index);
//
//    }
//
//    public ArrayList<String> getAllData(){
//        ArrayList<String> dataArrayList = new ArrayList<String>();
//        String name = "";
//        String selectQuery = "SELECT * FROM " + TABLE_NAME;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor c = db.rawQuery(selectQuery, null);
//        if(c.moveToFirst()){
//            do {
//                name = c.getString(c.getColumnIndex(TITLE));
//                dataArrayList.add(name);
//            }
//            while (c.moveToNext());
//            Log.d("ARRAY", dataArrayList.toString());
//
//        }
//        return dataArrayList;
//    }

}
