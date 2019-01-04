package com.app.adssan.ayucraze.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.app.adssan.ayucraze.Adapter.ExpandableListAdapter;
import com.app.adssan.ayucraze.Util.CustomSharedPrefs;
import com.app.adssan.ayucraze.baseActivity.ActivityBaseCartIcon;

import com.app.adssan.ayucraze.category.FilterCategory;
import com.app.adssan.ayucraze.model.MenuModel;
import com.app.adssan.ayucraze.model.User;
import com.app.adssan.ayucraze.prductGrid.ProductRecylerViewAdapter;
import com.app.adssan.ayucraze.userLogin.LoginAttemptActivity;
import com.app.adssan.ayucraze.userProfile.ProfileActivity;
import com.app.adssan.ayucraze.R;
import com.app.adssan.ayucraze.userProfile.UserFavActivity;
import com.app.adssan.ayucraze.userProfile.UserOrderActivity;
import com.app.adssan.ayucraze.Util.Constants;
import com.app.adssan.ayucraze.Util.NetworkHelper;
import com.app.adssan.ayucraze.Util.UtilityClass;
import com.app.adssan.ayucraze.cart.CartActivity;
import com.app.adssan.ayucraze.category.CategoryActivity;
import com.app.adssan.ayucraze.database.DataSource;
import com.app.adssan.ayucraze.model.Product;
import com.app.adssan.ayucraze.model.ProductCategory;
import com.app.adssan.ayucraze.model.SliderMain;
import com.app.adssan.ayucraze.services.CategoryService;
import com.app.adssan.ayucraze.services.ProductGridService;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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

    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();

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
        expandableListView = findViewById(R.id.expandableListView);
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

        prepareMenuData();
        populateExpandableList();

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

    public void onNairajaSilksClick(View v)
    {
        Intent intentSub = new Intent(MainActivity.this, FilterCategory.class);
        intentSub.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
        startActivity(intentSub);
        drawerLayout.closeDrawers();
    }


    private BroadcastReceiver productGridBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                Product[] products = (Product[]) intent.getParcelableArrayExtra(ProductGridService.MY_PAYLOAD);
                productGridList = new ArrayList<>(Arrays.asList(products));
                setingRecylerView();
            }catch (Exception e){
                Log.e("ERROR_Prouct",""+e.getMessage());
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
                Log.e("ERROR_category",""+e.getMessage());
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
                        intentSub.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
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


    private void prepareMenuData() {
        MenuModel menuModel = new MenuModel(getResources().getDrawable(R.drawable.ic_menu_home), "Home", true, true, ""); //Menu of Python Tutorials
        headerList.add(menuModel);
        menuModel = new MenuModel(getResources().getDrawable(R.drawable.ic_menu_user_black), "My Account", true, true, ""); //Menu of Python Tutorials
        headerList.add(menuModel);
        menuModel = new MenuModel(getResources().getDrawable(R.drawable.ic_menu_category_black), "Categories", true, true, ""); //Menu of Java Tutorials
        headerList.add(menuModel);
//        List<MenuModel> childModelsList = new ArrayList<>();
//        MenuModel childModel = new MenuModel("1. HOARDINGS / BILLBOARDS", false, false, "1");
//        childModelsList.add(childModel);
//        childModel = new MenuModel("2. UNIPOLES / MONOPOLES", false, false, "2");
//        childModelsList.add(childModel);
//        childModel = new MenuModel("3. CENTRALMEDIAN / POLE KIOSKS", false, false, "3");
//        childModelsList.add(childModel);
//        childModel = new MenuModel("4. BUS SHELTERS / BUS BAYS", false, false, "4");
//        childModelsList.add(childModel);
//        childModel = new MenuModel("5. ARCHES / GANTRIES / PANELS", false, false, "5");
//        childModelsList.add(childModel);
//        childModel = new MenuModel("6. FOOT OVER BRIDGES", false, false, "6");
//        childModelsList.add(childModel);
//        childModel = new MenuModel("7. TRAFFIC SIGNS / TRAFFIC SHELTERS", false, false, "7");
//        childModelsList.add(childModel);
//        childModel = new MenuModel("8. AUTO / CAB / BUS / TRAIN", false, false, "8");
//        childModelsList.add(childModel);
//        childModel = new MenuModel("9. OTHER OOH", false, false, "9");
//        childModelsList.add(childModel);
//        if (menuModel.hasChildren) {
//            Log.d("API123", "here");
//            childList.put(menuModel, childModelsList);
//        }
//        childModelsList = new ArrayList<>();

        menuModel = new MenuModel(getResources().getDrawable(R.drawable.ic_menu_orders_black), "My Orders", true, true, ""); //Menu of Python Tutorials
        headerList.add(menuModel);
        menuModel = new MenuModel(getResources().getDrawable(R.drawable.ic_menu_cart_black), "Shopping Cart", true, true, ""); //Menu of Python Tutorials
        headerList.add(menuModel);
        menuModel = new MenuModel(getResources().getDrawable(R.drawable.ic_menu_category_black), "My Favourite", true, true, ""); //Menu of Python Tutorials
        headerList.add(menuModel);
//        menuModel = new MenuModel(getResources().getDrawable(R.drawable.clothes), "Nairjara Silks", true, true, ""); //Menu of Python Tutorials
//        headerList.add(menuModel);


//        if (session.getPreferences(HomeActivity.this, Constants.LOGIN_STATUS).equalsIgnoreCase(Constants.LOGIN)) {
//            menuModel = new MenuModel(getResources().getDrawable(R.drawable.logout_icon), "Logout", true, true, ""); //Menu of Python Tutorials
//            headerList.add(menuModel);
//        }
//        if (menuModel.hasChildren) {
//            childList.put(menuModel, childModelsList);
//        }
    }

    private void populateExpandableList() {

        expandableListAdapter = new ExpandableListAdapter(this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (headerList.get(groupPosition).isGroup) {
                    //Toast.makeText(HomeActivity.this, ""+headerList.get(groupPosition).getName(), Toast.LENGTH_SHORT).show();

                    if (headerList.get(groupPosition).getName().equalsIgnoreCase("Home")) {
                        Intent intentMain = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intentMain);
                        drawerLayout.closeDrawers();
                    } else if (headerList.get(groupPosition).getName().equalsIgnoreCase("My Account")) {
                        if(loggedInUser == null){
                            Intent intentLogin = new Intent(MainActivity.this, LoginAttemptActivity.class);
                            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                            startActivity(intentLogin);
                        }else{
                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        }
                    }  else if (headerList.get(groupPosition).getName().equalsIgnoreCase("Categories")) {
                        Intent intentCategory = new Intent(MainActivity.this, CategoryActivity.class);
                        startActivity(intentCategory);
                        drawerLayout.closeDrawers();
                    } else if (headerList.get(groupPosition).getName().equalsIgnoreCase("My Orders")) {
                        if(loggedInUser == null){
                            Intent intentLogin = new Intent(MainActivity.this, LoginAttemptActivity.class);
                            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                            startActivity(intentLogin);
                        }else{
                            Intent intentOrder = new Intent(MainActivity.this, UserOrderActivity.class);
                            startActivity(intentOrder);
                        }
                    }
                    else if (headerList.get(groupPosition).getName().equalsIgnoreCase("Shopping Cart")) {
                        Intent intentCart = new Intent(MainActivity.this, CartActivity.class);
                        startActivity(intentCart);
                    }
                    else if (headerList.get(groupPosition).getName().equalsIgnoreCase("My Favourite")) {
                        if(loggedInUser == null){
                            Intent intentLogin = new Intent(MainActivity.this, LoginAttemptActivity.class);
                            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                            startActivity(intentLogin);
                        }else{
                            Intent intentFav = new Intent(MainActivity.this, UserFavActivity.class);
                            startActivity(intentFav);
                        }
//                    }else if (headerList.get(groupPosition).getName().equalsIgnoreCase("Nairjara Silks")) {
//                        Intent intentSub = new Intent(MainActivity.this, FilterCategory.class);
//                        intentSub.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
//                        startActivity(intentSub);
//                        drawerLayout.closeDrawers();
                    }
//                    } else if (headerList.get(groupPosition).getName().equalsIgnoreCase("Contact Us")) {
//                        startActivity(new Intent(MainActivity.this, ContactUs.class));
//                    } else if (headerList.get(groupPosition).getName().equalsIgnoreCase("About Us")) {
//                        startActivity(new Intent(MainActivity.this, AboutUs.class));
//                    } else if (headerList.get(groupPosition).getName().equalsIgnoreCase("Logout")) {
//
////                        AlertDialog.Builder builder1 = new AlertDialog.Builder(HomeActivity.this);
////                        builder1.setTitle("Logout");
////                        builder1.setMessage("Are you sure want to Logout?");
////                        builder1.setCancelable(true);
////                        builder1.setPositiveButton(
////                                "Logout",
////                                new DialogInterface.OnClickListener() {
////                                    public void onClick(DialogInterface dialog, int id) {
////                                        dialog.cancel();
////                                        Toast.makeText(HomeActivity.this, "Logout Successfull", Toast.LENGTH_SHORT).show();
////                                        session.setPreferences(HomeActivity.this, Constants.LOGIN_STATUS, Constants.LOGOUT);
////                                        startActivity(new Intent(HomeActivity.this, HomeActivity.class));
////                                    }
////                                });
////                        builder1.setNegativeButton(
////                                "Cancel",
////                                new DialogInterface.OnClickListener() {
////                                    public void onClick(DialogInterface dialog, int id) {
////                                        dialog.cancel();
////                                    }
////                                });
////                        AlertDialog alert11 = builder1.create();
////                        alert11.show();
//                    }

//                    if (!headerList.get(groupPosition).hasChildren) {
//                        Toast.makeText(HomeActivity.this, ""+headerList.get(groupPosition).getName(), Toast.LENGTH_SHORT).show();
//                    }
                }

                return false;
            }
        });

