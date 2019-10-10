package com.learning.sdk.gdtad;

import android.content.Context;
import android.os.SystemClock;

import com.learning.sdk.listener.RewardAdListener;
import com.learning.sdk.listener.RewardModel;
import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.comm.util.AdError;


public class GDTRewardVideo {
    private static RewardVideoAD rewardVideoAD;
    private static boolean adLoaded;//广告加载成功标志
    private static boolean videoCached;//视频素材文件下载完成标志
    private static RewardAdListener listener;

    public static void RewardVideoAd(Context context, String appid, String id, RewardAdListener rewardVideoListener){
        listener=rewardVideoListener;
        rewardVideoAD = new RewardVideoAD(context, appid, id, new RewardVideoADListener() {
            @Override
            public void onADLoad() {
                adLoaded = true;

                showAd();
            }

            @Override
            public void onVideoCached() {

            }

            @Override
            public void onADShow() {

            }

            @Override
            public void onADExpose() {

            }

            @Override
            public void onReward() {
                RewardModel rewardModel= new RewardModel();
                listener.onReward(rewardModel);
            }

            @Override
            public void onADClick() {
                listener.onAdClicked();
            }

            @Override
            public void onVideoComplete() {
                listener.onVideoPlayFinish();
            }

            @Override
            public void onADClose() {
                listener.onAdDismiss();
            }

            @Override
            public void onError(AdError adError) {
                listener.onAdLoadFail(adError.getErrorCode(),adError.getErrorMsg());
            }
        });
        adLoaded = false;
        videoCached = false;
        // 2. 加载激励视频广告
        rewardVideoAD.loadAD();
    }

    private static void showAd() {
        if(rewardVideoAD!=null){
            if(!rewardVideoAD.hasShown()){
                long delta = 1000;
                if (SystemClock.elapsedRealtime() < (rewardVideoAD.getExpireTimestamp() - delta)) {
                    rewardVideoAD.showAD();
                }
            }
        }
    }
}
