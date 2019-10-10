package com.learning.gdtsdk.gdtad;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.learning.gdtsdk.gdtad.listener.GDTBannerListener;
import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.comm.util.AdError;

/**
 * banner 2.0
 */
public class Banner {
    static ViewGroup bannerContainer;
    static UnifiedBannerView bv;
    static String posId;
    static Context ctx;

    public static void BannerAd(Context context, String appid, String id, ViewGroup viewGroup, int refresh, final GDTBannerListener gdtBannerListener) {
        ctx=context;
        bannerContainer=viewGroup;
        posId=id;
        if(bv != null){
            bannerContainer.removeView(bv);
            bv.destroy();
        }
        bv = new UnifiedBannerView((Activity)context, appid, posId, new UnifiedBannerADListener() {
            @Override
            public void onNoAD(AdError adError) {
                Log.e("Error",adError.getErrorMsg());
                gdtBannerListener.onNoAD(adError);
            }

            @Override
            public void onADReceive() {
                gdtBannerListener.onADReceive();

            }

            @Override
            public void onADExposure() {
                gdtBannerListener.onADExposure();

            }

            @Override
            public void onADClosed() {
                gdtBannerListener.onADClosed();

            }

            @Override
            public void onADClicked() {
                gdtBannerListener.onADClosed();

            }

            @Override
            public void onADLeftApplication() {
                gdtBannerListener.onADLeftApplication();

            }

            @Override
            public void onADOpenOverlay() {
                gdtBannerListener.onADOpenOverlay();

            }

            @Override
            public void onADCloseOverlay() {
                gdtBannerListener.onADCloseOverlay();

            }
        });
        bv.setRefresh(refresh);
        bannerContainer.addView(bv, getUnifiedBannerLayoutParams());
        bv.loadAD();
    }
    /**
     * banner2.0规定banner宽高比应该为6.4:1 , 开发者可自行设置符合规定宽高比的具体宽度和高度值
     *
     * @return
     */
    private static FrameLayout.LayoutParams getUnifiedBannerLayoutParams() {
        Point screenSize = new Point();
        ((Activity)ctx).getWindowManager().getDefaultDisplay().getSize(screenSize);
        return new FrameLayout.LayoutParams(screenSize.x,  Math.round(screenSize.x / 6.4F));
    }
}
