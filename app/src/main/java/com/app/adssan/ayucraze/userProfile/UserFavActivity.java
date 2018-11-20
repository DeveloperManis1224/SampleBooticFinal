package com.app.adssan.ayucraze.userProfile;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
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

import com.app.adssan.ayucraze.R;
import com.app.adssan.ayucraze.Util.Constants;
import com.app.adssan.ayucraze.Util.NetworkHelper;
import com.app.adssan.ayucraze.Util.RequestPackage;
import com.app.adssan.ayucraze.Util.UtilityClass;
import com.app.adssan.ayucraze.baseActivity.ActivityBaseCartIcon;
import com.app.adssan.ayucraze.cart.CartActivity;
import com.app.adssan.ayucraze.database.DataSource;
import com.app.adssan.ayucraze.model.Product;
import com.app.adssan.ayucraze.prductGrid.ProductGridActivity;
import com.app.adssan.ayucraze.prductGrid.ProductRecylerViewAdapter;
import com.app.adssan.ayucraze.services.ProductGridGetService;

import java.util.ArrayList;
import java.util.Map;

public class UserFavActivity extends ActivityBaseCartIcon {

    /*START OF MENU VERIABLE*/
    TextView toobarTitle;
    Toolbar toolbar;
    //ImageView toolbarLogo;
    TextView toolbarLogo;
    /*END OF MENU VERIABLE*/

    ArrayList<Product> productGridList;
    RecyclerView recyclerViewProduct;

    private boolean networkOK;

    private DataSource dataSource;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_fav);

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

        recyclerViewProduct = findViewById(R.id.rv_user_fav_product_grid);
        dataSource = new DataSource(this);
        productGridList = (ArrayList<Product>) dataSource.getAllFavProducts();

        settingRecylerView();

    }

    private void settingRecylerView() {
        FavProductRecylerViewAdapter favProductRecylerViewAdapter = new FavProductRecylerViewAdapter(productGridList, this);

        recyclerViewProduct.setHasFixedSize(false);
        recyclerViewProduct.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewProduct.setNestedScrollingEnabled(false);
        recyclerViewProduct.setAdapter(favProductRecylerViewAdapter);
    }


}
