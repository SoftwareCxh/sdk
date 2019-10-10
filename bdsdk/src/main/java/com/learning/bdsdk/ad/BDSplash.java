package com.learning.bdsdk.ad;

import android.content.Context;
import android.view.ViewGroup;

import com.baidu.mobads.SplashAd;
import com.baidu.mobads.SplashAdListener;
import com.learning.bdsdk.ad.listener.BDSplashListener;

public class BDSplash  {
    public static void  SplashAd(Context context, String id, ViewGroup viewGroup, final BDSplashListener listener){
        new SplashAd(context, viewGroup, new SplashAdListener() {
            @Override
            public void onAdPresent() {
                listener.onAdPresent();
            }

            @Override
            public void onAdDismissed() {
                listener.onAdDismissed();
            }

            @Override
            public void onAdFailed(String s) {
                listener.onAdFailed(s);
            }

            @Override
            public void onAdClick() {
                listener.onAdClick();

            }
        }, id, true);
    }
}
