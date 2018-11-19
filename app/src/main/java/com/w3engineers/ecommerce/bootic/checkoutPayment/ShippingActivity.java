package com.w3engineers.ecommerce.bootic.checkoutPayment;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.w3engineers.ecommerce.bootic.R;
import com.w3engineers.ecommerce.bootic.Util.Constants;
import com.w3engineers.ecommerce.bootic.Util.CustomSharedPrefs;
import com.w3engineers.ecommerce.bootic.Util.UtilityClass;
import com.w3engineers.ecommerce.bootic.model.CustomProductInventory;
import com.w3engineers.ecommerce.bootic.model.User;

import java.util.HashMap;
import java.util.Map;

public class ShippingActivity extends AppCompatActivity
        implements View.OnClickListener {

    android.support.v7.widget.Toolbar toolbar;

   // AdView mAdView;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    EditText etFirstName, etLastName, etAddress1, etAddress2, etCity, etZip, etState, etCountry;
    String firstName, lastName, address1, address2, city, zip, state, country;
    FrameLayout btnCompleteAddress;
    boolean isFieldEmpty = false;

    String totalprice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);

        /*START OF TOOLBAR*/
        TextView toobarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toobarTitle.setText(this.getString(R.string.title_shipping));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*END OF TOOLBAR*/

        // ADDMOB


//        mAdView=(AdView)findViewById(R.id.adView);
//        MobileAds.initialize(this,"ca-app-pub-3940256099942544~3347511713");
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);


        totalprice = getIntent().getStringExtra(Constants.TOTAL_PRICE);


        UtilityClass.buttonScaleIconRight(this, findViewById(R.id.btn_complete_address_inner), R.drawable.ic_arrow_right_white, .8, 1.1);

        etFirstName = findViewById(R.id.et_shipping_first_name);
        etLastName = findViewById(R.id.et_shipping_last_name);
        etAddress1 = findViewById(R.id.et_shipping_address_1);
        etAddress2 = findViewById(R.id.et_shipping_address_2);
        etCity = findViewById(R.id.et_shipping_city);
        etZip = findViewById(R.id.et_shipping_zip);
        etState = findViewById(R.id.et_shipping_state);
        etCountry = findViewById(R.id.et_shipping_country);

        btnCompleteAddress = findViewById(R.id.btn_complete_address);

        btnCompleteAddress.setOnClickListener(this);

        User loggedInUser = CustomSharedPrefs.getLoggedInUser(this);
        etFirstName.setText(loggedInUser.getFirst_name());
        etLastName.setText(loggedInUser.getLast_name());
    }


    private boolean isEmpty(String value) {
        return (value.length() < 1) ? true : false;
    }

    private void validateAddress() {

        firstName = etFirstName.getText().toString().trim();
        lastName = etLastName.getText().toString().trim();
        address1 = etAddress1.getText().toString().trim();
        address2 = etAddress2.getText().toString().trim();
        city = etCity.getText().toString().trim();
        zip = etZip.getText().toString().trim();
        state = etState.getText().toString().trim();
        etCountry.setText("India");
        country = etCountry.getText().toString().trim();

        if (isEmpty(firstName)) {
            Log.d("firstName", firstName);
            isFieldEmpty = true;
            etFirstName.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etFirstName.setBackgroundResource(R.drawable.edittext_round);
        }
        if (isEmpty(lastName)) {
            isFieldEmpty = true;
            etLastName.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etLastName.setBackgroundResource(R.drawable.edittext_round);
        }
        if (isEmpty(address1)) {
            isFieldEmpty = true;
            etAddress1.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etAddress1.setBackgroundResource(R.drawable.edittext_round);
        }
        if (isEmpty(city)) {
            isFieldEmpty = true;
            etCity.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etCity.setBackgroundResource(R.drawable.edittext_round);
        }
        if (isEmpty(zip)) {
            isFieldEmpty = true;
            etZip.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etZip.setBackgroundResource(R.drawable.edittext_round);
        }
        if (isEmpty(state)) {
            isFieldEmpty = true;
            etState.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etState.setBackgroundResource(R.drawable.edittext_round);
        }
//        if (isEmpty(country)) {
//            isFieldEmpty = true;
//            etCountry.setBackgroundResource(R.drawable.edittext_error);
//        } else {
//            isFieldEmpty = false;
//            etCountry.setBackgroundResource(R.drawable.edittext_round);
//        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_complete_address:
                validateAddress();

                if (!isFieldEmpty) {
                    final User user = new User();
                    user.setFirst_name(firstName);
                    user.setLast_name(lastName);
                    String Address = address1 + address2 + ", City : " + city + ", zip Code : " + zip
                            + ", State : " + state + ", Country :" + country;
                    user.setAddress(Address);

                    String JSON_URL = Constants.INSERT_ADDRESS;
                    RequestQueue queue = Volley.newRequestQueue(this);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("responseV", response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("responseV", ""+error.getMessage());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();

                            params.put("user_id",  CustomSharedPrefs.getLoggedInUserId(ShippingActivity.this));
                            params.put("address_line_1", address1);
                            params.put("address_line_2", address2);
                            params.put("city", city);
                            params.put("zip_code", zip);
                            params.put("state", state);
                            params.put("country", country);

                            return params;

                        }

                    };

                    queue.getCache().clear();
                    queue.add(stringRequest);
                    Intent intentUser = new Intent(ShippingActivity.this, ConfirmActivity.class);
                    intentUser.putExtra(Constants.CONFIRM_PAYMENT_INTENT, UtilityClass.userToJson(user));
                    intentUser.putExtra(Constants.TOTAL_PRICE, totalprice);
                    startActivity(intentUser);
                }
                else
                {
                    Toast.makeText(this, "Invalid details", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


}
