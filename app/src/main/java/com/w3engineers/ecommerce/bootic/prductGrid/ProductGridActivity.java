package com.w3engineers.ecommerce.bootic.prductGrid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.w3engineers.ecommerce.bootic.R;
import com.w3engineers.ecommerce.bootic.Util.Constants;
import com.w3engineers.ecommerce.bootic.Util.NetworkHelper;
import com.w3engineers.ecommerce.bootic.Util.RequestPackage;
import com.w3engineers.ecommerce.bootic.Util.UtilityClass;
import com.w3engineers.ecommerce.bootic.baseActivity.ActivityBaseCartIcon;
import com.w3engineers.ecommerce.bootic.cart.CartActivity;
import com.w3engineers.ecommerce.bootic.model.Product;
import com.w3engineers.ecommerce.bootic.services.ProductGridGetService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProductGridActivity extends ActivityBaseCartIcon{

    /*START OF MENU VERIABLE*/
    TextView toobarTitle;
    Toolbar toolbar;
    //ImageView toolbarLogo;
    TextView toolbarLogo;
    /*END OF MENU VERIABLE*/

    ArrayList<Product> productGridList;
    RecyclerView recyclerViewProduct;

    private boolean networkOK;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_grid);

        /*START OF TOOLBAR */
        toolbar = findViewById(R.id.toolbar);
        toobarTitle = findViewById(R.id.toolbar_title);
        toolbarLogo = findViewById(R.id.toolbar_logo);
        toolbar.setTitle("");
        toolbarLogo.setVisibility(View.VISIBLE);
        toobarTitle.setVisibility(View.GONE);
        toobarTitle.setText(this.getString(R.string.app_name));

        /*CHECKING THE TOOLBAR TITLE FROM USER FAVOURITES*/
        String toolbarTitle = getIntent().getStringExtra(UtilityClass.TOOLBAR_TITLE);
        if (toolbarTitle != null) {

            toobarTitle.setText(toolbarTitle);
            toolbarLogo.setVisibility(View.GONE);
            toobarTitle.setVisibility(View.VISIBLE);

        }
        /*CHECKING THE TOOLBAR TITLE FROM USER FAVOURITES*/

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /*END OF TOOLBAR */

        recyclerViewProduct = findViewById(R.id.rv_product_grid);


        String categoryId = getIntent().getStringExtra(Constants.INTENT_CATEGORY_ID);

        /*START INTENT SERVICE*/
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(productBroadcastReceiver, new IntentFilter(ProductGridGetService.MY_MESSAGE_GET));

        networkOK = NetworkHelper.hasNetworkAccess(this);
        if (networkOK) {

            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setEndPoint(Constants.PRODUCT_GRID_URL);
            requestPackage.setParam("category", categoryId);

            Intent intentProduct = new Intent(this, ProductGridGetService.class);
            intentProduct.putExtra(ProductGridGetService.PRODUCT_REQUEST_PACKAGE, requestPackage);
            startService(intentProduct);


        } else {
            Toast.makeText(this, "Could not connect to internet.", Toast.LENGTH_SHORT).show();
        }
        /*END INTENT SERVICE*/


    }

    BroadcastReceiver productBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Product[] products = (Product[]) intent.getParcelableArrayExtra(ProductGridGetService.MY_PAYLOAD_GET);
            productGridList = new ArrayList<>(Arrays.asList(products));
            settingRecylerView();
        }
    };

    private void settingRecylerView() {
        ProductRecylerViewAdapter productRecylerViewAdapter =
                new ProductRecylerViewAdapter(productGridList, this);

        recyclerViewProduct.setHasFixedSize(false);
        recyclerViewProduct.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewProduct.setNestedScrollingEnabled(false);
        recyclerViewProduct.setAdapter(productRecylerViewAdapter);
    }
}
