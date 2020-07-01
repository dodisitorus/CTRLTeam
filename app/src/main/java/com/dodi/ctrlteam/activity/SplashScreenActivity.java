package com.dodi.ctrlteam.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.dodi.ctrlteam.R;
import com.dodi.ctrlteam.common.UserPreferences;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));

        finish();
    }
}
