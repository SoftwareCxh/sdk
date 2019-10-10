package com.learning.ad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.baidu.mobads.InterstitialAd;
import com.learning.bdsdk.BDEntry;
import com.learning.bdsdk.ad.listener.BDInterstitialListener;
import com.learning.sdk.AdSDkEntry;
import com.learning.sdk.listener.RMAdListener;
import com.learning.sdk.listener.RewardAdListener;
import com.learning.sdk.listener.RewardModel;

public class AdListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_list);
        Button banner = findViewById(R.id.banner);
        final Button in = findViewById(R.id.interstitial);
        Button rewardVideo = findViewById(R.id.reward_video);
        final Button feed=findViewById(R.id.feed);
        final AdSDkEntry entry = AdSDkEntry.shareInstance(getApplicationContext(), "9811");
        final FrameLayout banner_root = findViewById(R.id.banner_root);
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entry.BannerEntry(AdListActivity.this, "9811", banner_root, new RMAdListener() {
                    @Override
                    public void onAdLoadSuccess() {

                    }

                    @Override
                    public void onAdLoadFail(int code, String msg) {

                    }

                    @Override
                    public void onAdClicked() {

                    }

                    @Override
                    public void onAdDismiss() {

                    }
                });
            }
        });

        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entry.InteractionEntry(AdListActivity.this, "9811", new RMAdListener() {
                    @Override
                    public void onAdLoadSuccess() {

                    }

                    @Override
                    public void onAdLoadFail(int code, String msg) {

                    }

                    @Override
                    public void onAdClicked() {

                    }

                    @Override
                    public void onAdDismiss() {

                    }
                });
            }
        });
        rewardVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entry.RewardVideoEntry(AdListActivity.this, "9811", new RewardAdListener() {
                    @Override
                    public void onVideoPlayFinish() {

                    }

                    @Override
                    public void onReward(RewardModel rewardModel) {

                    }

                    @Override
                    public void onAdLoadSuccess() {

                    }

                    @Override
                    public void onAdLoadFail(int code, String msg) {

                    }

                    @Override
                    public void onAdClicked() {

                    }

                    @Override
                    public void onAdDismiss() {

                    }
                });

            }
        });
        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdListActivity.this,FeedListActivity.class);
                startActivity(intent);
            }
        });

    }
}
