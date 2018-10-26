package com.google.android.gms.samples.vision.ocrreader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDB
        extends SQLiteOpenHelper
{
    public MyDB(Context paramContext)
    {
        super(paramContext, "mydb", null, 1);
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase)
    {
        paramSQLiteDatabase.execSQL("create table history(stext text,app text)");
    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
        paramSQLiteDatabase.execSQL("drop table if exists history");
        onCreate(paramSQLiteDatabase);
    }
}
