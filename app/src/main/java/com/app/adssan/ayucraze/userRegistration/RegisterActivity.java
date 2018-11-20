package com.app.adssan.ayucraze.userRegistration;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adssan.ayucraze.cart.CartActivity;
import com.app.adssan.ayucraze.userLogin.LoginActivity;
import com.app.adssan.ayucraze.R;
import com.app.adssan.ayucraze.Util.Constants;
import com.app.adssan.ayucraze.Util.NetworkHelper;
import com.app.adssan.ayucraze.Util.RequestPackage;
import com.app.adssan.ayucraze.Util.UtilityClass;
import com.app.adssan.ayucraze.services.UserRegistrationService;

import static com.app.adssan.ayucraze.services.UserRegistrationService.*;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegisterLogin;

    /*START OF MENU VERIABLE*/
    Toolbar toolbar;
    /*END OF MENU VERIABLE*/

    private boolean networkOK;

    Button btnUserRegistration;
    TextView tvTermsWriting;
    EditText etFirstName, etLastname, etEmail, etPassword;
    String firstName, lastname, email, password;
    TextView tvValidationEmail, tvValidationFirstName, tvValidationLastName, tvValidationPassword, tvRegMsg;
    boolean fromCart = false;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /*START OF TOOLBAR*/
        TextView toobarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");

        toobarTitle.setText(this.getString(R.string.title_register));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*END OF TOOLBAR*/

        btnUserRegistration= findViewById(R.id.btn_user_registration);
        tvTermsWriting = findViewById(R.id.tv_terms_writing);
        etFirstName = findViewById(R.id.et_register_first_name);
        etLastname = findViewById(R.id.et_register_last_name);
        etEmail = findViewById(R.id.et_register_email);
        etPassword = findViewById(R.id.et_register_password);

        tvValidationEmail = findViewById(R.id.tv_validation_msg_email);
        tvValidationFirstName = findViewById(R.id.tv_validation_msg_first_name);
        tvValidationLastName = findViewById(R.id.tv_validation_msg_last_name);
        tvValidationPassword = findViewById(R.id.tv_validation_msg_password);
        tvRegMsg = findViewById(R.id.tv_reg_msg);


       /* String prevActivity ="";
        prevActivity = getIntent().getStringExtra(Constants.TO_REGISTER);
        if(!prevActivity.equals("")){
            if(prevActivity.equals(Constants.FROM_CART)){
                fromCart = true;
            }
        }
*/

        UtilityClass.textViewScaleIconLeft(this, findViewById(R.id.et_register_email), R.drawable.ic_email_fill_black, .5);
        UtilityClass.textViewScaleIconLeft(this, findViewById(R.id.et_register_first_name), R.drawable.ic_user_fill_black, .5);
        UtilityClass.textViewScaleIconLeft(this, findViewById(R.id.et_register_last_name), R.drawable.ic_user_fill_black, .5);
        //UtilityClass.textViewScaleIconLeft(this, findViewById(R.id.et_register_password), R.drawable.ic_lock_fill_black, .5);

        /* START OF CLICKABLE LINK AREA */

        SpannableString tearmsAndCondition = new SpannableString(getResources().getString(R.string.register_terms_writing));

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                //startActivity(new Intent(MyActivity.this, NextActivity.class));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                //ds.setUnderlineText(false);
            }
        };

        int stringCount = tearmsAndCondition.length();

        tearmsAndCondition.setSpan(clickableSpan, stringCount - 20, stringCount, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        final TextView textView = findViewById(R.id.tv_terms_writing);
        textView.setText(tearmsAndCondition);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        /* END OF CLICKABLE LINK AREA */

        btnRegisterLogin = findViewById(R.id.btn_register_login);
        btnRegisterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        /*START OF VALIDATION*/
        firstName = etFirstName.getText().toString().trim();
        lastname = etLastname.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        btnUserRegistration.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                firstName = etFirstName.getText().toString().trim();
                lastname = etLastname.getText().toString().trim();
                email = etEmail.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                boolean isFieldEmpty = false;

                if(isEmpty(firstName)){
                    isFieldEmpty = true;
                    tvValidationFirstName.setVisibility(View.VISIBLE);
                    tvValidationFirstName.setText("First Name can't be empty.");
                }else{
                    tvValidationFirstName.setVisibility(View.GONE);
                    tvValidationFirstName.setText("");
                }

                if(isEmpty(lastname)){
                    isFieldEmpty = true;
                    tvValidationLastName.setVisibility(View.VISIBLE);
                    tvValidationLastName.setText("Last Name can't be empty.");
                }else{
                    tvValidationLastName.setVisibility(View.GONE);
                    tvValidationLastName.setText("");
                }

                if(isEmpty(email)){
                    isFieldEmpty = true;
                    tvValidationEmail.setVisibility(View.VISIBLE);
                    tvValidationEmail.setText("Email can't be empty.");
                }else if(!email.matches(emailPattern) && !isEmpty(email)){
                    isFieldEmpty = true;
                    tvValidationEmail.setVisibility(View.VISIBLE);
                    tvValidationEmail.setText("Invalid Email.");
                }else{
                    tvValidationEmail.setVisibility(View.GONE);
                    tvValidationEmail.setText("");
                }

                if(isEmpty(password)){
                    isFieldEmpty = true;
                    tvValidationPassword.setVisibility(View.VISIBLE);
                    tvValidationPassword.setText("Password can't be empty.");
                }else if(password.length() < 6 && !isEmpty(email)){
                    isFieldEmpty = true;
                    tvValidationPassword.setVisibility(View.VISIBLE);
                    tvValidationPassword.setText("Password Must be atleast 6 characters long.");
                }else{
                    tvValidationPassword.setVisibility(View.GONE);
                    tvValidationPassword.setText("");
                }


                if(!isFieldEmpty) {
                    /*START INTENT SERVICE*/
                    LocalBroadcastManager.getInstance(RegisterActivity.this)
                            .registerReceiver(registrationBroadcastReceiver, new IntentFilter(USER_REGISTRATION_POST));

                    networkOK = NetworkHelper.hasNetworkAccess(RegisterActivity.this);
                    if (networkOK) {

                        RequestPackage requestPackage = new RequestPackage();
                        requestPackage.setEndPoint(Constants.USER_REGISTRATION_URL);
                        requestPackage.setParam("first_name", firstName);
                        requestPackage.setParam("last_name", lastname);
                        requestPackage.setParam("email", email);
                        requestPackage.setParam("password", password);
                        requestPackage.setParam("image_name", "");
                        requestPackage.setParam("address", "");
                        requestPackage.setParam("membership", "");
                        requestPackage.setMethod("POST");

                    Intent intentUser = new Intent(RegisterActivity.this, UserRegistrationService.class);
                    intentUser.putExtra(USER_REGISTRATION_REQUEST_PACKAGE, requestPackage);
                    startService(intentUser);

                    } else {
                        Toast.makeText(RegisterActivity.this, "Could not connect to internet.", Toast.LENGTH_SHORT).show();
                    }
                    /*END INTENT SERVICE*/
                }
            }
        });


    }

    BroadcastReceiver registrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String successRegistration = intent.getStringExtra(USER_REGISTRATION_POST_PAYLOAD);
            tvRegMsg.setVisibility(View.VISIBLE);
            Log.d("startActivity",successRegistration);

            if(successRegistration.trim().equals("1")){

                Intent intent1 = new Intent(RegisterActivity.this, LoginActivity.class);
                intent1.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_CHECKOUT);
                startActivity(intent1);


                tvRegMsg.setText("Registration successful.");


            }else{
                tvRegMsg.setText("Registration UnSuccessfull.");
            }

            clearInputs();

        }
    };


    private void clearInputs(){
        etFirstName.setText("");
        etLastname.setText("");
        etEmail.setText("");
        etPassword.setText("");
    }

    private boolean isEmpty(String value){
        return (value.length() < 1) ? true : false;
    }

}
