package com.learning.sdk.csjad;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTInteractionAd;
import com.learning.sdk.TTAdManagerHolder;
import com.learning.sdk.listener.RMAdListener;

public class CSJInteraction {
    public static void InteractionAd(final Context context, String id, int width, int height, final RMAdListener listener) {

        TTAdNative mTTAdNative = TTAdManagerHolder.get().createAdNative(context);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(id)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(width, height) //根据广告平台选择的尺寸，传入同比例尺寸
                .build();

        //step5:请求广告，调用插屏广告异步请求接口
        mTTAdNative.loadInteractionAd(adSlot, new TTAdNative.InteractionAdListener() {
            @Override
            public void onError(int code, String message) {
                //  TToast.show(getApplicationContext(), "code: " + code + "  message: " + message);
                Log.e("onInteractionAdLoad", "code: " + code + "  message: " + message);
                listener.onAdLoadFail(code,message);
            }

            @Override
            public void onInteractionAdLoad(TTInteractionAd ttInteractionAd) {
                ttInteractionAd.setAdInteractionListener(new TTInteractionAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked() {
//                        Log.d(TAG, "被点击");
//                        TToast.show(mContext, "广告被点击");
                        Log.e("onInteractionAdLoad","广告被点击");
                        listener.onAdClicked();
                    }

                    @Override
                    public void onAdShow() {
//                        Log.d(TAG, "被展示");
//                        TToast.show(mContext, "广告被展示");
                        Log.e("onInteractionAdLoad","广告被展示" );
                    }

                    @Override
                    public void onAdDismiss() {
//                        Log.d(TAG, "插屏广告消失");
//                        TToast.show(mContext, "广告消失");

                        Log.e("onInteractionAdLoad","广告消失");
                        listener.onAdDismiss();
                    }
                });
                //如果是下载类型的广告，可以注册下载状态回调监听
                if (ttInteractionAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                    ttInteractionAd.setDownloadListener(new TTAppDownloadListener() {
                        @Override
                        public void onIdle() {
                            // Log.d(TAG, "点击开始下载");
                            //TToast.show(mContext, "点击开始下载");
                            Log.e("onInteractionAdLoad","点击开始下载");

                        }

                        @Override
                        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                            // Log.d(TAG, "下载中");
                            // TToast.show(mContext, "下载中");
                            Log.e("onInteractionAdLoad","下载中");

                        }

                        @Override
                        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                            // Log.d(TAG, "下载暂停");
                            //TToast.show(mContext, "下载暂停");
                            Log.e("onInteractionAdLoad","下载暂停");

                        }

                        @Override
                        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                            //Log.d(TAG, "下载失败");
//                            TToast.show(mContext, "下载失败");
                            Log.e("onInteractionAdLoad","下载失败");

                        }

                        @Override
                        public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                            //Log.d(TAG, "下载完成");
                            Log.e("onInteractionAdLoad","下载完成");
                        }

                        @Override
                        public void onInstalled(String fileName, String appName) {
                            //Log.d(TAG, "安装完成");
                            // TToast.show(mContext, "安装完成");
                            Log.e("onInteractionAdLoad","安装完成");
                        }
                    });
                }
                //弹出插屏广告
                ttInteractionAd.showInteractionAd((Activity)context);
            }
        });


    }
}
