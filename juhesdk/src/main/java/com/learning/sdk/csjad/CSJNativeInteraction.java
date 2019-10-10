package com.learning.sdk.csjad;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.learning.sdk.R;
import com.learning.sdk.TTAdManagerHolder;
import com.learning.sdk.listener.RMAdListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CSJNativeInteraction {
    static boolean mIsLoading = false;
    static Context ctx;
    static RequestManager mRequestManager;
    private static ViewGroup mRootView;
    static Dialog mAdDialog;
    static TextView mDislikeView;
    static ImageView mAdImageView;
    static RMAdListener listener;

    public static void NativeInteractionAd(Context context, String id, RMAdListener nativeInteractionListener ) {
        listener=nativeInteractionListener;
        ctx = context;
        mRequestManager = Glide.with(context);
        TTAdNative mTTAdNative = TTAdManagerHolder.get().createAdNative(context);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(id)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setNativeAdType(AdSlot.TYPE_INTERACTION_AD)//请求原生广告时候，请务必调用该方法，设置参数为TYPE_BANNER或TYPE_INTERACTION_AD
                .build();

        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadNativeAd(adSlot, new TTAdNative.NativeAdListener() {
            @Override
            public void onError(int code, String message) {
                mIsLoading = false;
                listener.onAdLoadFail(code,message);
                Log.e("CSJ插屏","code:"+code+",message:"+message);
                //  TToast.show(NativeInteractionActivity.this, "load error : " + code + ", " + message);
            }

            @Override
            public void onNativeAdLoad(List<TTNativeAd> ads) {
                listener.onAdLoadSuccess();
                mIsLoading = false;
                if (ads.get(0) == null) {
                    return;
                }

                showAd(ads.get(0));
            }

        });
    }

    private static void showAd(TTNativeAd ad) {
        mAdDialog = new Dialog(ctx, R.style.native_insert_dialog);
        mAdDialog.setCancelable(false);
        mAdDialog.setContentView(R.layout.native_insert_ad_layout);
        mRootView = mAdDialog.findViewById(R.id.native_insert_ad_root);
        mAdImageView = (ImageView) mAdDialog.findViewById(R.id.native_insert_ad_img);
        //限制dialog 的最大宽度不能超过屏幕，宽高最小为屏幕宽的 1/3
        DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        int maxWidth = (dm == null) ? 0 : dm.widthPixels;
        int minWidth = maxWidth / 3;
        mAdImageView.setMaxWidth(maxWidth);
        mAdImageView.setMinimumWidth(minWidth);
        //noinspection SuspiciousNameCombination
        mAdImageView.setMinimumHeight(minWidth);
        ImageView mCloseImageView = (ImageView) mAdDialog.findViewById(R.id.native_insert_close_icon_img);
        mDislikeView = mAdDialog.findViewById(R.id.native_insert_dislike_text);

        ImageView iv = mAdDialog.findViewById(R.id.native_insert_ad_logo);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ad.getAdLogo().compress(Bitmap.CompressFormat.PNG, 100, stream);
            mRequestManager
                    .load(stream.toByteArray())
                    .asBitmap()
                    .into(iv);
        } catch (Exception e) {

        } finally {
            try {
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mCloseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdDialog.dismiss();
            }
        });
        //绑定网盟dislike逻辑，有助于精准投放
        bindDislikeAction(ad);
        //绑定广告view事件交互
        bindViewInteraction(ad);
        //加载ad 图片资源
        loadAdImage(ad);
    }

    private static void loadAdImage(TTNativeAd ad) {
        if (ad.getImageList() != null && !ad.getImageList().isEmpty()) {
            TTImage image = ad.getImageList().get(0);
            if (image != null && image.isValid()) {
                mRequestManager.load(image.getImageUrl()).into(mAdImageView);
            }
        }

        TTImage image = ad.getImageList().get(0);
        int width = image.getWidth();
        String url = image.getImageUrl();
        mRequestManager.load(url).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
                if (mAdImageView != null) {
                    mAdImageView.setImageDrawable(glideDrawable);
                    showAd();
                }
            }
        });
    }

    private static void bindViewInteraction(TTNativeAd ad) {
        //可以被点击的view, 比如标题、icon等,点击后尝试打开落地页，也可以把nativeView放进来意味整个广告区域可被点击
        List<View> clickViewList = new ArrayList<>();
        clickViewList.add(mAdImageView);

        //触发创意广告的view（点击下载或拨打电话），比如可以设置为一个按钮，按钮上文案根据广告类型设定提示信息
        List<View> creativeViewList = new ArrayList<>();
        //如果需要点击图文区域也能进行下载或者拨打电话动作，请将图文区域的view传入
        //creativeViewList.add(nativeView);
        creativeViewList.add(mAdImageView);

        //重要! 这个涉及到广告计费，必须正确调用。convertView必须使用ViewGroup。
        ad.registerViewForInteraction(mRootView, clickViewList, creativeViewList, mDislikeView, new TTNativeAd.AdInteractionListener() {
            @Override
            public void onAdClicked(View view, TTNativeAd ad) {
                listener.onAdClicked();
                if (ad != null) {
                   // TToast.show(mContext, "广告" + ad.getTitle() + "被点击");
                }
                mAdDialog.dismiss();
            }

            @Override
            public void onAdCreativeClick(View view, TTNativeAd ad) {
                if (ad != null) {
                    //TToast.show(mContext, "广告" + ad.getTitle() + "被创意按钮被点击");
                }
                mAdDialog.dismiss();
            }

            @Override
            public void onAdShow(TTNativeAd ad) {
                if (ad != null) {
                  //  TToast.show(mContext, "广告" + ad.getTitle() + "展示");
                }
            }
        });

    }

    private static void bindDislikeAction(TTNativeAd ad) {
        final TTAdDislike ttAdDislike = ad.getDislikeDialog((Activity) ctx);
        if (ttAdDislike != null) {
            ttAdDislike.setDislikeInteractionCallback(new TTAdDislike.DislikeInteractionCallback() {
                @Override
                public void onSelected(int position, String value) {
                    mAdDialog.dismiss();
                }

                @Override
                public void onCancel() {

                }
            });
        }
        mDislikeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ttAdDislike != null)
                    ttAdDislike.showDislikeDialog();
            }
        });
    }



    private static void showAd() {
        if (((Activity)ctx).isFinishing()) {
            return;
        }
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new IllegalStateException("不能在子线程调用 TTInteractionAd.showInteractionAd");
        }
        mAdDialog.show();
    }
}
