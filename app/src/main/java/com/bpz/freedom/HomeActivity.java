package com.bpz.freedom;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebViewFragment;

import com.bpz.commonlibrary.net.web.WebViewFragmentN;
import com.bpz.commonlibrary.ui.bottombar.BottomBar;
import com.bpz.commonlibrary.entity.BottomBarBean;
import com.bpz.commonlibrary.adapter.MyBottomBarAdapter;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.freedom.ui.fragment.FragmentMine;
import com.bpz.freedom.ui.fragment.TestFragment;
import com.bpz.freedom.ui.fragment.TestFragment2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements MyBottomBarAdapter.OnSelectedListener {
    /**
     * 主页选中图标
     */
    public static final int[] BGS_SELECTED = new int[]{
            R.drawable.maintab_stack_icon_press,
            R.drawable.maintab_category_icon_hover,
            R.drawable.maintab_city_icon_hover,
            R.drawable.maintab_stack_icon_press
    };
    /**
     * 主页未选中图标
     */
    public static final int[] BGS_UN_SELECTED = new int[]{
            R.drawable.maintab_stack_icon,
            R.drawable.maintab_category_icon,
            R.drawable.maintab_city_icon,
            R.drawable.maintab_stack_icon
    };
    private static final String TAG = "HomeActivity";
    @BindView(R.id.bottomBar)
    BottomBar bottomBar;
    List<Fragment> fragmentList;
    String[] titles;
    MyBottomBarAdapter adapter;
    Fragment currentFragment;
    private String POSITION = "position";
    private int currentPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        //获取主页的title数组
        titles = getResources().getStringArray(R.array.home_tab_title);
        //fragment生成
        fragmentList = new ArrayList<>();
        //fragmentList.add(Temp.newInstance("1"));
        fragmentList.add(new TestFragment2());
        //fragmentList.add(new CategoryFragment());
        fragmentList.add(WebViewFragmentN.newInstance("https://www.baidu.com",
                WebViewFragmentN.URL_ONLY, null, false));
        fragmentList.add(new TestFragment());
        fragmentList.add(new FragmentMine());

        if (savedInstanceState != null) {
            //崩溃造成的fragment显示异常处理
            int cachedId = savedInstanceState.getInt(POSITION, 0);
            if (cachedId >= 0 && cachedId < fragmentList.size()) {
                currentPosition = cachedId;
                LogUtil.e(TAG, "hide current: " + currentPosition);
                Fragment fragmentByTag = getFragmentManager().findFragmentByTag(
                        currentPosition + titles[currentPosition]
                );
                if (fragmentByTag != null) {
                    //如果缓存的fragment不为空就直接从容器里移除即可
                    getFragmentManager()
                            .beginTransaction()
                            .remove(fragmentByTag)
                            .commit();
                }
            }
        }
        //fragment的数量必须和title的数量保持一致
        List<BottomBarBean> bottomBarBeen = new ArrayList<>();
        //数据填充
        for (int i = 0; i < titles.length; i++) {
            bottomBarBeen.add(new BottomBarBean(
                    BGS_UN_SELECTED[i], BGS_SELECTED[i], titles[i], i == 0, fragmentList.get(i)));
        }
        //设置Adapter
        adapter = new MyBottomBarAdapter(bottomBarBeen,
                getFragmentManager(), this);
        bottomBar.setAdapter(adapter);
        //默认选中第一页
        adapter.setDefaultPosition(0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, currentPosition);
    }

    @Override
    public void onSelected(View itemView, Fragment currentFragment, BottomBarBean bottomBarBean, int position) {
        currentPosition = position;
    }
}
