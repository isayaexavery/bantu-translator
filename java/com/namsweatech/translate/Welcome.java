package com.namsweatech.translate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Locale;

public class Welcome extends Activity{

    LinearLayout startLayout, noInternet;
    Button  tryAgainBtn;
    Handler handler = new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        startLayout = findViewById(R.id.start_translate);
        noInternet = findViewById(R.id.noInternet);
        tryAgainBtn = findViewById(R.id.tryAgain);

        if (isOnline()) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    Intent translate = new Intent(Welcome.this, MainActivity.class);
                    startActivity(translate);
                    finish();
                }
            };
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, 2000);

        } else {

            startLayout.setVisibility(View.GONE);
            noInternet.setVisibility(View.VISIBLE);

        }

        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
            }
        });
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}
