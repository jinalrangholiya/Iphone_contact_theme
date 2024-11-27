package com.icaller.callscreen.icalldialer.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.icaller.callscreen.icalldialer.AdsDemo.AdsPlacement;
import com.icaller.callscreen.icalldialer.R;
import com.icaller.callscreen.icalldialer.Model.CallLogDataItem;
import com.icaller.callscreen.icalldialer.Utils.FavoriteContactRemoverItemTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class RecentDataActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    TextView all;
    BottomNavigationView bottomNavigationView;
    RelativeLayout clear;
    RelativeLayout edit;
    LinearLayout layoutl;
    TextView missedcall;
    LinearLayout tab;
    TextView textname;
    TextView title;
    List<CallLogDataItem> uniqueCallLogs;
    int missedCallCount = 0;
    boolean recentdata = true;
    private int defaultItemId = R.id.menu_recent;
    Map<String, Integer> missedCallCounts = new HashMap();
    private boolean isEditMode = false;

    @Override
    public void onCreate(Bundle bundle) {
         boolean z = getSharedPreferences(getPackageName(), 0).getBoolean("dark_theme", false);
        if (z) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.darktheme));
            getWindow().getDecorView().setSystemUiVisibility(256);
            setTheme(R.style.AppThemeContactContact1);
        } else {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
            setTheme(R.style.AppThemeContactContact);
        }
        super.onCreate(bundle);
        setContentView(R.layout.fragment_recent_file);
        this.all = (TextView) findViewById(R.id.all);
        this.missedcall = (TextView) findViewById(R.id.missedcall);
        this.layoutl =  findViewById(R.id.layout);
        this.tab = (LinearLayout) findViewById(R.id.tab);
        this.edit = (RelativeLayout) findViewById(R.id.edit);
        this.clear = (RelativeLayout) findViewById(R.id.clear);
        this.textname = (TextView) findViewById(R.id.textname);
        this.title = (TextView) findViewById(R.id.recents);

        this.bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottonnav);
        if (bundle != null) {
            this.defaultItemId = bundle.getInt("selectedItemId", R.id.menu_recent);
        }
        this.bottomNavigationView.setSelectedItemId(this.defaultItemId);
        this.bottomNavigationView.setOnNavigationItemSelectedListener(this);
        loadFragment(new AllContactsRecentDataFragment(false));
        if (ContextCompat.checkSelfPermission(this, "android.permission.CALL_PHONE") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.CALL_PHONE"}, 1);
        }
        if (z) {
            this.bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.darktheme));
            this.all.setBackground(getResources().getDrawable(R.drawable.gradiant_bg_dark));
            this.tab.setBackgroundResource(R.drawable.tab_bg_dark);
            this.layoutl.setBackgroundColor(getResources().getColor(R.color.darktheme));
        } else {
            this.bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.white));
            this.all.setBackground(getResources().getDrawable(R.drawable.gradiant_bg_light));
            this.tab.setBackgroundResource(R.drawable.tab_select);
            this.layoutl.setBackgroundColor(getResources().getColor(R.color.white));
        }
        AdsPlacement.getInstance(this).Show_Banner_AD((ViewGroup) findViewById(R.id.banner_container));

        this.edit.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                RecentDataActivity recentActivity = RecentDataActivity.this;
                recentActivity.isEditMode = !recentActivity.isEditMode;
                if (!RecentDataActivity.this.isEditMode) {
                    RecentDataActivity.this.fetchCallLogs();
                    RecentDataActivity.this.clear.setVisibility(4);
                    RecentDataActivity.this.textname.setText("Edit");
                    RecentDataActivity.this.title.setVisibility(8);
                    RecentDataActivity.this.tab.setVisibility(0);
                    RecentDataActivity.this.loadFragment(new AllContactsRecentDataFragment(true));
                    return;
                }
                RecentDataActivity.this.textname.setText("Done");
                RecentDataActivity.this.clear.setVisibility(0);
                RecentDataActivity.this.title.setVisibility(0);
                RecentDataActivity.this.tab.setVisibility(8);
                RecentDataActivity.this.loadFragment(new AllListDataFragment(true));
            }
        });
        fetchCallLogs();
        this.clear.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                FavoriteContactRemoverItemTask.removeRecentAllCallLogs(RecentDataActivity.this.getContentResolver());
                Toast.makeText(RecentDataActivity.this, "Remove All Recent Calls", 0).show();
                RecentDataActivity.this.clear.setVisibility(4);
                RecentDataActivity.this.loadFragment(new AllListDataFragment(true));
            }
        });
        this.all.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                RecentDataActivity.this.recentdata = true;
                if (z) {
                    RecentDataActivity.this.all.setBackgroundResource(R.drawable.gradiant_bg_dark);
                    RecentDataActivity.this.missedcall.setBackgroundResource(R.drawable.tab_bg_dark);
                } else {
                    RecentDataActivity.this.all.setBackgroundResource(R.drawable.gradiant_bg_light);
                    RecentDataActivity.this.missedcall.setBackgroundResource(R.drawable.tab_bg);
                }
                RecentDataActivity.this.loadFragment(new AllContactsRecentDataFragment(false));
            }
        });
        this.missedcall.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                RecentDataActivity.this.recentdata = false;
                if (z) {
                    RecentDataActivity.this.all.setBackgroundResource(R.drawable.tab_bg_dark);
                    RecentDataActivity.this.missedcall.setBackgroundResource(R.drawable.gradiant_bg_dark);
                } else {
                    RecentDataActivity.this.all.setBackgroundResource(R.drawable.tab_bg);
                    RecentDataActivity.this.missedcall.setBackgroundResource(R.drawable.gradiant_bg_light);
                }
                RecentDataActivity.this.loadFragment(new MissedCallsRecentDataFragment());
            }
        });
    }

    public void fetchCallLogs() {
        Cursor query;
        ArrayList arrayList = new ArrayList();
        if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_CALL_LOG") == 0 && (query = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, "date DESC")) != null) {
            int columnIndex = query.getColumnIndex("number");
            int columnIndex2 = query.getColumnIndex("date");
            int columnIndex3 = query.getColumnIndex("type");
            int columnIndex4 = query.getColumnIndex("name");
            HashMap hashMap = new HashMap();
            while (query.moveToNext()) {
                String string = query.getString(columnIndex);
                String string2 = query.getString(columnIndex4);
                long j = query.getLong(columnIndex2);
                int i = query.getInt(columnIndex3);
                if (i == 3) {
                    this.missedCallCount++;
                }
                if (string != null && !string.trim().isEmpty()) {
                    if (hashMap.containsKey(string)) {
                        hashMap.put(string, Integer.valueOf(((Integer) hashMap.get(string)).intValue() + 1));
                    } else {
                        hashMap.put(string, 1);
                    }
                    if (i == 3) {
                        Map<String, Integer> map = this.missedCallCounts;
                        map.put(string, Integer.valueOf(map.getOrDefault(string, 0).intValue() + 1));
                    }
                    if (string != null) {
                        arrayList.add(new CallLogDataItem(string2 == null ? "" : string2, string, new Date(j), i, this.missedCallCounts.size()));
                    }
                }
            }
            query.close();
        }
        List<CallLogDataItem> uniqueCallLogs = getUniqueCallLogs(arrayList);
        this.uniqueCallLogs = uniqueCallLogs;
        if (uniqueCallLogs.size() == 0) {
            this.edit.setVisibility(4);
        } else {
            this.edit.setVisibility(0);
        }
    }

    private List<CallLogDataItem> getUniqueCallLogs(List<CallLogDataItem> list) {
        HashSet hashSet = new HashSet();
        ArrayList arrayList = new ArrayList();
        for (CallLogDataItem callLogItem : list) {
            if (!hashSet.contains(callLogItem.getNumber())) {
                arrayList.add(callLogItem);
                hashSet.add(callLogItem.getNumber());
            }
        }
        return arrayList;
    }

    void loadFragment(Fragment fragment) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.framlayout, fragment);
        beginTransaction.commit();
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
                case R.id.menu_fav:
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
