package com.bpz.freedom.ui;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpz.commonlibrary.adapter.BannerPagerAdapter;
import com.bpz.commonlibrary.entity.NotifyEntity;
import com.bpz.commonlibrary.interf.listener.OnImgShowListener;
import com.bpz.commonlibrary.manager.MyNotificationManager;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.freedom.ImgUrl;
import com.bpz.freedom.R;
import com.bumptech.glide.Glide;
import com.zhihu.matisse.Matisse;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SomeTestActivity extends AppCompatActivity {
    private static final String TAG = "SomeTestActivity";
    private static final int REQUEST_CODE_CHOOSE = 999;
    @BindView(R.id.tv_choose)
    TextView tvChoose;
    List<Uri> mSelected;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    List<String> stringList;
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

        stringList = Arrays.asList(ImgUrl.IMG_S);
        final BannerPagerAdapter<String> adapter = new BannerPagerAdapter<>(stringList, new OnImgShowListener<String>() {
            @Override
            public void onImageShow(ImageView imageView, String data) {
                Glide.with(SomeTestActivity.this)
                        .load(data)
                        .into(imageView);
            }
        });
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(stringList.size() <= 1 ? 0 : getCurrentStart0());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int realPosition = stringList.size() <= 1 ? position : position % stringList.size();
                LogUtil.e(TAG, "current position: " + realPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private int getCurrentStart0() {
        return Integer.MAX_VALUE / 2 / stringList.size() * stringList.size();
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
