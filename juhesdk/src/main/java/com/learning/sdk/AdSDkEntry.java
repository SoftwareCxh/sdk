package com.learning.sdk;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.learning.sdk.apiad.ApiBanner;
import com.learning.sdk.apiad.ApiSplash;
import com.learning.sdk.bdad.BDBanner;
import com.learning.sdk.bdad.BDFeed;
import com.learning.sdk.bdad.BDInterstitial;
import com.learning.sdk.bdad.BDRewardVideo;
import com.learning.sdk.bdad.BDSplash;
import com.learning.sdk.csjad.CSJBanner;
import com.learning.sdk.csjad.CSJFeed;
import com.learning.sdk.csjad.CSJNativeBanner;
import com.learning.sdk.csjad.CSJNativeInteraction;
import com.learning.sdk.csjad.CSJRewardVideo;
import com.learning.sdk.csjad.CSJSplash;
import com.learning.sdk.gdtad.GDTBanner;
import com.learning.sdk.gdtad.GDTFeed;
import com.learning.sdk.gdtad.GDTInterstitial;
import com.learning.sdk.gdtad.GDTRewardVideo;
import com.learning.sdk.gdtad.GDTSplash;
import com.learning.sdk.listener.FeedAdListener;
import com.learning.sdk.listener.RMAdListener;
import com.learning.sdk.listener.RewardAdListener;

import java.util.Random;


public class AdSDkEntry {
    private static AdSDkEntry _a = null; //单键实例

    public static synchronized AdSDkEntry shareInstance(Context context, String appId) { //shareInstance方法名称也可以变化但需要告知我方
        if (_a != null) return _a;
        _a = new AdSDkEntry(context, appId);
        return _a;
    }

    AdSDkEntry(Context context, String appId) {
        ADRunImpl.vendorRun(context, appId);
    }


    public void SplashAD(Context context, String id, ViewGroup frame, RMAdListener rmAdListener) {
        if(Constant.splashWeight==0){
            rmAdListener.onAdLoadFail(1,"未成功加载");

            return;
        }
        int i = new Random().nextInt(Constant.splashWeight);
        if (i > 0 && i <= Constant.GDTSplashWeight) {
            //GDT
            Log.e("进入","GDTSplash");
            GDTSplash.SplashAd(context, Constant.GDTSplashId, frame, rmAdListener);
        } else if (i > Constant.GDTSplashWeight && i <= Constant.GDTSplashWeight + Constant.BDSplashWeight) {
            //BD
            Log.e("进入","BDSplash");
            BDSplash.SplashAd(context, Constant.BDSplashId, frame, rmAdListener);
        } else if (i > Constant.GDTSplashWeight + Constant.GDTSplashWeight && i <= Constant.GDTSplashWeight + Constant.BDSplashWeight + Constant.APiSplashWeight) {
            //Api
            Log.e("进入","APiSplash");
            ApiSplash.SplashAd(context, frame, rmAdListener);
        } else if (i > Constant.GDTSplashWeight + Constant.GDTSplashWeight + Constant.APiSplashWeight && i <=  Constant.splashWeight) {
            //CSJ
            Log.e("进入","CSJSplash");
            CSJSplash.splashAd(context, Constant.CSJSplashId, frame, rmAdListener);
        }

    }

    public void InteractionEntry(Context context, String id, RMAdListener listener) {
        if(Constant.interstitialWeight==0){
            listener.onAdLoadFail(1,"未成功加载");
            return;
        }
        int i = new Random().nextInt(Constant.interstitialWeight);
        if (i > 0 && i <= Constant.GDTInterstitialWeight) {
            //GDT
            Log.e("进入","GDT");
            GDTInterstitial.InterstitialAd(context, Constant.GDTInterstitialId, true, listener);
        } else if (i > Constant.GDTInterstitialWeight && i <= Constant.GDTInterstitialWeight + Constant.BDInterstitialWeight) {
            //BD
            Log.e("进入","BD");
            BDInterstitial.BDInterstitial(context, Constant.BDInterstitialId, listener);
        } else if (i > Constant.GDTInterstitialWeight + Constant.BDInterstitialWeight && i <= Constant.GDTInterstitialWeight + Constant.BDInterstitialWeight + Constant.ApiInterstitialWeight) {
            Log.e("进入","Api");

        } else if (i > Constant.GDTInterstitialWeight + Constant.BDInterstitialWeight + Constant.ApiInterstitialWeight && i <= Constant.interstitialWeight) {
            //CSJ
            Log.e("进入","CSJ");
            CSJNativeInteraction.NativeInteractionAd(context, Constant.CSJInterstitialId, listener);
        }
    }


    public void BannerEntry(Context context, String id, ViewGroup viewGroup,RMAdListener listener) {
        if(Constant.bannerWeight==0){
            listener.onAdLoadFail(1,"未成功加载");
            return;
        }
        int i = new Random().nextInt(Constant.bannerWeight);
        if (i > 0 && i <= Constant.GDTBannerWeight) {
            //GDT
            Log.e("进入","GDTBanner");
            GDTBanner.BannerAd(context,Constant.GDTAppId,Constant.GDTBannerId,viewGroup,30,listener);
        } else if (i > Constant.GDTBannerWeight && i <= Constant.GDTBannerWeight + Constant.BDBannerWeight) {
            //BD
            Log.e("进入","BDBanner");
            BDBanner.BannerAd(context,Constant.BDBannerId,viewGroup,600,253,listener);
        } else if (i > Constant.GDTBannerWeight + Constant.BDBannerWeight && i <= Constant.GDTBannerWeight + Constant.BDBannerWeight + Constant.ApiBannerWeight) {
            Log.e("进入","APiBanner");
            ApiBanner.BannerAd(context,viewGroup,listener);
        } else if (i > Constant.GDTBannerWeight + Constant.BDBannerWeight + Constant.ApiBannerWeight && i <= Constant.bannerWeight) {
            //CSJ
            Log.e("进入","CSJBanner");
            CSJNativeBanner.NativeBannerAD(context,Constant.CSJBannerId,listener,600,253,viewGroup);
        }
    }

