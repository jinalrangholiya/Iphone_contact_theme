package com.icaller.callscreen.icalldialer.Model;

import android.os.Parcel;
import android.os.Parcelable;


public class ContactItemList implements Parcelable {
    public static  Creator<ContactItemList> CREATOR = new Creator<ContactItemList>() { 
        @Override 
        public ContactItemList createFromParcel(Parcel parcel) {
            return new ContactItemList(parcel);
        }

        @Override 
        public ContactItemList[] newArray(int i) {
            return new ContactItemList[i];
        }
    };
    private long id;
    private String name;
    private String phoneNumber;

    @Override 
    public int describeContents() {
        return 0;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setId(long j) {
        this.id = j;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setPhoneNumber(String str) {
        this.phoneNumber = str;
    }

    public ContactItemList(String str, String str2, long j) {
        this.name = str;
        this.phoneNumber = str2;
        this.id = j;
    }

    public ContactItemList() {
    }

    protected ContactItemList(Parcel parcel) {
        this.name = parcel.readString();
        this.phoneNumber = parcel.readString();
        this.id = parcel.readLong();
    }

    @Override 
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.phoneNumber);
        parcel.writeLong(this.id);
    }
}
