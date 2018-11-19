package com.w3engineers.ecommerce.bootic.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.w3engineers.ecommerce.bootic.Util.HttpHelper;
import com.w3engineers.ecommerce.bootic.Util.RequestPackage;
import com.w3engineers.ecommerce.bootic.model.Product;
import com.w3engineers.ecommerce.bootic.model.User;

import java.io.IOException;

public class UserLoginService extends IntentService {

    public static final String USER_LOGIN_POST = "userLoginPost";
    public static final String USER_LOGIN_POST_PAYLOAD = "userLoginPostPayload";
    public static final String USER_LOGIN_REQUEST_PACKAGE = "userLoginRequestPackage";

    public UserLoginService() { super("UserRegistrationService"); }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        RequestPackage requestPackage = (RequestPackage) intent.getParcelableExtra(USER_LOGIN_REQUEST_PACKAGE);

        String returnValues = null;
        try {
            returnValues = HttpHelper.downloadUrl(requestPackage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        User[] Users = null;
        if(returnValues.length() > 0) {
            Gson gson = new Gson();
            Users = gson.fromJson(returnValues, User[].class);
        }
        Intent myIntent = new Intent(USER_LOGIN_POST);
        myIntent.putExtra(USER_LOGIN_POST_PAYLOAD, Users);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(myIntent);

    }
}
