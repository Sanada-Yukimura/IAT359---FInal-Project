package com.example.interactive_newspaper_SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.jar.Attributes;

public class SQLMyDatabase {
    private SQLiteDatabase db;
    private Context context;
    private final SQLDatabaseHelper helper;


    public SQLMyDatabase(Context c) {
        context = c;
        helper = new SQLDatabaseHelper(context);
    }

    public long addData (String title){
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Title", title);
        long id = db.insert("BOOKMARKS", null,contentValues);
        Log.d("ID", String.valueOf(id));
        return id;
    }

    public ArrayList<String> getAllBookmarks(){
        ArrayList<String> bookmarkList = new ArrayList<>();
        String name = "";
        String selectQuery = "SELECT  * FROM " + "BOOKMARKS";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()){
            do{
                name = c.getString(c.getColumnIndex("Title"));
                bookmarkList.add(name);
            }
            while(c.moveToNext());
                Log.d("ARRAY", bookmarkList.toString());

        }
        return bookmarkList;
    }

    public Cursor getData()
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {"_id", "Title"};
        Cursor cursor = db.query("BOOKMARKS", columns, null, null, null, null, null);
        return cursor;
    }

    public void deleteData(String title){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("BOOKMARKS", "Title=?", new String[]{title});
//        db.execSQL("DELETE FROM "+"BOOKMARKS"+" WHERE Title='"+title+"'");




    }
    public void deleteTable(){
        db.delete("BOOKMARKS", null, null);
    }



}
