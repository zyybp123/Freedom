package com.bpz.freedom.ui.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bpz.commonlibrary.ui.fragment.BaseFragment;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.util.SPUtil;
import com.bpz.commonlibrary.util.StringUtil;
import com.bpz.freedom.R;
import com.bpz.freedom.entity.tzq.LoginInfo;
import com.bpz.freedom.global.Freedom;
import com.bpz.freedom.net.TzqHost;
import com.google.gson.Gson;

public class FragmentMine extends BaseFragment {

    EditText etHost;
    TextView tvSure;

    @Override
    public boolean isNeedLazy() {
        return false;
    }

    @Override
    public int getRootViewLayoutId() {
        return R.layout.fragment_mine_layout;
    }

    @Override
    public void viewHasBind() {
        etHost = mRootView.findViewById(R.id.et_host);
        tvSure = mRootView.findViewById(R.id.btn_sure);

        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String host = StringUtil.getNotNullStr(etHost.getText().toString());
                Freedom.mBaseUrlMap.put(TzqHost.TAG_TZQ, host);
                LogUtil.e(mFragmentTag, "urlMap: " + Freedom.mBaseUrlMap);

                //SPUtil.getInstance("setUp").put("host", host);
                //SPUtil.getInstance("config").put("aaa", 123);
            }
        });

        new Thread() {
            @Override
            public void run() {
                LogUtil.e(mFragmentTag, "current thread:  " + Thread.currentThread());
            }
        }.start();
    }

    @Override
    public void initialRequest() {

    }
}
