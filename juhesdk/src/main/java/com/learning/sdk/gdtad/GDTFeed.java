package com.learning.sdk.gdtad;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.learning.sdk.Constant;
import com.learning.sdk.R;
import com.qq.e.ads.ContentAdType;
import com.qq.e.ads.contentad.ContentAD;
import com.qq.e.ads.contentad.ContentAdData;
import com.qq.e.ads.nativ.MediaListener;
import com.qq.e.ads.nativ.MediaView;
import com.qq.e.ads.nativ.NativeMediaADData;
import com.qq.e.comm.util.AdError;

import java.util.List;

public class GDTFeed {
    static ContentAD contentAD;
    static List<ContentAdData> contentAdDataList;
    static MediaView mediaView;
    static Button btnDownload, play;
    static ImageView imgPoster;
    static AQuery $;
    public static void FeedAd(final Context context, String id, final View view) {
        contentAD = new ContentAD(context, Constant.GDTAppId, id, new ContentAD.ContentADListener() {
            @Override
            public void onContentADLoaded(List<ContentAdData> contentAdDatas) {
                if (contentAdDatas.size() > 0) {
                    contentAdDataList = contentAdDatas;
                    preLoadVideo();
                    showAD(view);
                }
            }

            @Override
            public void onNoContentAD(int i) {

            }

            @Override
            public void onContentADStatusChanged(ContentAdData contentAdData) {
                if (contentAdData.getType() == ContentAdType.AD) {
                    NativeMediaADData adData = (NativeMediaADData) contentAdData;
                    $.id(R.id.btn_download).text(getADButtonText(adData));
                    // btnDownload(myAdapter.getPosition(adData),);
                }
            }

            @Override
            public void onContentADError(ContentAdData contentAdData, int i) {
                Log.d("error",contentAdData.toString());
            }

            @Override
            public void onADVideoLoaded(ContentAdData contentAdData) {
                if (contentAdData.getType() == ContentAdType.AD) {
                    NativeMediaADData adData = (NativeMediaADData) contentAdData;
                }
            }
        });
        int pageNumber = 1;
        int channel = 105;
        boolean isManualOperation = true;
        contentAD.loadAD(pageNumber, channel, isManualOperation);
    }

    private static void showAD(View view) {
        final ContentAdData contentAdData = contentAdDataList.get(0);
        if (contentAdData.getType() == ContentAdType.INFORMATION) {
            return;
        }
        mediaView = view.findViewById(R.id.gdt_media_view);
        imgPoster = view.findViewById(R.id.img_poster);
        btnDownload = view.findViewById(R.id.btn_download);
        play = view.findViewById(R.id.btn_play);

        final NativeMediaADData nativeADDataRef = (NativeMediaADData) contentAdData;

        $.id(R.id.img_logo).image(nativeADDataRef.getIconUrl(), false, true);
        $.id(imgPoster).image(nativeADDataRef.getImgUrl(), false, true, 0, 0,
                new BitmapAjaxCallback() {
                    @Override
                    protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                        // AQuery框架有一个问题，就是即使在图片加载完成之前将ImageView设置为了View.GONE，在图片加载完成后，这个ImageView会被重新设置为VIEW.VISIBLE。
                        // 所以在这里需要判断一下，如果已经把ImageView设置为隐藏，开始播放视频了，就不要再显示广告的大图。开发者在用其他的图片加载框架时，也应该注意检查下是否有这个问题。
                        if (iv.getVisibility() == View.VISIBLE) {
                            iv.setImageBitmap(bm);
                        }
                    }
                });
        $.id(R.id.text_title).text(nativeADDataRef.getTitle());
        $.id(R.id.text_desc).text(nativeADDataRef.getDesc());
        $.id(btnDownload).text(getADButtonText(nativeADDataRef));

