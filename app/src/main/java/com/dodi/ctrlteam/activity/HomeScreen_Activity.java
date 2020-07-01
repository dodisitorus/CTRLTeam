package com.dodi.ctrlteam.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.dodi.ctrlteam.R;
import com.dodi.ctrlteam.activity.fragment.NewProjectFragment;
import com.dodi.ctrlteam.activity.fragment.WhatsNewFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeScreen_Activity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.navigation) BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_);
        ButterKnife.bind(this);

        // set default fragment
        loadFragment(new WhatsNewFragment());
        navigation.setSelectedItemId(R.id.navigation_what_s_new);

        navigation.setOnNavigationItemSelectedListener(this);
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()){
            case R.id.navigation_what_s_new:
                fragment = new WhatsNewFragment();
                break;
            case R.id.navigation_project:
                fragment = new NewProjectFragment();
                break;
            case R.id.navigation_product:
                Intent intent = new Intent(this, ListContact_Activity.class);
                intent.putExtra("typeNav", "home");
                startActivity(intent);
                break;
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment){
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
