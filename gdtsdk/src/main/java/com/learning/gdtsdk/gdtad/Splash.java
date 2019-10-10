package com.learning.gdtsdk.gdtad;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.learning.gdtsdk.R;
import com.learning.gdtsdk.gdtad.listener.GDTSplashListener;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;


public class Splash {
    private static final String SKIP_TEXT = "点击跳过 %d";
    private static SplashAD splashAD;
    private static long fetchSplashADTime = 0;
    public static  void SplashAd(Context context, String appid, String id, ViewGroup viewGroup, View d ,final GDTSplashListener listener){
        View view=((Activity)context).getLayoutInflater().inflate(R.layout.splash_layout,null);
        Log.e("splash",appid +"|"+id);
        FrameLayout frameLayout=view.findViewById(R.id.splash_container);
        final TextView skipView=view.findViewById(R.id.skip_view);
        fetchSplashAD((Activity) context, frameLayout, skipView, appid, id, new SplashADListener() {
            @Override
            public void onADDismissed() {
                listener.onADDismissed();
            }

            @Override
            public void onNoAD(AdError adError) {
                Log.e("error",adError.getErrorMsg());

                listener.onNoAD(adError);
            }

            @Override
            public void onADPresent() {
                listener.onADPresent();
            }

            @Override
            public void onADClicked() {
                listener.onADClicked();
            }

            @Override
            public void onADTick(long l) {

                skipView.setText(String.format(SKIP_TEXT, Math.round(l / 1000f)));
                listener.onADTick(l);
            }

            @Override
            public void onADExposure() {
                listener.onADExposure();

            }
        }, 0);
        viewGroup.addView(view);

    }
    private static void fetchSplashAD(Activity activity, ViewGroup adContainer, View skipContainer,
                               String appId, String posId, SplashADListener adListener, int fetchDelay) {
        Log.e("splash",appId+"|"+posId);
        fetchSplashADTime = System.currentTimeMillis();
        splashAD = new SplashAD(activity, skipContainer, appId, posId, adListener, fetchDelay);
        splashAD.fetchAndShowIn(adContainer);
    }


}
