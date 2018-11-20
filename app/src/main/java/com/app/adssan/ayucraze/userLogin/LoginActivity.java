package com.app.adssan.ayucraze.userLogin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adssan.ayucraze.R;
import com.app.adssan.ayucraze.Util.Constants;
import com.app.adssan.ayucraze.Util.CustomSharedPrefs;
import com.app.adssan.ayucraze.Util.NetworkHelper;
import com.app.adssan.ayucraze.Util.RequestPackage;
import com.app.adssan.ayucraze.Util.UtilityClass;
import com.app.adssan.ayucraze.cart.CartActivity;
import com.app.adssan.ayucraze.category.CategoryActivity;
import com.app.adssan.ayucraze.main.MainActivity;
import com.app.adssan.ayucraze.model.User;
import com.app.adssan.ayucraze.productDetail.ProductDetailActivity;
import com.app.adssan.ayucraze.services.UserLoginService;
import com.app.adssan.ayucraze.services.UserRegistrationService;
import com.app.adssan.ayucraze.userRegistration.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    /*START OF MENU VERIABLE*/
    Toolbar toolbar;
    /*END OF MENU VERIABLE*/

    Button btnUserLogin;
    EditText etLoginEmail, etLoginPassword;
    String email, password;
    TextView tvValidationLoginEmail, tvValidationLoginPassword, tvLoginMsg;

    private boolean networkOK;

    private String prevActivity;


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*START OF TOOLBAR*/
        TextView toobarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toobarTitle.setText(this.getString(R.string.title_sign_in));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*END OF TOOLBAR*/


        prevActivity = getIntent().getStringExtra(Constants.LOGIN_PREV_ACTIVITY);
        Log.d("prevActivity", prevActivity);


        UtilityClass.textViewScaleIconLeft(this, findViewById(R.id.et_login_email), R.drawable.ic_email_fill_black, .5);
        //UtilityClass.textViewScaleIconLeft(this, findViewById(R.id.et_login_password), R.drawable.ic_lock, .5);

        Button btnSignUp = findViewById(R.id.btn_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

        btnUserLogin= findViewById(R.id.btn_user_sign_in);
        etLoginEmail = findViewById(R.id.et_login_email);
        etLoginPassword = findViewById(R.id.et_login_password);

        tvValidationLoginEmail = findViewById(R.id.tv_validation_login_msg_email);
        tvValidationLoginPassword = findViewById(R.id.tv_validation_login_msg_password);
        tvLoginMsg = findViewById(R.id.tv_login_msg);

        /*START OF VALIDATION*/
        email = etLoginEmail.getText().toString().trim();
        password = etLoginPassword.getText().toString().trim();
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        btnUserLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                email = etLoginEmail.getText().toString().trim();
                password = etLoginPassword.getText().toString().trim();
                boolean isFieldEmpty = false;

                if(isEmpty(email)){
                    isFieldEmpty = true;
                    tvValidationLoginEmail.setVisibility(View.VISIBLE);
                    tvValidationLoginEmail.setText("Email can't be empty.");
                }else if(!email.matches(emailPattern) && !isEmpty(email)){
                    isFieldEmpty = true;
                    tvValidationLoginEmail.setVisibility(View.VISIBLE);
                    tvValidationLoginEmail.setText("Invalid Email.");
                }else{
                    tvValidationLoginEmail.setVisibility(View.GONE);
                    tvValidationLoginEmail.setText("");
                }

                if(isEmpty(password)){
                    isFieldEmpty = true;
                    tvValidationLoginPassword.setVisibility(View.VISIBLE);
                    tvValidationLoginPassword.setText("Password can't be empty.");
                }else if(password.length() < 6 && !isEmpty(email)){
                    isFieldEmpty = true;
                    tvValidationLoginPassword.setVisibility(View.VISIBLE);
                    tvValidationLoginPassword.setText("Password Must be atleast 6 characters long.");
                }else{
                    tvValidationLoginPassword.setVisibility(View.GONE);
                    tvValidationLoginPassword.setText("");
                }

                if(!isFieldEmpty) {
                    /*START INTENT SERVICE*/
                    LocalBroadcastManager.getInstance(LoginActivity.this)
                            .registerReceiver(loginBroadcastReceiver, new IntentFilter(UserLoginService.USER_LOGIN_POST));

                    networkOK = NetworkHelper.hasNetworkAccess(LoginActivity.this);
                    if (networkOK) {

                        RequestPackage requestPackage = new RequestPackage();
                        requestPackage.setEndPoint(Constants.USER_LOGIN_URL);
                        requestPackage.setParam("email", email);
                        requestPackage.setParam("password", password);
                        requestPackage.setMethod("POST");

                        Intent intentUser = new Intent(LoginActivity.this, UserLoginService.class);
                        intentUser.putExtra(UserLoginService.USER_LOGIN_REQUEST_PACKAGE, requestPackage);
                        startService(intentUser);

                    } else {
                        Toast.makeText(LoginActivity.this, "Could not connect to internet.", Toast.LENGTH_SHORT).show();
                    }
                    /*END INTENT SERVICE*/
                }
            }
        });

    }

    BroadcastReceiver loginBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            User[] users = (User[]) intent.getParcelableArrayExtra(UserLoginService.USER_LOGIN_POST_PAYLOAD);

            tvLoginMsg.setVisibility(View.VISIBLE);
            if(users != null && users.length > 0) {

                CustomSharedPrefs.setLoggedInUser(LoginActivity.this, users[0]);
                if(prevActivity.equals(Constants.LOGIN_PREV_CATEGORY)){

                    Intent intentCategory = new Intent(LoginActivity.this, CategoryActivity.class);
                    startActivity(intentCategory);

                }else if(prevActivity.equals(Constants.LOGIN_PREV_MAIN_ACTIVITY)){

                    Intent intentCategory = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intentCategory);

                }else if(prevActivity.equals(Constants.LOGIN_PREV_PRODUCT_DETAIL)){

                    Intent intentCategory = new Intent(LoginActivity.this, ProductDetailActivity.class);
                    startActivity(intentCategory);

                }else if(prevActivity.equals(Constants.LOGIN_PREV_CHECKOUT)){

                    Intent intentCategory = new Intent(LoginActivity.this, CartActivity.class);
                    startActivity(intentCategory);

                }

            }else{
                tvLoginMsg.setText("Invalid email/password.");
            }

        }
    };

    private boolean isEmpty(String value){
        return (value.length() < 1) ? true : false;
    }

}
