package com.icaller.callscreen.icalldialer.Fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.icaller.callscreen.icalldialer.AdsDemo.AdsPlacement;
import com.icaller.callscreen.icalldialer.R;
import com.icaller.callscreen.icalldialer.Adapter.ContactFavriteAdapter;
import com.icaller.callscreen.icalldialer.CustomLoadProgressDialogView;
import com.icaller.callscreen.icalldialer.Model.ContactFavList;
import com.icaller.callscreen.icalldialer.SwipeToDeleteDataCallbackContact;
import com.icaller.callscreen.icalldialer.Utils.FavoriteContactRemoverItemTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;


public class FavouriteDataActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    RelativeLayout clear;
    private ContactFavriteAdapter contactAdapter;
    CustomLoadProgressDialogView customProgressDialog;
    RelativeLayout edit;
    LinearLayout nodata;
    ImageView nodataimage;
    private RecyclerView recyclerView;
    LinearLayout rel;
    TextView text;
    private boolean isEditMode = false;
    private int defaultItemId = R.id.menu_fav;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        boolean z = getSharedPreferences(getPackageName(), 0).getBoolean("dark_theme", false);
        if (z) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.darktheme));
            getWindow().getDecorView().setSystemUiVisibility(256);
            setTheme(R.style.AppThemeContactContact1);
        } else {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
            setTheme(R.style.AppThemeContactContact);
        }
        setContentView(R.layout.fragment_favourite_data);

        this.customProgressDialog = new CustomLoadProgressDialogView(this);
        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        this.nodata = (LinearLayout) findViewById(R.id.nodata);
        this.rel =  findViewById(R.id.layout);
        this.edit = (RelativeLayout) findViewById(R.id.edit);
        this.clear = (RelativeLayout) findViewById(R.id.clear);
        this.text = (TextView) findViewById(R.id.text);
        this.nodataimage = (ImageView) findViewById(R.id.nodataimage);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ContactFavriteAdapter contactFavAdapter = new ContactFavriteAdapter(new ArrayList(), this, false);
        this.contactAdapter = contactFavAdapter;
        this.recyclerView.setAdapter(contactFavAdapter);
        this.clear.setVisibility(View.INVISIBLE);
        this.bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottonnav);
        if (bundle != null) {
            this.defaultItemId = bundle.getInt("selectedItemId", R.id.menu_fav);
        }
        this.bottomNavigationView.setSelectedItemId(this.defaultItemId);
        this.bottomNavigationView.setOnNavigationItemSelectedListener(this);
        if (checkPermissions()) {
            loadFavouriteContacts();
        }

        AdsPlacement.getInstance(this).Show_Banner_AD((ViewGroup) findViewById(R.id.banner_container));

        if (z) {
            this.bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.darktheme));
            this.nodataimage.setImageResource(R.drawable.favourite);
            this.rel.setBackgroundColor(getResources().getColor(R.color.darktheme));
        } else {
            this.bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.white));
            this.nodataimage.setImageResource(R.drawable.favourite);
            this.rel.setBackgroundColor(getResources().getColor(R.color.white));
        }
        if (this.contactAdapter.contacts.size() == 0) {
            this.edit.setVisibility(4);
        } else {
            this.edit.setVisibility(0);
        }
        this.edit.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                if (FavouriteDataActivity.this.checkPermissions()) {
                    FavouriteDataActivity.this.loadFavouriteContacts();
                }
                FavouriteDataActivity favouriteActivity = FavouriteDataActivity.this;
                favouriteActivity.isEditMode = !favouriteActivity.isEditMode;
                if (FavouriteDataActivity.this.isEditMode) {
                    FavouriteDataActivity.this.text.setText("Done");
                    if (FavouriteDataActivity.this.contactAdapter.contacts.size() == 0) {
                        FavouriteDataActivity.this.clear.setVisibility(4);
                    } else {
                        FavouriteDataActivity.this.clear.setVisibility(0);
                    }
                    FavouriteDataActivity.this.contactAdapter.setEditMode(FavouriteDataActivity.this.isEditMode);
                    return;
                }
                FavouriteDataActivity.this.clear.setVisibility(4);
                FavouriteDataActivity.this.text.setText("Edit");
                if (FavouriteDataActivity.this.contactAdapter.contacts.size() == 0) {
                    FavouriteDataActivity.this.edit.setVisibility(4);
                } else {
                    FavouriteDataActivity.this.edit.setVisibility(0);
                }
                FavouriteDataActivity.this.contactAdapter.setEditMode(FavouriteDataActivity.this.isEditMode);
            }
        });
        this.clear.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                Toast.makeText(FavouriteDataActivity.this, "Clear data ", 0).show();
                FavoriteContactRemoverItemTask.removeAllFavorites(FavouriteDataActivity.this.getContentResolver());
                FavouriteDataActivity.this.contactAdapter.contacts.clear();
                if (FavouriteDataActivity.this.contactAdapter.contacts.size() == 0) {
                    FavouriteDataActivity.this.clear.setVisibility(4);
                    FavouriteDataActivity.this.nodata.setVisibility(0);
                    FavouriteDataActivity.this.recyclerView.setVisibility(8);
                    return;
                }
                FavouriteDataActivity.this.clear.setVisibility(0);
                FavouriteDataActivity.this.nodata.setVisibility(8);
                FavouriteDataActivity.this.recyclerView.setVisibility(0);
            }
        });
        new SwipeToDeleteDataCallbackContact(this, this.recyclerView) {
            @Override
            public void instantiateUnderlayButton( RecyclerView.ViewHolder viewHolder, List<SwipeToDeleteDataCallbackContact.UnderlayButton> list) {
                list.add(new SwipeToDeleteDataCallbackContact.UnderlayButton(FavouriteDataActivity.this, "", R.drawable.delete_btn, Color.parseColor("#FF3C30"), new SwipeToDeleteDataCallbackContact.UnderlayButtonClickListener() {
                    @Override
                    public void onClick(int i) {
                        String phoneNumber = FavouriteDataActivity.this.contactAdapter.contacts.get(viewHolder.getAdapterPosition()).getPhoneNumber();
                        String name = FavouriteDataActivity.this.contactAdapter.contacts.get(viewHolder.getAdapterPosition()).getName();
                        Log.e("TAG", "onClick: " + phoneNumber);
                        FavouriteDataActivity.this.contactAdapter.removeItem(viewHolder.getAdapterPosition());
                        ContentResolver contentResolver = FavouriteDataActivity.this.getContentResolver();
                        String contactID = FavoriteContactRemoverItemTask.getContactID(contentResolver, phoneNumber, name);
                        if (contactID != null) {
                            FavoriteContactRemoverItemTask.removeFromFavorites(contentResolver, contactID);
                        }
                        if (FavouriteDataActivity.this.contactAdapter.contacts.size() == 0) {
                            FavouriteDataActivity.this.edit.setVisibility(4);
                            FavouriteDataActivity.this.nodata.setVisibility(0);
                            FavouriteDataActivity.this.recyclerView.setVisibility(8);
                        } else {
                            FavouriteDataActivity.this.nodata.setVisibility(8);
                            FavouriteDataActivity.this.edit.setVisibility(0);
                            FavouriteDataActivity.this.recyclerView.setVisibility(0);
                        }
                        Toast.makeText(FavouriteDataActivity.this, "Delete Contact", 0).show();
                    }
                }));
            }
        }.attachSwipe();
    }

    public boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, "android.permission.READ_CONTACTS") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.WRITE_CONTACTS") == 0;
    }

    public void loadFavouriteContacts() {
        List<ContactFavList> favouriteContacts = getFavouriteContacts();
        if (favouriteContacts.size() == 0) {
            this.edit.setVisibility(4);
            this.clear.setVisibility(4);
            this.nodata.setVisibility(0);
            this.recyclerView.setVisibility(8);
        } else {
            this.edit.setVisibility(0);
            this.nodata.setVisibility(8);
            this.recyclerView.setVisibility(0);
        }
        Log.e("TAG", "loadFavouriteContacts: " + favouriteContacts.size());
        this.contactAdapter.updateContacts(favouriteContacts);
        this.customProgressDialog.dismiss();
    }

    public List<ContactFavList> getFavouriteContacts() {
        this.customProgressDialog.show();
        ArrayList arrayList = new ArrayList();
        ContentResolver contentResolver = getContentResolver();
        Cursor query = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[]{"_id", "display_name", "has_phone_number"}, "starred = ?", new String[]{"1"}, null);
        if (query != null) {
            while (query.moveToNext()) {
                long j = query.getLong(query.getColumnIndexOrThrow("_id"));
                String string = query.getString(query.getColumnIndexOrThrow("display_name"));
                if (query.getInt(query.getColumnIndexOrThrow("has_phone_number")) > 0) {
                    Cursor query2 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{"data1"}, "contact_id = ?", new String[]{String.valueOf(j)}, null);
                    if (query2 != null && query2.moveToFirst()) {
                        arrayList.add(new ContactFavList(string, query2.getString(query2.getColumnIndexOrThrow("data1")), j));
                        query2.close();
                    }
                } else {
                    arrayList.add(new ContactFavList(string, "No Contact details", j));
                }
            }
            query.close();
        }
        return arrayList;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavouriteContacts();
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
                case R.id.menu_recent:
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
