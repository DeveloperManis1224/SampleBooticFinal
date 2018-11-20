package com.app.adssan.ayucraze.productDetail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.app.adssan.ayucraze.Util.Constants;
import com.app.adssan.ayucraze.Util.CustomSharedPrefs;
import com.app.adssan.ayucraze.Util.NetworkHelper;
import com.app.adssan.ayucraze.Util.RequestPackage;
import com.app.adssan.ayucraze.baseActivity.ActivityBaseCartIcon;
import com.app.adssan.ayucraze.database.DataSource;
import com.app.adssan.ayucraze.model.AvailableProduct;
import com.app.adssan.ayucraze.model.CustomProductInventory;
import com.app.adssan.ayucraze.model.ProductImages;
import com.app.adssan.ayucraze.model.SelectedProduct;
import com.app.adssan.ayucraze.prductGrid.ProductRecylerViewAdapter;
import com.app.adssan.ayucraze.R;
import com.app.adssan.ayucraze.Util.UtilityClass;
import com.app.adssan.ayucraze.cart.CartActivity;
import com.app.adssan.ayucraze.model.Product;
import com.app.adssan.ayucraze.services.ProductImagesService;
import com.app.adssan.ayucraze.userLogin.LoginAttemptActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ProductDetailActivity extends ActivityBaseCartIcon
        implements View.OnClickListener, OnLikeListener, ProductDetailDialog.ProductDialogListener {

    private SelectedProduct mSelectedProduct;

    public ArrayList<ProductImages> productDetailSliderImgList;
    public ArrayList<String> productDetailSliderImgNameList;
    private int productSlideCount;
    private LinearLayout dotsIndicators;
    private TextView tvProductDetailHeading;

    // SPONSORED PRODUCTS GRID
    ArrayList<CustomProductInventory> customProductInventories;
    ArrayList<Product> productGridList;
    RecyclerView recyclerViewProduct;

    /*START OF MENU VERIABLE*/
    Toolbar toolbar;
    Button btnAddCart;
    /*END OF MENU VERIABLE*/

    // START OF DESCRIPTION AREA
    int firstDescLineCount;
    int firstDescLineHeight;
    int firstDescVisibleLineCount = 4;

    int firstDescListLineCount;
    int secondDescListLineHeight;
    int secondDescListVisibleLineCount = 4;
    // END OF DESCRIPTION AREA

    LikeButton btnPDetailFavourite;

    private DataSource dataSource;
    private Product mproduct;

    private boolean networkOK;

    String productid;
    String available_qty_response;
    TextView tvPDetailDescription, tvPreviousPrice, tvPrice;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        /*START OF TOOLBAR*/
        TextView toobarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toobarTitle.setText(this.getString(R.string.title_product_detail));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*END OF TOOLBAR*/

        dataSource = new DataSource(this);
        mproduct = UtilityClass.jsonToProduct(getIntent().getStringExtra(Constants.PRODUCT_DETAIL_INTENT));





        tvProductDetailHeading = findViewById(R.id.tv_product_detail_heading);
        tvProductDetailHeading.setText(mproduct.getTitle());

        // START OF FAVOURITE BUTTON
        btnPDetailFavourite = findViewById(R.id.btn_p_detail_favourite);
        btnPDetailFavourite.setOnLikeListener(this);

        String pId = String.valueOf(mproduct.getId());
        String[] favProductIds = CustomSharedPrefs.getFavProductsInPref(this, dataSource);
        for (int i = 0; i < favProductIds.length; i++) {
            if (favProductIds[i].equals(pId)) {
                btnPDetailFavourite.setLiked(true);
            }
        }

        // END OF FAVOURITE BUTTON

        tvPDetailDescription = findViewById(R.id.tv_p_detail_description);
        tvPreviousPrice = findViewById(R.id.tv_product_detail_pre_price);
        tvPrice = findViewById(R.id.tv_product_detail_price);

        tvPDetailDescription.setText(mproduct.getDescription());
        tvPreviousPrice.setText(UtilityClass.getNumberFormat(mproduct.getPrevious_price()));
        tvPrice.setText(UtilityClass.getNumberFormat(mproduct.getPrice()));


        /*START INTENT SERVICE*/
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(productImageBroadcastReceiver, new IntentFilter(ProductImagesService.PRODUCT_IMAGES_MESSAGE));

        networkOK = NetworkHelper.hasNetworkAccess(this);
        if (networkOK) {

            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setEndPoint(Constants.PRODUCT_IMAGES_URL);
            requestPackage.setParam("product_id", String.valueOf(mproduct.getId()));

            Intent intentProductImage = new Intent(this, ProductImagesService.class);
            intentProductImage.putExtra(ProductImagesService.PRODUCT_IMAGES_REQUEST_PACKAGE, requestPackage);
            startService(intentProductImage);

        } else {
            Toast.makeText(this, "Could not connect to internet.", Toast.LENGTH_SHORT).show();
        }
        /*END INTENT SERVICE*/

        /*START OF DIALOG*/
        Button btnProductDetailSize = findViewById(R.id.btn_product_detail_size);
        Button btnProductDetailColor = findViewById(R.id.btn_product_detail_color);
        Button btnProductDetailQuantity = findViewById(R.id.btn_product_detail_quantity);

        btnProductDetailSize.setOnClickListener(this);
        btnProductDetailColor.setOnClickListener(this);
        btnProductDetailQuantity.setOnClickListener(this);
        /*END OF DIALOG*/


        /*START OF ADD TO CART*/
        btnAddCart = findViewById(R.id.btn_product_detail_add_cart);
        btnAddCart.setOnClickListener(this);
        /*END OF ADD TO CART*/


        // START OF PRODUCT DESCRIPTION
        btnDescReadMore = findViewById(R.id.btn_p_detail_desc_read_more);
        btnDescReadMore.setOnClickListener(this);
        pDetailDesc = findViewById(R.id.p_detail_desc);

        //  START OF FIRST VISIBLE ON ACTIVE PRODUCT DESCRIPTION

        final TextView tvpDetailsDesc = findViewById(R.id.tv_p_detail_description);
        ((TextView) tvpDetailsDesc).post(new Runnable() {

            @Override
            public void run() {

                firstDescLineCount = ((TextView) tvpDetailsDesc).getLineCount();
                firstDescLineHeight = ((TextView) tvpDetailsDesc).getLineHeight();

                ViewGroup.LayoutParams params = pDetailDesc.getLayoutParams();
                params.height = firstDescLineHeight * firstDescVisibleLineCount;
                pDetailDesc.setLayoutParams(params);

            }

        });
        //  END OF FIRST VISIBLE ON ACTIVE PRODUCT DESCRIPTION



        // START OF SIZE/COLOR/QTY ICON RIGHT RESIZING
        UtilityClass.buttonScaleIconRight(this, btnProductDetailSize, R.drawable.ic_arrow_down_black, .3, 1);
        UtilityClass.buttonScaleIconRight(this, btnProductDetailColor, R.drawable.ic_arrow_down_black, .3, 1);
        UtilityClass.buttonScaleIconRight(this, btnProductDetailQuantity, R.drawable.ic_arrow_down_black, .3, 1);
        UtilityClass.buttonScaleIconRight(this, btnDescReadMore, R.drawable.ic_arrow_down_primary, 1, 1);
        // END OF SIZE/COLOR/QTY ICON RIGHT RESIZING


        // START DOTS INDICATORS
        dotsIndicators = findViewById(R.id.layout_product_Detail_dots);
        // END OF DOTS INDICATORS


        //START RECYLERVIEW PRODUCT GRID

        productGridList = new ArrayList<>();

        recyclerViewProduct = findViewById(R.id.rv_p_detail_product_grid);
        ProductRecylerViewAdapter productRecylerViewAdapter = new ProductRecylerViewAdapter(productGridList, this);

        recyclerViewProduct.setHasFixedSize(false);
        recyclerViewProduct.setLayoutManager(new GridLayoutManager(this, 2));
        //ProductRecylerViewAdapter.ItemOffsetDecoration itemDecoration = new ProductRecylerViewAdapter.ItemOffsetDecoration(this, R.dimen.fab_margin_quarter);
        // recyclerViewProduct.addItemDecoration(itemDecoration);
        recyclerViewProduct.setNestedScrollingEnabled(false);

        recyclerViewProduct.setAdapter(productRecylerViewAdapter);

        //END FOR RECYLER PRODUCT VIEW

    }


    BroadcastReceiver productImageBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ProductImages[] productImages = (ProductImages[]) intent.getParcelableArrayExtra(ProductImagesService.PRODUCT_IMAGES_PAYLOAD);
            productDetailSliderImgList = new ArrayList<>(Arrays.asList(productImages));

            productDetailSliderImgNameList = new ArrayList<>();
            for (ProductImages pImg : productDetailSliderImgList) {

                productDetailSliderImgNameList.add(pImg.getImage_name());

            }

            settingProductImageSlider();
            dots(0);
        }
    };

    private void settingProductImageSlider() {

        productSlideCount = productDetailSliderImgNameList.size();

        ViewPager vpProductDetailsImageSlider = findViewById(R.id.vp_product_detail_image_slider);
        PagerAdapter pagerAdapter = new SliderProductDeatilAdapter(getSupportFragmentManager());
        vpProductDetailsImageSlider.setAdapter(pagerAdapter);

        vpProductDetailsImageSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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


    private void dots(int current) {

        dotsIndicators.removeAllViews();
        for (int i = 0; i < productSlideCount; i++) {
            TextView dot = new TextView(this);
            dot.setIncludeFontPadding(false);
            dot.setHeight((int) UtilityClass.convertDpToPixel(10, ProductDetailActivity.this));
            dot.setWidth((int) UtilityClass.convertDpToPixel(10, ProductDetailActivity.this));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int marginAsDp = (int) UtilityClass.convertDpToPixel(4, ProductDetailActivity.this);
            params.setMargins(marginAsDp, marginAsDp, marginAsDp, marginAsDp);
            dot.setLayoutParams(params);

            dot.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_border_bg_1));

            if (i == current) {
                dot.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_bg));
            }

            dotsIndicators.addView(dot);

        }
    }


    Button btnDescReadMore;
    Button btnDescListReadMore;

    LinearLayout pDetailListDesc;
    boolean pDetailListDescExpanded = false;

    LinearLayout pDetailDesc;
    boolean pDetailDescExpanded = false;

    public static final String ADD_TO_CART_INTENT = "addToCartIntent";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_product_detail_add_cart:

                if (mSelectedProduct == null) {

                    getInventory();

                } else {

                    Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);

                    mSelectedProduct.setProduct(mproduct);
                    mSelectedProduct.setId(mproduct.getId());

                    dataSource.addCartProduct(mSelectedProduct);

                    startActivity(intent);
                }
                break;
            case R.id.btn_product_detail_size:

                getInventory();
                break;
            case R.id.btn_product_detail_color:
                getInventory();
                break;
            case R.id.btn_product_detail_quantity:
                getInventory();
                break;

            case R.id.btn_p_detail_desc_read_more:

                if (!pDetailDescExpanded) {
                    pDetailDescExpanded = true;
                    ViewGroup.LayoutParams params = pDetailDesc.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    pDetailDesc.setLayoutParams(params);
                    UtilityClass.buttonScaleIconRight(this, btnDescReadMore, R.drawable.ic_arrow_up_primary, 1, 1);
                    btnDescReadMore.setText(getResources().getString(R.string.read_less));
                } else {
                    pDetailDescExpanded = false;
                    ViewGroup.LayoutParams params = pDetailDesc.getLayoutParams();
                    params.height = firstDescLineHeight * firstDescVisibleLineCount;
                    pDetailDesc.setLayoutParams(params);
                    UtilityClass.buttonScaleIconRight(this, btnDescReadMore, R.drawable.ic_arrow_down_primary, 1, 1);
                    btnDescReadMore.setText(getResources().getString(R.string.read_more));
                }
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSelectedProduct = null;
        invalidateOptionsMenu();
    }

    private void getInventory(){
        String JSON_URL = Constants.PRODUCT_INVENTORY + "?product_ids=" + mproduct.getId();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Type listType = new TypeToken<List<CustomProductInventory>>() {}.getType();
                customProductInventories = new Gson().fromJson(response, listType);


                AvailableProduct availableProduct = new AvailableProduct();
                availableProduct.setQuantity(23);
                ProductDetailDialog productDetailDialog = ProductDetailDialog.newInstance(availableProduct, customProductInventories);

                productDetailDialog.show(getFragmentManager(), "PRODUCT_DETAIL_DIALOG");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.getCache().clear();
        queue.add(stringRequest);
    }




    public void liked(LikeButton likeButton) {

        if (CustomSharedPrefs.getLoggedInUser(this) != null) {

            dataSource.addFavProduct(mproduct);
            CustomSharedPrefs.setFavProductsInPref(this, dataSource);

        } else {

            Intent intentLogin = new Intent(this, LoginAttemptActivity.class);
            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
            this.startActivity(intentLogin);
            likeButton.setLiked(false);
            CustomSharedPrefs.setFavProductsInPref(this, dataSource);

        }
    }

    @Override
    public void unLiked(LikeButton likeButton) {
        dataSource.removeFavProductById(mproduct.getId());
    }

    @Override
    public void onProductEntryComplete(SelectedProduct selectedProduct) {

        mSelectedProduct = selectedProduct;
    }


    /*START OF SLIDER IMAGE VIEWPAGER*/
    public class SliderProductDeatilAdapter extends FragmentStatePagerAdapter {

        public SliderProductDeatilAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return SliderProductDetailFullItemFragment.newInstance(productDetailSliderImgNameList.get(position));
        }

        @Override
        public int getCount() {
            return productDetailSliderImgList.size();
        }

    }
    /*END OF SLIDER IMAGE VIEWPAGER*/
}
