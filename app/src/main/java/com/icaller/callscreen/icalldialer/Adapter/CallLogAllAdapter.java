package com.icaller.callscreen.icalldialer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.icaller.callscreen.icalldialer.R;
import com.icaller.callscreen.icalldialer.Model.CallLogDataItem;
import com.icaller.callscreen.icalldialer.Utils.FavoriteContactRemoverItemTask;
import com.google.i18n.phonenumbers.PhoneNumberUtil;


import java.util.List;
import java.util.Locale;


public class CallLogAllAdapter extends RecyclerView.Adapter<CallLogAllAdapter.ViewHolder> {
    public List<CallLogDataItem> callLogs;
    Context context;
    boolean result;

    public CallLogAllAdapter(List<CallLogDataItem> list, Context context) {
        this.callLogs = list;
        this.context = context;
    }

    @Override 
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_call_log_all, viewGroup, false));
    }

    @SuppressLint("RestrictedApi")
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView")  int i) {
         CallLogDataItem callLogItem = this.callLogs.get(i);
        viewHolder.dateTextView.setText(callLogItem.getFormattedDate());
        viewHolder.typeTextView.setText(getCountryNameFromNumber(callLogItem.getNumber()));
        Context context = this.context;
        boolean z = context.getSharedPreferences(context.getPackageName(), 0).getBoolean("dark_theme", false);
        Log.e("TAG", "onBindViewHolder **: " + this.result);
        if (z) {
            viewHolder.line.setBackgroundResource(R.color.darktheme);
            this.context.setTheme(R.style.AppThemeContactContact1);
        } else {
            viewHolder.line.setBackgroundResource(R.color.lightline);
            this.context.setTheme(R.style.AppThemeContactContact);
        }
        if (callLogItem.getName().equals("")) {
            viewHolder.numberTextView.setText(callLogItem.getNumber());
        } else {
            viewHolder.numberTextView.setText(callLogItem.getName());
        }
        if (callLogItem.getCallType().equals("Missed")) {
            viewHolder.numberTextView.setTextColor(SupportMenu.CATEGORY_MASK);
        } else if (z) {
            viewHolder.numberTextView.setTextColor(-1);
            this.context.setTheme(R.style.AppThemeContactContact1);
        } else {
            viewHolder.numberTextView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            this.context.setTheme(R.style.AppThemeContactContact);
        }
        viewHolder.info.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                if (!callLogItem.getName().equals("")) {
                    CallLogAllAdapter.this.openContactDetails(callLogItem.getName());
                } else {
                    Toast.makeText(CallLogAllAdapter.this.context, "No Details because Number not Save", Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                CallLogAllAdapter.this.removeItem(i);
                FavoriteContactRemoverItemTask.removeRecentCallLogs(CallLogAllAdapter.this.context.getContentResolver(), callLogItem.getNumber());
                CallLogAllAdapter.this.notifyDataSetChanged();
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                CallLogAllAdapter.this.makeDirectCall(callLogItem.getNumber());
            }
        });
    }

    public void removeItem(int i) {
        this.callLogs.remove(i);
        notifyItemRemoved(i);
    }

    public void makeDirectCall(String str) {
        if (ContextCompat.checkSelfPermission(this.context, "android.permission.CALL_PHONE") != 0) {
            Toast.makeText(this.context, "Please Permission Allow", Toast.LENGTH_SHORT).show();
        } else {
            startDirectCall(str);
        }
    }

    private void startDirectCall(String str) {
        this.context.startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + str)));
    }

    @Override 
    public int getItemCount() {
        return this.callLogs.size();
    }

    
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        ImageView delete;
        ImageView info;
        View line;
        TextView numberTextView;
        TextView typeTextView;

        ViewHolder(View view) {
            super(view);
            this.numberTextView = (TextView) view.findViewById(R.id.number_text_view);
            this.dateTextView = (TextView) view.findViewById(R.id.date_text_view);
            this.typeTextView = (TextView) view.findViewById(R.id.type_text_view);
            this.info = (ImageView) view.findViewById(R.id.info);
            this.delete = (ImageView) view.findViewById(R.id.delteicon);
            this.line = view.findViewById(R.id.line);
        }
    }

    public static String getCountryNameFromNumber(String str) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            return new Locale("", phoneNumberUtil.getRegionCodeForNumber(phoneNumberUtil.parse(str, ""))).getDisplayCountry();
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    @SuppressLint("Range")
    public void openContactDetails(String str) {
        Cursor query = this.context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, new String[]{"_id"}, "display_name = ?", new String[]{str}, null);
        if (query != null) {
            if (query.moveToFirst()) {
                this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, query.getString(query.getColumnIndex("_id")))));
            }
            query.close();
        }
    }
}
