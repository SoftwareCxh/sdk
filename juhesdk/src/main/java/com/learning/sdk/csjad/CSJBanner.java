package com.learning.sdk.csjad;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTBannerAd;
import com.learning.sdk.TTAdManagerHolder;
import com.learning.sdk.listener.RMAdListener;

public class CSJBanner {

    public static void BannerAd(Context context, String id, final ViewGroup mBannerContainer, final int time, int width, int height, final RMAdListener bannerListener) {

        TTAdNative mTTAdNative = TTAdManagerHolder.get().createAdNative(context);
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(id) //广告位id
                .setSupportDeepLink(true)
                .setImageAcceptedSize(width, height)
                .build();
        mTTAdNative.loadBannerAd(adSlot, new TTAdNative.BannerAdListener() {
            @Override
            public void onError(int i, String s) {
                Log.e("CSJBannerAd", "code: " + i + "  message: " + s);
                bannerListener.onAdLoadFail(i,s);

            }

            @Override
            public void onBannerAdLoad(TTBannerAd ad) {
                if(ad==null){
                    return;
                }
                View bannerView=ad.getBannerView();
                if(bannerView==null){
                    return;
                }
                //设置轮播的时间间隔  间隔在30s到120秒之间的值，不设置默认不轮播
                ad.setSlideIntervalTime(time);
                mBannerContainer.removeAllViews();
                mBannerContainer.addView(bannerView);
                //设置广告互动监听回调
                ad.setBannerInteractionListener(new TTBannerAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
                        Log.e("onAdClicked","广告被点击");
                        bannerListener.onAdClicked();

                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        Log.e("onAdShow","广告展示");
                    }
                });
                //在banner中显示网盟提供的dislike icon，有助于广告投放精准度提升
                ad.setShowDislikeIcon(new TTAdDislike.DislikeInteractionCallback() {
                    @Override
                    public void onSelected(int position, String value) {
                        //TToast.show(mContext, "点击 " + value);
                        //用户选择不喜欢原因后，移除广告展示
                        mBannerContainer.removeAllViews();
                    }

                    @Override
                    public void onCancel() {
                        //TToast.show(mContext, "点击取消 ");
                        bannerListener.onAdDismiss();
                    }
                });
            }
        });
    }
}
