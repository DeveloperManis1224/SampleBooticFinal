package com.w3engineers.ecommerce.bootic.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.util.Log;

import com.w3engineers.ecommerce.bootic.database.DBCartProducts;
import com.w3engineers.ecommerce.bootic.database.DBFavouriteProducts;

public class SelectedProduct extends Product{

    private int cart_id;
    private int invetory_id;
    private int qunatity;
    private ProductSize productSize;
    private ProductColor productColor;


    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_db_id) {
        this.cart_id = cart_db_id;
    }

    public int getInvetory_id() {
        return invetory_id;
    }

    public void setInvetory_id(int invetory_id) {
        this.invetory_id = invetory_id;
    }

    public void setProduct(Product product){
        setTitle(product.getTitle());
        setCategory(product.getCategory());
        setDescription(product.getDescription());
        setPrice(product.getPrice());
        setPrevious_price(product.getPrevious_price());
        setSort(product.getSort());
        setDate(product.getDate());
        setImage_name(product.getImage_name());
        setUser_id(product.getUser_id());
    }

    public int getQunatity() {
        return qunatity;
    }

    public ProductSize getProductSize() {
        return productSize;
    }

    public ProductColor getProductColor() {
        return productColor;
    }


    public void setQunatity(int qunatity) {
        this.qunatity = qunatity;
    }

    public void setProductSize(ProductSize productSize) {
        this.productSize = productSize;
    }

    public void setProductColor(ProductColor productColor) {
        this.productColor = productColor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.qunatity);
        dest.writeParcelable(this.productSize, flags);
        dest.writeParcelable(this.productColor, flags);
    }

    public SelectedProduct() {
    }

    protected SelectedProduct(Parcel in) {
        super(in);
        this.qunatity = in.readInt();
        this.productSize = in.readParcelable(ProductSize.class.getClassLoader());
        this.productColor = in.readParcelable(ProductColor.class.getClassLoader());
    }

    public static final Creator<SelectedProduct> CREATOR = new Creator<SelectedProduct>() {
        @Override
        public SelectedProduct createFromParcel(Parcel source) {
            return new SelectedProduct(source);
        }

        @Override
        public SelectedProduct[] newArray(int size) {
            return new SelectedProduct[size];
        }
    };

    public ContentValues toValues(){

        ContentValues values = new ContentValues(10);
        //values.put(DBCartProducts.COLUMN_ID, getCart_id());
        values.put(DBCartProducts.COLUMN_PRODUCT_ID, getId());
        values.put(DBCartProducts.COLUMN_TITLE, getTitle());
        values.put(DBCartProducts.COLUMN_PRICE, getPrice());
        values.put(DBCartProducts.COLUMN_IMAGE, getImage_name());
        values.put(DBCartProducts.COLUMN_USER_ID, getUser_id());
        values.put(DBCartProducts.COLUMN_QUANTITY, String.valueOf(qunatity));
        values.put(DBCartProducts.COLUMN_SIZE_ID, productSize.getSize_id());
        values.put(DBCartProducts.COLUMN_SIZE_NAME, productSize.getSizeName());
        values.put(DBCartProducts.COLUMN_COLOR_ID, productColor.getColor_id());
        values.put(DBCartProducts.COLUMN_COLOR_NAME, productColor.getColorName());
        values.put(DBCartProducts.COLUMN_INVENTORY_ID, getInvetory_id());

        return values;
    }

}
