package com.learning.bdsdk;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mobads.SplashAdListener;
import com.learning.bdsdk.ad.BDBanner;
import com.learning.bdsdk.ad.BDInterstitial;
import com.learning.bdsdk.ad.BDRewardVideo;
import com.learning.bdsdk.ad.BDSplash;
import com.learning.bdsdk.ad.listener.BDBannerListener;
import com.learning.bdsdk.ad.listener.BDInterstitialListener;
import com.learning.bdsdk.ad.listener.BDRewardVideoListener;
import com.learning.bdsdk.ad.listener.BDSplashListener;

public class BDEntry {
    static BDEntry _a=null;
    public static BDEntry shareInstance(Context context,String appId){
        if(_a==null){
            _a=new BDEntry(context,appId);
        }
        return _a;
    }

    BDEntry(Context context,String appId){
        BDEntryRunImpl.vendorRun(context,appId);
    }

    public void BDBannerEntry(Context context, String id, ViewGroup viewGroup,int width,int height,BDBannerListener listener){
        BDBanner.BannerAd(context,id,viewGroup,width,height,listener);
    }

    public void BDRewardVideoEntry(Context context, String id, BDRewardVideoListener rewardVideoListener){
        BDRewardVideo.RewardVideo(context,id,rewardVideoListener);
    }

    public void BDInterstitialEntry(Context context, String id, BDInterstitialListener bdInterstitialListener){
        BDInterstitial.BDInterstitial(context,id,bdInterstitialListener);
    }

    public void BDSplashEntry(Context context, String id, ViewGroup viewGroup, BDSplashListener bdSplashListener){
        BDSplash.SplashAd(context,id,viewGroup,bdSplashListener);
    }



}
