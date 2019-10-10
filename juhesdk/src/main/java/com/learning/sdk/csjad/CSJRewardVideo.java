package com.learning.sdk.csjad;

import android.app.Activity;
import android.content.Context;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.learning.sdk.TTAdManagerHolder;
import com.learning.sdk.listener.RewardAdListener;
import com.learning.sdk.listener.RewardModel;

public class CSJRewardVideo {
    private static TTRewardVideoAd mttRewardVideoAd;

    private static boolean mHasShowDownloadActive = false;
    public static  void RewardVideoAd(final Context context, String id, final RewardAdListener listener, int orientation, String userid, String type, int num){
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        TTAdNative mTTAdNative = ttAdManager.createAdNative(context);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(id)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setRewardName(type) //奖励的名称
                .setRewardAmount(num)  //奖励的数量
                .setUserID(userid)//用户id,必传参数
                .setMediaExtra("media_extra") //附加参数，可选
                .setOrientation(orientation) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();

        //step5:请求广告
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                //TToast.show(RewardVideoActivity.this, message);
                listener.onAdLoadFail(code,message);
            }

            //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
               // TToast.show(RewardVideoActivity.this, "rewardVideoAd video cached");
            }

            //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
                //TToast.show(RewardVideoActivity.this, "rewardVideoAd loaded");
                mttRewardVideoAd = ad;
                mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

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

                    //视频播放完成回调
                    @Override
                    public void onVideoComplete() {
                        listener.onVideoPlayFinish();
                    }

                    @Override
                    public void onVideoError() {
                        listener.onAdLoadFail(0,"");
                    }

                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                    @Override
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
                        if(rewardVerify){
                            RewardModel rewardModel=new RewardModel();
                            rewardModel.amount=rewardAmount;
                            rewardModel.name=rewardName;

                            listener.onReward(rewardModel);
                        }

                    }

                    @Override
                    public void onSkippedVideo() {
                    }

                });
                mttRewardVideoAd.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        mHasShowDownloadActive = false;
                    }

                    @Override
                    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                        if (!mHasShowDownloadActive) {
                            mHasShowDownloadActive = true;
                        }
                    }

                    @Override
                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                    }

                    @Override
                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                    }

                    @Override
                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                    }
                });
                mttRewardVideoAd.showRewardVideoAd((Activity)context, TTAdConstant.RitScenes.CUSTOMIZE_SCENES,"scenes_test");
                mttRewardVideoAd = null;
            }
        });


    }
}
