package com.icaller.callscreen.icalldialer.Model;


public class ContactFavList {
    private long id;
    private String name;
    private String phoneNumber;

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

    public ContactFavList(String str, String str2, long j) {
        this.name = str;
        this.phoneNumber = str2;
        this.id = j;
    }
}
