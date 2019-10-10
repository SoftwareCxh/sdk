package com.learning.gdtsdk;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.learning.gdtsdk.gdtad.Banner;
import com.learning.gdtsdk.gdtad.Interstitial;
import com.learning.gdtsdk.gdtad.RewardVideo;
import com.learning.gdtsdk.gdtad.Splash;
import com.learning.gdtsdk.gdtad.listener.GDTBannerListener;
import com.learning.gdtsdk.gdtad.listener.GDTInterstitialListener;
import com.learning.gdtsdk.gdtad.listener.GDTRewardVideoListener;
import com.learning.gdtsdk.gdtad.listener.GDTSplashListener;

public class GDTEntry {
    private static GDTEntry _a = null; //单键实例
    public static synchronized GDTEntry shareInstance(Context context) { //shareInstance方法名称也可以变化但需要告知我方
        if (_a != null) return _a;
        _a = new GDTEntry(context);
        return _a;
    }

    GDTEntry(Context context){
        GDTRunImpl.vendorRun(context);
    }

    public void SplashEntry(Context context, String appid, String id, ViewGroup frameLayout, View view, GDTSplashListener splashADListener){
        Splash.SplashAd( context, appid,  id,  frameLayout,view,  splashADListener);
    }

    public void InterstitialEntry(Context context, String appid, String id, boolean isPopupWindow, GDTInterstitialListener listener){
        Interstitial.InterstitialAd(context,appid,id,isPopupWindow,listener);
    }

    public void RewardVideoEntry(Context context, String appid, String id, GDTRewardVideoListener listener){
        RewardVideo.RewardVideoAd(context,appid,id,listener);
    }

    public void BannerEntry(Context context, String appid, String id, ViewGroup viewGroup, int refresh, GDTBannerListener listener){
        Banner.BannerAd(context,appid,id,viewGroup,refresh,listener);
    }
}
