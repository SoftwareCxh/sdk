package com.learning.gdtsdk.gdtad;

import android.app.Activity;
import android.content.Context;

import com.learning.gdtsdk.gdtad.listener.GDTInterstitialListener;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.comm.util.AdError;

public class Interstitial {
    static UnifiedInterstitialAD iad;
    static String posId;

    public static void InterstitialAd(Context context, String appid, String id, final boolean flag, final GDTInterstitialListener listener) {
        if (iad == null || !posId.equals(id)){
            posId=id;
            if (iad != null) {
                iad.close();
                iad.destroy();
                iad = null;
            }
            iad = new UnifiedInterstitialAD((Activity) context, appid, posId, new UnifiedInterstitialADListener() {
                @Override
                public void onADReceive() {
                    listener.onADReceive();
                    if(flag){
                        iad.showAsPopupWindow();
                    }else{
                        iad.show();
                    }

                }

                @Override
                public void onNoAD(AdError adError) {
                    listener.onNoAD(adError);
                }

                @Override
                public void onADOpened() {
                    listener.onADOpened();
                }

                @Override
                public void onADExposure() {
                    listener.onADExposure();
                }

                @Override
                public void onADClicked() {
                    listener.onADClicked();

                }

                @Override
                public void onADLeftApplication() {
                    listener.onADLeftApplication();

                }

                @Override
                public void onADClosed() {
                    listener.onADClosed();

                }
            });
        }
        iad.loadAD();

    }


}
