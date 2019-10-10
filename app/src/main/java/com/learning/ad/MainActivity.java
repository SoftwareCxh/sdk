package com.learning.ad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import com.learning.sdk.AdSDkEntry;
import com.learning.sdk.listener.RMAdListener;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout frameLayout=findViewById(R.id.splash_container);
        final AdSDkEntry entry= AdSDkEntry.shareInstance(this,"9811");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       entry.SplashAD(this, "", frameLayout, new RMAdListener() {
            @Override
            public void onAdLoadSuccess() {

            }

            @Override
            public void onAdLoadFail(int code, String msg) {
                Log.e(code + "", msg);
                ;
            }

            @Override
            public void onAdClicked() {

            }

            @Override
            public void onAdDismiss() {
                Intent intent=new Intent(MainActivity.this,AdListActivity.class);
                getApplication().startActivity(intent);
                finish();

            }
        });
        //BDEntry bdEntry=BDEntry.shareInstance(this,"e866cfb0");
//
//        GDTEntry gdtEntry=GDTEntry.shareInstance(this);
//        gdtEntry.SplashEntry(this, "1101152570", "8863364436303842593", frameLayout, frameLayout, new GDTSplashListener() {
//            @Override
//            public void onADDismissed() {
//               // Log.e("点击跳过","123123131312312312313123");
//            }
//
//            @Override
//            public void onNoAD(AdError adError) {
//
//            }
//
//            @Override
//            public void onADPresent() {
//
//            }
//
//            @Override
//            public void onADClicked() {
//
//            }
//
//            @Override
//            public void onADTick(long l) {
//
//            }
//
//            @Override
//            public void onADExposure() {
//               // Log.e("点击跳过","123123131312312312313123");
//            }
//        });




    }
}
