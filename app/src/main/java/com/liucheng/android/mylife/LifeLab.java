package com.liucheng.android.mylife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.liucheng.android.mylife.database.LifeBaseHelper;
import com.liucheng.android.mylife.database.LifeCursorWrapper;
import com.liucheng.android.mylife.database.LifeDBSchema;
import com.liucheng.android.mylife.database.LifeDBSchema.LifeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by liucheng on 2017/5/17.
 */

public class LifeLab {
    private static LifeLab sLifeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static LifeLab get(Context context){
        if (sLifeLab == null){
            sLifeLab = new LifeLab(context);
        }
        return sLifeLab;
    }

    public LifeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new LifeBaseHelper(mContext).getWritableDatabase();
    }

    public Life getLifeById(UUID lifeId){
        LifeCursorWrapper cursorWrapper = queryLife(LifeTable.Cols.UUID + " = ? ",
                new String[]{lifeId.toString()});
        try{
            if (cursorWrapper.getCount() == 0){
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getLife();
        } finally {
            cursorWrapper.close();
        }
    }

    public List<Life> getLifes(){
        List<Life> lifes = new ArrayList<>();
        LifeCursorWrapper cursorWrapper = queryLife(null, null);
        try{
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()){
                lifes.add(cursorWrapper.getLife());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }
        return lifes;
    }

    public void addLife(Life life){
        ContentValues values = getContentValues(life);
        mDatabase.insert(LifeTable.NAME, null, values);
    }

    public void deleteLife(UUID uuid){
        mDatabase.delete(LifeTable.NAME, LifeTable.Cols.UUID + " = ? ", new String[]{uuid.toString()});
    }

    public void updateLife(Life life){
        String uuid = life.getId().toString();
        ContentValues values = getContentValues(life);
        mDatabase.update(LifeTable.NAME, values, LifeTable.Cols.UUID + " = ? ", new String[]{uuid});
    }

    public boolean isHasLife(Life life){
        boolean b = true;
        LifeCursorWrapper cursorWrapper = queryLife(LifeTable.Cols.UUID + " = ? ",
                new String[]{life.getId().toString()});
        try{
            cursorWrapper.moveToFirst();
            if (cursorWrapper.isAfterLast()){
                b = false;
            }
        } finally {
            cursorWrapper.close();
        }
        return b;
    }

    private static ContentValues getContentValues(Life life){
        ContentValues values = new ContentValues();
        values.put(LifeTable.Cols.UUID, life.getId().toString());
        values.put(LifeTable.Cols.TITLE, life.getTitle());
        values.put(LifeTable.Cols.FEEL, life.getFeel());
        values.put(LifeTable.Cols.DATE, life.getDate().getTime());
        values.put(LifeTable.Cols.PICTURES, life.getPicturePath());
        return values;
    }

    private LifeCursorWrapper queryLife(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(LifeTable.NAME, null, whereClause, whereArgs, null, null, null);
        return new LifeCursorWrapper(cursor);
    }


}
