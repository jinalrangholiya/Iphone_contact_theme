package com.icaller.callscreen.icalldialer.AdsDemo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.gson.Gson;

import java.util.Arrays;

public class AdsPlacement {
    public static Activity activity;
    public static int interCheck;
    public static AdsPlacement mInstance;
    public String ADs_Type;
    public String AppOpen_Ad_ID;
    public String Banner_Ad_ID;
    public String Interstitial_Ad_Count;
    public String Interstitial_Ad_ID;
    InterstitialAd googleInterstitialAd;
    public int intervalue;
    MyCallback myCallback;

    /* loaded from: classes.dex */
    public interface MyCallback {
        void callbackCall();
    }

    public String GetAppOpenId() {
        return this.AppOpen_Ad_ID;
    }

    public AdsPlacement(AdModelData adModel) {
        this.intervalue = 0;
        this.ADs_Type = adModel.getAdmob().getAds_type();
        this.Banner_Ad_ID = adModel.getAdmob().getBannerId();
        this.Interstitial_Ad_ID = adModel.getAdmob().getInterstitialId();
        this.AppOpen_Ad_ID = adModel.getAdmob().getAppOpenId();
        String str = adModel.getAdmob().getinterstitial_ad_count();
        this.Interstitial_Ad_Count = str;
        this.intervalue = Integer.parseInt(str);
    }

    public static AdsPlacement getInstance(Activity activity2) {
        activity = activity2;
        if (mInstance == null) {
            mInstance = new AdsPlacement(getAdModelData(activity2));
        }
        return mInstance;
    }

    public static void setAdModelData(Context context, AdModelData adModel) {
        SharedPreferences.Editor edit = context.getSharedPreferences("USER_PREF", 0).edit();
        edit.putString("AdModel", new Gson().toJson(adModel));
        edit.apply();
    }

    public static AdModelData getAdModelData(Context context) {
        return (AdModelData) new Gson().fromJson(context.getSharedPreferences("USER_PREF", 0).getString("AdModel", ""), AdModelData.class);
    }

    public void preloadAd() {
        MobileAds.initialize(activity, new OnInitializationCompleteListener() {
            @Override 
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("5FD576584D54311841FDBB72010E20C5"));
        Load_Interstitial_AD();
    }

    public void showGoogleBannerAd( ViewGroup viewGroup) {
         AdView adView = new AdView(activity);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(this.Banner_Ad_ID);
        adView.loadAd(new AdRequest.Builder().build());
        adView.setAdListener(new AdListener() { 
            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override 
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                viewGroup.setVisibility(View.GONE);

            }

            @Override 
            public void onAdImpression() {
                super.onAdImpression();
            }

            @Override 
            public void onAdLoaded() {
                viewGroup.removeAllViews();
                viewGroup.addView(adView);
            }

            @Override 
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override 
            public void onAdSwipeGestureClicked() {
                super.onAdSwipeGestureClicked();
            }
        });
    }

    public void Load_Interstitial_AD() {
        InterstitialAd.load(activity, this.Interstitial_Ad_ID, new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
            public void onAdLoaded(InterstitialAd interstitialAd) {
                AdsPlacement.this.googleInterstitialAd = interstitialAd;
                AdsPlacement.this.googleInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override 
                    public void onAdClicked() {
                    }

                    @Override
                    public void onAdImpression() {
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        AdsPlacement.this.interstitialCallBack();
                        AdsPlacement.this.Load_Interstitial_AD();
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                AdsPlacement.this.interstitialCallBack();
            }
        });
    }

    public void showFullGoogleScreen(MyCallback myCallback) {
        if (myCallback == null) {
            interstitialCallBack();
            Load_Interstitial_AD();
            return;
        }
        this.myCallback = myCallback;
        InterstitialAd interstitialAd = this.googleInterstitialAd;
        if (interstitialAd != null) {
            interstitialAd.show(activity);
        } else {
            interstitialCallBack();
        }
    }

    public void interstitialCallBack() {
        MyCallback myCallback = this.myCallback;
        if (myCallback != null) {
            myCallback.callbackCall();
            this.myCallback = null;
        }
    }

    public void Show_Banner_AD(ViewGroup viewGroup) {
        showGoogleBannerAd(viewGroup);
    }

    public void Show_Interstitial_AD(MyCallback myCallback) {
        int i = interCheck + 1;
        interCheck = i;
        if (this.intervalue == i) {
            interCheck = 0;
            showFullGoogleScreen(myCallback);
        } else if (myCallback == null) {
        } else {
            this.myCallback = myCallback;
            interstitialCallBack();
        }
    }




}
