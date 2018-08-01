package com.bpz.freedom.ui;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.bpz.commonlibrary.MyMimeType;
import com.bpz.commonlibrary.entity.NotifyEntity;
import com.bpz.commonlibrary.manager.MyNotificationManager;
import com.bpz.commonlibrary.net.Glide4Engine;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.freedom.R;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SomeTestActivity extends AppCompatActivity {
    private static final String TAG = "SomeTestActivity";
    private static final int REQUEST_CODE_CHOOSE = 999;
    @BindView(R.id.tv_choose)
    TextView tvChoose;
    List<Uri> mSelected;
    private int notifyId;

    public static void startSelf(Activity activity) {
        Intent intent = new Intent(activity, SomeTestActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_some_test);
        ButterKnife.bind(this);
        tvChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NotifyEntity entity = new NotifyEntity();
                entity.setTitle("测试通知！");
                entity.setIconRes(R.mipmap.ic_launcher);
                entity.setContentText("跫音不响，三月的春帷不揭，你的心是小小的寂寞的城！");
                entity.setFlags(Notification.FLAG_AUTO_CANCEL);
                MyNotificationManager
                        .getInstance(SomeTestActivity.this)
                        .sendDefaultNotification(notifyId, entity);
                notifyId++;

                /*Matisse.from(SomeTestActivity.this)
                        .choose(MimeType.ofAll())
                        .countable(true)
                        .maxSelectable(9)
                        //.addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .gridExpectedSize(getResources()
                                .getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new Glide4Engine())
                        .forResult(REQUEST_CODE_CHOOSE);*/
                // MimeType
            }
        });
        /*new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    int a = 100 / 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            LogUtil.e(TAG, "mSelected: " + mSelected);
        }
    }
}
