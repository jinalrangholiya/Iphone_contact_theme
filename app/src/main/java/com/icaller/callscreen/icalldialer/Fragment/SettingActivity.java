package com.icaller.callscreen.icalldialer.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.icaller.callscreen.icalldialer.AdsDemo.AdsPlacement;
import com.icaller.callscreen.icalldialer.R;
import com.icaller.callscreen.icalldialer.CustomLoadProgressDialogView;
import com.icaller.callscreen.icalldialer.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;



public class SettingActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    CustomLoadProgressDialogView customProgressDialog;
    LinearLayout layout;
    View line1;
    View line2;
    AppCompatImageView mode;
    LinearLayout rate;
    LinearLayout share;
    private boolean isClicked = false;
    private int defaultItemId = R.id.Setting;

    @Override
    public void onCreate(Bundle bundle) {
         SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), 0);
        boolean z = sharedPreferences.getBoolean("dark_theme", false);
        this.isClicked = z;
        if (z) {
            Log.e("TAG", "onCreate: ****************");
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.darktheme));
            getWindow().getDecorView().setSystemUiVisibility(256);
            setTheme(R.style.AppThemeContactContact1);
        } else {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
            setTheme(R.style.AppThemeContactContact);
        }
        super.onCreate(bundle);
        setContentView(R.layout.fragment_setting);
        this.rate = (LinearLayout) findViewById(R.id.rate);
        this.share = (LinearLayout) findViewById(R.id.Share);
        this.mode = (AppCompatImageView) findViewById(R.id.theme);
        this.layout =  findViewById(R.id.layout);
        this.line1 = findViewById(R.id.line1);
        this.line2 = findViewById(R.id.line2);
        AdsPlacement.getInstance(this).Show_Banner_AD((ViewGroup) findViewById(R.id.banner_container));

        this.bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottonnav);
        if (bundle != null) {
            this.defaultItemId = bundle.getInt("selectedItemId", R.id.menu_fav);
        }
        this.customProgressDialog = new CustomLoadProgressDialogView(this);
        this.bottomNavigationView.setSelectedItemId(this.defaultItemId);
        this.bottomNavigationView.setOnNavigationItemSelectedListener(this);
        if (this.isClicked) {
            Log.e("TAG", "onCreate: ****************");
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.darktheme));
            this.bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.darktheme));
            getWindow().getDecorView().setSystemUiVisibility(256);
            this.line1.setBackgroundResource(R.color.darkline);
            this.line2.setBackgroundResource(R.color.darkline);
            this.mode.setBackgroundResource(R.drawable.on_button);
            this.layout.setBackgroundColor(getColor(R.color.darktheme));
        } else {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
            this.bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.white));
            this.line1.setBackgroundResource(R.color.lightline);
            this.line2.setBackgroundResource(R.color.lightline);
            this.mode.setBackgroundResource(R.drawable.off_button);
            this.layout.setBackgroundColor(getColor(R.color.white));
        }
        this.rate.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                SettingActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + SettingActivity.this.getPackageName())));
            }
        });
        this.share.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                String packageName = SettingActivity.this.getPackageName();
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.TEXT", "Check out the App at: https://play.google.com/store/apps/details?id=" + packageName);
                intent.setType("text/plain");
                SettingActivity.this.startActivity(intent);
            }
        });
        this.mode.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                SettingActivity settingActivity = SettingActivity.this;
                settingActivity.isClicked = !settingActivity.isClicked;
                if (SettingActivity.this.isClicked) {
                    SettingActivity.this.mode.setBackgroundResource(R.drawable.on_button);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putBoolean("dark_theme", true);
                    edit.apply();
                } else {
                    SettingActivity.this.mode.setBackgroundResource(R.drawable.off_button);
                    SharedPreferences.Editor edit2 = sharedPreferences.edit();
                    edit2.putBoolean("dark_theme", false);
                    edit2.apply();
                }
                SettingActivity.this.startActivity(new Intent(SettingActivity.this, MainActivity.class));
                SettingActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId != R.id.Setting) {
            switch (itemId) {
                case R.id.menu_dial :
                    startActivity(new Intent(this, KeypadDataActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.menu_fav :
                    startActivity(new Intent(this, FavouriteDataActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.menu_recent :
                    startActivity(new Intent(this, RecentDataActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.menu_user :
                    startActivity(new Intent(this, ContactsListActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                default:
                    return false;
            }
        }
        startActivity(new Intent(this, SettingActivity.class));
        overridePendingTransition(0, 0);
        finish();
        return true;
    }
}
