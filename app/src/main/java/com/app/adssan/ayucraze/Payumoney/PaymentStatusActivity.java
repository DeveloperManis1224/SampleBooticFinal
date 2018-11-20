package com.app.adssan.ayucraze.Payumoney;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.adssan.ayucraze.R;
import com.app.adssan.ayucraze.Util.Constants;
import com.app.adssan.ayucraze.Util.CustomSharedPrefs;
import com.app.adssan.ayucraze.cart.CartActivity;
import com.app.adssan.ayucraze.checkoutPayment.ConfirmActivity;
import com.app.adssan.ayucraze.database.DataSource;
import com.app.adssan.ayucraze.main.MainActivity;
import com.app.adssan.ayucraze.model.SelectedProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public  class PaymentStatusActivity extends Activity{
DataSource dataSource;
    String totalPrice = "";
    boolean status,isFromOrder;
    int id;
    String tranaction_id;
    ProgressDialog pd=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_activity);
        pd = new ProgressDialog(PaymentStatusActivity.this);
        pd.setMessage("Processing...");

        dataSource = new DataSource(this);
        totalPrice = getIntent().getStringExtra(Constants.TOTAL_PRICE);
        status=getIntent().getBooleanExtra("status",false);
        tranaction_id = getIntent().getStringExtra("transaction_id");
        id = getIntent().getIntExtra("id",0);
        isFromOrder = getIntent().getBooleanExtra("isFromOrder",false);

       // Toast.makeText(this, "Payment Status :"+status, Toast.LENGTH_SHORT).show();

        if(status)
        {
            dopayment_Online("Online Payment",tranaction_id);
        }
        else
        {
            Toast.makeText(this, "Transaction Failed...", Toast.LENGTH_SHORT).show();
        }
    }

    public void dopayment_Online(final String pay_ment_method,final String txn_id)
    {

        pd.show();
        String JSON_URL = Constants.PATH_TO_PAYMENT;
        RequestQueue queueOrder = Volley.newRequestQueue(PaymentStatusActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Log.v("EEEEEEE",""+response);
                final String responsed_order_id = response.toString().trim();
                ArrayList<SelectedProduct> cartModels = (ArrayList<SelectedProduct>) dataSource.getAllCartProducts();
                for (final SelectedProduct cartModel : cartModels)
                {
                    final SelectedProduct currentCartModel = cartModel;
                    String JSON_URL = Constants.PATH_TO_PAYMENT_COMPLETE;
                    RequestQueue queue = Volley.newRequestQueue(PaymentStatusActivity.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.v("EEEEEEE","2nd Response - "+response);
                            pd.dismiss();
                            Toast.makeText(PaymentStatusActivity.this, "Product ordered Successfully.", Toast.LENGTH_SHORT).show();
                            dataSource.removeAllCartProducts();
                            Intent in = new Intent(PaymentStatusActivity.this, MainActivity.class);
                            startActivity(in);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("responseV", error.getMessage());
                            Toast.makeText(PaymentStatusActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("product_id", cartModel.getId());
                            params.put("order_id", responsed_order_id);
                            params.put("ordered_quantity", String.valueOf(cartModel.getQunatity()));
                            params.put("product_size_id", String.valueOf(cartModel.getProductSize().getSize_id()));
                            params.put("product_color_id", String.valueOf(cartModel.getProductColor().getColor_id()));
                            params.put("transaction_id",txn_id);
                            params.put("inventory_id", String.valueOf(cartModel.getInvetory_id()));
                            return params;
                        }
                    };
                    queue.getCache().clear();
                    queue.add(stringRequest);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("responseV", error.getMessage());
                Toast.makeText(PaymentStatusActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("order_method", pay_ment_method);
                params.put("order_amount", "1.0");
                params.put("order_user_id", CustomSharedPrefs.getLoggedInUser(PaymentStatusActivity.this).getUser_id());

                return params;

            }
        };

        queueOrder.getCache().clear();
        queueOrder.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent(PaymentStatusActivity.this, MainActivity.class);
        startActivity(in);
        finish();
    }
}
