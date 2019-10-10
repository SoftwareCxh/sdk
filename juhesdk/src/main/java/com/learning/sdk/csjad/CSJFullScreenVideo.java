package com.learning.sdk.csjad;

import android.app.Activity;
import android.content.Context;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.learning.sdk.TTAdManagerHolder;
import com.learning.sdk.listener.RMAdListener;

public class CSJFullScreenVideo {

    static TTFullScreenVideoAd mttFullVideoAd;

    public static void FullScreenVideoAd(final Context context, String id, final RMAdListener listener, int orientation) {
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        TTAdNative mTTAdNative = ttAdManager.createAdNative(context);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(id)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setOrientation(orientation)//必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();

        mTTAdNative.loadFullScreenVideoAd(adSlot, new TTAdNative.FullScreenVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                listener.onAdLoadFail(code, message);

            }

            @Override
            public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ad) {
                mttFullVideoAd = ad;
                mttFullVideoAd.setFullScreenVideoAdInteractionListener(new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {

                    @Override
                    public void onAdShow() {
                    }

                    @Override
                    public void onAdVideoBarClick() {
                    }

                    @Override
                    public void onAdClose() {
                        listener.onAdDismiss();
                    }

                    @Override
                    public void onVideoComplete() {

                    }

                    @Override
                    public void onSkippedVideo() {
                    }

                });
                mttFullVideoAd.showFullScreenVideoAd((Activity) context, TTAdConstant.RitScenes.GAME_GIFT_BONUS, null);
                mttFullVideoAd = null;
            }

            @Override
            public void onFullScreenVideoCached() { }
        });
    }
}
