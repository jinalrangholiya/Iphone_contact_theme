package com.icaller.callscreen.icalldialer.Fragment;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.icaller.callscreen.icalldialer.AdsDemo.AdsPlacement;
import com.icaller.callscreen.icalldialer.R;
import com.icaller.callscreen.icalldialer.Adapter.AlphabetItemListViewAdapter;
import com.icaller.callscreen.icalldialer.CustomLoadProgressDialogView;
import com.icaller.callscreen.icalldialer.Model.ContactItemList;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


public class ContactsListActivity extends ListActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static String TAG = "ContactListFragment";
    private static float sideIndexX;
    private static float sideIndexY;
    private AlphabetItemListViewAdapter adapter;
    private TextView animatedText;
    BottomNavigationView bottomNavigationView;
    CustomLoadProgressDialogView customProgressDialog;
    public LinearLayout data;
    private int indexListSize;
    private EditText inputSearch;
    ListView listView;
    public LinearLayout nodata;
    LinearLayout relativeLayout;
    private LinearLayout sideIndex;
    private int sideIndexHeight;
    private List<Object[]> alphabet = new ArrayList();
    private HashMap<String, Integer> sections = new HashMap<>();
    private int defaultItemId = R.id.menu_user;
    private HashMap<String, ContactItemList> contacts = new HashMap<>();

    @SuppressLint("ResourceType")
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
        setContentView(R.layout.fragment_contacts_list);
        this.customProgressDialog = new CustomLoadProgressDialogView(this);
        this.adapter = new AlphabetItemListViewAdapter(this);
        this.inputSearch = (EditText) findViewById(R.id.inputSearch);
        this.animatedText = (TextView) findViewById(R.id.animated_letter);
        this.sideIndex = (LinearLayout) findViewById(R.id.sideIndex);
        this.relativeLayout =  findViewById(R.id.list_alphabet_layout);
        this.bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottonnav);
        this.nodata = (LinearLayout) findViewById(R.id.nodata);
        this.listView = (ListView) findViewById(16908298);
        this.data = (LinearLayout) findViewById(R.id.data);
        ;
        AdsPlacement.getInstance(this).Show_Banner_AD((ViewGroup) findViewById(R.id.banner_container));

        readcontacts();
        if (bundle != null) {
            this.defaultItemId = bundle.getInt("selectedItemId", R.id.menu_user);
        }
        this.bottomNavigationView.setSelectedItemId(this.defaultItemId);
        this.bottomNavigationView.setOnNavigationItemSelectedListener(this);
        if (z) {
            this.inputSearch.setTextColor(-1);
            this.bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.darktheme));
            this.listView.setDivider(new ColorDrawable(getResources().getColor(R.color.darkline)));
            this.listView.setDividerHeight((int) getResources().getDimension(R.dimen._0_6dp));
            this.relativeLayout.setBackgroundColor(getResources().getColor(R.color.darktheme));
            this.inputSearch.setBackgroundResource(R.drawable.tab_bg_dark);
        } else {
            this.inputSearch.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            this.bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.white));
            this.listView.setDivider(new ColorDrawable(getResources().getColor(R.color.lightline)));
            this.listView.setDividerHeight((int) getResources().getDimension(R.dimen._0_6dp));
            this.relativeLayout.setBackgroundColor(getResources().getColor(R.color.white));
            this.inputSearch.setBackgroundResource(R.drawable.tab_bg_light);
        }
        this.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override 
            public void afterTextChanged(Editable editable) {
            }

            @Override 
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override 
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                ContactsListActivity.this.adapter.getFilter().filter(charSequence);
                Log.e(ContactsListActivity.TAG, "onTextChanged: " + ContactsListActivity.this.adapter.getFilter());
            }
        });
    }

    public void updatelayout(boolean z) {
        if (z) {
            this.data.setVisibility(8);
            this.nodata.setVisibility(0);
            return;
        }
        this.data.setVisibility(0);
        this.nodata.setVisibility(8);
    }

    public void handleAnimatedLetter(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 2) {
            if (this.animatedText.getVisibility() != 8) {
                return;
            }
            this.animatedText.setVisibility(0);
        } else if (motionEvent.getAction() != 1 || this.animatedText.getVisibility() != 0) {
        } else {
            this.animatedText.setVisibility(8);
        }
    }

    public void updateList() {
        this.sideIndex.removeAllViews();
        int size = this.alphabet.size();
        this.indexListSize = size;
        if (size < 1) {
            return;
        }
        int floor = (int) Math.floor(this.sideIndex.getHeight() / 20.0d);
        int i = this.indexListSize;
        while (i > floor) {
            i /= 2;
        }
        if (i > 0) {
            int i2 = this.indexListSize / i;
        }
        for (int i3 = 0; i3 <= 26; i3++) {
            String obj = new Object[]{"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}[i3].toString();
            TextView textView = new TextView(this);
            textView.setText(obj);
            textView.setGravity(17);
            textView.setTextSize(10.0f);
            textView.setTextColor(getResources().getColor(R.color.colorAccent));
            textView.setLayoutParams(new LinearLayout.LayoutParams(-2, -2, 1.0f));
            this.sideIndex.addView(textView);
        }
        this.sideIndexHeight = this.sideIndex.getHeight();
        this.sideIndex.setOnTouchListener(new View.OnTouchListener() { 
            @Override 
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("bla", "event: " + motionEvent.getAction());
                float unused = ContactsListActivity.sideIndexX = motionEvent.getX();
                float unused2 = ContactsListActivity.sideIndexY = motionEvent.getY();
                ContactsListActivity.this.displayListItem();
                ContactsListActivity.this.handleAnimatedLetter(motionEvent);
                return motionEvent.getAction() == 2 || motionEvent.getAction() == 0;
            }
        });
    }

    public void displayListItem() {
        int height = this.sideIndex.getHeight();
        this.sideIndexHeight = height;
        int i = (int) (sideIndexY / (height / this.indexListSize));
        if (i >= this.alphabet.size() || i < 0) {
            return;
        }
        Object[] objArr = this.alphabet.get(i);
        this.animatedText.setText(String.valueOf(objArr[0]));
        Log.e(TAG, "displayListItem sections: " + this.sections.get(objArr[0]));
        Log.e(TAG, "displayListItem indexItem : " + objArr);
        if (this.sections.get(objArr[0]) == null) {
            return;
        }
        getListView().setSelection(this.sections.get(objArr[0]).intValue());
    }

    @Override 
    public void onListItemClick(ListView listView, View view, int i, long j) {
        HashMap<String, ContactItemList> hashMap;
        AlphabetItemListViewAdapter.Cell cell = (AlphabetItemListViewAdapter.Cell) view.getTag();
        Log.e(TAG, "onListItemClick: " + cell);
        if (cell != null && (hashMap = this.contacts) != null) {
            ContactItemList contactItem = hashMap.get(cell.text);
            if (contactItem != null) {
                String phoneNumber = contactItem.getPhoneNumber();
                new HashMap().put(cell.text, phoneNumber);
                Log.e(TAG, "onListItemClick: " + phoneNumber);
                startActivity(new Intent("android.intent.action.VIEW", Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactItem.getId()))));
                return;
            }
            Log.e(TAG, "onListItemClick: ContactItem is null.");
            return;
        }
        Log.e(TAG, "onListItemClick: Contacts HashMap is null or item is null.");
    }

    public void readcontacts() {
        this.customProgressDialog.show();
        Cursor query = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{"display_name", "data1", "contact_id"}, null, null, "display_name ASC");
        if (query != null && query.moveToFirst()) {
            do {
                String string = query.getString(0);
                String string2 = query.getString(1);
                long j = query.getLong(2);
                ContactItemList contactItem = new ContactItemList();
                contactItem.setName(string);
                contactItem.setPhoneNumber(string2);
                contactItem.setId(j);
                this.contacts.put(string, contactItem);
            } while (query.moveToNext());
            query.close();
        }
        displayContactList(this.contacts);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.contacts != null) {
            Log.e(TAG, "onCreateView ******* " + this.contacts);
            readcontacts();
        }
    }

    public void displayContactList(HashMap<String, ContactItemList> hashMap) {
        ArrayList<String> arrayList = new ArrayList(hashMap.keySet());
        Log.e(TAG, "displayContactList:|||||||||||||||||||||| " + arrayList.size());
        Collections.sort(arrayList);
        ArrayList arrayList2 = new ArrayList();
        Pattern compile = Pattern.compile("[0-9]");
        String str = null;
        int i = 0;
        for (String str2 : arrayList) {
            if (str2 != null) {
                String substring = str2.substring(0, 1);
                if (compile.matcher(substring).matches()) {
                    substring = "#";
                }
                if (str != null && !substring.equals(str)) {
                    int size = arrayList2.size();
                    this.alphabet.add(new Object[]{str.toUpperCase(Locale.UK), Integer.valueOf(i), Integer.valueOf(size - 1)});
                    i = size;
                }
                if (!substring.equals(str)) {
                    arrayList2.add(new AlphabetItemListViewAdapter.Header(substring));
                    this.sections.put(substring, Integer.valueOf(i));
                }
                arrayList2.add(new AlphabetItemListViewAdapter.Cell(str2));
                str = substring;
            }
        }
        if (str != null) {
            this.alphabet.add(new Object[]{str.toUpperCase(Locale.UK), Integer.valueOf(i), Integer.valueOf(arrayList2.size() - 1)});
        }
        if (arrayList2.size() == 0) {
            Log.e(TAG, "onContactsLoaded: " + arrayList2.size());
            this.data.setVisibility(8);
            this.nodata.setVisibility(0);
        } else {
            Log.e(TAG, "onContactsLoaded**********: " + arrayList2.size());
            this.data.setVisibility(0);
            this.nodata.setVisibility(8);
        }
        this.customProgressDialog.dismiss();
        this.adapter.setRows(arrayList2);
        setListAdapter(this.adapter);
        this.adapter.notifyDataSetChanged();
        updateList();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId != R.id.Setting) {
            switch (itemId) {
                case R.id.menu_dial:
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
                    Log.e("TAG", "oSelected: ************");
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
