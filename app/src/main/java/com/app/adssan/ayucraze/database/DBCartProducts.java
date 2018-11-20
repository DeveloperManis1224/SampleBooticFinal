package com.app.adssan.ayucraze.database;

public class DBCartProducts {

    public static final String TABLE_NAME = "cartProducts";
    public static final String COLUMN_ID = "cart_id";
    public static final String COLUMN_PRODUCT_ID = "product_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_USER_ID = "userId";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_SIZE_ID = "size_id";
    public static final String COLUMN_SIZE_NAME= "size_name";
    public static final String COLUMN_INVENTORY_ID = "inventory_id";
    public static final String COLUMN_COLOR_ID = "color_id";
    public static final String COLUMN_COLOR_NAME= "color_name";

    public static final String[] ALL_COLUMN =
            {COLUMN_ID, COLUMN_PRODUCT_ID, COLUMN_TITLE, COLUMN_PRICE, COLUMN_IMAGE, COLUMN_USER_ID, COLUMN_QUANTITY, COLUMN_SIZE_NAME, COLUMN_SIZE_ID, COLUMN_INVENTORY_ID, COLUMN_COLOR_NAME, COLUMN_COLOR_ID};

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    COLUMN_PRODUCT_ID + " TEXT, " +
                    COLUMN_TITLE + " TEXT," +
                    COLUMN_PRICE + " REAL," +
                    COLUMN_IMAGE + " TEXT," +
                    COLUMN_USER_ID + " TEXT," +
                    COLUMN_QUANTITY + " INTEGER," +
                    COLUMN_SIZE_NAME + " TEXT," +
                    COLUMN_SIZE_ID + " INTEGER," +
                    COLUMN_INVENTORY_ID + " INTEGER," +
                    COLUMN_COLOR_NAME + " TEXT," +
                    COLUMN_COLOR_ID + " INTEGER" + ");";

    public static final String SQL_DELETE =
            "DROP TABLE " + TABLE_NAME;

}
