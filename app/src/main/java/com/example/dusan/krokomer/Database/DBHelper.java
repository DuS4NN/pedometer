package com.example.dusan.krokomer.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 19;//15
    private static final String DATABASE_NAME = "krokomer";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE_STEPS = "CREATE TABLE "+ MyContract.Steps.TABLE_NAME + "("
                + MyContract.Steps.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MyContract.Steps.COLUMN_DATE + " TEXT, "
                + MyContract.Steps.COLUMN_STEPS + " TEXT" +
                ")";

        String SQL_CREATE_TABLE_USER = "CREATE TABLE "+MyContract.User.TABLE_NAME + "("
                + MyContract.User.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MyContract.User.COLUMN_BIRTH + " TEXT, "
                + MyContract.User.COLUMN_HEIGHT + " TEXT, "
                + MyContract.User.COLUMN_SEX + " TEXT, "
                + MyContract.User.COLUMN_STEPS + " TEXT, "
                + MyContract.User.COLUMN_ACHIEVEMENT + " TEXT, "
                + MyContract.User.COLUMN_GOAL + " TEXT, "
                + MyContract.User.COLUMN_WEIGHT + " TEXT, "
                + MyContract.User.COLUMN_TRACK + " TEXT" +
                ")";

        db.execSQL(SQL_CREATE_TABLE_STEPS);
        db.execSQL(SQL_CREATE_TABLE_USER);

        ContentValues values = new ContentValues();
        values.put(MyContract.User.COLUMN_BIRTH, "01/01/1990");
        values.put(MyContract.User.COLUMN_HEIGHT, "175");
        values.put(MyContract.User.COLUMN_SEX, "Muž");
        values.put(MyContract.User.COLUMN_ACHIEVEMENT, "01/01/1990");
        values.put(MyContract.User.COLUMN_WEIGHT, "70");
        values.put(MyContract.User.COLUMN_STEPS, "0");
        values.put(MyContract.User.COLUMN_GOAL, "6000");
        values.put(MyContract.User.COLUMN_TRACK, "1");
        long newRowsId = db.insert(MyContract.User.TABLE_NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+MyContract.Steps.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+MyContract.User.TABLE_NAME);
        onCreate(db);
    }

    public void changeSteps(long steps){
        SQLiteDatabase db = getWritableDatabase();

        HashMap<String,String> map = getUserData();

        ContentValues values = new ContentValues();
        values.put(MyContract.User.COLUMN_BIRTH, map.get("birth"));
        values.put(MyContract.User.COLUMN_HEIGHT, map.get("height"));
        values.put(MyContract.User.COLUMN_SEX, map.get("sex"));
        values.put(MyContract.User.COLUMN_WEIGHT, map.get("weight"));
        values.put(MyContract.User.COLUMN_ACHIEVEMENT, map.get("achievement"));
        values.put(MyContract.User.COLUMN_STEPS, steps+"");
        values.put(MyContract.User.COLUMN_GOAL, map.get("goal"));
        values.put(MyContract.User.COLUMN_TRACK, map.get("track"));
        db.update(MyContract.User.TABLE_NAME, values, MyContract.User.COLUMN_ID + "= ?", new String [] { ""+1});
    }

    public void changeAchievement(String date){
        SQLiteDatabase db = getWritableDatabase();

        HashMap<String,String> map = getUserData();

        ContentValues values = new ContentValues();
        values.put(MyContract.User.COLUMN_BIRTH, map.get("birth"));
        values.put(MyContract.User.COLUMN_HEIGHT, map.get("height"));
        values.put(MyContract.User.COLUMN_SEX, map.get("sex"));
        values.put(MyContract.User.COLUMN_WEIGHT, map.get("weight"));
        values.put(MyContract.User.COLUMN_ACHIEVEMENT, date);
        values.put(MyContract.User.COLUMN_STEPS, map.get("steps"));
        values.put(MyContract.User.COLUMN_GOAL, map.get("goal"));
        values.put(MyContract.User.COLUMN_TRACK, map.get("track"));
        db.update(MyContract.User.TABLE_NAME, values, MyContract.User.COLUMN_ID + "= ?", new String [] { ""+1});
    }

    public long getStepsInDay(String date){
        String d = date.substring(0,10);
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT SUM("+MyContract.Steps.COLUMN_STEPS+") FROM "+MyContract.Steps.TABLE_NAME+" WHERE substr("+MyContract.Steps.COLUMN_DATE+",1,10) = '"+d+"'",null);
        c.moveToFirst();
        if(c.getString(0)==null){
            return 0;
        }
        return Integer.parseInt(c.getString(0));
    }

    public HashMap<String,String> getUserData(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM user",null);
        HashMap<String,String> map = new HashMap<>();
        c.moveToFirst();
        map.put("birth",c.getString(c.getColumnIndex(MyContract.User.COLUMN_BIRTH)));
        map.put("goal",c.getString(c.getColumnIndex(MyContract.User.COLUMN_GOAL)));
        map.put("height",c.getString(c.getColumnIndex(MyContract.User.COLUMN_HEIGHT)));
        map.put("sex",c.getString(c.getColumnIndex(MyContract.User.COLUMN_SEX)));
        map.put("steps",c.getString(c.getColumnIndex(MyContract.User.COLUMN_STEPS)));
        map.put("track",c.getString(c.getColumnIndex(MyContract.User.COLUMN_TRACK)));
        map.put("weight",c.getString(c.getColumnIndex(MyContract.User.COLUMN_WEIGHT)));
        map.put("achievement",c.getString(c.getColumnIndex(MyContract.User.COLUMN_ACHIEVEMENT)));
        c.close();
        return map;
    }

    public String getLastDate(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT MAX("+MyContract.Steps.COLUMN_DATE+") FROM "+MyContract.Steps.TABLE_NAME,null);
        c.moveToNext();
        db.close();
        return c.getString(0);
    }

    public List<HashMap<String,String>> getStepsPerHours(String date){
        String d = date.substring(0,10);
        List<HashMap<String,String>> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+MyContract.Steps.TABLE_NAME+" WHERE substr("+MyContract.Steps.COLUMN_DATE+",1,10) = '"+d+"'" ,null);
        if(c.moveToNext()){
            do{
                HashMap<String,String> map = new HashMap<>();
                map.put("steps",c.getString(c.getColumnIndex(MyContract.Steps.COLUMN_STEPS)));
                map.put("date",c.getString(c.getColumnIndex(MyContract.Steps.COLUMN_DATE)));
                list.add(map);
            }while(c.moveToNext());
        }
        return list;
    }

    public void createNewStep(Steps steps){
        ContentValues values = new ContentValues();
        values.put(MyContract.Steps.COLUMN_DATE,steps.getDate());
        values.put(MyContract.Steps.COLUMN_STEPS, steps.getSteps());

        SQLiteDatabase db = getWritableDatabase();
        long newRowsId = db.insert(MyContract.Steps.TABLE_NAME, null, values);
        Log.d("NEW STEP",steps.getDate()+" "+steps.getSteps());
    }

    public String getCountOfAchievements(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM (SELECT "+MyContract.Steps.COLUMN_DATE+", SUM("+MyContract.Steps.COLUMN_STEPS+") as 'steps' FROM "+MyContract.Steps.TABLE_NAME+" GROUP BY substr("+MyContract.Steps.COLUMN_DATE+",1,10)) ss WHERE cast(ss."+MyContract.Steps.COLUMN_STEPS+" as real ) >'5999'",null);
        c.moveToFirst();
        return c.getString(0);
    }

    public String getMaxSteps(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT ss."+MyContract.Steps.COLUMN_DATE+", MAX(ss."+MyContract.Steps.COLUMN_STEPS+") FROM (SELECT "+MyContract.Steps.COLUMN_DATE+", SUM("+MyContract.Steps.COLUMN_STEPS+") as 'steps' FROM "+MyContract.Steps.TABLE_NAME+" GROUP BY substr("+MyContract.Steps.COLUMN_DATE+",1,10)) ss ",null);
        c.moveToFirst();
        return c.getString(0)+";"+c.getString(1);
    }

    public void changeUser(String track, String goal){
        SQLiteDatabase db = getWritableDatabase();

        HashMap<String,String> map = getUserData();

        ContentValues values = new ContentValues();
        values.put(MyContract.User.COLUMN_BIRTH, map.get("birth"));
        values.put(MyContract.User.COLUMN_HEIGHT, map.get("height"));
        values.put(MyContract.User.COLUMN_SEX, map.get("sex"));
        values.put(MyContract.User.COLUMN_WEIGHT, map.get("weight"));
        values.put(MyContract.User.COLUMN_ACHIEVEMENT, map.get("achievement"));
        values.put(MyContract.User.COLUMN_STEPS, map.get("steps"));
        values.put(MyContract.User.COLUMN_GOAL, goal);
        values.put(MyContract.User.COLUMN_TRACK, track);
        db.update(MyContract.User.TABLE_NAME, values, MyContract.User.COLUMN_ID + "= ?", new String [] { ""+1});
    }

    public List<String> getAllDays(){
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT "+MyContract.Steps.COLUMN_DATE+", SUM("+MyContract.Steps.COLUMN_STEPS+") as 'sum' FROM "+MyContract.Steps.TABLE_NAME+" GROUP BY substr("+MyContract.Steps.COLUMN_DATE+",1,10)",null);
        if(c.moveToNext()){
            do{
                list.add(c.getString(0)+";"+c.getString(1));
            }while(c.moveToNext());
        }
        return list;
    }
}
