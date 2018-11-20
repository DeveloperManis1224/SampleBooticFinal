package com.app.adssan.ayucraze.database;

public class DBFavouriteProducts {

    public static final String TABLE_NAME = "favouriteProducts";
    public static final String COLUMN_ID = "id";
    //public static final String COLUMN_TITLE = "subtitle";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_PREV_PRICE = "previousPrice";
    public static final String COLUMN_DATE = "addedDate";
    public static final String COLUMN_POSITION = "sortPosition";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_USER_ID = "userId";

    public static final String[] ALL_COLUMN =
            {COLUMN_ID, COLUMN_TITLE, COLUMN_CATEGORY, COLUMN_DESCRIPTION,
                    COLUMN_PRICE, COLUMN_PREV_PRICE, COLUMN_DATE, COLUMN_POSITION, COLUMN_IMAGE, COLUMN_USER_ID};

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " TEXT PRIMARY KEY ," +
                    COLUMN_TITLE + " TEXT," +
                    COLUMN_CATEGORY + " TEXT," +
                    COLUMN_DESCRIPTION + " TEXT," +
                    COLUMN_PRICE + " REAL," +
                    COLUMN_PREV_PRICE + " REAL," +
                    COLUMN_DATE + " DATE," +
                    COLUMN_POSITION + " INTEGER," +
                    COLUMN_IMAGE + " TEXT," +
                    COLUMN_USER_ID + " TEXT" + ");";


    public static final String SQL_DELETE =
            "DROP TABLE " + TABLE_NAME;

}