    public void RewardVideoEntry(Context context, String id, RewardAdListener rewardAdListener){
        if(Constant.rewardWeight==0){
            rewardAdListener.onAdLoadFail(1,"未成功加载");
            Log.e("ad",Constant.rewardWeight+"");
            return;
        }
        int i=new Random().nextInt(Constant.rewardWeight);
        if(i>0&&i<=Constant.GDTRewardWeight){
            //GDT
            Log.e("进入","GDTReward");
            GDTRewardVideo.RewardVideoAd(context,Constant.GDTAppId,Constant.GDTRewardId,rewardAdListener);
        }else if(i>Constant.GDTRewardWeight&&i<=Constant.GDTRewardWeight+Constant.BDRewardWeight){
            //BD
            Log.e("进入","BDReward");
            BDRewardVideo.RewardVideo(context,Constant.BDRewardId,rewardAdListener);
        }else if(i>Constant.GDTRewardWeight+Constant.BDRewardWeight&&i<=Constant.GDTRewardWeight+Constant.BDRewardWeight+Constant.ApiRewardWeight){
            //Api
            Log.e("进入","ApiReward");
        }else if(i>Constant.GDTRewardWeight+Constant.BDRewardWeight+Constant.ApiRewardWeight&&i<=Constant.rewardWeight){
            //CSJ
            Log.e("进入","CSJReward");
            CSJRewardVideo.RewardVideoAd(context,Constant.CSJRewardId,rewardAdListener, TTAdConstant.VERTICAL,"admin","",1);

        }
    }

    public void FeedListEntry(Context context, View view,FeedAdListener feedAdListener){
        if(Constant.feedWeight==0){
            Log.e("ad",Constant.feedWeight+"");
        }
        int i=new Random().nextInt(Constant.feedWeight);
        if(i>0&&i<=Constant.GDTFeedWeight){
            //GDT
            Log.e("进入","GDTReward");
            view=  LayoutInflater.from(context).inflate(R.layout.item_ad ,(FrameLayout)view, true) ;
            GDTFeed.FeedAd(context,Constant.GDTFeedId,view);
        }else if(i>Constant.GDTFeedWeight&&i<=Constant.GDTFeedWeight+Constant.BDFeedWeight){
            //BD
            Log.e("进入","BDReward");
            //view= ((Activity) context).getLayoutInflater().inflate(R.layout.feed_native_listview_ad_row, null);
            view=  LayoutInflater.from(context).inflate(R.layout.feed_native_listview_ad_row ,(FrameLayout)view, true) ;
            BDFeed.FeedAd(context,Constant.BDFeedId,view);
        }else if(i>Constant.GDTFeedWeight+Constant.BDFeedWeight&&i<=Constant.GDTFeedWeight+Constant.BDFeedWeight+Constant.ApiFeedWeight){
            //Api
            Log.e("进入","ApiReward");
        }else if(i>Constant.GDTFeedWeight+Constant.BDFeedWeight+Constant.ApiFeedWeight&&i<=Constant.feedWeight){
            //CSJ
            Log.e("进入","CSJReward");
            view=  LayoutInflater.from(context).inflate(R.layout.listitem_ad_large_pic ,(FrameLayout)view, true) ;
            //view = ((Activity) context).getLayoutInflater().inflate(R.layout.listitem_ad_vertical_pic, null);
            CSJFeed.FeedAd(context,Constant.CSJFeedId,view,feedAdListener);
        }
    }
//    public View InflateView(Context context,FrameLayout viewGroup) {
//        View view;
//        if(Constant.feedWeight==0){
//            Log.e("ad",Constant.feedWeight+"");
//            return  null;
//        }
//        int i=new Random().nextInt(Constant.feedWeight);
//        if(i>0&&i<=Constant.GDTFeedWeight){
//            //GDT
//            view = ((Activity) context).getLayoutInflater().inflate(R.layout.item_ad, null);
//            return view;
//        }else if(i>Constant.GDTFeedWeight&&i<=Constant.GDTFeedWeight+Constant.BDFeedWeight){
//            //BD
//            Log.e("进去了","BDInflateView");
//            view=  LayoutInflater.from(context).inflate(R.layout.feed_native_listview_ad_row ,viewGroup, true) ;
//                    //((Activity) context).getLayoutInflater().inflate(R.layout.feed_native_listview_ad_row, null);
//            return view;
//        }else if(i>Constant.GDTFeedWeight+Constant.BDFeedWeight&&i<=Constant.GDTFeedWeight+Constant.BDFeedWeight+Constant.ApiFeedWeight){
//            //Api
//            Log.e("进入","ApiReward");
//        }else if(i>Constant.GDTFeedWeight+Constant.BDFeedWeight+Constant.ApiFeedWeight&&i<=Constant.feedWeight){
//            //CSJ
//            Log.e("进入","CSJReward");
//            view = ((Activity) context).getLayoutInflater().inflate(R.layout.listitem_ad_vertical_pic, null);
//            return view;
//        }
//        return null;
//    }
}
