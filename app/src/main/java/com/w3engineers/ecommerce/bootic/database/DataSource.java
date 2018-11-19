package com.w3engineers.ecommerce.bootic.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.w3engineers.ecommerce.bootic.Util.UtilityClass;
import com.w3engineers.ecommerce.bootic.model.AvailableProduct;
import com.w3engineers.ecommerce.bootic.model.Product;
import com.w3engineers.ecommerce.bootic.model.ProductColor;
import com.w3engineers.ecommerce.bootic.model.ProductSize;
import com.w3engineers.ecommerce.bootic.model.SelectedProduct;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class DataSource {

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private SQLiteOpenHelper mHelper;

    public DataSource(Context context) {

        this.mContext = context;
        mHelper = new DBHelper(mContext);
        mDatabase = mHelper.getWritableDatabase();

    }

    public void deleteTebale(String tableName) {
        mDatabase.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    public void open() {
        mDatabase = mHelper.getWritableDatabase();
    }

    public void close() {
        mHelper.close();
    }

    public boolean addFavProduct(Product product) {
        ContentValues values = product.toValues();
        return (mDatabase.insert(DBFavouriteProducts.TABLE_NAME, null, values) > -1);

    }

    public long getFavProductCount() {
        return DatabaseUtils.queryNumEntries(mDatabase, DBFavouriteProducts.TABLE_NAME);
    }

    public List<Product> getAllFavProducts() {
        List<Product> favProducts = new ArrayList<>();
        Cursor cursor = mDatabase.query(DBFavouriteProducts.TABLE_NAME, DBFavouriteProducts.ALL_COLUMN,
                null, null, null, null, null);

        while (cursor.moveToNext()) {

            Product product = new Product();
            product.setId(cursor.getString(cursor.getColumnIndex(DBFavouriteProducts.COLUMN_ID)));
            product.setTitle(cursor.getString(cursor.getColumnIndex(DBFavouriteProducts.COLUMN_TITLE)));
            product.setCategory(cursor.getString(cursor.getColumnIndex(DBFavouriteProducts.COLUMN_CATEGORY)));
            product.setDescription(cursor.getString(cursor.getColumnIndex(DBFavouriteProducts.COLUMN_DESCRIPTION)));
            product.setDescription(cursor.getString(cursor.getColumnIndex(DBFavouriteProducts.COLUMN_DESCRIPTION)));
            product.setPrice(cursor.getDouble(cursor.getColumnIndex(DBFavouriteProducts.COLUMN_PRICE)));
            product.setPrevious_price(cursor.getDouble(cursor.getColumnIndex(DBFavouriteProducts.COLUMN_PREV_PRICE)));
            product.setSort(cursor.getInt(cursor.getColumnIndex(DBFavouriteProducts.COLUMN_POSITION)));
            /*try {
                product.setDate(UtilityClass.stringToDate(cursor.getString(cursor.getColumnIndex(DBFavouriteProducts.COLUMN_DATE))));
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
            product.setImage_name(cursor.getString(cursor.getColumnIndex(DBFavouriteProducts.COLUMN_IMAGE)));
            product.setUser_id(cursor.getString(cursor.getColumnIndex(DBFavouriteProducts.COLUMN_USER_ID)));

            favProducts.add(product);

        }

        return favProducts;

    }

    public List<Integer> getAllFavProductIds() {
        List<Integer> favProducts = new ArrayList<>();
        Cursor cursor = mDatabase.query(DBFavouriteProducts.TABLE_NAME, DBFavouriteProducts.ALL_COLUMN,
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            favProducts.add(cursor.getInt(cursor.getColumnIndex(DBFavouriteProducts.COLUMN_ID)));
        }
        return favProducts;

    }


    public void removeFavProducts() {
        mDatabase.execSQL("delete from " + DBFavouriteProducts.TABLE_NAME);
    }

    public Product getFavProductbyId(int productId) {
        List<Product> favProducts = new ArrayList<>();

        Cursor cursor = null;
        String producIds[] = {String.valueOf(productId)};

        if (productId != 0) {

            cursor = mDatabase.query(DBFavouriteProducts.TABLE_NAME, DBFavouriteProducts.ALL_COLUMN,
                    DBFavouriteProducts.COLUMN_ID + "=?", producIds, null, null, null);
        }

        Product product = new Product();
        product.setId(cursor.getString(cursor.getColumnIndex(DBFavouriteProducts.COLUMN_ID)));
        product.setTitle(cursor.getString(cursor.getColumnIndex(DBFavouriteProducts.COLUMN_TITLE)));
        product.setCategory(cursor.getString(cursor.getColumnIndex(DBFavouriteProducts.COLUMN_CATEGORY)));
        product.setDescription(cursor.getString(cursor.getColumnIndex(DBFavouriteProducts.COLUMN_DESCRIPTION)));
        product.setPrice(cursor.getDouble(cursor.getColumnIndex(DBFavouriteProducts.COLUMN_PRICE)));
        product.setPrevious_price(cursor.getDouble(cursor.getColumnIndex(DBFavouriteProducts.COLUMN_PREV_PRICE)));
        product.setSort(cursor.getInt(cursor.getColumnIndex(DBFavouriteProducts.COLUMN_POSITION)));
        try {
            product.setDate(UtilityClass.stringToDate(cursor.getString(cursor.getColumnIndex(DBFavouriteProducts.COLUMN_DATE))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        product.setImage_name(cursor.getString(cursor.getColumnIndex(DBFavouriteProducts.COLUMN_IMAGE)));

        return product;

    }

    public boolean removeFavProductById(String productId) {

        String table = DBFavouriteProducts.TABLE_NAME;
        String whereClause = DBFavouriteProducts.COLUMN_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(productId)};

        return mDatabase.delete(table, whereClause, whereArgs) > 0;

    }

    public boolean addCartProduct(SelectedProduct selectedProduct) {
        ContentValues values = selectedProduct.toValues();
        return (mDatabase.insert(DBCartProducts.TABLE_NAME, null, values) > -1);

    }

    public List<SelectedProduct> getAllCartProducts() {
        List<SelectedProduct> selectedProducts = new ArrayList<>();
        Cursor cursor = mDatabase.query(DBCartProducts.TABLE_NAME, DBCartProducts.ALL_COLUMN,
                null, null, null, null, null);

        while (cursor.moveToNext()) {

            SelectedProduct selectedProduct = new SelectedProduct();

            selectedProduct.setCart_id(cursor.getInt(cursor.getColumnIndex(DBCartProducts.COLUMN_ID)));
            selectedProduct.setId(cursor.getString(cursor.getColumnIndex(DBCartProducts.COLUMN_PRODUCT_ID)));
            selectedProduct.setTitle(cursor.getString(cursor.getColumnIndex(DBCartProducts.COLUMN_TITLE)));
            selectedProduct.setPrice(cursor.getDouble(cursor.getColumnIndex(DBCartProducts.COLUMN_PRICE)));
            selectedProduct.setImage_name(cursor.getString(cursor.getColumnIndex(DBCartProducts.COLUMN_IMAGE)));
            selectedProduct.setUser_id(cursor.getString(cursor.getColumnIndex(DBCartProducts.COLUMN_USER_ID)));
            selectedProduct.setQunatity(cursor.getInt(cursor.getColumnIndex(DBCartProducts.COLUMN_QUANTITY)));

            ProductColor productColor = new ProductColor();
            productColor.setColor_id(cursor.getInt(cursor.getColumnIndex(DBCartProducts.COLUMN_COLOR_ID)));
            productColor.setColorName(cursor.getString(cursor.getColumnIndex(DBCartProducts.COLUMN_COLOR_NAME)));

            ProductSize productSize = new ProductSize();
            productSize.setSize_id(cursor.getInt(cursor.getColumnIndex(DBCartProducts.COLUMN_SIZE_ID)));
            productSize.setSizeName(cursor.getString(cursor.getColumnIndex(DBCartProducts.COLUMN_SIZE_NAME)));

            selectedProduct.setInvetory_id(cursor.getInt(cursor.getColumnIndex(DBCartProducts.COLUMN_INVENTORY_ID)));

            selectedProduct.setProductColor(productColor);
            selectedProduct.setProductSize(productSize);

            selectedProducts.add(selectedProduct);

        }

        return selectedProducts;

    }

    public long getCartProductCount() {
        return DatabaseUtils.queryNumEntries(mDatabase, DBCartProducts.TABLE_NAME);
    }


    public boolean removeAllCartProducts() {
        String table = DBCartProducts.TABLE_NAME;

        return mDatabase.delete(table, null, null) > 0;

    }


    public boolean removeCartProductById(int cartId) {

        String table = DBCartProducts.TABLE_NAME;

        return mDatabase.delete(table, DBCartProducts.COLUMN_ID + "=" + cartId, null) > 0;

    }

    public void updateQuantityCart(double quantity, int cartId) {

        String table = DBCartProducts.TABLE_NAME;
        ContentValues cv = new ContentValues();
        cv.put(DBCartProducts.COLUMN_QUANTITY, quantity);

        mDatabase.update(table, cv, "cart_id=" + cartId, null);


    }

}
