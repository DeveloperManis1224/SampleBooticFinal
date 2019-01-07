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
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adssan.ayucraze.Adapter.ExpandableListAdapter;
import com.app.adssan.ayucraze.Util.CustomSharedPrefs;
import com.app.adssan.ayucraze.baseActivity.ActivityBaseCartIcon;
import com.app.adssan.ayucraze.main.MainActivity;
import com.app.adssan.ayucraze.model.MenuModel;
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
import java.util.List;
import java.util.Map;

public class CategoryActivity extends ActivityBaseCartIcon{

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

  //  public static String getTypeCategeory="";
    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();

    private boolean netwotkOK;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        /*START OF TOOLBAR */
        expandableListView = findViewById(R.id.expandableListView);
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
//       getTypeCategeory= getIntent().getExtras().getString("type");
//       Log.v("TTTTT",""+getTypeCategeory);

        categoryList = new ArrayList<>();

        recyclerViewCategory = findViewById(R.id.rv_product_category_grid);

        //END FOR RECYLER PRODUCT VIEW

        /*START OF INTENT SERVICES*/
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mbroadcastReceiver, new IntentFilter(CategoryService.CATEGORY_MESSAGE));

        netwotkOK = NetworkHelper.hasNetworkAccess(this);
        if (netwotkOK) {

            Intent intent = new Intent(this, CategoryService.class);
            intent.setData(Uri.parse(Constants.CATEGORY_GRID_URL1 ));
            this.startService(intent);

        } else {
            Toast.makeText(this, "Could not connect to internet.", Toast.LENGTH_SHORT).show();
        }
        /*END OF INTENT SERVICES*/
        prepareMenuData();
        populateExpandableList();

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
                        Intent intentMain = new Intent(CategoryActivity.this, MainActivity.class);
                        startActivity(intentMain);
                        drawerLayout.closeDrawers();
                    } else if (headerList.get(groupPosition).getName().equalsIgnoreCase("My Account")) {
                        if(loggedInUser == null){
                            Intent intentLogin = new Intent(CategoryActivity.this, LoginAttemptActivity.class);
                            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                            startActivity(intentLogin);
                        }else{
                            Intent intent = new Intent(CategoryActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        }
                    }  else if (headerList.get(groupPosition).getName().equalsIgnoreCase("Categories")) {
                        Intent intentCategory = new Intent(CategoryActivity.this, CategoryActivity.class);
                        startActivity(intentCategory);
                        drawerLayout.closeDrawers();
                    } else if (headerList.get(groupPosition).getName().equalsIgnoreCase("My Orders")) {
                        if(loggedInUser == null){
                            Intent intentLogin = new Intent(CategoryActivity.this, LoginAttemptActivity.class);
                            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                            startActivity(intentLogin);
                        }else{
                            Intent intentOrder = new Intent(CategoryActivity.this, UserOrderActivity.class);
                            startActivity(intentOrder);
                        }
                    }
                    else if (headerList.get(groupPosition).getName().equalsIgnoreCase("Shopping Cart")) {
                        Intent intentCart = new Intent(CategoryActivity.this, CartActivity.class);
                        startActivity(intentCart);
                    }
                    else if (headerList.get(groupPosition).getName().equalsIgnoreCase("My Favourite")) {
                        if(loggedInUser == null){
                            Intent intentLogin = new Intent(CategoryActivity.this, LoginAttemptActivity.class);
                            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                            startActivity(intentLogin);
                        }else{
                            Intent intentFav = new Intent(CategoryActivity.this, UserFavActivity.class);
                            startActivity(intentFav);
                        }

                    }
                }

                return false;
            }
        });

    }

    public void onNairajaSilksClick(View v)
    {
        Intent intentSub = new Intent(CategoryActivity.this, FilterCategory.class);
        intentSub.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
        startActivity(intentSub);
        drawerLayout.closeDrawers();
    }

    private BroadcastReceiver mbroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ProductCategory[] productCategories =
                    (ProductCategory[]) intent.getParcelableArrayExtra(CategoryService.CATEGORY_PAYLOAD);
            categoryList = new ArrayList<>(Arrays.asList(productCategories));

           settingRecylerView();

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

        loggedInUser = CustomSharedPrefs.getLoggedInUser(CategoryActivity.this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_main_nav);
        View header = navigationView.getHeaderView(0);
        TextView tvName = header.findViewById(R.id.menu_profile_name);
        ImageView ivProfileImage = header.findViewById(R.id.iv_menu_profile_image);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(loggedInUser == null){
                    Intent intentLogin = new Intent(CategoryActivity.this, LoginAttemptActivity.class);
                    intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                    startActivity(intentLogin);
                }else{
                    Intent intent = new Intent(CategoryActivity.this, ProfileActivity.class);
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
                        Intent intentMain = new Intent(CategoryActivity.this, MainActivity.class);
                        startActivity(intentMain);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_account:

                        if(loggedInUser == null){
                            Intent intentLogin = new Intent(CategoryActivity.this, LoginAttemptActivity.class);
                            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                            startActivity(intentLogin);
                        }else{
                            Intent intent = new Intent(CategoryActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        }
                        break;

                    case R.id.nav_category:
                        Intent intentCategory = new Intent(CategoryActivity.this, CategoryActivity.class);
                        startActivity(intentCategory);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_sub:
                        Intent intentSub = new Intent(CategoryActivity.this, FilterCategory.class);
                        startActivity(intentSub);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_orders:

                        if(loggedInUser == null){
                            Intent intentLogin = new Intent(CategoryActivity.this, LoginAttemptActivity.class);
                            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                            startActivity(intentLogin);
                        }else{
                            Intent intentOrder = new Intent(CategoryActivity.this, UserOrderActivity.class);
                            startActivity(intentOrder);
                        }
                        break;
                    case R.id.nav_cart:
                        Intent intentCart = new Intent(CategoryActivity.this, CartActivity.class);
                        startActivity(intentCart);
                        //drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_favourites:
                        if(loggedInUser == null){
                            Intent intentLogin = new Intent(CategoryActivity.this, LoginAttemptActivity.class);
                            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                            startActivity(intentLogin);
                        }else{
                            Intent intentFav = new Intent(CategoryActivity.this, UserFavActivity.class);
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
