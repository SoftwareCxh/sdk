package com.learning.sdk.bdad;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.baidu.mobads.InterstitialAd;
import com.baidu.mobads.InterstitialAdListener;
import com.learning.sdk.listener.RMAdListener;

public class BDInterstitial {
    private static InterstitialAd mInterAd;
    static Context ctx;
    static String adid;
    public static void BDInterstitial(final Context context, String id,final RMAdListener listener) {
        ctx = context;
        Log.e("InterstitialAd", "进入");
        if (mInterAd == null||!id.equals(adid)) {
            adid=id;
            mInterAd = new InterstitialAd(context, id);
            mInterAd.setListener(new InterstitialAdListener() {
                @Override
                public void onAdReady() {
                    Log.e("InterstitialAd", "onAdReady");
                    if (mInterAd.isAdReady()) {
                        showAd();
                        listener.onAdLoadSuccess();
                    }
                }

                @Override
                public void onAdPresent() {

                }

                @Override
                public void onAdClick(InterstitialAd interstitialAd) {
                    listener.onAdClicked();
                }

                @Override
                public void onAdDismissed() {
                    listener.onAdDismiss();
                    Log.e("InterstitialAd", "dismisiss");

                }

                @Override
                public void onAdFailed(String s) {
                    Log.e("InterstitialAd", "onAdFailed:" + s);
                    listener.onAdLoadFail(10001,s);

                }
            });
        }
        mInterAd.loadAd();
    }

    private static void showAd() {
        mInterAd.showAd((Activity) ctx);
    }
}
