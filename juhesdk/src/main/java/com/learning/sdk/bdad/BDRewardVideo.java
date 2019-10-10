package com.learning.sdk.bdad;

import android.content.Context;
import android.util.Log;

import com.baidu.mobads.rewardvideo.RewardVideoAd;
import com.learning.sdk.listener.RewardAdListener;
import com.learning.sdk.listener.RewardModel;

public class BDRewardVideo {
    static RewardVideoAd mRewardVideoAd;

    public static void RewardVideo(Context context, String id, final RewardAdListener rewardVideoListener) {

        mRewardVideoAd = new RewardVideoAd(context, id, new RewardVideoAd.RewardVideoAdListener() {
            @Override
            public void onAdShow() {
            }

            @Override
            public void onAdClick() {
                rewardVideoListener.onAdClicked();
            }

            @Override
            public void onAdClose(float v) {
                rewardVideoListener.onAdDismiss();
            }

            @Override
            public void onAdFailed(String s) {
                Log.e("BD",s);

                rewardVideoListener.onAdLoadFail(0,s);
            }

            @Override
            public void onVideoDownloadSuccess() {
                rewardVideoListener.onAdLoadSuccess();
                mRewardVideoAd.show();
            }

            @Override
            public void onVideoDownloadFailed() {


            }

            @Override
            public void playCompletion() {
                rewardVideoListener.onVideoPlayFinish();
                RewardModel rewardModel=new RewardModel();
                rewardVideoListener.onReward(rewardModel);
            }
        }, false);
        mRewardVideoAd.load();
    }
}
