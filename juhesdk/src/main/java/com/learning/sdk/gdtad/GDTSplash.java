package com.learning.sdk.gdtad;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.learning.sdk.Constant;
import com.learning.sdk.R;
import com.learning.sdk.listener.RMAdListener;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;


public class GDTSplash {
    private static SplashAD splashAD;
    private static long fetchSplashADTime = 0;
    private static final String SKIP_TEXT = "点击跳过 %d";
    public static  void SplashAd(Context context, String id, ViewGroup viewGroup, final RMAdListener listener){
        View view=((Activity)context).getLayoutInflater().inflate(R.layout.splash_layout,null);
        FrameLayout frameLayout=view.findViewById(R.id.splash_container);
        final TextView textView=view.findViewById(R.id.skip_view);
        viewGroup.addView(view);
        fetchSplashAD((Activity) context, frameLayout,textView, Constant.GDTAppId, id, new SplashADListener() {
            @Override
            public void onADDismissed() {
                listener.onAdDismiss();
            }

            @Override
            public void onNoAD(AdError adError) {
                listener.onAdLoadFail(adError.getErrorCode(),adError.getErrorMsg());
            }

            @Override
            public void onADPresent() {
            }

            @Override
            public void onADClicked() {
                listener.onAdClicked();
            }

            @Override
            public void onADTick(long l) {
                textView.setText(String.format(SKIP_TEXT, Math.round(l / 1000f)));

            }

            @Override
            public void onADExposure() {

            }
        }, 0);

    }
    private static void fetchSplashAD(Activity activity, ViewGroup adContainer, View skipContainer,
                               String appId, String posId, SplashADListener adListener, int fetchDelay) {
        fetchSplashADTime = System.currentTimeMillis();
        //Log.e(appId+"",posId+"");
        splashAD = new SplashAD(activity, skipContainer, appId, posId, adListener, fetchDelay);
        splashAD.fetchAndShowIn(adContainer);
    }


}
