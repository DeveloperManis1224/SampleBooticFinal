package com.app.adssan.ayucraze.category;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adssan.ayucraze.Util.CustomSharedPrefs;
import com.app.adssan.ayucraze.baseActivity.ActivityBaseCartIcon;
import com.app.adssan.ayucraze.main.MainActivity;
import com.app.adssan.ayucraze.model.User;
import com.app.adssan.ayucraze.prductGrid.ProductGridActivity;
import com.app.adssan.ayucraze.userLogin.LoginActivity;
import com.app.adssan.ayucraze.userLogin.LoginAttemptActivity;
import com.app.adssan.ayucraze.userProfile.ProfileActivity;
import com.app.adssan.ayucraze.R;
import com.app.adssan.ayucraze.userProfile.UserFavActivity;
import com.app.adssan.ayucraze.userProfile.UserOrderActivity;
import com.app.adssan.ayucraze.Util.Constants;
import com.app.adssan.ayucraze.Util.NetworkHelper;
import com.app.adssan.ayucraze.Util.UtilityClass;
import com.app.adssan.ayucraze.cart.CartActivity;
import com.app.adssan.ayucraze.model.Product;
import com.app.adssan.ayucraze.model.ProductCategory;
import com.app.adssan.ayucraze.services.CategoryService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FilterCategory extends ActivityBaseCartIcon{

    /*START OF MENU VERIABLE*/
    TextView toobarTitle;
    Toolbar toolbar;
    //ImageView toolbarLogo;
    TextView toolbarLogo;
    /*END OF MENU VERIABLE*/

    ArrayList<Product> productGridList;
    RecyclerView recyclerViewProduct;

    /*START OF NAVIGATION DRAWER*/
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView nvMainNav;
    /*END OF NAVIGATION DRAWER*/

    ArrayList<ProductCategory> categoryList;
    RecyclerView recyclerViewCategory;

    public static String getTypeCategeory="";

    private boolean netwotkOK;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_category);

        /*START OF TOOLBAR */
        toolbar = findViewById(R.id.toolbar);
        toobarTitle = findViewById(R.id.toolbar_title);
        toolbarLogo = findViewById(R.id.toolbar_logo);
        toolbar.setTitle("");
        toobarTitle.setText(this.getString(R.string.app_name));
        toolbarLogo.setVisibility(View.VISIBLE);
        toobarTitle.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        /*END OF TOOLBAR */

        settingNavDrawer();
        getTypeCategeory= getIntent().getExtras().getString("type");
        Log.v("TTTTT",""+getTypeCategeory);

        categoryList = new ArrayList<>();

        recyclerViewCategory = findViewById(R.id.rv_product_category_grid);

        //END FOR RECYLER PRODUCT VIEW

        /*START OF INTENT SERVICES*/
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mbroadcastReceiver, new IntentFilter(CategoryService.CATEGORY_MESSAGE));

        netwotkOK = NetworkHelper.hasNetworkAccess(this);
        if (netwotkOK) {
            Intent intent = new Intent(this, CategoryService.class);
            intent.setData(Uri.parse(Constants.FILTER_GRID_URL ));
            this.startService(intent);
        } else {
            Toast.makeText(this, "Could not connect to internet.", Toast.LENGTH_SHORT).show();
        }
        /*END OF INTENT SERVICES*/
    }



    private BroadcastReceiver mbroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                ProductCategory[] productCategories =
                        (ProductCategory[]) intent.getParcelableArrayExtra(CategoryService.CATEGORY_PAYLOAD);

                categoryList = new ArrayList<>(Arrays.asList(productCategories));
                settingRecylerView();

            }catch (NullPointerException ex)
            {
                ex.printStackTrace();
                Toast.makeText(context, "No Products...", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void settingRecylerView(){
        CategoryRecylerViewGridAdapter productRecylerViewAdapter = new CategoryRecylerViewGridAdapter(categoryList, this);

        //recyclerViewCategory.setHasFixedSize(true);
        recyclerViewCategory.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerViewCategory.setNestedScrollingEnabled(false);
        recyclerViewCategory.setAdapter(productRecylerViewAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mbroadcastReceiver);
    }

    User loggedInUser;

    private void settingNavDrawer(){

        /*START OF NAVIGATION DRAWER */
        drawerLayout = findViewById(R.id.dl_navigation_drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        loggedInUser = CustomSharedPrefs.getLoggedInUser(FilterCategory.this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_main_nav);
        View header = navigationView.getHeaderView(0);
        TextView tvName = header.findViewById(R.id.menu_profile_name);
        ImageView ivProfileImage = header.findViewById(R.id.iv_menu_profile_image);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(loggedInUser == null){
                    Intent intentLogin = new Intent(FilterCategory.this, LoginAttemptActivity.class);
                    intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                    startActivity(intentLogin);
                }else{
                    Intent intent = new Intent(FilterCategory.this, ProfileActivity.class);
                    startActivity(intent);
                }
            }
        });

        if(loggedInUser != null){
            tvName.setText(loggedInUser.getFirst_name());
        }


        nvMainNav = findViewById(R.id.nv_main_nav);
        nvMainNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {



                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intentMain = new Intent(FilterCategory.this, MainActivity.class);
                        startActivity(intentMain);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_account:

                        if(loggedInUser == null){
                            Intent intentLogin = new Intent(FilterCategory.this, LoginAttemptActivity.class);
                            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                            startActivity(intentLogin);
                        }else{
                            Intent intent = new Intent(FilterCategory.this, ProfileActivity.class);
                            startActivity(intent);
                        }
                        break;

                    case R.id.nav_category:
                        Intent intentCategory = new Intent(FilterCategory.this, CategoryActivity.class);
                        startActivity(intentCategory);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_sub:
                        Intent intentSub = new Intent(FilterCategory.this, FilterCategory.class);
                        startActivity(intentSub);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_orders:

                        if(loggedInUser == null){
                            Intent intentLogin = new Intent(FilterCategory.this, LoginAttemptActivity.class);
                            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                            startActivity(intentLogin);
                        }else{
                            Intent intentOrder = new Intent(FilterCategory.this, UserOrderActivity.class);
                            startActivity(intentOrder);
                        }
                        break;

                    case R.id.nav_cart:
                        Intent intentCart = new Intent(FilterCategory.this, CartActivity.class);
                        startActivity(intentCart);
                        //drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_favourites:
                        if(loggedInUser == null){
                            Intent intentLogin = new Intent(FilterCategory.this, LoginAttemptActivity.class);
                            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                            startActivity(intentLogin);
                        }else{
                            Intent intentFav = new Intent(FilterCategory.this, UserFavActivity.class);
                            startActivity(intentFav);
                        }

                        break;
                }

                return false;
            }
        });
        /*END OF NAVIGATION DRAWER */

    }

}

