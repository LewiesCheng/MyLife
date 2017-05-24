package com.liucheng.android.mylife.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.liucheng.android.mylife.database.LifeDBSchema.LifeTable;

/**
 * Created by liucheng on 2017/5/22.
 */

public class LifeBaseHelper extends SQLiteOpenHelper{

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "lifeBase.db";

    public LifeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + LifeTable.NAME + " (" +
                "_id integer primary key autoincrement, " +
                LifeTable.Cols.UUID + ", " +
                LifeTable.Cols.TITLE + ", " +
                LifeTable.Cols.FEEL + ", " +
                LifeTable.Cols.DATE + ", " +
                LifeTable.Cols.PICTURES + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
