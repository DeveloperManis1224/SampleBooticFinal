package com.w3engineers.ecommerce.bootic.splashScreen;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.w3engineers.ecommerce.bootic.R;
import com.w3engineers.ecommerce.bootic.main.MainActivity;


public class SplashActivity extends AppCompatActivity {

    //private ImageView ivSplashImage;
    private TextView ivSplashImage;
    private Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

         mainIntent = new Intent(SplashActivity.this, MainActivity.class);

            ivSplashImage = findViewById(R.id.iv_splash_image);
            Animation transitionAlfa = AnimationUtils.loadAnimation(this, R.anim.transition_alfa);
            ivSplashImage.startAnimation(transitionAlfa);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();

                }
            }, 1000);

    }

}
