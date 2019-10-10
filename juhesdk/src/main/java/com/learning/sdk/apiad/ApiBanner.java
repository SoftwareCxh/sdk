package com.learning.sdk.apiad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class ApiBanner {
    static Context context;
    static ViewGroup viewGroup;
    static RMAdListener listener;
    final static Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what!=1){
                return;
            }
            String json=(String)msg.obj;
            try {
                JSONObject object = new JSONObject(json);
                int ret = object.getInt("ret");
                if (ret == 0) {
                    JSONObject data = object.getJSONObject("data");
                    int crtType = data.getInt("crt_type");
                    String imgUrl;
                    String titleText, descText;
                    View view;
                    ImageView imgLogo;
                    TextView title, desc;
                    switch (crtType) {
                        case 1:
                            view = ((Activity) context).getLayoutInflater().inflate(R.layout.api_banner7, null);
                            imgLogo = view.findViewById(R.id.img);
                            imgLogo.setVisibility(View.GONE);
                            title = view.findViewById(R.id.title);
                            desc = view.findViewById(R.id.desc);
                            titleText = data.getString("title");
                            descText = data.getString("description");
                            title.setText(titleText);
                            desc.setText(descText);
                            viewGroup.addView(view);
                            break;
                        case 2:
                            ImageView img = new ImageView(context);
                            imgUrl = data.getString("img_url");
                            //int w=1,h=2;
                            int width = AppUtils.getScreenWidths(context);
                            int height = width / 640 * 100;
                            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
                            img.setLayoutParams(layoutParams);
                            Glide.with(context).load(imgUrl).into(img);
                            viewGroup.addView(img);
                            break;
                        case 7:
                            view = ((Activity) context).getLayoutInflater().inflate(R.layout.api_banner7, null);
                            imgLogo = view.findViewById(R.id.img);
                            title = view.findViewById(R.id.title);
                            desc = view.findViewById(R.id.desc);
                            imgUrl = data.getString("img_url");
                            titleText = data.getString("title");
                            descText = data.getString("description");
                            Glide.with(context).load(imgUrl).into(imgLogo);
                            title.setText(titleText);
                            desc.setText(descText);
                            viewGroup.addView(view);
                            break;
                    }

                    listener.onAdLoadSuccess();
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
                    viewGroup.setOnClickListener(new View.OnClickListener() {
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
                } else {
                    listener.onAdLoadFail(ret, "");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                listener.onAdLoadFail(10001, e.getMessage());
            }
        }
    };
    public static void BannerAd(final Context ctx, final ViewGroup vg,final RMAdListener rmAdListener) {
        context=ctx;
        viewGroup=vg;
        listener=rmAdListener;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map map = AppUtils.getMap(context);
                map.put("uid", Constant.UID);
                map.put("ad_type", "1");
                String json = HttpClientUtil.requestGet(Constant.ApiAdTest, map);
                Message msg=new Message();
                msg.what=1;
                msg.obj=json;
                handler.sendMessage(msg);
            }
        }).start();

    }

}
