package com.learning.bdsdk.ad;

import android.content.Context;

import com.baidu.mobads.rewardvideo.RewardVideoAd;
import com.learning.bdsdk.ad.listener.BDRewardVideoListener;

public class BDRewardVideo {
    static RewardVideoAd mRewardVideoAd;

    public static void RewardVideo(Context context, String id, final BDRewardVideoListener rewardVideoListener) {

        mRewardVideoAd = new RewardVideoAd(context, id, new RewardVideoAd.RewardVideoAdListener() {
            @Override
            public void onAdShow() {
                rewardVideoListener.onAdShow();
            }

            @Override
            public void onAdClick() {
                rewardVideoListener.onAdClick();
            }

            @Override
            public void onAdClose(float v) {
                rewardVideoListener.onAdClose(v);
            }

            @Override
            public void onAdFailed(String s) {
                rewardVideoListener.onAdFailed(s);
            }

            @Override
            public void onVideoDownloadSuccess() {
                rewardVideoListener.onVideoDownloadSuccess();
                mRewardVideoAd.show();
            }

            @Override
            public void onVideoDownloadFailed() {
                rewardVideoListener.onVideoDownloadFailed();

            }

            @Override
            public void playCompletion() {
                rewardVideoListener.playCompletion();
            }
        }, false);
        mRewardVideoAd.load();
    }
}
