package com.icaller.callscreen.icalldialer.Model;

import android.icu.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;


public class CallLogDataItem {
    private int callType;
    private int count;
    Date date;
    private String name;
    private String number;

    public String getCallType() {
        int i = this.callType;
        return i != 1 ? i != 2 ? i != 3 ? "Unknown" : "Missed" : "Outgoing" : "Incoming";
    }

    public int getCount() {
        return this.count;
    }

    public String getName() {
        return this.name;
    }

    public String getNumber() {
        return this.number;
    }

    public CallLogDataItem(String str, String str2, Date date, int i, int i2) {
        this.number = str2;
        this.date = date;
        this.callType = i;
        this.name = str;
        this.count = i2;
    }

    public String getFormattedDate() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(this.date);
    }
}
