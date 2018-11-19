package com.w3engineers.ecommerce.bootic.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.w3engineers.ecommerce.bootic.Util.CustomSharedPrefs;
import com.w3engineers.ecommerce.bootic.baseActivity.ActivityBaseCartIcon;

import com.w3engineers.ecommerce.bootic.category.FilterCategory;
import com.w3engineers.ecommerce.bootic.model.User;
import com.w3engineers.ecommerce.bootic.prductGrid.ProductRecylerViewAdapter;
import com.w3engineers.ecommerce.bootic.userLogin.LoginAttemptActivity;
import com.w3engineers.ecommerce.bootic.userProfile.ProfileActivity;
import com.w3engineers.ecommerce.bootic.R;
import com.w3engineers.ecommerce.bootic.userProfile.UserFavActivity;
import com.w3engineers.ecommerce.bootic.userProfile.UserOrderActivity;
import com.w3engineers.ecommerce.bootic.Util.Constants;
import com.w3engineers.ecommerce.bootic.Util.NetworkHelper;
import com.w3engineers.ecommerce.bootic.Util.UtilityClass;
import com.w3engineers.ecommerce.bootic.cart.CartActivity;
import com.w3engineers.ecommerce.bootic.category.CategoryActivity;
import com.w3engineers.ecommerce.bootic.database.DataSource;
import com.w3engineers.ecommerce.bootic.model.Product;
import com.w3engineers.ecommerce.bootic.model.ProductCategory;
import com.w3engineers.ecommerce.bootic.model.SliderMain;
import com.w3engineers.ecommerce.bootic.services.CategoryService;
import com.w3engineers.ecommerce.bootic.services.ProductGridService;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends ActivityBaseCartIcon{


    TextView tvCartCount;
    private boolean netwotkOK;

    ArrayList<SliderMain> sliderMainList;
    public ArrayList<Product> productGridList;

    ViewPager vpSliderMain;
    RecyclerView recyclerViewProduct;
    ViewPager vpPopularProducts;

    private LinearLayout dotsIndicators;

    /*START OF MENU VERIABLE*/
    TextView toobarTitle;
   // ImageView toobarLogo;
    TextView toobarLogo;
    Toolbar toolbar;
    /*END OF MENU VERIABLE*/

    /*START OF NAVIGATION DRAWER*/
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView nvMainNav;
    /*END OF NAVIGATION DRAWER*/

    ArrayList<ProductCategory> categoryList;
    ArrayList<Product> sliderPopularProductList;

    RecyclerView recyclerView;

    DataSource dataSource;

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settingToolBar();

        settingPopularProductSlider();

        settingNavDrawer();

       /* DATABASE*/
        dataSource = new DataSource(this);
        dataSource.open();

        CustomSharedPrefs.setFavProductsInPref(this,dataSource);

        //dataSource.deleteTebale(DBFavouriteProducts.TABLE_NAME);

       /* CustomSharedPrefs.removeLoggedInUser(this);*/


        /*START OF PRODUCT GRID INTENT SERVICES*/
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(productGridBroadcastReceiver , new IntentFilter(ProductGridService.MY_MESSAGE));

        netwotkOK = NetworkHelper.hasNetworkAccess(this);
        if (netwotkOK) {
            Intent intent = new Intent(this, ProductGridService.class);
            intent.setData(Uri.parse(Constants.PRODUCT_GRID_URL1));
            this.startService(intent);
        } else {
            Toast.makeText(this, "Could not connect to internet.", Toast.LENGTH_SHORT).show();
        }
        /*END OF PRODUCT GRID INTENT SERVICES*/
        recyclerView = findViewById(R.id.rv_product_category);

        /*START OF CATEGORY INTENT SERVICES*/
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(categoryBroadcastReceiver, new IntentFilter(CategoryService.CATEGORY_MESSAGE));

        netwotkOK = NetworkHelper.hasNetworkAccess(this);
        if (netwotkOK) {

            Intent intentCategory = new Intent(this, CategoryService.class);
            intentCategory.setData(Uri.parse(Constants.CATEGORY_GRID_URL));
            this.startService(intentCategory);

        } else {
            Toast.makeText(this, "Could not connect to internet.", Toast.LENGTH_SHORT).show();
        }
        /*END OF CATEGORY INTENT SERVICES*/

        // START VIEWPAGER POPULAR PRODUCTS SLIDER IMAGES

        sliderPopularProductList = new ArrayList<>();
        Product p1 = new Product();
        p1.setId("1");
        p1.setTitle("23d Lorem Epsam");
        Product p2 = new Product();
        p2.setId("2");
        p2.setTitle("Loreaa m Epsam D");
        sliderPopularProductList.add(p1);
        sliderPopularProductList.add(p2);


        vpPopularProducts = findViewById(R.id.vp_popular_products);
        PagerAdapter sliderPopularProductAdapter = new SliderPopularProductAdapter(getSupportFragmentManager());
        vpPopularProducts.setAdapter(sliderPopularProductAdapter);
        // END VIEWPAGER POPULAR PRODUCTS SLIDER IMAGES


        // START VIEWPAGER MAIN SLIDER IMAGES
        sliderMainList = new ArrayList<>();
        SliderMain sliderMain1 = new SliderMain("slider_1", "Up to 40% OFF Autumn Sale", "MEGA SALE");
        SliderMain sliderMain2 = new SliderMain("slider_2", "Newest outfits has just arrived", "NEW ARRIVALS");
        //SliderMain sliderMain3 = new SliderMain("slider_3", "Sale Upto 30% This Week", "WOMEN’S COLLECTION");

        sliderMainList.add(sliderMain1);
        sliderMainList.add(sliderMain2);
       // sliderMainList.add(sliderMain3);

        vpSliderMain = findViewById(R.id.vp_slider_main);
        PagerAdapter sliderMainAdapter = new SliderMainAdapter(getSupportFragmentManager());
        vpSliderMain.setAdapter(sliderMainAdapter);
        // END OF VIEWPAGER SLIDER IMAGES


        //START RECYLERVIEW PRODUCT GRID

        recyclerViewProduct = findViewById(R.id.rv_p_detail_product_grid);

        //END FOR RECYLER PRODUCT VIEW


        /*START OF DOTS INDICATORS*/
        dotsIndicators = findViewById(R.id.layout_slider_main_dots);
        dots(0);

        vpSliderMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                dots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }


    private BroadcastReceiver productGridBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                Product[] products = (Product[]) intent.getParcelableArrayExtra(ProductGridService.MY_PAYLOAD);
                productGridList = new ArrayList<>(Arrays.asList(products));
                setingRecylerView();
            }catch (Exception e){
                Toast.makeText(MainActivity.this, "Couldn't connect to server.", Toast.LENGTH_LONG).show();
            }
        }
    };

    private BroadcastReceiver categoryBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                ProductCategory[] productCategories = (ProductCategory[]) intent.getParcelableArrayExtra(CategoryService.CATEGORY_PAYLOAD);
                categoryList = new ArrayList<>(Arrays.asList(productCategories));
                settingHorizontalCategoryList();
            }catch (Exception e){
                Toast.makeText(MainActivity.this, "Couldn't connect to server.", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(productGridBroadcastReceiver );
        LocalBroadcastManager.getInstance(this).unregisterReceiver(categoryBroadcastReceiver);
    }

    private void setingRecylerView() {

        ProductRecylerViewAdapter productRecylerViewAdapter = new ProductRecylerViewAdapter(productGridList, this);

        recyclerViewProduct.setHasFixedSize(false);
        recyclerViewProduct.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewProduct.setNestedScrollingEnabled(false);
        recyclerViewProduct.setAdapter(productRecylerViewAdapter);
    }

    public void settingHorizontalCategoryList(){

        CategoryRecylerViewAdapter adapter = new CategoryRecylerViewAdapter(categoryList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        /*END OF HORIZOANTAL CATEGORY LIST*/

    }

    private void dots(int current) {

        dotsIndicators.removeAllViews();
        for (int i = 0; i < sliderMainList.size(); i++) {
            TextView dot = new TextView(this);
            dot.setIncludeFontPadding(false);
            dot.setHeight((int) UtilityClass.convertDpToPixel(10, this));
            dot.setWidth((int) UtilityClass.convertDpToPixel(10, this));

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int marginAsDp = (int) UtilityClass.convertDpToPixel(4, this);
            params.setMargins(marginAsDp, marginAsDp, marginAsDp, marginAsDp);
            dot.setLayoutParams(params);

            dot.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_border_bg_1));

            if (i == current) {
                dot.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_bg));
            }
            dotsIndicators.addView(dot);

        }
    }


    /*START OF MAIN SLIDER ADAPTER*/
    public class SliderMainAdapter extends FragmentStatePagerAdapter {

        public SliderMainAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return SliderMainItemFragment.newInstance(sliderMainList.get(position));
        }

        @Override
        public int getCount() {
            return sliderMainList.size();
        }

    }
    /*END OF MAIN SLIDER ADAPTER*/


    /*START OF POLULAR PRODUCT SLIDER ADAPTER*/
    public class SliderPopularProductAdapter extends FragmentStatePagerAdapter {


        public SliderPopularProductAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return SliderProductItemFragment.newInstance(sliderPopularProductList.get(position));
        }

        @Override
        public float getPageWidth(int position) {
            return .55f;
        }

        @Override
        public int getCount() {
            return sliderPopularProductList.size();
        }

    }
    /*END OF POLULAR PRODUCT SLIDER ADAPTER*/


    public void settingToolBar() {

        /*START OF TOOLBAR */
        toolbar = findViewById(R.id.toolbar);
        toobarTitle = findViewById(R.id.toolbar_title);
        toobarLogo = findViewById(R.id.toolbar_logo);
        toolbar.setTitle("");
        toobarTitle.setText(this.getString(R.string.app_name));
        toobarLogo.setVisibility(View.VISIBLE);
        toobarTitle.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        /*END OF TOOLBAR */
    }



    public void settingPopularProductSlider(){
        // START VIEWPAGER POPULAR PRODUCTS SLIDER IMAGES
        sliderPopularProductList = new ArrayList<>();
        Product p = new Product();
        sliderPopularProductList.add(p);
        // END VIEWPAGER POPULAR PRODUCTS SLIDER IMAGES

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*START FOR NAVIGATION DRAWER*/
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        /*END FOR NAVIGATION DRAWER*/

        return super.onOptionsItemSelected(item);
    }

    User loggedInUser;

    private void settingNavDrawer(){

        /*START OF NAVIGATION DRAWER */
        drawerLayout = findViewById(R.id.dl_navigation_drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        loggedInUser = CustomSharedPrefs.getLoggedInUser(MainActivity.this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_main_nav);
        View header = navigationView.getHeaderView(0);
        TextView tvName = header.findViewById(R.id.menu_profile_name);
        ImageView ivProfileImage = header.findViewById(R.id.iv_menu_profile_image);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(loggedInUser == null){
                    Intent intentLogin = new Intent(MainActivity.this, LoginAttemptActivity.class);
                    intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                    startActivity(intentLogin);
                }else{
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
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
                        Intent intentMain = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intentMain);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_account:

                        if(loggedInUser == null){
                            Intent intentLogin = new Intent(MainActivity.this, LoginAttemptActivity.class);
                            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                            startActivity(intentLogin);
                        }else{
                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        }
                        break;

                    case R.id.nav_category:
                        Intent intentCategory = new Intent(MainActivity.this, CategoryActivity.class);
                        startActivity(intentCategory);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_sub:
                        Intent intentSub = new Intent(MainActivity.this, FilterCategory.class);
                        startActivity(intentSub);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_orders:

                        if(loggedInUser == null){
                            Intent intentLogin = new Intent(MainActivity.this, LoginAttemptActivity.class);
                            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                            startActivity(intentLogin);
                        }else{
                            Intent intentOrder = new Intent(MainActivity.this, UserOrderActivity.class);
                            startActivity(intentOrder);
                        }

                        break;

                    case R.id.nav_cart:
                        Intent intentCart = new Intent(MainActivity.this, CartActivity.class);
                        startActivity(intentCart);
                        //drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_favourites:
                        if(loggedInUser == null){
                            Intent intentLogin = new Intent(MainActivity.this, LoginAttemptActivity.class);
                            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                            startActivity(intentLogin);
                        }else{
                            Intent intentFav = new Intent(MainActivity.this, UserFavActivity.class);
                            startActivity(intentFav);
                        }

                        break;
                }

                return false;
            }
        });
        /*END OF NAVIGATION DRAWER */

    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
}
