package com.app.adssan.ayucraze.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.app.adssan.ayucraze.Util.HttpHelper;
import com.app.adssan.ayucraze.Util.RequestPackage;
import com.app.adssan.ayucraze.model.ProductCategory;
import com.app.adssan.ayucraze.model.ProductImages;

import java.io.IOException;

public class ProductImagesService extends IntentService{

    public static final String PRODUCT_IMAGES_MESSAGE = "productImagesMessage";
    public static final String PRODUCT_IMAGES_PAYLOAD = "productImagesPayload";

    public static final String PRODUCT_IMAGES_REQUEST_PACKAGE = "productImagesRequestPackage";


    public ProductImagesService() {
        super("ProductImagesService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        RequestPackage requestPackage = (RequestPackage) intent.getParcelableExtra(PRODUCT_IMAGES_REQUEST_PACKAGE);
        String returnValues = null;

        try {
            returnValues = HttpHelper.downloadUrl(requestPackage);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Gson gson = new Gson();
        ProductImages[] productImages = gson.fromJson(returnValues, ProductImages[].class);

        Intent myIntent = new Intent(PRODUCT_IMAGES_MESSAGE);
        myIntent.putExtra(PRODUCT_IMAGES_PAYLOAD, productImages);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(myIntent);

    }
}
