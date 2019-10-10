package com.learning.sdk.csjad;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.DownloadStatusController;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.learning.sdk.R;
import com.learning.sdk.TTAdManagerHolder;
import com.learning.sdk.listener.FeedAdListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class CSJFeed {
    static Context ctx;
    static FeedAdListener feedAdListener;
    static View convertView;
    private static Map<View, TTAppDownloadListener> mTTAppDownloadListenerMap = new WeakHashMap<>();

    public static void FeedAd(final Context context, String id, View view, final FeedAdListener listener) {
        convertView = view;
        feedAdListener = listener;
        ctx = context;
        final AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(id)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(640, 320)
                .setAdCount(1) //请求广告数量为1到3条
                .build();
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        TTAdNative mTTAdNAtive = ttAdManager.createAdNative(context);
        mTTAdNAtive.loadFeedAd(adSlot, new TTAdNative.FeedAdListener() {
            @Override
            public void onError(int i, String s) {
                Log.e("onError",s);
                listener.onAdLoadFail(i, s);
            }

            @Override
            public void onFeedAdLoad(List<TTFeedAd> list) {
                if (list == null || list.isEmpty()) {
                    return;
                } else {
                    TTFeedAd ad = list.get(0);
                    switch (ad.getImageMode()) {
                        case TTAdConstant.IMAGE_MODE_SMALL_IMG:
                            Log.e("CSJFeed","IMAGE_MODE_SMALL_IMG");
                            break;
                        case TTAdConstant.IMAGE_MODE_LARGE_IMG:
                            Log.e("CSJFeed","IMAGE_MODE_LARGE_IMG");
                            bindData(ad);
                            break;
                        case TTAdConstant.IMAGE_MODE_GROUP_IMG:
                            Log.e("CSJFeed","IMAGE_MODE_GROUP_IMG");

                            break;
                        case TTAdConstant.IMAGE_MODE_VIDEO:
                            Log.e("CSJFeed","IMAGE_MODE_VIDEO");
                            break;
                        case TTAdConstant.IMAGE_MODE_VERTICAL_IMG:
                            Log.e("CSJFeed","穿山甲FeedList");
                            bindData(ad);
                            break;
                    }

                }
            }
        });
    }

    public static void bindData(TTFeedAd ad) {
        TextView mTitle = convertView.findViewById(R.id.tv_listitem_ad_title);
        TextView mSource = convertView.findViewById(R.id.tv_listitem_ad_source);
        TextView mDescription = convertView.findViewById(R.id.tv_listitem_ad_desc);
        ImageView mLargeImage = convertView.findViewById(R.id.iv_listitem_image);
        ImageView mIcon = convertView.findViewById(R.id.iv_listitem_icon);
        ImageView mDislike = convertView.findViewById(R.id.iv_listitem_dislike);
        Button mCreativeButton = convertView.findViewById(R.id.btn_listitem_creative);
        final Button mStopButton = convertView.findViewById(R.id.btn_listitem_stop);
        Button mRemoveButton = convertView.findViewById(R.id.btn_listitem_remove);
        bindDislikeCustom(mDislike, ad);
        List<View> clickViewList = new ArrayList<>();
        clickViewList.add(convertView);
        List<View> creativeViewList = new ArrayList<>();
        creativeViewList.add(mCreativeButton);
        ad.registerViewForInteraction((ViewGroup) convertView, clickViewList, creativeViewList, new TTNativeAd.AdInteractionListener() {
            @Override
            public void onAdClicked(View view, TTNativeAd ttNativeAd) {

            }

            @Override
            public void onAdCreativeClick(View view, TTNativeAd ttNativeAd) {

            }

            @Override
            public void onAdShow(TTNativeAd ttNativeAd) {

            }
        });
        mTitle.setText(ad.getTitle());
        mDescription.setText(ad.getDescription()); //description为广告的较长的说明
        mSource.setText(ad.getSource() == null ? "广告来源" : ad.getSource());
        TTImage icon = ad.getIcon();
        if (icon != null && icon.isValid()) {
            Glide.with(ctx).load(icon.getImageUrl()).into(mIcon);
        }
        final Button adCreativeButton = mCreativeButton;
        switch (ad.getInteractionType()) {
            case TTAdConstant.INTERACTION_TYPE_DOWNLOAD:
                //如果初始化ttAdManager.createAdNative(getApplicationContext())没有传入activity 则需要在此传activity，否则影响使用Dislike逻辑
                if (ctx instanceof Activity) {
                    ad.setActivityForDownloadApp((Activity) ctx);
                }
                adCreativeButton.setVisibility(View.VISIBLE);
                if (mStopButton != null) {
                    mStopButton.setVisibility(View.VISIBLE);
                }
                mRemoveButton.setVisibility(View.VISIBLE);
                // bindDownloadListener(adCreativeButton, adViewHolder, ad);
                TTAppDownloadListener downloadListener = new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        if (!isValid()) {
                            return;
                        }
                        adCreativeButton.setText("开始下载");
                        if (mStopButton != null) {
                            mStopButton.setText("开始下载");
                        }
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                        if (!isValid()) {
                            return;
                        }
                        if (totalBytes <= 0L) {
                            adCreativeButton.setText("0%");
                        } else {
                            adCreativeButton.setText((currBytes * 100 / totalBytes) + "%");
                        }
                        if (mStopButton != null) {
                            mStopButton.setText("下载中");
                        }
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                        if (!isValid()) {
                            return;
                        }
                        if (totalBytes <= 0L) {
                            adCreativeButton.setText("0%");
                        } else {
                            adCreativeButton.setText((currBytes * 100 / totalBytes) + "%");
                        }
                        if (mStopButton != null) {
                            mStopButton.setText("下载暂停");
                        }
                    }

                    @Override
                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        if (!isValid()) {
                            return;
                        }
                        adCreativeButton.setText("重新下载");
                        if (mStopButton != null) {
                            mStopButton.setText("重新下载");
                        }
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                        if (!isValid()) {
                            return;
                        }
                        adCreativeButton.setText("点击打开");
                        if (mStopButton != null) {
                            mStopButton.setText("点击打开");
                        }
                    }

                    @Override
                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                        if (!isValid()) {
                            return;
                        }
                        adCreativeButton.setText("点击安装");
                        if (mStopButton != null) {
                            mStopButton.setText("点击安装");
                        }
                    }

                    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
                    private boolean isValid() {
                        return mTTAppDownloadListenerMap.get(convertView) == this;
                        //return true;
                    }
                };
                //一个ViewHolder对应一个downloadListener, isValid判断当前ViewHolder绑定的listener是不是自己
                ad.setDownloadListener(downloadListener); // 注册下载监听器
                mTTAppDownloadListenerMap.put(convertView, downloadListener);
