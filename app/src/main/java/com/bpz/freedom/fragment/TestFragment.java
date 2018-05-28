package com.bpz.freedom.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpz.commonlibrary.ui.banner.PBanner;
import com.bpz.commonlibrary.ui.widget.CustomEditText;
import com.bpz.commonlibrary.ui.widget.StateLayout;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.freedom.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TestFragment extends Fragment {

    private StateLayout stateLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        List<String> mList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mList.add("" + i);
        }
        stateLayout = (StateLayout) inflater.inflate(R.layout.base_state_layout, null);
        stateLayout.setListener(new StateLayout.OnRetryListener() {
            @Override
            public void onRetry(int state) {
                LogUtil.e("---retry click---");
            }
        });
        PBanner<String> banner = (PBanner<String>) inflater
                .inflate(R.layout.banner, null);
        banner.setData(new PBanner.ImgShowListener<String>() {
            @Override
            public void setImgShow(ImageView imageView, String data) {
                Glide.with(getActivity()).load(data).into(imageView);
            }
        },mList);
        banner.startLoop();
        stateLayout.setStatePage(StateLayout.State.ON_SUCCESS, banner);
        return stateLayout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stateLayout.showCurrentPage(StateLayout.State.ON_NO_NET);
        /*new Timer().schedule(new TimerTask() {
            int count = -2;

            @Override
            public void run() {
                count++;
                if (count >= 5) {
                    cancel();
                }
                if (getActivity() != null) {
                    getActivity()
                            .runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    stateLayout.showCurrentPage(count);
                                }
                            });
                }
            }
        }, 1000, 3000);*/

    }


}
