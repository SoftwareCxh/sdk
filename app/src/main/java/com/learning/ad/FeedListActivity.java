package com.learning.ad;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.learning.sdk.AdSDkEntry;
import com.learning.sdk.listener.FeedAdListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FeedListActivity extends AppCompatActivity {
    AdSDkEntry entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_list);
        entry = AdSDkEntry.shareInstance(this, "9811");

        ListView listView = findViewById(R.id.list_item);
        List<String> list = new ArrayList();
        for (int i = 0; i < 20; i++) {
            list.add("code" + i);
        }
        MyAdapter myAdapter = new MyAdapter(this, list);
        listView.setAdapter(myAdapter);

    }

    class MyAdapter extends BaseAdapter {
        List<String> list;
        Context context;

        MyAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position % 5 == 1) {
                convertView = new FrameLayout(context);
                entry.FeedListEntry(context, convertView, new FeedAdListener() {
                    @Override
                    public void dislike() {

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
            } else {
                convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.list, null);
                TextView textView = convertView.findViewById(R.id.text);
                textView.setText(list.get(position));
            }
            return convertView;
        }
    }
}
