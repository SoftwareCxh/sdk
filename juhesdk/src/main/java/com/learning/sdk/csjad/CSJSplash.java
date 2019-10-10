package com.learning.sdk.csjad;

import android.content.Context;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.learning.sdk.TTAdManagerHolder;
import com.learning.sdk.listener.RMAdListener;

public class CSJSplash{

    /**
     *
     * 开屏广告
     */
    private static final int AD_TIME_OUT = 3000;


    /**
     * @param context
     * @param id
     * @return
     */


    public static void splashAd(Context context, String id,final ViewGroup mSplashContainer,final RMAdListener listener) {



        TTAdNative mTTAdNative = TTAdManagerHolder.get().createAdNative(context);

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(id)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1960)
                .build();

        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            public void onError(int i, String s) {
                Log.e("onError:", s);
                listener.onAdLoadFail(i,s);//setFlag(1);//出错
            }

            @Override
            public void onTimeout() {
                listener.onAdLoadFail(1001,"超时");
                Log.e("onTimeOut", "超时");

            }

            @Override
            public void onSplashAdLoad(TTSplashAd ad) {
                Log.d("onSplashAdLoad", "开屏广告请求成功");
                if (ad == null) {
                    //listener.onSplashAdLoadError("ad is empty");
                    listener.onAdLoadFail(1002,"广告为空");
                    //setFlag(3);
                    return;
                }
                View view = ad.getSplashView();
                if (view != null) {
                    mSplashContainer.removeAllViews();
                    //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕宽
                    mSplashContainer.addView(view);
                    //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                    //ad.setNotAllowSdkCountdown();
                } else {
                    //listener.onSplashAdLoadError("view is empty");
                    //setFlag(4);
                    return;
                }
                ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int i) {
                        Log.e("onAdClicked", "开屏广告点击");
                        listener.onAdClicked();
                    }

                    @Override
                    public void onAdShow(View view, int i) {
                        Log.e("onAdShow", "开屏广告展示");
                        listener.onAdLoadSuccess();
                    }

                    @Override
                    public void onAdSkip() {
                        Log.e("onAdSkip", "开屏广告跳过");
                        listener.onAdDismiss();
                    }

                    @Override
                    public void onAdTimeOver() {
                        Log.e("onAdTimeOver", "计时结束");
                        listener.onAdDismiss();

                    }
                });
                if (ad.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                    ad.setDownloadListener(new TTAppDownloadListener() {
                        boolean hasShow = false;

                        @Override
                        public void onIdle() {

                        }

                        @Override
                        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                            if (!hasShow) {
                                //showToast("下载中...");
                                hasShow = true;
                            }
                        }

                        @Override
                        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                            //showToast("下载暂停...");

                        }

                        @Override
                        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                            //showToast("下载失败...");

                        }

                        @Override
                        public void onDownloadFinished(long totalBytes, String fileName, String appName) {

                        }

                        @Override
                        public void onInstalled(String fileName, String appName) {

                        }
                    });
                }

            }
        }, AD_TIME_OUT);

    }
}
