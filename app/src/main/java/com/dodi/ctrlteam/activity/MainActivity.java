package com.dodi.ctrlteam.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.dodi.ctrlteam.R;
import com.dodi.ctrlteam.common.UserPreferences;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckLoginStatus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    private void CheckLoginStatus() {
        if (UserPreferences.getDataBool(UserPreferences.loginKey, this)) {
            startActivity(new Intent(this, HomeScreen_Activity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }
}
