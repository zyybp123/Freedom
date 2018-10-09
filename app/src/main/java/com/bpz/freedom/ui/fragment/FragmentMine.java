package com.bpz.freedom.ui.fragment;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bpz.commonlibrary.adapter.MyFilterBarAdapter;
import com.bpz.commonlibrary.interf.listener.OnItemClickListener;
import com.bpz.commonlibrary.mvp.BasePresenter;
import com.bpz.commonlibrary.ui.fragment.BaseFragment;
import com.bpz.commonlibrary.ui.pop.PPopupWindow;
import com.bpz.commonlibrary.ui.widget.BigImgDragView;
import com.bpz.commonlibrary.ui.widget.LinearContainer;
import com.bpz.commonlibrary.ui.widget.MySurfaceView3;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.util.SpanUtil;
import com.bpz.commonlibrary.util.StringUtil;
import com.bpz.freedom.R;
import com.bpz.freedom.entity.FilterTestEntity;
import com.bpz.freedom.global.Freedom;
import com.bpz.freedom.net.TzqHost;
import com.bpz.freedom.ui.SomeTestActivity;
import com.qmuiteam.qmui.widget.textview.QMUILinkTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

public class FragmentMine extends BaseFragment {

    EditText etHost;
    TextView tvSure;
    TextView tvTest;
    LinearLayout llTest;
    LinearContainer filterBar;
    Unbinder unbinder;
    @BindView(R.id.bidv)
    BigImgDragView bidv;

    @Override
    public boolean isNeedLazy() {
        return false;
    }

    @Override
    public int getRootViewLayoutId() {
        return R.layout.fragment_mine_layout;
    }

    @Override
    public void initialRequest() {

    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void viewHasBind() {
        etHost = mRootView.findViewById(R.id.et_host);
        tvSure = mRootView.findViewById(R.id.btn_sure);
        tvTest = mRootView.findViewById(R.id.tv_test);
        filterBar = mRootView.findViewById(R.id.fr_filter_bar);
        tvTest.setMovementMethod(LinkMovementMethod.getInstance());
        llTest = mRootView.findViewById(R.id.ll_test);
        String text = "vafiz12yyhaasf你好sz12yyi你好dofizSLLLLL11yyaaaslsdf你zyy好slkdajfiojos你好a你好id" +
                "jfohd你好你好soifwsdsshszyyohfasil你好lkzSL12LL展开全文LLyylswsdss";
        String[] changes = new String[]{"你好", "zyy", "i", "ss"};
        Integer[] colors = new Integer[]{Color.MAGENTA, Color.RED, Color.GREEN};
        String[] changes1 = new String[]{"LLL"};
        String[] changes2 = new String[]{"12"};
        String[] changes3 = new String[]{"aas"};
        String[] changes4 = new String[]{"wsdss"};
        String[] changes5 = new String[]{"展开全文"};
        SpannableString spannableString3 = SpanUtil.changeColors(
                new SpannableString(text), Arrays.asList(changes), Arrays.asList(colors)
                , SpanUtil.COLOR_FOREGROUND);
        SpannableString underline = SpanUtil.someStyle(spannableString3, Arrays.asList(changes1)
                , SpanUtil.STYLE_UNDERLINE);
        SpannableString strike = SpanUtil.someStyle(underline, Arrays.asList(changes2)
                , SpanUtil.STYLE_STRIKETHROUGH);
        SpannableString sub = SpanUtil.someStyle(strike, Arrays.asList(changes3)
                , SpanUtil.STYLE_SUBSCRIPT);
        SpannableString sup = SpanUtil.someStyle(sub, Arrays.asList(changes4)
                , SpanUtil.STYLE_SUPERSCRIPT);
        SpanUtil.someClick(sup, Arrays.asList(changes5), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.e(mFragmentTag, "click ... " + v);
            }
        });
        QMUILinkTextView textView = new QMUILinkTextView(mActivity);
        textView.setText("1234567,http://www.baidu.com,ajdallskdla;kdlkal;");
        //textView.set
        tvTest.setText(sup);

        llTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SomeTestActivity.startSelf(mActivity);
            }
        });

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
        List<FilterTestEntity> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            FilterTestEntity entity = new FilterTestEntity();
            entity.setBarTile("筛选条件" + i);
            list.add(entity);
        }
        final PPopupWindow pPopupWindow = new PPopupWindow(mActivity);
        //filterBar.mParams.gravity = Gravity.CENTER;
        filterBar.setAdapter(new MyFilterBarAdapter<>(list,
                new OnItemClickListener<FilterTestEntity>() {
                    @Override
                    public void onItemClick(View v, int position, FilterTestEntity itemData) {
                        switch (position) {
                            case 0:
                                //pPopupWindow.showPop(v, false);
                                break;
                        }
                    }
                }));

        //bidv.setZOrderOnTop(true);
        //bidv.getHolder().setFormat(PixelFormat.RGBA_8888);

        //bidv.setZOrderMediaOverlay(true);

        /*bidv.setBitmap(BitmapFactory.decodeFile(
                "/sdcard/MagazineUnlock/" +
                        "blues-13-2.3.001-bigpicture_13_1_land.jpg"));*/

    }


    @Override
    public void onSubscribe(String methodTag, Disposable d) {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onError(String methodTag, String describe) {

    }

    @Override
    public void onEmpty(String methodTag) {

    }

    @Override
    public void noNet() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