        $.id(mediaView).visibility(View.GONE);
        $.id(imgPoster).visibility(View.VISIBLE);
        if (nativeADDataRef.isVideoAD() && nativeADDataRef.isVideoLoaded()) {
            if (nativeADDataRef.isPlaying()) {
                mediaView.setVisibility(View.VISIBLE);
                imgPoster.setVisibility(View.GONE);
                play.setVisibility(View.GONE);
            } else {
                mediaView.setVisibility(View.VISIBLE);
                imgPoster.setVisibility(View.GONE);
                nativeADDataRef.bindView(mediaView, true); // 只有将MediaView和广告实例绑定之后，才能播放视频
                nativeADDataRef.play();
                /** 设置视频播放过程中的监听器 */
                nativeADDataRef.setMediaListener(new MediaListener() {

                    /**
                     * 视频播放器初始化完成，准备好可以播放了
                     *
                     * @param videoDuration 视频素材的总时长
                     */
                    @Override
                    public void onVideoReady(long videoDuration) {
                        //Log.i(TAG, "onVideoReady, videoDuration = " + videoDuration);
                    }

                    /** 视频开始播放 */
                    @Override
                    public void onVideoStart() {
                        //Log.i(TAG, "onVideoStart");
                    }

                    /** 视频暂停 */
                    @Override
                    public void onVideoPause() {
                        //Log.i(TAG, "onVideoPause");
                    }

                    /** 视频自动播放结束，到达最后一帧 */
                    @Override
                    public void onVideoComplete() {
                        //Log.i(TAG, "onVideoComplete");
                    }

                    /** 视频播放时出现错误 */
                    @Override
                    public void onVideoError(AdError error) {
                        // Log.i(TAG, "onVideoError, errorCode: " + error.getErrorCode());
                    }

                    /** SDK内置的播放器控制条中的重播按钮被点击 */
                    @Override
                    public void onReplayButtonClicked() {
                        //Log.i(TAG, "onReplayButtonClicked");
                    }

                    /**
                     * SDK内置的播放器控制条中的下载/查看详情按钮被点击 注意:
                     * 这里是指UI中的按钮被点击了，而广告的点击事件回调是在onADClicked中，开发者如需统计点击只需要在onADClicked回调中进行一次统计即可。
                     */
                    @Override
                    public void onADButtonClicked() {
                        //Log.i(TAG, "onADButtonClicked");
                    }

                    /** SDK内置的全屏和非全屏切换回调，进入全屏时inFullScreen为true，退出全屏时inFullScreen为false */
                    @Override
                    public void onFullScreenChanged(boolean inFullScreen) {

                        // 原生视频广告默认静音播放，进入到全屏后建议开发者可以设置为有声播放
                        if (inFullScreen) {
                            nativeADDataRef.setVolumeOn(true);
                        } else {
                            nativeADDataRef.setVolumeOn(false);
                        }
                    }
                });
            }
        }

        nativeADDataRef.onExposured(view.findViewById(R.id.ad_info)); // 需要先调用曝光接口
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nativeADDataRef.onClicked(v);
            }
        });


    }

    private static void preLoadVideo() {
        if (contentAdDataList != null && !contentAdDataList.isEmpty()) {
            for (int i = 0; i < contentAdDataList.size(); i++) {
                ContentAdData ad = contentAdDataList.get(i);
                NativeMediaADData mediaADData = null;
                if (ad.getType() == ContentAdType.AD
                        && (mediaADData = (NativeMediaADData) ad).isVideoAD()) {
                    mediaADData.preLoadVideo(); // 加载结果在onADVideoLoaded回调中返回
                }
            }
        }
    }

    private static String getADButtonText(NativeMediaADData adItem) {
        if (adItem == null) {
            return "……";
        }
        if (!adItem.isAPP()) {
            return "查看详情";
        }
        switch (adItem.getAPPStatus()) {
            case 0:
                return "点击下载";
            case 1:
                return "点击启动";
            case 2:
                return "点击更新";
            case 4:
                return adItem.getProgress() > 0 ? "下载中" + adItem.getProgress() + "%" : "下载中"; // 特别注意：当进度小于0时，不要使用进度来渲染界面
            case 8:
                return "下载完成";
            case 16:
                return "下载失败,点击重试";
            default:
                return "查看详情";
        }
    }

}
