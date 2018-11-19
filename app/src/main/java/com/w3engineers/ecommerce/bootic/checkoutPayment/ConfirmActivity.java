package com.w3engineers.ecommerce.bootic.checkoutPayment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.w3engineers.ecommerce.bootic.Payumoney.PayUMoneyActivity;
import com.w3engineers.ecommerce.bootic.R;
import com.w3engineers.ecommerce.bootic.Util.Constants;
import com.w3engineers.ecommerce.bootic.Util.CustomSharedPrefs;
import com.w3engineers.ecommerce.bootic.Util.UtilityClass;
import com.w3engineers.ecommerce.bootic.cart.CartActivity;
import com.w3engineers.ecommerce.bootic.database.DataSource;
import com.w3engineers.ecommerce.bootic.model.SelectedProduct;
import com.w3engineers.ecommerce.bootic.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cz.msebera.android.httpclient.Header;


public class ConfirmActivity extends AppCompatActivity {

    public static final String TAG = "braintreeTag";
    private static final int BRAINTREE_REQUEST_CODE = 4949;
    android.support.v7.widget.Toolbar toolbar;
    TextView tvConfirmPayAddress;
    FrameLayout btnConfirmPayment;
    FrameLayout btnCashOnDelivery;
    String totalPrice = "";
    DataSource dataSource;
    private String clientToken;
    ProgressDialog pd=null;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        /*START OF TOOLBAR*/
        TextView toobarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toobarTitle.setText(this.getString(R.string.title_confirm_payment));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*END OF TOOLBAR*/

        dataSource = new DataSource(this);

        totalPrice = getIntent().getStringExtra(Constants.TOTAL_PRICE);
        User user = UtilityClass.jsonToUser(getIntent().getStringExtra(Constants.CONFIRM_PAYMENT_INTENT));

        tvConfirmPayAddress = findViewById(R.id.tv_confirm_pay_address);
        String payAddress = user.getFirst_name() + " " + user.getLast_name() + "\n" + user.getAddress();
        tvConfirmPayAddress.setText(payAddress);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TextView tvAmount = findViewById(R.id.tv_amount);
        tvAmount.setText("Amount : " + totalPrice);

        btnCashOnDelivery = findViewById(R.id.btn_cash_on_delivery);
        btnCashOnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dopayment("Cash On Delivery");
            }
        });

        btnConfirmPayment = findViewById(R.id.btn_confirm_payment);
        btnConfirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getClientTokenFromServer();

            }
        });
    }


    private void getClientTokenFromServer() {

        final ProgressDialog dialog = new ProgressDialog(ConfirmActivity.this);
        dialog.setMessage("Please Wait......");

        dialog.show();
        AsyncHttpClient androidClient = new AsyncHttpClient();
        androidClient.get(Constants.PATH_TO_SERVER, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseToken) {

                clientToken = responseToken;
                dialog.dismiss();
               Random r = new Random(System.currentTimeMillis());
               int id= ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));

                Intent intent = new Intent(ConfirmActivity.this, PayUMoneyActivity.class);
                intent.putExtra("name",CustomSharedPrefs.getLoggedInUser(ConfirmActivity.this).getFirst_name());
                intent.putExtra("email",CustomSharedPrefs.getLoggedInUser(ConfirmActivity.this).getEmail());
                intent.putExtra("amount",totalPrice);
                intent.putExtra("phone","");
                intent.putExtra("id",id);
                Log.v("sdadasdsdas",String.valueOf(id));
                intent.putExtra("isFromOrder",true);
                startActivity(intent);
            }
        });
    }

    public void onBraintreeSubmit() {
        DropInRequest dropInRequest = new DropInRequest().clientToken(clientToken);
        startActivityForResult(dropInRequest.getIntent(this), BRAINTREE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BRAINTREE_REQUEST_CODE) {
            if (RESULT_OK == resultCode) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                String paymentNonce = result.getPaymentMethodNonce().getNonce();
                String payamount = totalPrice;
                sendPaymentNonceToServer(paymentNonce, payamount);
            } else if (resultCode == Activity.RESULT_CANCELED) {

            } else {
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);

            }
        }
    }

    private void sendPaymentNonceToServer(String paymentNonce, String Amounts) {
        RequestParams params = new RequestParams();
        params.put("NONCE", paymentNonce);
        params.put("amount", Amounts);
        AsyncHttpClient androidClient = new AsyncHttpClient();
        androidClient.post(Constants.PATH_TO_SERVER, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                String paymentMethod = "";

                String[] separated = responseString.split("_");
                if (separated.length == 2) {
                    paymentMethod = UtilityClass.capFirstLetter(separated[0]) + " " + UtilityClass.capFirstLetter(separated[1]);
                } else {
                    paymentMethod = responseString;
                }
                dopayment(paymentMethod);
            }
        });
    }

    public void dopayment(final String pay_ment_method)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ConfirmActivity.this);
        builder1.setTitle("Confirmation");
        builder1.setMessage("Are you sure want to order product by Cash On Delivery?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        pd = new ProgressDialog(ConfirmActivity.this);
                        pd.setMessage("Processing...");
                        pd.show();
                        String JSON_URL = Constants.PATH_TO_PAYMENT;
                        RequestQueue queueOrder = Volley.newRequestQueue(ConfirmActivity.this);
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
                                    RequestQueue queue = Volley.newRequestQueue(ConfirmActivity.this);
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            pd.dismiss();
                                            Log.v("EEEEEEE","2nd Response - "+response);
                                            Toast.makeText(ConfirmActivity.this, "Product ordered Successfully", Toast.LENGTH_SHORT).show();
                                            dataSource.removeAllCartProducts();
                                            Intent in = new Intent(ConfirmActivity.this, CartActivity.class);
                                            startActivity(in);
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            pd.dismiss();
                                            Log.d("responseV", error.getMessage());
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
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("order_method", pay_ment_method);
                                params.put("order_amount", totalPrice);
                                params.put("order_user_id", CustomSharedPrefs.getLoggedInUser(ConfirmActivity.this).getUser_id());

                                return params;

                            }
                        };

                        queueOrder.getCache().clear();
                        queueOrder.add(stringRequest);

                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();




    }




    }