//        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//
//                if (childList.get(headerList.get(groupPosition)) != null) {
//                    MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
//
//                    // Toast.makeText(HomeActivity.this, "cvcvcvc", Toast.LENGTH_SHORT).show();
//
//                    if (childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("1")) {
//                        startActivity(new Intent(HomeActivity.this, PrdouctActivity.class).
//                                putExtra(Constants.PAGE_FROM, Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID, "1"));
//                    } else if (childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("2")) {
//                        startActivity(new Intent(HomeActivity.this, PrdouctActivity.class)
//                                .putExtra(Constants.PAGE_FROM, Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID, "2"));
//                    } else if (childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("3")) {
//                        startActivity(new Intent(HomeActivity.this, PrdouctActivity.class)
//                                .putExtra(Constants.PAGE_FROM, Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID, "3"));
//                    } else if (childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("4")) {
//                        startActivity(new Intent(HomeActivity.this, PrdouctActivity.class)
//                                .putExtra(Constants.PAGE_FROM, Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID, "4"));
//                    } else if (childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("5")) {
//                        startActivity(new Intent(HomeActivity.this, PrdouctActivity.class)
//                                .putExtra(Constants.PAGE_FROM, Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID, "5"));
//                    } else if (childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("6")) {
//                        startActivity(new Intent(HomeActivity.this, PrdouctActivity.class)
//                                .putExtra(Constants.PAGE_FROM, Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID, "6"));
//                    } else if (childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("7")) {
//                        startActivity(new Intent(HomeActivity.this, PrdouctActivity.class)
//                                .putExtra(Constants.PAGE_FROM, Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID, "7"));
//                    } else if (childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("8")) {
//                        startActivity(new Intent(HomeActivity.this, PrdouctActivity.class)
//                                .putExtra(Constants.PAGE_FROM, Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID, "8"));
//                    } else if (childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("9")) {
//                        startActivity(new Intent(HomeActivity.this, PrdouctActivity.class)
//                                .putExtra(Constants.PAGE_FROM, Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID, "9"));
//                    }
////                    if (model.getuId().length() > 0) {
////                            onBackPressed();
////                    }
//                }
//                return false;
//            }
//        });
    }



    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
}
