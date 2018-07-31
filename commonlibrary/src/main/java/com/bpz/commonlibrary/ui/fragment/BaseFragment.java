package com.bpz.commonlibrary.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bpz.commonlibrary.bus.RxBus;
import com.bpz.commonlibrary.mvp.BasePresenter;
import com.bpz.commonlibrary.mvp.BaseView;
import com.bpz.commonlibrary.util.LogUtil;
import com.trello.rxlifecycle2.components.RxFragment;

/**
 * Created by ZYY
 * on 2018/1/16 21:36.
 * Fragment的基类，可以控制是否进行懒加载
 *
 * @author ZYY
 */

public abstract class BaseFragment<P extends BasePresenter>
        extends RxFragment implements BaseView{
    public String mFragmentTag = this.getClass().getSimpleName();
    /**
     * 屏幕的宽高
     */
    public int screenWidth;
    public int screenHeight;
    /**
     * 以状态层布局作为根布局
     */
    public View mRootView;
    //public Unbinder unbinder;
    /**
     * 是否需要懒加载,默认不需要，false，不需要
     */
    public boolean isNeedLazy;
    /**
     * 宿主Activity
     */
    public Activity mActivity;
    /**
     * 视图是否已经初初始化
     */
    protected boolean isInit = false;
    /**
     * 是否是第一次加载
     */
    protected boolean isFirstLoad = false;
    /**
     * 网络请求管理器
     */
    protected P mPresenter;

    /**
     * 空参构造，初始化一些值
     */
    public BaseFragment() {
        isNeedLazy = isNeedLazy();

    }

    /**
     * 是否需要懒加载，由子类实现
     *
     * @return 返回true则需要
     */
    public abstract boolean isNeedLazy();

    /**
     * @param isVisibleToUser 对用户是否可见,一旦可见就会被回调
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtil.e(mFragmentTag + getTag(), " setUserVisibleHint() --> isVisibleToUser = " + isVisibleToUser);
        //对用户可见时，又不需要更新ui时的操作可在此处进行
        if (isNeedLazy) {
            isCanLoadData();
        }
    }

    /**
     * 创建view，加载布局
     *
     * @param inflater           布局加载器
     * @param container          容器
     * @param savedInstanceState 保存fragment的状态
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //在view pager里会多次被调用
        LogUtil.e(mFragmentTag + getTag(), " onCreateView()");
        //创根布局,butterKnife绑定控件
        if (mRootView == null) {
            //只有根布局为空的时候才创建新的布局
            mRootView = inflater.inflate(getRootViewLayoutId(), container, false);
        }
        return mRootView;
    }

    /**
     * 获取根布局，由子类来实现
     *
     * @return 返回根布局资源id
     */
    @LayoutRes
    public abstract int getRootViewLayoutId();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.e(mFragmentTag + getTag(), " onActivityCreated()");
    }

    /**
     * 能否加载数据
     */
    private void isCanLoadData() {
        if (!isInit) {
            //未完成初始化，直接返回
            return;
        }
        //如果需要懒加载
        if (isNeedLazy) {
            if (getUserVisibleHint() && isFirstLoad) {
                //可见，且第一次可见才发起数据请求
                initialRequest();
                isFirstLoad = false;
            }
        } else {
            //不需要懒加载，直接发起请求
            initialRequest();
        }
    }

    /**
     * 发起请求的方法
     */
    public abstract void initialRequest();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtil.e(mFragmentTag + getTag(), " onAttach()");
        //attach方法在viewpager里只会执行一次，用它来判断是否是该页第一次加载
        isFirstLoad = true;
        //获取屏幕宽高
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        //获取宿主activity实例，缓存到内存（可能会内存泄漏，但比getActivity获得null安全一些）
        mActivity = activity;
        //获取Presenter对象
        mPresenter = getPresenter();
    }

    /**
     * 由子类提供presenter对象
     *
     * @return 返回一个presenter
     */
    protected abstract P getPresenter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e(mFragmentTag + getTag(), " onCreate()......" + getUserVisibleHint());
    }

    /**
     * 界面布局成功加载后
     *
     * @param view               根布局
     * @param savedInstanceState 保存fragment的状态
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtil.e(mFragmentTag + getTag(), " onViewCreated() ---> " + mRootView);
        //留给子类初始化控件用
        initView();
        //此时的View一定存在，在此完成绑定等相关操作
        //unbinder = ButterKnife.bind(this, view);
        //留给子类在绑定完控件后初始化页面
        viewHasBind();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        isInit = true;
        //初始化完成的时候去加载数据
        isCanLoadData();
    }

    /**
     * 初始化界面，比如找到控件
     */
    public void initView() {

    }

    /**
     * 根布局已经绑定成功，子类强制实现
     */
    public abstract void viewHasBind();

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.e(mFragmentTag + getTag(), " onStart()");
        LogUtil.e(mFragmentTag + getTag(), "getUserVisibleHint..." + getUserVisibleHint());
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.e(mFragmentTag + getTag(), " onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.e(mFragmentTag + getTag(), " onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.e(mFragmentTag + getTag(), " onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInit = false;
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        onMyDestroyView();
        LogUtil.e(mFragmentTag + getTag(), "onDestroyView()");
    }

    /**
     * 子类若是要调用onDestroyView，可直接覆盖此方法
     */
    public void onMyDestroyView() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e(mFragmentTag + getTag(), "onDestroy()");
    }
}
