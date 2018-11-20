package com.app.adssan.ayucraze.cart;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.app.adssan.ayucraze.R;
import com.app.adssan.ayucraze.Util.Constants;
import com.app.adssan.ayucraze.Util.CustomSharedPrefs;
import com.app.adssan.ayucraze.Util.UtilityClass;
import com.app.adssan.ayucraze.checkoutPayment.ShippingActivity;
import com.app.adssan.ayucraze.database.DataSource;
import com.app.adssan.ayucraze.model.CartProduct;
import com.app.adssan.ayucraze.model.CustomProductInventory;
import com.app.adssan.ayucraze.model.ProductColor;
import com.app.adssan.ayucraze.model.ProductSize;
import com.app.adssan.ayucraze.model.SelectedProduct;
import com.app.adssan.ayucraze.userLogin.LoginActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    public static double totalPrice = 0;
    TextView tvTotalWriting;
    android.support.v7.widget.Toolbar toolbar;
    private List<SelectedProduct> selectedProductList;
    private DataSource dataSource;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dataSource = new DataSource(this);

        UtilityClass.buttonScaleIconRight(this, findViewById(R.id.btn_cart_checkout), R.drawable.ic_arrow_right_white, .8, 1.1);

        /*START OF TOOLBAR*/
        TextView toobarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toobarTitle.setText(this.getString(R.string.title_cart));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*END OF TOOLBAR*/

        tvTotalWriting = findViewById(R.id.tv_total_price);

        /*START OF CART */

        selectedProductList = dataSource.getAllCartProducts();

        StringBuilder inventoryIds = new StringBuilder();
        for(SelectedProduct selectedProduct : selectedProductList){
            inventoryIds.append(selectedProduct.getInvetory_id() + "-");
        }
        getInventories(inventoryIds.toString());

        totalPrice = 0;
        for (SelectedProduct selectedProduct : selectedProductList) {
            totalPrice = totalPrice + getTotalPrice(selectedProduct);
        }
        tvTotalWriting.setText(UtilityClass.getNumberFormat(totalPrice));
        Button btn_cart_checkout = findViewById(R.id.btn_cart_checkout);
        btn_cart_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CustomSharedPrefs.getLoggedInUser(CartActivity.this) == null) {
                    Intent intent = new Intent(CartActivity.this, LoginActivity.class);
                    intent.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_CHECKOUT);
                    //intent.putExtra(Constants.TO_REGISTER, Constants.FROM_CART);
                    startActivity(intent);
                } else {

                    String totalPriceStr = String.valueOf(totalPrice);

                    if (totalPrice > 0) {
                        Intent intent = new Intent(CartActivity.this, ShippingActivity.class);
                        intent.putExtra(Constants.TOTAL_PRICE, totalPriceStr);
                        startActivity(intent);
                    } else {
                        Toast.makeText(CartActivity.this, "Please add a product", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    ArrayList<CustomProductInventory> customProductInventoryList;

    private void getInventories(String inventory_ids){

        Log.v("INCCCC",""+inventory_ids);
        String JSON_URL = Constants.INVENTORY_BY_ID + "?inventory_ids=" + inventory_ids;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.v("INCCCC","RES"+response);
                Type listType = new TypeToken<List<CustomProductInventory>>() {}.getType();
                customProductInventoryList = new Gson().fromJson(response, listType);
                CartListAdapter cartListAdapter = new CartListAdapter(CartActivity.this, R.layout.item_list_cart_product,
                        selectedProductList, tvTotalWriting, customProductInventoryList);
                ListView lvCartProductList = findViewById(R.id.lv_cart_product_list);
                lvCartProductList.setAdapter(cartListAdapter);
                lvCartProductList.setDivider(null);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CartActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        queue.getCache().clear();
        queue.add(stringRequest);
    }



    private double getTotalPrice(SelectedProduct selectedProduct) {

        return selectedProduct.getPrice() * selectedProduct.getQunatity();
    }



}
