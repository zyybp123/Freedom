package com.bpz.freedom.ui;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpz.commonlibrary.entity.NotifyEntity;
import com.bpz.commonlibrary.manager.MyNotificationManager;
import com.bpz.commonlibrary.net.web.WebViewFragmentN;
import com.bpz.commonlibrary.ui.banner.PBanner;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.freedom.ImgUrl;
import com.bpz.freedom.R;
import com.bpz.freedom.entity.BannerEntity;
import com.zhihu.matisse.Matisse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SomeTestActivity extends AppCompatActivity {
    private static final String TAG = "SomeTestActivity";
    private static final int REQUEST_CODE_CHOOSE = 999;
    @BindView(R.id.tv_choose)
    TextView tvChoose;
    List<Uri> mSelected;
    @BindView(R.id.banner)
    PBanner<BannerEntity> banner;
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
            }
        });
        final WebViewFragmentN webViewFragmentN = WebViewFragmentN.newInstance(
                "", WebViewFragmentN.URL_ONLY, null, false
        );
        getFragmentManager()
                .beginTransaction()
                .add(R.id.fr_frame_layout_container, webViewFragmentN, "WebViewFragmentN")
                .commit();
        List<BannerEntity> list = new ArrayList<>();
        for (int i = 0; i < ImgUrl.IMG_S.length; i++) {
            list.add(new BannerEntity(ImgUrl.IMG_S[i], ImgUrl.IMG_S[i],"标题" + (i + 1)));
        }
        banner.setModel(PBanner.Model.ONLY_INDICATOR);
        banner.setLocation(PBanner.Location.CENTER);
        banner.setDataList(list);
        banner.setBannerListener(new PBanner.BannerListener<BannerEntity>() {
            @Override
            public void onItemClick(View view,int position, BannerEntity itemData) {
                //条目点击事件
                String bannerClickUrl = itemData.getBannerClickUrl();
                webViewFragmentN.loadWebPage(bannerClickUrl);
            }
        });
    }

    @Override
    protected void onResume() {
        if (banner != null) {
            banner.autoStart();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (banner != null){
            banner.autoStop();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (banner != null){
            banner.autoStop();
        }
        super.onDestroy();
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
