package com.w3engineers.ecommerce.bootic.userLogin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;
import com.w3engineers.ecommerce.bootic.R;
import com.w3engineers.ecommerce.bootic.Util.Constants;
import com.w3engineers.ecommerce.bootic.Util.CustomSharedPrefs;
import com.w3engineers.ecommerce.bootic.Util.UtilityClass;
import com.w3engineers.ecommerce.bootic.main.MainActivity;
import com.w3engineers.ecommerce.bootic.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginAttemptActivity extends Activity
        implements View.OnClickListener {


    public static final String MY_GLOBAL_PREFS = "my_global_pref";
    public static final String FB_FIRST_NAME = "fb_first_name";


    LoginButton btnFBLogin;
    FrameLayout btnFBLoginMain;
    CallbackManager callbackManager;
    TextView tvFBLoginMessage;

    ImageView ivFBProfileImage;

    private String prevActivity;

    private static final int RC_SIGN_IN = 234;
    private static final String TAG = "booticEcommerce";


    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;
    //And also a Firebase Auth object
    FirebaseAuth mAuth;

    Button btnGoogleLogin;

    User users = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        printHashKey(this);

        prevActivity = getIntent().getStringExtra(Constants.LOGIN_PREV_ACTIVITY);

        requestWindowFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_login_attempt);

        initializeControlls();

        btnFBLoginMain.setOnClickListener(this);


        loginWIthFacebook();


        UtilityClass.buttonScaleIconLeft(this, findViewById(R.id.btn_facebook_inner), R.drawable.ic_facebook_white, 1);
        UtilityClass.buttonScaleIconLeft(this, findViewById(R.id.btn_google_inner), R.drawable.ic_google_white, 1);


        Button btnSignAttemptLogin = findViewById(R.id.btn_sign_attempt_login);
        btnSignAttemptLogin.setOnClickListener(this);


        //first we intialized the FirebaseAuth object
        mAuth = FirebaseAuth.getInstance();
        //Then we need a GoogleSignInOptions object
        //And we need to build it as below
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    //test that user is login or not
    @Override
    protected void onStart() {
        super.onStart();

        //if the user is already signed in then go to the MainActivity
        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(LoginAttemptActivity.this, MainActivity.class));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_facebook:
                if (!isNetworkAvailable()) {
                    Toast.makeText(this, "Something Wrong.Check Your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                }else{
                    btnFBLogin.performClick();
                }
                break;

            case R.id.btn_sign_attempt_login:

                Intent intentLogin = new Intent(LoginAttemptActivity.this, LoginActivity.class);
                intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, prevActivity);
                startActivity(intentLogin);
                break;
        }
    }


    private void initializeControlls() {
        callbackManager = CallbackManager.Factory.create();
        btnFBLogin = findViewById(R.id.btn_fb_login);
        btnFBLoginMain = findViewById(R.id.btn_facebook);
        tvFBLoginMessage = findViewById(R.id.tv_fb_login_message);

        ivFBProfileImage = findViewById(R.id.iv_fb_profile_image);
        btnGoogleLogin = findViewById(R.id.btn_google_inner);

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(LoginAttemptActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(LoginAttemptActivity.this, "User Signed In", Toast.LENGTH_SHORT).show();

                            String gmailUserName = user.getDisplayName();
                            String gmailUserEmail = user.getEmail();
                            String photo_url = user.getPhotoUrl().toString();
                            users.setEmail(gmailUserEmail);
                            users.setUsername(gmailUserName);
                            users.setImage(photo_url);
                            CustomSharedPrefs.setLoggedInUser(LoginAttemptActivity.this, users);

                            startActivity(new Intent(LoginAttemptActivity.this,MainActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginAttemptActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    //this method is called on click
    private void signIn() {
        //getting the google signin intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    /* My Change of Code Starts From Here*/
    private void loginWIthFacebook() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String accessToken = loginResult.getAccessToken().getToken();
                // prefUtil.saveAccessToken(accessToken);
                final String userid=loginResult.getAccessToken().getUserId();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {


                        //Bundle bFacebookData =
                        getFacebookData(object);


                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();
                Intent intent=new Intent(LoginAttemptActivity.this, MainActivity.class);
                startActivity(intent);
                //textView.setText("Login Succes\n"+loginResult.getAccessToken().getUserId()+"\n"+loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                //textView.setText("Login Cancelled");
                //deleteAccessToken();
                tvFBLoginMessage.setText("Unsuccess");
            }

            @Override
            public void onError(FacebookException error) {
                tvFBLoginMessage.setText("success" + error.getMessage());
            }
        });


    }


    private void getFacebookData(JSONObject object) {

        try {
            String id = object.getString("id");
            String pro_pic="https://graph.facebook.com/" + id+ "/picture?type=large";
            String first_Name="";
            String last_Name="";
            String email="";
            String user_Id="";
            try {
                first_Name = object.getString("first_name");
                last_Name = object.getString("last_name");
                email = object.getString("email");
                user_Id = object.getString("id");
                // gender=object.getString("gender");
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
            User user = new User();
            user.setFirst_name(first_Name);
            user.setLast_name(last_Name);
            user.setImage(pro_pic);
            user.setPassword("");
            user.setMembership("");
            user.setEmail(email);
            //user.setUser_id(Integer.parseInt(id));
            user.setUsername(user.getFirst_name()+user.getLast_name());
            user.setNumber("");
            user.setAddress("");
            CustomSharedPrefs.setLoggedInUser(this,user);

            //return bundle;
        } catch (JSONException e) {
            Log.d("tag", "Error parsing JSON");
        }

        //return null;
    }


    /*end Of Facebook Login*/








    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.w3engineers.ecommerce.bootic",   // Package name
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

}
