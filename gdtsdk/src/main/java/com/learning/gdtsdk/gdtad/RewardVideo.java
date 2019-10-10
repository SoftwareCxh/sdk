package com.learning.gdtsdk.gdtad;

import android.content.Context;
import android.os.SystemClock;
import com.learning.gdtsdk.gdtad.listener.GDTRewardVideoListener;
import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.comm.util.AdError;


public class RewardVideo {
    private static RewardVideoAD rewardVideoAD;
    private static boolean adLoaded;//广告加载成功标志
    private static boolean videoCached;//视频素材文件下载完成标志
    private static GDTRewardVideoListener listener;

    public static void RewardVideoAd(Context context, String appid, String id, GDTRewardVideoListener rewardVideoListener){
        listener=rewardVideoListener;
        rewardVideoAD = new RewardVideoAD(context, appid, id, new RewardVideoADListener() {
            @Override
            public void onADLoad() {
                adLoaded = true;
                //String msg = "load ad success ! expireTime = " + new Date(System.currentTimeMillis() +
                //       rewardVideoAD.getExpireTimestamp() - SystemClock.elapsedRealtime());
                //Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                //Log.d(TAG, "eCPM = " + rewardVideoAD.getECPM() + " , eCPMLevel = " + rewardVideoAD.getECPMLevel());
                showAd();
                listener.onADLoad();
            }

            @Override
            public void onVideoCached() {
                listener.onVideoCached();

            }

            @Override
            public void onADShow() {
                listener.onADShow();

            }

            @Override
            public void onADExpose() {
                listener.onADExpose();
            }

            @Override
            public void onReward() {
                listener.onReward();
            }

            @Override
            public void onADClick() {
                listener.onADClick();
            }

            @Override
            public void onVideoComplete() {
                listener.onVideoComplete();
            }

            @Override
            public void onADClose() {
                listener.onADClose();
            }

            @Override
            public void onError(AdError adError) {
                listener.onError(adError);
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
                }else{
                    listener.onAdShowError("广告已过期");
                }
            }else{
                listener.onAdShowError("广告已经展示过");
            }
        }else{
            listener.onAdShowError("广告为空");
        }
    }
}
