package com.w3engineers.ecommerce.bootic.baseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.w3engineers.ecommerce.bootic.R;
import com.w3engineers.ecommerce.bootic.cart.CartActivity;
import com.w3engineers.ecommerce.bootic.database.DataSource;
public class ActivityBaseCartIcon extends AppCompatActivity {

    DataSource dataSource;
    TextView tvCartCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        /* DATABASE*/
        dataSource = new DataSource(this);
        dataSource.open();

    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        final Menu m = menu;

        final MenuItem item = menu.findItem(R.id.action_cart_count);
        RelativeLayout parentLayout = (RelativeLayout) item.getActionView();
        tvCartCount = parentLayout.findViewById(R.id.tv_cart_count);
        tvCartCount.setText(String.valueOf(dataSource.getCartProductCount()));

        item.getActionView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                /*START CART ACTIVITY*/
                Intent intent = new Intent(ActivityBaseCartIcon.this, CartActivity.class);
                startActivity(intent);
            }
        });
        return true;
    }




}
