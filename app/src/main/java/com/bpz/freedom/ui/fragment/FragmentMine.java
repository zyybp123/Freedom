package com.bpz.freedom.ui.fragment;

import android.graphics.Color;
import android.text.SpannableString;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bpz.commonlibrary.ui.fragment.BaseFragment;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.util.SPUtil;
import com.bpz.commonlibrary.util.SpanUtil;
import com.bpz.commonlibrary.util.StringUtil;
import com.bpz.freedom.R;
import com.bpz.freedom.entity.tzq.LoginInfo;
import com.bpz.freedom.global.Freedom;
import com.bpz.freedom.net.TzqHost;
import com.google.gson.Gson;

import java.util.Arrays;

public class FragmentMine extends BaseFragment {

    EditText etHost;
    TextView tvSure;
    TextView tvTest;

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
        tvTest = mRootView.findViewById(R.id.tv_test);
        String text = "vafiz12yyhaasf你好sz12yyi你好dofizSLLLLL11yyaaaslsdf你zyy好slkdajfiojos你好a你好id" +
                "jfohd你好你好soifwsdsshszyyohfasil你好lkzSL12LLLLyylswsdss";
        String[] changes = new String[]{"你好", "zyy", "i", "ss"};
        Integer[] colors = new Integer[]{Color.MAGENTA, Color.RED, Color.GREEN};
        String[] changes1 = new String[]{"LLL"};
        String[] changes2 = new String[]{"12"};
        String[] changes3 = new String[]{"aas"};
        String[] changes4 = new String[]{"wsdss"};
        SpannableString spannableString3 = SpanUtil.changeColors(
                new SpannableString(text), Arrays.asList(changes), Arrays.asList(colors)
                , SpanUtil.COLOR_FOREGROUND);
        SpannableString underline = SpanUtil.someStyle(spannableString3, Arrays.asList(changes1)
        ,SpanUtil.STYLE_UNDERLINE);
        SpannableString strike = SpanUtil.someStyle(underline, Arrays.asList(changes2)
                , SpanUtil.STYLE_STRIKETHROUGH);
        SpannableString sub = SpanUtil.someStyle(strike, Arrays.asList(changes3)
                , SpanUtil.STYLE_SUBSCRIPT);
        SpannableString sup = SpanUtil.someStyle(sub, Arrays.asList(changes4)
                , SpanUtil.STYLE_SUPERSCRIPT);
        tvTest.setText(sup);


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
