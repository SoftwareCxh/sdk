package com.learning.sdk.gdtad;

import android.app.Activity;
import android.content.Context;

import com.learning.sdk.Constant;
import com.learning.sdk.listener.RMAdListener;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.comm.util.AdError;

public class GDTInterstitial {
    static UnifiedInterstitialAD iad;
    static String posId;

    public static void InterstitialAd(Context context, String id, final boolean flag, final RMAdListener listener) {
        if (iad == null || !posId.equals(id)) {
            posId = id;
            if (iad != null) {
                iad.close();
                iad.destroy();
                iad = null;
            }
            iad = new UnifiedInterstitialAD((Activity) context, Constant.GDTInterstitialId, posId, new UnifiedInterstitialADListener() {
                @Override
                public void onADReceive() {
                    listener.onAdLoadSuccess();
                    if (flag) {
                        iad.showAsPopupWindow();
                    } else {
                        iad.show();
                    }
                }

                @Override
                public void onNoAD(AdError adError) {
                    listener.onAdLoadFail(adError.getErrorCode(), adError.getErrorMsg());
                }

                @Override
                public void onADOpened() {
                }

                @Override
                public void onADExposure() {

                }

                @Override
                public void onADClicked() {
                    listener.onAdClicked();
                }

                @Override
                public void onADLeftApplication() {
                }

                @Override
                public void onADClosed() {
                    listener.onAdDismiss();
                }
            });
        }
        iad.loadAD();

    }


}
