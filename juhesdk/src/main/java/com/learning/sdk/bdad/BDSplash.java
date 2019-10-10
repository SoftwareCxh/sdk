package com.learning.sdk.bdad;

import android.content.Context;
import android.view.ViewGroup;

import com.baidu.mobads.SplashAd;
import com.baidu.mobads.SplashAdListener;
import com.learning.sdk.listener.RMAdListener;

public class BDSplash  {
    public static void  SplashAd(Context context, String id, ViewGroup viewGroup, final RMAdListener listener){
        new SplashAd(context, viewGroup, new SplashAdListener() {
            @Override
            public void onAdPresent() {
                listener.onAdLoadSuccess();
            }
            @Override
            public void onAdDismissed() {
                listener.onAdDismiss();
            }
            @Override
            public void onAdFailed(String s) {
                listener.onAdLoadFail(0,s);
            }
            @Override
            public void onAdClick() {
                listener.onAdClicked();
            }
        }, id, true);
    }
}
