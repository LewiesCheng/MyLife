package com.liucheng.android.mylife.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.liucheng.android.mylife.Life;
import com.liucheng.android.mylife.database.LifeDBSchema.LifeTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by liucheng on 2017/5/22.
 */

public class LifeCursorWrapper extends CursorWrapper {
    public LifeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Life getLife(){
        String uuidString = getString(getColumnIndex(LifeTable.Cols.UUID));
        String title = getString(getColumnIndex(LifeTable.Cols.TITLE));
        String feel = getString(getColumnIndex(LifeTable.Cols.FEEL));
        Long date = getLong(getColumnIndex(LifeTable.Cols.DATE));
        String pictures = getString(getColumnIndex(LifeTable.Cols.PICTURES));

        Life life = new Life(UUID.fromString(uuidString));
        life.setTitle(title);
        life.setFeel(feel);
        life.setDate(new Date(date));
        life.setPicturePath(pictures);
        return life;
    }
}
