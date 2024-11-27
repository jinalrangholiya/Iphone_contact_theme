package com.icaller.callscreen.icalldialer;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.FirebaseDatabase;
import com.icaller.callscreen.icalldialer.AdsDemo.AdsPlacement;
import com.icaller.callscreen.icalldialer.Fragment.ContactsListActivity;
import com.icaller.callscreen.icalldialer.Fragment.FavouriteDataActivity;
import com.icaller.callscreen.icalldialer.Fragment.KeypadDataActivity;
import com.icaller.callscreen.icalldialer.Fragment.RecentDataActivity;
import com.icaller.callscreen.icalldialer.Fragment.SettingActivity;
import com.icaller.callscreen.icalldialer.Model.ContactItemList;
import com.icaller.callscreen.icalldialer.Utils.Permission;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    HashMap<String, ContactItemList> contact;
    private int defaultItemId = R.id.menu_user;
    FrameLayout frameLayout;

    ApplicationInfo ai;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle bundle) {
        boolean z = getSharedPreferences(getPackageName(), 0).getBoolean("dark_theme", false);
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
        setContentView(R.layout.activity_main);
         AdsPlacement.getInstance(this).Show_Banner_AD((ViewGroup) findViewById(R.id.banner_container));

        getWindow().addFlags(128);
        MobileAds.initialize(this);
        this.bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottonnav);
        this.frameLayout = (FrameLayout) findViewById(R.id.framlayout);
        if (!Permission.isPhonenGranted(this)) {
            Permission.getPhonePermission(this, 1234);
        } else {
            startActivity(new Intent(this, ContactsListActivity.class));
            overridePendingTransition(0, 0);
            finish();
        }
        Log.e("TAG", "onCreate: " + this.contact);

        if (bundle != null) {
            this.defaultItemId = bundle.getInt("selectedItemId", R.id.menu_user);
        }
        this.bottomNavigationView.setSelectedItemId(this.defaultItemId);
        if (z) {
            this.frameLayout.setBackgroundColor(getResources().getColor(R.color.darktheme));
            this.bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.darktheme));
            return;
        }
        this.frameLayout.setBackgroundColor(getResources().getColor(R.color.white));
        this.bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.white));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId != R.id.Setting) {
            switch (itemId) {
                case R.id.menu_dial :
                    if (!Permission.isPhonenGranted(this)) {
                        Permission.getPhonePermission(this, 1234);
                    } else {
                        startActivity(new Intent(this, KeypadDataActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                    }
                    return true;
                case R.id.menu_fav :
                    if (!Permission.isPhonenGranted(this)) {
                        Permission.getPhonePermission(this, 1234);
                    } else {
                        startActivity(new Intent(this, FavouriteDataActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                    }
                    return true;
                case R.id.menu_recent :
                    if (!Permission.isPhonenGranted(this)) {
                        Permission.getPhonePermission(this, 1234);
                    } else {
                        Log.e("TAG", "onNavigationItemSelected: ************");
                        startActivity(new Intent(this, RecentDataActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                    }
                    return true;
                case R.id.menu_user :
                    if (!Permission.isPhonenGranted(this)) {
                        Permission.getPhonePermission(this, 1234);
                    } else {
                        startActivity(new Intent(this, ContactsListActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                    }
                    return true;
                default:
                    return false;
            }
        }
        if (!Permission.isPhonenGranted(this)) {
            Permission.getPhonePermission(this, 1234);
        } else {
            startActivity(new Intent(this, SettingActivity.class));
            overridePendingTransition(0, 0);
            finish();
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("selectedItemId", this.bottomNavigationView.getSelectedItemId());
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 1234) {
            for (int i2 : iArr) {
                if (i2 != 0) {
                    Toast.makeText(this, "Please Permission Allow", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            this.bottomNavigationView.setOnNavigationItemSelectedListener(this);
            startActivity(new Intent(this, ContactsListActivity.class));
            overridePendingTransition(0, 0);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1234) {
            if (!Permission.isPhonenGranted(this)) {
                Permission.getPhonePermission(this, 1234);
                return;
            }
            this.bottomNavigationView.setOnNavigationItemSelectedListener(this);
            Toast.makeText(this, "Please Permission Allow..", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ContactsListActivity.class));
            overridePendingTransition(0, 0);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        getWindow().clearFlags(128);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
