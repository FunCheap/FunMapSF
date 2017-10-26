package com.funcheap.funmapsf.commons.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.raizlabs.android.dbflow.annotation.Database;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

@Database(name = MyDatabase.NAME, version = MyDatabase.VERSION)
public class MyDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "MySFfuncheapDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public static final String NAME = "MySFfuncheapDatabase";

    public static final int VERSION = 1;

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // you can use an alternate constructor to specify a database location
        // (such as a folder on the sd card)
        // you must ensure that this folder is available and you have permission
        // to write to it
        //super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);

    }

    public void getDB()
    {
        SQLiteDatabase db = getWritableDatabase();
    }
}