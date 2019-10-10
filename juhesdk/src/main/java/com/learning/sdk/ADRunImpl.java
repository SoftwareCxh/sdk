package com.learning.sdk;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.baidu.mobads.AdView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ADRunImpl {
    static Map<String, String> map = new HashMap();

    public static void vendorRun(final Context context, String appId) {
        Constant.UID=appId;
        String hash = "";
        map.put("id", appId);
        Log.e("pkg", context.getPackageName());
        new Thread(new Runnable() {
            @Override
            public void run() {
                String json = HttpClientUtil.requestGet(Constant.test, map);
                Log.e("json",json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    Constant.HASH = jsonObject.getString("hash");
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray app = data.getJSONArray("app");
                    JSONObject ad = data.getJSONObject("ad");
                    JSONObject pingback = data.getJSONObject("pingback");
                    for (int i = 0; i < app.length(); i++) {
                        JSONObject object = app.getJSONObject(i);
                        if (object.getInt("type") == 1) {
                            Constant.GDTAppId = object.getString("id");
                        } else if (object.getInt("type") == 2) {
                            Constant.BDAppId = object.getString("id");
                            AdView.setAppSid(context,Constant.BDAppId);
                        } else if (object.getInt("type") == 3) {
                            Constant.ApiId = object.getString("id");
                        } else if (object.getInt("type") == 4) {
                            Constant.CSJAppId = object.getString("id");
                            TTAdManagerHolder.init(context, Constant.CSJAppId);
                        }
                    }
                    JSONArray splash=ad.getJSONArray("2");
                    for(int i=0;i<splash.length();i++){
                        JSONObject object=splash.getJSONObject(i);
                        if(object.getInt("type")==1){
                            Constant.GDTSplashId=object.getString("id");
                            Constant.GDTSplashWeight=object.getInt("weight");
                            Constant.splashWeight+=Constant.GDTSplashWeight;
                        }else if(object.getInt("type")==2){
                            Constant.BDSplashId=object.getString("id");
                            Constant.BDSplashWeight=object.getInt("weight");
                            Constant.splashWeight+=Constant.BDSplashWeight;
                        }else if(object.getInt("type")==3){
                            Constant.ApiSplashId=object.getString("id");
                            Constant.APiSplashWeight=object.getInt("weight");
                            Constant.splashWeight+=Constant.APiSplashWeight;
                        }else if(object.getInt("type")==4){
                            Constant.CSJSplashId=object.getString("id");
                            Constant.CSJSplashWeight=object.getInt("weight");
                            Constant.splashWeight+=Constant.CSJSplashWeight;
                        }
                        Log.e("splashweight",Constant.splashWeight+"");
                    }

                    JSONArray interstitial=ad.getJSONArray("3");
                    for(int i=0;i<interstitial.length();i++){
                        JSONObject object=interstitial.getJSONObject(i);
                        if(object.getInt("type")==1){
                            Constant.GDTInterstitialId=object.getString("id");
                            Constant.GDTInterstitialWeight=object.getInt("weight");
                            Constant.interstitialWeight+= Constant.GDTInterstitialWeight;
                        }else if(object.getInt("type")==2){
                            Constant.BDInterstitialId=object.getString("id");
                            Constant.BDInterstitialWeight=object.getInt("weight");
                            Constant.interstitialWeight+= Constant.BDInterstitialWeight;
                        }else if(object.getInt("type")==3){
                            Constant.ApiInterstitialId=object.getString("id");
                            Constant.ApiInterstitialWeight=object.getInt("weight");
                            Constant.interstitialWeight+= Constant.ApiInterstitialWeight;
                        }else if(object.getInt("type")==4){
                            Constant.CSJInterstitialId=object.getString("id");
                            Constant.CSJInterstitialWeight=object.getInt("weight");
                            Constant.interstitialWeight+= Constant.CSJInterstitialWeight;
                        }
                    }
                    JSONArray banner=ad.getJSONArray("4");
                    for(int i=0;i<banner.length();i++){
                        JSONObject object=banner.getJSONObject(i);
                        if(object.getInt("type")==1){
                            Constant.GDTBannerId=object.getString("id");
                            Constant.GDTBannerWeight=object.getInt("weight");
                            Constant.bannerWeight+=Constant.GDTBannerWeight;
                        }else if(object.getInt("type")==2){
                            Constant.BDBannerId=object.getString("id");
                            Constant.BDBannerWeight=object.getInt("weight");
                            Constant.bannerWeight+=Constant.BDBannerWeight;
                        }else if(object.getInt("type")==3){
                            Constant.ApiBannerId=object.getString("id");
                            Constant.ApiBannerWeight=object.getInt("weight");
                            Constant.bannerWeight+=Constant.ApiBannerWeight;
                        }else if(object.getInt("type")==4){
                            Constant.CSJBannerId=object.getString("id");
                            Constant.CSJBannerWeight=object.getInt("weight");
                            Constant.bannerWeight+=Constant.CSJBannerWeight;
                        }
                    }
                    JSONArray feed=ad.getJSONArray("5");
                    for(int i=0;i<feed.length();i++){
                        JSONObject object=feed.getJSONObject(i);
                        if(object.getInt("type")==1){
                            Constant.GDTFeedId=object.getString("id");
                            Constant.GDTFeedWeight=object.getInt("weight");
                            Constant.feedWeight+=Constant.GDTFeedWeight;
                        }else if(object.getInt("type")==2){
                            Constant.BDFeedId=object.getString("id");
                            Constant.BDFeedWeight=object.getInt("weight");
                            Constant.feedWeight+=Constant.BDFeedWeight;
                        }else if(object.getInt("type")==3){
                            Constant.ApiFeedId=object.getString("id");
                            Constant.ApiFeedWeight=object.getInt("weight");
                            Constant.feedWeight+=Constant.ApiFeedWeight;
                        }else if(object.getInt("type")==4){
                            Constant.CSJFeedId=object.getString("id");
                            Constant.CSJFeedWeight=object.getInt("weight");
                            Constant.feedWeight+=Constant.CSJFeedWeight;
                        }
                        Log.e("feedWeight",Constant.feedWeight+"");
                    }
                    JSONArray reward=ad.getJSONArray("6");
                    for(int i=0;i<reward.length();i++){
                        JSONObject object=reward.getJSONObject(i);
                        if(object.getInt("type")==1){
                            Constant.GDTRewardId=object.getString("id");
                            Constant.GDTRewardWeight=object.getInt("weight");
                            Constant.rewardWeight+=Constant.GDTRewardWeight;
                        }else if(object.getInt("type")==2){
                            Constant.BDRewardId=object.getString("id");
                            Constant.BDRewardWeight=object.getInt("weight");
                            Constant.rewardWeight+=Constant.BDRewardWeight;
                        }else if(object.getInt("type")==3){
                            Constant.ApiRewardId=object.getString("id");
                            Constant.ApiRewardWeight=object.getInt("weight");
                            Constant.rewardWeight+=Constant.ApiRewardWeight;
                        }else if(object.getInt("type")==4){
                            Constant.CSJRewardId=object.getString("id");
                            Constant.CSJRewardWeight=object.getInt("weight");
                            Constant.rewardWeight+=Constant.CSJRewardWeight;
                        }
                    }
                    Constant.HOST=pingback.getString("host");
                    Constant.PID=pingback.getString("PID");
                    Constant.SECRET=pingback.getString("SECRET");

                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        //穿山甲SDK初始化
        //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
        //
        //建议在申请权限时使用我们提供的方法TTAdManager.requestPermissionIfNecessary(this)
        //如果高于6.0申请权限
        if (Build.VERSION.SDK_INT > 23) {
            checkAndRequestPermission(context);
            //     TTAdManagerHolder.get().requestPermissionIfNecessary(context);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void checkAndRequestPermission(Context context) {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE))) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (!(checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!(checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION))) {
            lackedPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!(checkSelfPermission(context, Manifest.permission.INTERNET))) {
            lackedPermission.add(Manifest.permission.INTERNET);
        }
        if (!(checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE))) {
            lackedPermission.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (!(checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE))) {
            lackedPermission.add(Manifest.permission.ACCESS_WIFI_STATE);
        }
        if (!(checkSelfPermission(context, Manifest.permission.REQUEST_INSTALL_PACKAGES))) {
            lackedPermission.add(Manifest.permission.REQUEST_INSTALL_PACKAGES);
        }
        if (lackedPermission.size() == 0) {
            // 权限都已经有了，那么直接调用SDK
            //   fetchSplashAD();
        } else {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            ((Activity) context).requestPermissions(requestPermissions, 1000);
        }
    }

    public static boolean checkSelfPermission(Context context, String permission) {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                Method method = Context.class.getMethod("checkSelfPermission",
                        String.class);
                return (Integer) method.invoke(context, permission) == PackageManager.PERMISSION_GRANTED;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


//    static TTNativeExpressAd mTTAd;
//    private static long startTime = 0;
//    public static void InteractionAdExpress(Context ctx, String id, Handler handler) {
//        TTAdNative  mTTAdNative = TTAdManagerHolder.get().createAdNative(context);
//        context =ctx;
//        AdSlot adSlot = new AdSlot.Builder()
//                .setCodeId(id) //广告位id
//                .setSupportDeepLink(true)
//                .setAdCount(1) //请求广告数量为1到3条
//                .setImageAcceptedSize(600,600 )//这个参数设置即可，不影响模板广告的size
//                .build();
//        mTTAdNative.loadInteractionExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
//            @Override
//            public void onError(int code, String message) {
//                //TToast.show(InteractionExpressActivity.this, "load error : " + code + ", " + message);
//            }
//
//            @Override
//            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
//                if (ads == null || ads.size() == 0){
//                    return;
//                }
//                mTTAd = ads.get(0);
//                bindAdListener(mTTAd);
//                startTime = System.currentTimeMillis();
//                mTTAd.render();
//            }
//        });
//    }
//
//    private static void bindAdListener(TTNativeExpressAd ad) {
//        ad.setExpressInteractionListener(new TTNativeExpressAd.AdInteractionListener() {
//            @Override
//            public void onAdDismiss() {
//            }
//
//            @Override
//            public void onAdClicked(View view, int type) {
//            }
//
//            @Override
//            public void onAdShow(View view, int type) {
//            }
//
//            @Override
//            public void onRenderFail(View view, String msg, int code) {
//                Log.e("ExpressView","render fail:"+(System.currentTimeMillis() - startTime));
//            }
//
//            @Override
//            public void onRenderSuccess(View view, float width, float height) {
//                Log.e("ExpressView","render suc:"+(System.currentTimeMillis() - startTime));
//                mTTAd.showInteractionExpressAd((Activity)context);
//
//            }
//        });
//
//        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD){
//            return;
//        }
//        ad.setDownloadListener(new TTAppDownloadListener() {
//            @Override
//            public void onIdle() {
//            }
//
//            @Override
//            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
//
//            }
//
//            @Override
//            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
//            }
//
//            @Override
//            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
//            }
//
//            @Override
//            public void onInstalled(String fileName, String appName) {
//            }
//
//            @Override
//            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
//            }
//        });
//    }
}
