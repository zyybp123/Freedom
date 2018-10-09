package com.bpz.freedom.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bpz.commonlibrary.LibApp;
import com.bpz.commonlibrary.mvp.BasePresenter;
import com.bpz.commonlibrary.ui.fragment.BaseFragment;
import com.bpz.commonlibrary.ui.widget.SortBar;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.util.StringUtil;
import com.bpz.freedom.R;
import com.bpz.freedom.adapter.Adapter2PopList;
import com.bpz.freedom.entity.Test2Entity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

public class SortBarTestFragment extends BaseFragment {
    @BindView(R.id.fr_recycler_view)
    RecyclerView frRecyclerView;
    @BindView(R.id.sb)
    SortBar sb;
    Unbinder unbinder;
    Adapter2PopList mAdapter;
    HashMap<String, Object> map = new HashMap<>();
    HashMap<String, Object> map2 = new HashMap<>();
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_py)
    TextView tvPy;
    Unbinder unbinder1;
    private List mList = new ArrayList();
    private List<String> list2First = new ArrayList<>();

    @Override
    public boolean isNeedLazy() {
        return false;
    }

    @Override
    public int getRootViewLayoutId() {
        return R.layout.test_2;
    }

    @Override
    public void initialRequest() {
        List<Test2Entity> namesList = new ArrayList<>();
        String pw_name = getFromAssets("pw_name.txt");
        if (!StringUtil.isSpace(pw_name)) {
            String[] split = pw_name.split(",");
            for (int i = 0; i < split.length; i++) {
                namesList.add(new Test2Entity(i + 1, split[i]));
            }
        }

        sb.setTextColor(Color.rgb(37, 148, 97));
        sb.setOnTouchListener(new SortBar.OnTouchListener() {
            @Override
            public void onTouch(int position, String s) {
                //1.先去集合中寻找首字母的条目，然后将其置顶
                for (int i = 0; i < mList.size(); i++) {
                    if ((mList.get(i) instanceof String) && s.equalsIgnoreCase((String) mList.get(i))) {
                        //说明找到了，那么直接置顶即可
                        LinearLayoutManager layoutManager = (LinearLayoutManager)
                                frRecyclerView.getLayoutManager();
                        if (layoutManager != null) {
                            layoutManager.scrollToPositionWithOffset(i, 0);
                        }
                        break;//找到立即中断
                    }
                }
                //显示当前按下的字母
                //UiUtils.showMyToastAB(s);
            }

        });
        frRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity,
                LinearLayoutManager.VERTICAL, false));
        mAdapter = new Adapter2PopList(mList);
        frRecyclerView.setAdapter(mAdapter);
        dataParse(namesList);
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*String str = "";
                if (!StringUtil.isSpace(s.toString()) && list2First != null) {
                    for (int i = 0; i < map2.size(); i++) {
                        String name = ((Test2Entity) list2First.get(i)).getName();
                        int id = ((Test2Entity) mList.get(i)).getId();
                        String nameId = StringUtil.getNotNullStr(name + id);
                        if (nameId.contains(s.toString())) {
                            tvPy.setText(nameId);
                            return;
                        }
                    }
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void viewHasBind() {
        frRecyclerView = mRootView.findViewById(R.id.fr_recycler_view);
        sb = mRootView.findViewById(R.id.sb);
        etName = mRootView.findViewById(R.id.et_name);
        tvPy = mRootView.findViewById(R.id.tv_py);
    }

    public String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(
                    getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            StringBuilder result = new StringBuilder();
            while ((line = bufReader.readLine()) != null)
                result.append(line);
            LogUtil.e(mFragmentTag, "result: " + result.toString());
            return result.toString();
        } catch (Exception e) {
            LogUtil.e(mFragmentTag, "Exception: " + e);
            e.printStackTrace();
        }
        return "";
    }

    private void dataParse(List<Test2Entity> data) {
        list2First.clear();
        mList.clear();
        map.clear();
        //重新排序
        if (data != null) {
            //取出名字并按名字保存
            for (int i = 0; i < data.size(); i++) {
                Test2Entity test2Entity = data.get(i);
                String name = test2Entity.getName();
                String stringPinYin = LibApp.chineseToHanYuPYTest.convertChineseToPinyin(name);
                String temp = "";
                if (!TextUtils.isEmpty(stringPinYin)) {
                    temp = "" + stringPinYin.charAt(0) + name;
                }
                map.put(name, test2Entity);
                map2.put(temp,test2Entity);
                list2First.add(temp);
            }
            //将名字集合排序
            Collections.sort(list2First);
            if (list2First.size() > 0) {
                String sName = list2First.get(0);
                char s1 = '#';
                if (!TextUtils.isEmpty(sName)) {
                    s1 = sName.charAt(0);
                }
                mList.add(s1 + "");
                for (int i = 0; i < list2First.size(); i++) {
                    String str = list2First.get(i);
                    if (!TextUtils.isEmpty(str)) {
                        if (str.charAt(0) != s1) {
                            s1 = str.charAt(0);
                            mList.add(s1 + "");
                        }
                        mList.add(map.get(str.substring(1)));
                    }
                }
            }

            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
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

}
