package com.app.adssan.ayucraze.userProfile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.app.adssan.ayucraze.Adapter.DataAdapter;
import com.app.adssan.ayucraze.Adapter.DataParser;
import com.app.adssan.ayucraze.R;
import com.app.adssan.ayucraze.Util.Constants;
import com.app.adssan.ayucraze.Util.CustomSharedPrefs;
import com.app.adssan.ayucraze.baseActivity.ActivityBaseCartIcon;
import com.app.adssan.ayucraze.cart.CartActivity;
import com.app.adssan.ayucraze.cart.CartListAdapter;
import com.app.adssan.ayucraze.database.DataSource;
import com.app.adssan.ayucraze.model.CartProduct;
import com.app.adssan.ayucraze.model.CustomProductInventory;
import com.app.adssan.ayucraze.model.ProductColor;
import com.app.adssan.ayucraze.model.ProductSize;
import com.app.adssan.ayucraze.model.SelectedProduct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserOrderActivity extends ActivityBaseCartIcon {

    /*START OF MENU VERIABLE*/
    TextView toobarTitle;
    android.support.v7.widget.Toolbar toolbar;
    /*END OF MENU VERIABLE*/

    ArrayList<DataParser> productDetails=new ArrayList<> ();
    public static RecyclerView recycleView;
    private List<SelectedProduct> selectedProductList;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private String activityTitle = "aaaaa";
    DataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);
        recycleView = findViewById(R.id.lv_ordered_product);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);
        /*START OF TOOLBAR*/
        TextView toobarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toobarTitle.setText(getString(R.string.title_orders));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*END OF TOOLBAR*/

        /*START OF ORDEDRED PRODUCTS */

        dataSource = new DataSource(this);

//        selectedProductList = dataSource.getAllCartProducts();
        selectedProductList = dataSource.getAllCartProducts();
        StringBuilder inventoryIds = new StringBuilder();
        for(SelectedProduct selectedProduct : selectedProductList){
            inventoryIds.append(selectedProduct.getInvetory_id() + "-");
        }
       // getInventories(inventoryIds.toString());
        getInventories("44");
    }


    ArrayList<CustomProductInventory> customProductInventoryList;

    private void getInventories( String inventory_ids){

        String JSON_URL = "https://ayucraze.com/api.php?user_id=" + CustomSharedPrefs.getLoggedInUserId(UserOrderActivity.this);
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.v("VALUESSSSS",""+"///"+JSON_URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("VALUESSSSS",""+response);
                DataAdapter dad=new DataAdapter(productDetails);
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray jary = obj.getJSONArray("Result");

                    if (jary.length() == 0) {
                        Toast.makeText(UserOrderActivity.this, "No Orders", Toast.LENGTH_SHORT).show();
                    } else {

                        for (int i = 0; i < jary.length(); i++) {
                            JSONObject c = null;
                            try {
                                c = jary.getJSONObject(i);
                                String orderId = c.getString("order_id");
                                String orderMethod = c.getString("order_method");
                                String orderAmount = c.getString("order_amount");
                                String productId = "00";
                                String productTitle = c.getString("title");
                                String productPrice = c.getString("price");
                                productDetails.add(new DataParser(orderId, orderMethod, orderAmount, productId, productTitle, productPrice));
                                recycleView.setAdapter(dad);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    } catch(JSONException e){
                        e.printStackTrace();
                    }



//                ListView lvCartProductList = findViewById(R.id.lv_ordered_product_list);
//                lvCartProductList.setAdapter(dad);
//                lvCartProductList.setDivider(null);
//                Log.v("INVENTRY","     "+response+"///");
//                Type listType = new TypeToken<List<CustomProductInventory>>() {}.getType();
//                customProductInventoryList = new Gson().fromJson(response, listType);
//
//                CartListAdapter cartListAdapter = new CartListAdapter(UserOrderActivity.this, R.layout.item_list_cart_product,
//                        selectedProductList, null, customProductInventoryList);
//                ListView lvCartProductList = findViewById(R.id.lv_ordered_product_list);
//                lvCartProductList.setAdapter(cartListAdapter);
//                lvCartProductList.setDivider(null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("INVENTRY","Error : "+error);
                Toast.makeText(UserOrderActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.getCache().clear();
        queue.add(stringRequest);
    }
}