//                //绑定下载状态控制器
                final DownloadStatusController controller = ad.getDownloadStatusController();
                if (mStopButton != null) {
                    mStopButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (controller != null) {
                                controller.changeDownloadStatus();
                                //TToast.show(mContext, "改变下载状态");
                                //Log.d(TAG, "改变下载状态");
                            }
                        }
                    });
                }
                mRemoveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (controller != null) {
                            controller.cancelDownload();
                            // TToast.show(mContext, "取消下载");
                            // Log.d(TAG, "取消下载");
                        }
                    }
                });
                break;
            case TTAdConstant.INTERACTION_TYPE_DIAL:
                adCreativeButton.setVisibility(View.VISIBLE);
                adCreativeButton.setText("立即拨打");
                if (mStopButton != null) {
                    mStopButton.setVisibility(View.GONE);
                }
                mRemoveButton.setVisibility(View.GONE);
                break;
            case TTAdConstant.INTERACTION_TYPE_LANDING_PAGE:
            case TTAdConstant.INTERACTION_TYPE_BROWSER:
//                    adCreativeButton.setVisibility(View.GONE);
                adCreativeButton.setVisibility(View.VISIBLE);
                adCreativeButton.setText("查看详情");
                if (mStopButton != null) {
                    mStopButton.setVisibility(View.GONE);
                }
                mRemoveButton.setVisibility(View.GONE);
                break;
            default:
                adCreativeButton.setVisibility(View.GONE);
                if (mStopButton != null) {
                    mStopButton.setVisibility(View.GONE);
                }
                mRemoveButton.setVisibility(View.GONE);
        }

        if (ad.getImageList() != null && !ad.getImageList().isEmpty()) {
            TTImage image = ad.getImageList().get(0);
            if (image != null && image.isValid()) {
                Glide.with(ctx).load(image.getImageUrl()).into(mLargeImage);
            }
        }
    }


    private static void bindDislikeCustom(ImageView mDislike, TTFeedAd ad) {
        List<FilterWord> words = ad.getFilterWords();
        if (words == null || words.isEmpty()) {
            return;
        }

        final DislikeDialog dislikeDialog = new DislikeDialog(ctx, words);
        dislikeDialog.setOnDislikeItemClick(new DislikeDialog.OnDislikeItemClick() {
            @Override
            public void onItemClick(FilterWord filterWord) {
                //屏蔽广告
//                mData.remove(ad);
//                notifyDataSetChanged();
                feedAdListener.dislike();

            }
        });
        final TTAdDislike ttAdDislike = ad.getDislikeDialog(dislikeDialog);

        mDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //展示dislike可以自行调用dialog
                dislikeDialog.show();

                //也可以使用接口来展示
                //ttAdDislike.showDislikeDialog();
            }
        });
    }


}
