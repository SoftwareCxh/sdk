package com.learning.bdsdk.ad;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;

import com.baidu.mobads.AdSettings;
import com.baidu.mobads.InterstitialAd;
import com.baidu.mobads.InterstitialAdListener;
import com.learning.bdsdk.ad.listener.BDInterstitialListener;

public class BDInterstitial {
    private static InterstitialAd mInterAd;
    static Context ctx;

    public static void BDInterstitial(final Context context, String id, final BDInterstitialListener listener) {
        ctx = context;
        Log.e("InterstitialAd", "进入");
        if (mInterAd == null) {
            mInterAd = new InterstitialAd(context, id);
            mInterAd.setListener(new InterstitialAdListener() {
                @Override
                public void onAdReady() {
                    Log.e("InterstitialAd", "onAdReady");
                    if (mInterAd.isAdReady()) {
                        showAd();
                        listener.onAdReady();
                    }
                }

                @Override
                public void onAdPresent() {
                    listener.onAdPresent();

                }

                @Override
                public void onAdClick(InterstitialAd interstitialAd) {
                    listener.onAdClick(interstitialAd);
                }

                @Override
                public void onAdDismissed() {
                    listener.onAdDismissed();
                    Log.e("InterstitialAd", "dismisiss");

                }

                @Override
                public void onAdFailed(String s) {
                    Log.e("InterstitialAd", "onAdFailed:" + s);
                    listener.onAdFailed(s);

                }
            });
        }

        mInterAd.loadAd();
    }

    private static void showAd() {

        mInterAd.showAd((Activity) ctx);
    }
}
