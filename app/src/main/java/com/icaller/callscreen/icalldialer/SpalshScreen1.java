package com.icaller.callscreen.icalldialer;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;


import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.icaller.callscreen.icalldialer.AdsDemo.AdModelData;
import com.icaller.callscreen.icalldialer.AdsDemo.AdsPlacement;
import com.icaller.callscreen.icalldialer.AdsDemo.GlobalsClass;
import com.icaller.callscreen.icalldialer.AdsDemo.UtilsAPp;

import java.util.concurrent.atomic.AtomicBoolean;

public class SpalshScreen1 extends AppCompatActivity {

    LinearLayout linearLayout;
    private ConsentInformation consentInformation;
    private  AtomicBoolean isMobileAdsInitializeCalled = new AtomicBoolean(false);
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_spalsh_screen);

        linearLayout = findViewById(R.id.layout);
        if (getSharedPreferences(getPackageName(), 0).getBoolean("dark_theme", false)) {
            linearLayout.setBackgroundColor(getResources().getColor(R.color.darktheme));
        } else {
            linearLayout.setBackgroundColor(getResources().getColor(R.color.white));
        }

        ConsentRequestParameters build = new ConsentRequestParameters.Builder().build();
        ConsentInformation consentInformation = UserMessagingPlatform.getConsentInformation(this);
        this.consentInformation = consentInformation;
        consentInformation.requestConsentInfoUpdate(this, build, new ConsentInformation.OnConsentInfoUpdateSuccessListener() { 
            @Override 
            public  void onConsentInfoUpdateSuccess() {
                SpalshScreen1.this.m150lambda$onCreate$1$comicalldialerioscallscreenSpalshScreen();
            }
        }, new ConsentInformation.OnConsentInfoUpdateFailureListener() { 
            @Override 
            public  void onConsentInfoUpdateFailure(FormError formError) {
                SpalshScreen1.lambda$onCreate$2(formError);
            }
        });
        if (this.consentInformation.canRequestAds()) {
            initializeMobileAdsSdk();
        }
    }

    static void lambda$initializeMobileAdsSdk$3(InitializationStatus initializationStatus) {
    }

    static void lambda$onCreate$2(FormError formError) {
    }
    void m150lambda$onCreate$1$comicalldialerioscallscreenSpalshScreen() {
        UserMessagingPlatform.loadAndShowConsentFormIfRequired(this, new ConsentForm.OnConsentFormDismissedListener() { 
            @Override 
            public  void onConsentFormDismissed(FormError formError) {
                SpalshScreen1.this.m149lambda$onCreate$0$comicalldialerioscallscreenSpalshScreen(formError);
            }
        });
    }

    
    void m149lambda$onCreate$0$comicalldialerioscallscreenSpalshScreen(FormError formError) {
        if (this.consentInformation.canRequestAds()) {
            initializeMobileAdsSdk();
        }
    }

    private void initializeMobileAdsSdk() {
        if (this.isMobileAdsInitializeCalled.getAndSet(true)) {
            return;
        }
        callJsonFromUrl();
        MobileAds.initialize(this, new OnInitializationCompleteListener() { 
            @Override
            public  void onInitializationComplete(InitializationStatus initializationStatus) {
                SpalshScreen1.lambda$initializeMobileAdsSdk$3(initializationStatus);
            }
        });
    }

    private void callJsonFromUrl() {
        if (UtilsAPp.isOnline(this).booleanValue()) {


            DatabaseReference reference = database.getReference("icontactAds");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    AdModelData adModel = new AdModelData();
                    AdModelData.Admob admob = adModel.new Admob();
                    admob.setAds_type(snapshot.child("AdsType").getValue(String.class));
                    admob.setAppOpenId(snapshot.child("AppOpenId").getValue(String.class));
                    admob.setBannerId(snapshot.child("banneid").getValue(String.class));
                    admob.setInterstitialId(snapshot.child("inter").getValue(String.class));
                    admob.setNativeId(snapshot.child("NAtivId").getValue(String.class));
                    admob.setinterstitial_ad_count(snapshot.child("InterAdCount").getValue(String.class));
                    adModel.setAdmob(admob);
                    AdsPlacement.setAdModelData(SpalshScreen1.this, adModel);
                    AdsPlacement.getInstance(SpalshScreen1.this).preloadAd();
                    SpalshScreen1 spalshScreen = SpalshScreen1.this;
                    new AppOpenManager(spalshScreen, AdsPlacement.getInstance(spalshScreen).GetAppOpenId()).fetchAd();
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SpalshScreen1.this, "No Data Change...", Toast.LENGTH_SHORT).show();
                }
            });



        }
        else {
            Toast.makeText(this, "No Internet Connection...", Toast.LENGTH_SHORT).show();
        }

    }

    public void nextscreen() {
        if (GlobalsClass.CheckNet(this).booleanValue()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        Toast.makeText(this, "Please enable internet connection to further proceed!", Toast.LENGTH_SHORT).show();
    }

    public class AppOpenManager implements LifecycleObserver, Application.ActivityLifecycleCallbacks {
        String Appopenid;
        private Activity currentActivity;
        SharedPreferences.Editor editor2;
        private AppOpenAd.AppOpenAdLoadCallback loadCallback;
        SharedPreferences preferences2;
        SpalshScreen1 spleshActivity;
        private boolean isShowingAd = false;
        private AppOpenAd appOpenAd = null;

        public boolean isAdAvailable() {
            return this.appOpenAd != null;
        }

        @Override 
        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        @Override 
        public void onActivityDestroyed(Activity activity) {
            this.currentActivity = null;
        }

        @Override 
        public void onActivityPaused(Activity activity) {
        }

        @Override 
        public void onActivityResumed(Activity activity) {
            this.currentActivity = activity;
        }

        @Override 
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        @Override 
        public void onActivityStarted(Activity activity) {
            this.currentActivity = activity;
        }

        @Override 
        public void onActivityStopped(Activity activity) {
        }

        public AppOpenManager(SpalshScreen1 spalshScreen, String str) {
            this.spleshActivity = spalshScreen;
            this.Appopenid = str;
            SharedPreferences sharedPreferences = spalshScreen.getSharedPreferences("data", 0);
            this.preferences2 = sharedPreferences;
            this.editor2 = sharedPreferences.edit();
        }

        public void fetchAd() {
            if (isAdAvailable()) {
                return;
            }
            this.loadCallback = new AppOpenAd.AppOpenAdLoadCallback() { 
                public void onAdLoaded(AppOpenAd appOpenAd) {
                    AppOpenManager.this.appOpenAd = appOpenAd;
                    AppOpenManager.this.showAdIfAvailable();
                }

                @Override 
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    SpalshScreen1.this.nextscreen();
                }
            };
            AppOpenAd.load(SpalshScreen1.this, this.Appopenid, getAdRequest(), 1, this.loadCallback);
        }

        private AdRequest getAdRequest() {
            return new AdRequest.Builder().build();
        }

        public void showAdIfAvailable() {
            if (this.isShowingAd || !isAdAvailable()) {
                return;
            }
            this.appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    AppOpenManager.this.appOpenAd = null;
                    AppOpenManager.this.isShowingAd = false;
                    SpalshScreen1.this.nextscreen();
                }

                @Override 
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    SpalshScreen1.this.nextscreen();
                }

                @Override 
                public void onAdShowedFullScreenContent() {
                    AppOpenManager.this.isShowingAd = true;
                }
            });
            this.appOpenAd.show(this.currentActivity);
        }
    }
}