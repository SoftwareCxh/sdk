package com.learning.sdk.bdad;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
import com.learning.sdk.listener.RMAdListener;

import org.json.JSONObject;

public class BDBanner {

    public static void BannerAd(Context context , String id, ViewGroup viewGroup, int scaleWidth, int scaleHeight, final RMAdListener listener){
        AdView adView = new AdView(context, id);
        viewGroup.removeAllViews();
        adView.setListener(new AdViewListener() {
            @Override
            public void onAdReady(AdView adView) {
                listener.onAdLoadSuccess();
            }

            @Override
            public void onAdShow(JSONObject jsonObject) { }

            @Override
            public void onAdClick(JSONObject jsonObject) {
                listener.onAdClicked();
            }

            @Override
            public void onAdFailed(String s) {
                Log.e("BDError",s);
                listener.onAdLoadFail(0,s);
            }

            @Override
            public void onAdSwitch() {

            }

            @Override
            public void onAdClose(JSONObject jsonObject) {
                listener.onAdDismiss();
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        int winW = dm.widthPixels;
        int winH = dm.heightPixels;
        int width = Math.min(winW, winH);
        int height = width * scaleHeight / scaleWidth;
        // 将adView添加到父控件中(注：该父控件不一定为您的根控件，只要该控件能通过addView能添加广告视图即可)
        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(width, height);
        rllp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        viewGroup.addView(adView, rllp);

    }
}
