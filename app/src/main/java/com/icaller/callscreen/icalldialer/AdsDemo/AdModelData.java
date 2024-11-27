package com.icaller.callscreen.icalldialer.AdsDemo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AdModelData {
    @SerializedName("Admob")
    @Expose
    private Admob admob;

    public Admob getAdmob() {
        return this.admob;
    }

    public void setAdmob(Admob admob) {
        this.admob = admob;
    }

    /* loaded from: classes.dex */
    public class Admob {
        public String getInterstitial_ad_count() {
            return interstitial_ad_count;
        }

        public void setInterstitial_ad_count(String interstitial_ad_count) {
            this.interstitial_ad_count = interstitial_ad_count;
        }

        @SerializedName("ad_type")
        @Expose
        private String Ads_type;
        @SerializedName("app_open_id")
        @Expose
        private String appOpenId;
        @SerializedName("banner_id")
        @Expose
        private String bannerId;
        @SerializedName("interstitial_id")
        @Expose
        private String interstitialId;
        @SerializedName("interstitial_ad_count")
        @Expose
        private String interstitial_ad_count;
        @SerializedName("native_id")
        @Expose
        private String nativeId;

        public String getAds_type() {
            return this.Ads_type;
        }

        public String getAppOpenId() {
            return this.appOpenId;
        }

        public String getBannerId() {
            return this.bannerId;
        }

        public String getInterstitialId() {
            return this.interstitialId;
        }

        public String getNativeId() {
            return this.nativeId;
        }

        public String getinterstitial_ad_count() {
            return this.interstitial_ad_count;
        }

        public void setAds_type(String str) {
            this.Ads_type = str;
        }

        public void setAppOpenId(String str) {
            this.appOpenId = str;
        }

        public void setBannerId(String str) {
            this.bannerId = str;
        }

        public void setInterstitialId(String str) {
            this.interstitialId = str;
        }

        public void setNativeId(String str) {
            this.nativeId = str;
        }

        public void setinterstitial_ad_count(String str) {
            this.interstitial_ad_count = str;
        }


    }
}
