package com.liucheng.android.mylife.database;

/**
 * Created by liucheng on 2017/5/22.
 */

public class LifeDBSchema {

    public static final class LifeTable{
        public static final String NAME = "lifes";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String FEEL = "feel";
            public static final String DATE = "date";
            public static final String PICTURES = "pictures";
        }
    }
}
