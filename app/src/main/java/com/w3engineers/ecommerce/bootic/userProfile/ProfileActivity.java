package com.w3engineers.ecommerce.bootic.userProfile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.w3engineers.ecommerce.bootic.Util.Constants;
import com.w3engineers.ecommerce.bootic.Util.CustomSharedPrefs;
import com.w3engineers.ecommerce.bootic.baseActivity.ActivityBaseCartIcon;
import com.w3engineers.ecommerce.bootic.category.CategoryActivity;
import com.w3engineers.ecommerce.bootic.model.User;
import com.w3engineers.ecommerce.bootic.prductGrid.ProductGridActivity;
import com.w3engineers.ecommerce.bootic.R;
import com.w3engineers.ecommerce.bootic.Util.UtilityClass;
import com.w3engineers.ecommerce.bootic.cart.CartActivity;

public class ProfileActivity extends ActivityBaseCartIcon
        implements View.OnClickListener {


    /*START OF MENU VERIABLE*/
    TextView toobarTitle;
    android.support.v7.widget.Toolbar toolbar;
    /*END OF MENU VERIABLE*/

    Button btnMyOrders;
    Button btnMyFavourites;

    TextView tvUserName;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    User loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /*START OF TOOLBAR*/
        TextView toobarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toobarTitle.setText("User");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*END OF TOOLBAR*/

        // START OF ORDER/FAVOURITE BUTTON
        btnMyOrders = findViewById(R.id.btn_my_orders);
        btnMyOrders.setOnClickListener(this);

        btnMyFavourites = findViewById(R.id.btn_my_favourite);
        btnMyFavourites.setOnClickListener(this);
        // END OF ORDER/FAVOURITE BUTTON

        loggedInUser = CustomSharedPrefs.getLoggedInUser(ProfileActivity.this);
        tvUserName = findViewById(R.id.tv_user_name);
        tvUserName.setText(loggedInUser.getFirst_name() + " " + loggedInUser.getLast_name());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_my_orders:
                Intent intentOrder = new Intent(ProfileActivity.this, UserOrderActivity.class);
                startActivity(intentOrder);
                break;
            case R.id.btn_my_favourite:
                Intent intentFav = new Intent(ProfileActivity.this, UserFavActivity.class);
                startActivity(intentFav);

                break;
        }
    }


}
