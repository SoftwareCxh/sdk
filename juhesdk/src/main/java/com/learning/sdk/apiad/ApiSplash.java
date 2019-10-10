package com.learning.sdk.apiad;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.learning.sdk.AppUtils;
import com.learning.sdk.Constant;
import com.learning.sdk.HttpClientUtil;
import com.learning.sdk.R;
import com.learning.sdk.listener.RMAdListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import android.os.Handler;

public class ApiSplash {
    static Context context;
    static ViewGroup viewGroup;
    static RMAdListener listener;
    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what != 2) {
                return;
            }

            String json = (String) msg.obj;
            try {
                JSONObject object = new JSONObject(json);
                int ret = object.getInt("ret");
                if (ret == 0) {
                    JSONObject data = object.getJSONObject("data");
                    int crtType = data.getInt("crt_type");
                    String imgUrl = data.getString("img_url");
                    Log.e("ImgUrl", imgUrl);
                    View view = LayoutInflater.from(context).inflate(R.layout.api_splash_image, null);
                    switch (crtType) {
                        case 2:
                            ImageView img = view.findViewById(R.id.large_image);
                            Glide.with(context).load(imgUrl).into(img);
                            viewGroup.addView(view);
                            break;
                        case 7:
                            ImageView img1 = view.findViewById(R.id.large_image);
                            Glide.with(context).load(imgUrl).into(img1);
                            viewGroup.addView(view);
                            break;
                    }
                    final JSONArray urls = data.getJSONArray("impress_notice_urls");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Map hashMap = new HashMap<String, String>();
                            for (int i = 0; i < urls.length(); i++) {
                                try {
                                    HttpClientUtil.requestGet(urls.getString(i), hashMap);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                    final JSONArray clickUrls = data.getJSONArray("click_notice_urls");
                    final String linkUrl=data.getString("click_link");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Map hashMap = new HashMap<String, String>();
                                    for (int i = 0; i < clickUrls.length(); i++) {
                                        try {
                                            HttpClientUtil.requestGet(clickUrls.getString(i), hashMap);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(linkUrl));
                            context.startActivity(intent);

                        }
                    });

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listener.onAdDismiss();
                        }
                    }, 5000);
                    listener.onAdLoadSuccess();
                } else {
                    listener.onAdLoadFail(ret, "");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                listener.onAdLoadFail(1001, e.getMessage());
            }
        }
    };

    public static void SplashAd(Context ctx, ViewGroup vg, RMAdListener rmAdListener) {
        context = ctx;
        viewGroup = vg;
        listener = rmAdListener;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map map = AppUtils.getMap(context);
                map.put("uid", Constant.UID);
                map.put("ad_type", "2");
                String json = HttpClientUtil.requestGet(Constant.ApiAdTest, map);
                Message message = new Message();
                message.obj = json;
                message.what = 2;
                handler.sendMessage(message);
            }
        }).start();
    }
}
