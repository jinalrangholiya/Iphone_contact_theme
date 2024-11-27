package com.icaller.callscreen.icalldialer.Fragment;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;


import com.icaller.callscreen.icalldialer.AdsDemo.AdsPlacement;
import com.icaller.callscreen.icalldialer.R;
import com.icaller.callscreen.icalldialer.CustomLoadProgressDialogView;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.HashSet;
import java.util.Iterator;


public class KeypadDataActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private static  int REQUEST_CALL_PHONE = 1;
    BottomNavigationView bottomNavigationView;
    RelativeLayout button_1;
    RelativeLayout button_10;
    RelativeLayout button_11;
    RelativeLayout button_12;
    RelativeLayout button_2;
    RelativeLayout button_3;
    RelativeLayout button_4;
    RelativeLayout button_5;
    RelativeLayout button_6;
    RelativeLayout button_7;
    RelativeLayout button_8;
    RelativeLayout button_9;
    ImageView cacncel;
    ContentResolver contentResolver;
    CustomLoadProgressDialogView customProgressDialog;
    private int defaultItemId = R.id.menu_dial;
    private EditText editText;
    ImageView img1;
    ImageView img10;
    ImageView img11;
    ImageView img12;
    ImageView img2;
    ImageView img3;
    ImageView img4;
    ImageView img5;
    ImageView img6;
    ImageView img7;
    ImageView img8;
    ImageView img9;
    String number;
    LinearLayout relativeLayout;
    TextView textView;

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
        setContentView(R.layout.fragment_keypad_screen);
        this.relativeLayout =  findViewById(R.id.layout);
        this.editText = (EditText) findViewById(R.id.editText);
        this.textView = (TextView) findViewById(R.id.Addnumber);
        this.button_1 = (RelativeLayout) findViewById(R.id.button_1);
        this.button_2 = (RelativeLayout) findViewById(R.id.button_2);
        this.button_3 = (RelativeLayout) findViewById(R.id.button_3);
        this.button_4 = (RelativeLayout) findViewById(R.id.button_4);
        this.button_5 = (RelativeLayout) findViewById(R.id.button_5);
        this.button_6 = (RelativeLayout) findViewById(R.id.button_6);
        this.button_7 = (RelativeLayout) findViewById(R.id.button_7);
        this.button_8 = (RelativeLayout) findViewById(R.id.button_8);
        this.button_9 = (RelativeLayout) findViewById(R.id.button_9);
        this.button_10 = (RelativeLayout) findViewById(R.id.button_10);
        this.button_11 = (RelativeLayout) findViewById(R.id.button_11);
        this.button_12 = (RelativeLayout) findViewById(R.id.button_12);
        this.cacncel = (ImageView) findViewById(R.id.removebtn);
        this.img1 = (ImageView) findViewById(R.id.btn1);
        this.img2 = (ImageView) findViewById(R.id.btn2);
        this.img3 = (ImageView) findViewById(R.id.btn3);
        this.img4 = (ImageView) findViewById(R.id.btn4);
        this.img5 = (ImageView) findViewById(R.id.btn5);
        this.img6 = (ImageView) findViewById(R.id.btn6);
        this.img7 = (ImageView) findViewById(R.id.btn7);
        this.img8 = (ImageView) findViewById(R.id.btn8);
        this.img9 = (ImageView) findViewById(R.id.btn9);
        this.img10 = (ImageView) findViewById(R.id.btnstar);
        this.img11 = (ImageView) findViewById(R.id.btn0);
        this.img12 = (ImageView) findViewById(R.id.btnhash);
        this.cacncel = (ImageView) findViewById(R.id.removebtn);
        this.button_1.setOnClickListener(this);
        this.button_2.setOnClickListener(this);
        this.button_3.setOnClickListener(this);
        this.button_4.setOnClickListener(this);
        this.button_5.setOnClickListener(this);
        this.button_6.setOnClickListener(this);
        this.button_7.setOnClickListener(this);
        this.button_8.setOnClickListener(this);
        this.button_9.setOnClickListener(this);
        this.button_10.setOnClickListener(this);
        this.button_11.setOnClickListener(this);
        this.button_12.setOnClickListener(this);

        this.bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottonnav);
        if (bundle != null) {
            this.defaultItemId = bundle.getInt("selectedItemId", R.id.menu_fav);
        }
        this.bottomNavigationView.setSelectedItemId(this.defaultItemId);
        this.bottomNavigationView.setOnNavigationItemSelectedListener(this);
        this.customProgressDialog = new CustomLoadProgressDialogView(this);

        AdsPlacement.getInstance(this).Show_Banner_AD((ViewGroup) findViewById(R.id.banner_container));

        if (z) {
            this.relativeLayout.setBackgroundColor(getResources().getColor(R.color.darktheme));
            this.bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.darktheme));
            this.editText.setTextColor(-1);
            this.cacncel.setImageResource(R.drawable.camcel_dark_btn);
            this.img1.setImageResource(R.drawable.button1);
            this.img2.setImageResource(R.drawable.button2);
            this.img3.setImageResource(R.drawable.button3);
            this.img4.setImageResource(R.drawable.button4);
            this.img5.setImageResource(R.drawable.button5);
            this.img6.setImageResource(R.drawable.button6);
            this.img7.setImageResource(R.drawable.button7);
            this.img8.setImageResource(R.drawable.button8);
            this.img9.setImageResource(R.drawable.button9);
            this.img10.setImageResource(R.drawable.buttom_star);
            this.img11.setImageResource(R.drawable.button0);
            this.img12.setImageResource(R.drawable.button_hash);
        } else {
            this.editText.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            this.relativeLayout.setBackgroundColor(getResources().getColor(R.color.white));
            this.bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.white));
            this.img1.setImageResource(R.drawable.btn_1_light);
            this.img2.setImageResource(R.drawable.btn_2_light);
            this.img3.setImageResource(R.drawable.btn_3_light);
            this.img4.setImageResource(R.drawable.btn_4_light);
            this.img5.setImageResource(R.drawable.btn_5_light);
            this.img6.setImageResource(R.drawable.btn_6_light);
            this.img7.setImageResource(R.drawable.btn_7_light);
            this.img8.setImageResource(R.drawable.btn_8_light);
            this.img9.setImageResource(R.drawable.btn_9_light);
            this.img10.setImageResource(R.drawable.btn_star_light);
            this.img11.setImageResource(R.drawable.btn_0_light);
            this.img12.setImageResource(R.drawable.btn_hash_light);
            this.cacncel.setImageResource(R.drawable.cancel_light_btn);
        }
        this.cacncel.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                String obj = KeypadDataActivity.this.editText.getText().toString();
                int length = (obj == null || obj.isEmpty()) ? 0 : obj.length();
                Log.e("TAG", "onClick: " + length);
                if (length != 0) {
                    KeypadDataActivity.this.onClearClick(view);
                }
            }
        });
        this.cacncel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                KeypadDataActivity.this.editText.setText("");
                KeypadDataActivity.this.textView.setVisibility(4);
                Toast.makeText(KeypadDataActivity.this, "Text cleared!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        findViewById(R.id.enterbtn).setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                KeypadDataActivity keypadActivity = KeypadDataActivity.this;
                keypadActivity.makeDirectCall(keypadActivity.number);
            }
        });
        this.editText.addTextChangedListener(new TextWatcher() {
            @Override 
            public void afterTextChanged(Editable editable) {
            }

            @Override 
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override 
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (!KeypadDataActivity.this.editText.getText().equals("")) {
                    KeypadDataActivity keypadActivity = KeypadDataActivity.this;
                    keypadActivity.number = keypadActivity.editText.getText().toString();
                    KeypadDataActivity keypadActivity2 = KeypadDataActivity.this;
                    new ContactComparisonTask(keypadActivity2, keypadActivity2.textView).execute(KeypadDataActivity.this.number);
                    KeypadDataActivity.this.textView.setVisibility(0);
                }
            }
        });
        this.textView.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                if (KeypadDataActivity.this.textView.getText().equals("Add Number")) {
                    KeypadDataActivity keypadActivity = KeypadDataActivity.this;
                    keypadActivity.openAddContactScreen(keypadActivity.number);
                    return;
                }
                KeypadDataActivity keypadActivity2 = KeypadDataActivity.this;
                keypadActivity2.openContactDetails(keypadActivity2.textView.getText().toString());
            }
        });
    }

    public void openAddContactScreen(String str) {
        Intent intent = new Intent("android.intent.action.INSERT");
        intent.setType("vnd.android.cursor.dir/contact");
        intent.putExtra("phone", str);
        startActivity(intent);
    }

    public void onClearClick(View view) {
        if (this.number.length() > 0) {
            String str = this.number;
            String substring = str.substring(0, str.length() - 1);
            this.number = substring;
            this.editText.setText(substring);
            this.editText.setSelection(this.number.length());
            if (this.number.length() != 0) {
                return;
            }
            this.textView.setVisibility(4);
        }
    }

    @SuppressLint("Range")
    public void openContactDetails(String str) {
        Cursor query = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, new String[]{"_id"}, "display_name = ?", new String[]{str}, null);
        if (query != null) {
            if (query.moveToFirst()) {
                startActivity(new Intent("android.intent.action.VIEW", Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, query.getString(query.getColumnIndex("_id")))));
            }
            query.close();
        }
    }

    public void makeDirectCall(String str) {
        if (ContextCompat.checkSelfPermission(this, "android.permission.CALL_PHONE") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.CALL_PHONE"}, 1);
        } else {
            startDirectCall(str);
        }
    }

    private void startDirectCall(String str) {
        startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + str)));
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 1) {
            if (iArr.length > 0 && iArr[0] == 0) {
                makeDirectCall(this.number);
            } else {
                Toast.makeText(this, "Permission denied, cannot make direct calls.", 0).show();
            }
        }
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

    
    public class ContactComparisonTask extends AsyncTask<String, Void, HashSet<String>> {
        private Context context;
        private TextView textView;

        public ContactComparisonTask(Context context, TextView textView) {
            this.context = context;
            this.textView = textView;
        }

        @SuppressLint("Range")
        public HashSet<String> doInBackground(String... strArr) {
            Cursor query;
            Log.e("TAG", "doInBackground:************************************* ");
            String str = strArr[0];
            HashSet<String> hashSet = new HashSet<>();
            ContentResolver contentResolver = this.context.getContentResolver();
            String replaceAll = str.replaceAll("[^0-9]", "");
            Cursor query2 = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (query2 != null) {
                while (query2.moveToNext()) {
                    String string = query2.getString(query2.getColumnIndex("_id"));
                    if (query2.getInt(query2.getColumnIndex("has_phone_number")) > 0 && (query = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "contact_id = ?", new String[]{string}, null)) != null) {
                        while (query.moveToNext()) {
                            hashSet.add(query.getString(query.getColumnIndex("data1")).replaceAll("[^0-9]", ""));
                        }
                        query.close();
                    }
                }
                query2.close();
            }
            hashSet.contains(replaceAll);
            return hashSet;
        }

        public void onPostExecute(HashSet<String> hashSet) {
            String replaceAll = KeypadDataActivity.this.number.replaceAll("[^0-9]", "");
            Iterator<String> it = hashSet.iterator();
            while (it.hasNext()) {
                String next = it.next();
                String replaceAll2 = next.replaceAll("[^0-9]", "");
                if (replaceAll2.startsWith("91")) {
                    Log.e("TAG", "onPostExecute: ");
                    replaceAll2 = replaceAll2.substring(2);
                }
                if (replaceAll.equals(replaceAll2)) {
                    KeypadDataActivity keypadActivity = KeypadDataActivity.this;
                    String contactName = keypadActivity.getContactName(keypadActivity, next);
                    TextView textView = this.textView;
                    if (contactName == null) {
                        contactName = "Unknown Contact";
                    }
                    textView.setText(contactName);
                    return;
                }
            }
            this.textView.setText("Add Number");
        }
    }

    public String getContactName(Context context, String str) {
        String replaceAll = str.replaceAll("[^0-9]", "");
        Log.d("getContactName", "Standardized Input Number: " + replaceAll);
        this.contentResolver = context.getContentResolver();
        Cursor query = this.contentResolver.query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(replaceAll)), new String[]{"display_name"}, null, null, null);
        if (query != null && query.moveToFirst()) {
            @SuppressLint("Range") String string = query.getString(query.getColumnIndex("display_name"));
            query.close();
            Log.d("getContactName", "Contact Name: " + string);
            return string;
        }
        Log.d("getContactName", "Contact not found for number: " + str);
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAG", "findContactNa  : " + this.number);
        if (this.number != null) {
            new ContactComparisonTask(this, this.textView).execute(this.number);
        }
    }

    @Override 
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_1 :
                AddNumber("1");
                return;
            case R.id.button_10 :
                AddNumber("*");
                return;
            case R.id.button_11 :
                AddNumber("0");
                return;
            case R.id.button_12 :
                AddNumber("#");
                return;
            case R.id.button_2 :
                AddNumber("2");
                return;
            case R.id.button_3 :
                AddNumber("3");
                return;
            case R.id.button_4 :
                AddNumber("4");
                return;
            case R.id.button_5 :
                AddNumber("5");
                return;
            case R.id.button_6 :
                AddNumber("6");
                return;
            case R.id.button_7 :
                AddNumber("7");
                return;
            case R.id.button_8 :
                AddNumber("8");
                return;
            case R.id.button_9 :
                AddNumber("9");
                return;
            default:
                return;
        }
    }

    public void AddNumber(String str) {
        this.editText.append(str);
    }
}
