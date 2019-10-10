package com.learning.sdk.bdad;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.baidu.mobad.feeds.BaiduNative;
import com.baidu.mobad.feeds.NativeErrorCode;
import com.baidu.mobad.feeds.NativeResponse;
import com.baidu.mobad.feeds.RequestParameters;
import com.bumptech.glide.Glide;
import com.learning.sdk.R;
import com.learning.sdk.listener.RMAdListener;

import java.util.List;

public class BDFeed {
    static Context ctx;
    public static void FeedAd(final Context context, String id,final View view){
        ctx=context;
        BaiduNative baidu = new BaiduNative(context, id, new BaiduNative.BaiduNativeNetworkListener() {

            @Override
            public void onNativeFail(NativeErrorCode arg0) {
                Log.w("ListViewActivity", "onNativeFail reason:" + arg0.name());
            }

            @Override
            public void onNativeLoad(List<NativeResponse> arg0) {
                // 一个广告只允许展现一次，多次展现、点击只会计入一次
                if (arg0 != null && arg0.size() > 0) {
//                    nrAdList.addAll(arg0);
//                    mList.setAdapter(mAdapter);
//                    mRefreshLayout.setRefreshing(false);
                    NativeResponse nativeResponse=arg0.get(0);

                    bindData(view,nativeResponse);
                }
            }

        });
        RequestParameters requestParameters = new RequestParameters.Builder()
                .downloadAppConfirmPolicy(RequestParameters.DOWNLOAD_APP_CONFIRM_ONLY_MOBILE)
                .build();
        baidu.makeRequest(requestParameters);
    }

    private static void bindData(View view, NativeResponse nrAd) {
        AQuery aq = new AQuery(view);
        aq.id(R.id.native_icon_image).image(nrAd.getIconUrl(), false, true);
        aq.id(R.id.native_main_image).image(nrAd.getImageUrl(), false, true);
        aq.id(R.id.native_text).text(nrAd.getDesc());
        aq.id(R.id.native_title).text(nrAd.getTitle());
        aq.id(R.id.native_brand_name).text(nrAd.getBrandName());
        aq.id(R.id.native_adlogo).image(nrAd.getAdLogoUrl(), false, true);
        aq.id(R.id.native_baidulogo).image(nrAd.getBaiduLogoUrl(), false, true);

//        ImageView icon=view.findViewById(R.id.native_icon_image);
//        ImageView main=view.findViewById(R.id.native_main_image);
//        TextView text=view.findViewById(R.id.native_text);
//        TextView title=view.findViewById(R.id.native_title);
//        TextView brandName=view.findViewById(R.id.native_brand_name);
//        ImageView adlogo=view.findViewById(R.id.native_adlogo);
//        ImageView baidulogo=view.findViewById(R.id.native_baidulogo);
//        Glide.with(ctx).load(nrAd.getIconUrl()).into(icon);
//        Glide.with(ctx).load(nrAd.getImageUrl()).into(main);
//        text.setText(nrAd.getDesc());
//        title.setText(nrAd.getTitle());
//        brandName.setText(nrAd.getBrandName());
//        Glide.with(ctx).load(nrAd.getAdLogoUrl()).into(adlogo);
//        Glide.with(ctx).load(nrAd.getBaiduLogoUrl()).into(baidulogo);

        // nrAd.isDownloadApp() ? "下载" : "查看";
    }
}
