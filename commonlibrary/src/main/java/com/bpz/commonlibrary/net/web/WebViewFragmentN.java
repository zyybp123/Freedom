package com.bpz.commonlibrary.net.web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bpz.commonlibrary.BuildConfig;
import com.bpz.commonlibrary.R;
import com.bpz.commonlibrary.entity.HpmFileBean;
import com.bpz.commonlibrary.entity.ResInfo;
import com.bpz.commonlibrary.manager.MyNotificationManager;
import com.bpz.commonlibrary.net.download.MyDownloadListener;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.util.StringUtil;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;


/**
 * WebView的封装
 */
public class WebViewFragmentN extends Fragment implements IWebListener, View.OnClickListener,
        MyDownloadListener {
    public static final String WEB_CACHE_DIR = "/fr/web/cache";
    public static final String PATH_HEADER = "file://";
    public static final String BASE_URL_ASSETS = PATH_HEADER + "/android_asset";
    public static final int URL_ONLY = 0;
    public static final int URL_IN_LOCAL = 1;
    public static final int URL_IN_ASSETS = 2;
    public static final int STR_HTML = 3;
    public static final String HAS_AUTH = "hasAuth";
    public static final String ABOUT_BLANK = "about:blank";
    public static final String HPM_FILE = "hpmFile";
    private static final String TAG = "WebViewFragmentN";
    private static final String JS_TAG = "frAndroid";
    private static final int TEXT_ZOOM = 100;
    private static final int APP_MAX_CACHE_SIZE = 80 * 1024 * 1024;
    private static final String URL = "url";
    private static final String PARAMS_TAG = "paramsTag";
    private static final String ENCODING = "utf-8";
    private static final String MIME_TYPE = "text/html";
    public Activity mActivity;
    // 网页是否加载完成
    public boolean mPageFinish;
    // 全屏时视频加载view
    FrameLayout videoFullView;
    ImageButton ibtnBack;
    ImageButton ibtnClose;
    TextView tvTitle;
    ImageButton ibtnMenu;
    LinearLayout llHeader;
    ProgressBar pbProgress;
    WebView webView;
    WebChromeClientImpl webChromeClient;
    private String url = "";
    private int urlTag;
    private HpmFileBean configBean;
    private boolean showTitleBar = true;
    private boolean showMenu = false;
    private boolean showCloseIcon = false;
    private boolean showTitle = true;
    private boolean showLoading = true;
    private boolean showToolBar = false;
    /**
     * 是否显示固定的标题
     */
    private boolean isShowFixedTitle = false;
    private String fixedTitle = "";
    /**
     * 是否有授权，默认没有
     */
    private boolean hasAuth = false;
    //private Bubble<HpmFileBean.MenuParamsBean> bubble = new Bubble<>();
    private View mRootView;

    /**
     * 传入标识和对应内容
     *
     * @param url 支持传入链接，本地html文件的全路径，assets的文件名，富文本字符串
     * @param tag url，local，assets，富文本
     */
    public static WebViewFragmentN newInstance(String url, int tag,
                                               HpmFileBean configBean, boolean hasAuth) {
        WebViewFragmentN fragmentOne = new WebViewFragmentN();
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        bundle.putInt(PARAMS_TAG, tag);
        bundle.putParcelable(HPM_FILE, configBean);
        bundle.putBoolean(HAS_AUTH, hasAuth);
        fragmentOne.setArguments(bundle);
        return fragmentOne;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fr_web_view_fragment_layout, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            url = StringUtil.getNotNullStr(arguments.getString(URL));
            urlTag = arguments.getInt(PARAMS_TAG);
            configBean = arguments.getParcelable(HPM_FILE);
            hasAuth = arguments.getBoolean(HAS_AUTH);
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //视图已经创建完毕
        manager = MyNotificationManager.getInstance(mActivity);
        initView();
        initWebView();
        ibtnBack.setOnClickListener(this);
        ibtnClose.setOnClickListener(this);
        setDefaultWebSettings(webView);
        setDefaultReactiveSettings(configBean);
        switch (urlTag) {
            case URL_ONLY:
                //只是url
                loadWebPage(url);
                break;
            case URL_IN_ASSETS:
                //加载assets的html文件（传入的是文件名）
                loadWebPage(BASE_URL_ASSETS + url);
                break;
            case URL_IN_LOCAL:
                //加载本地文件
                loadWebPage(PATH_HEADER + File.separator + url);
                break;
            case STR_HTML:
                //加载富文本
                webView.loadDataWithBaseURL(null, url, MIME_TYPE,
                        ENCODING, null);
                break;
            default:
                //默认加载urlOnly
                loadWebPage(url);
                break;
        }
        LogUtil.e(TAG, "urlTag: " + urlTag + ", url: " + url);
    }

    private void initView() {
        webView = mRootView.findViewById(R.id.fr_web_view);
        ibtnBack = mRootView.findViewById(R.id.fr_back);
        ibtnClose = mRootView.findViewById(R.id.fr_close);
        ibtnMenu = mRootView.findViewById(R.id.fr_menu);
        pbProgress = mRootView.findViewById(R.id.fr_pb_progress);
        tvTitle = mRootView.findViewById(R.id.fr_tv_title);
        llHeader = mRootView.findViewById(R.id.fr_ll_header);
        videoFullView = mRootView.findViewById(R.id.fr_video_fullView);
    }

    private void initWebView() {
        webView.setInitialScale(1);
        webChromeClient = new WebChromeClientImpl(this, this);
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(new WebClientImpl(mActivity, this));
        webView.setDownloadListener(new WebDownloadListener(this));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(BuildConfig.HAS_LOG);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setDefaultWebSettings(@NotNull WebView webView) {
        WebSettings webSettings = webView.getSettings();
        //5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //设置加载进来的页面自适应手机屏幕
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        // 排版适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        //缩放控制，支持缩放，使用内部变焦机制，不展现在屏幕控件上，禁用文字缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setTextZoom(TEXT_ZOOM);
        // WebView是否支持多个窗口。
        webSettings.setSupportMultipleWindows(true);
        //允许js代码
        webSettings.setJavaScriptEnabled(true);
        //与js交互
        webView.addJavascriptInterface(new JsInterface(
                mActivity, this, hasAuth), JS_TAG);
        // 启动应用缓存，设置缓存模式，缓存路径
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAppCachePath(mActivity.getCacheDir().getAbsolutePath()
                + WEB_CACHE_DIR);
        //80M缓存，api 18后，系统自动管理。
        webSettings.setAppCacheMaxSize(APP_MAX_CACHE_SIZE);
        //允许SessionStorage/LocalStorage存储
        webSettings.setDomStorageEnabled(true);
        //if (NetworkUtils.isConnected()) {
        //根据cache-control决定是否从网络上取数据。
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //} else {
        //没网，则从本地获取，即离线加载
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //}
        //开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        // 保存表单数据
        webSettings.setSaveFormData(true);
        //允许WebView使用File协议
        webSettings.setAllowFileAccess(true);
        //自动加载图片
        webSettings.setLoadsImagesAutomatically(true);

    }

    /**
     * 根据配置数据实体来配置相关信息
     */
    public void setDefaultReactiveSettings(HpmFileBean configBean) {
        if (configBean == null) {
            //不传信息，不配置
            return;
        }
        HpmFileBean.LaunchParamsBean launchParams = configBean.getLaunchParams();
        if (launchParams == null) {
            return;
        }
        LogUtil.e(TAG, "configBean: " + configBean);
        showMenu = launchParams.isShowMenu();
        showCloseIcon = launchParams.isShowCloseIcon();
        showLoading = launchParams.isShowLoading();
        showTitle = launchParams.isShowTitle();
        showTitleBar = launchParams.isShowTitleBar();
        showToolBar = launchParams.isShowToolBar();
        isShowFixedTitle = launchParams.isShowFixedTitle();
        fixedTitle = launchParams.getFixedTitle();
        setTitleBar(fixedTitle);
        setToolBar();
        setMenu(configBean);
        setLoadingBar();
    }

    private void loadWebPage(String s) {
        if (webView != null) {
            webView.loadUrl(s);
        }
    }

    private void setTitleBar(String fixedTitle) {
        if (llHeader != null) {
            llHeader.setVisibility(showTitleBar ? View.VISIBLE : View.GONE);
        }
        if (tvTitle != null) {
            tvTitle.setText(isShowFixedTitle ? fixedTitle : "");
            tvTitle.setVisibility(showTitle ? View.VISIBLE : View.GONE);
        }
        if (ibtnClose != null) {
            ibtnClose.setVisibility(showCloseIcon ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void setToolBar() {
        // todo
    }

    private void setMenu(final HpmFileBean configBean) {
        if (ibtnMenu != null) {
            ibtnMenu.setVisibility(showMenu ? View.VISIBLE : View.INVISIBLE);
            ibtnMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击弹出菜单
                    bubbleSet(configBean.getMenuParams());
                }
            });
        }

    }

    private void setLoadingBar() {
        if (pbProgress != null) {
            pbProgress.setVisibility(showLoading ? View.VISIBLE : View.GONE);
        }
    }

    private void bubbleSet(final List<HpmFileBean.MenuParamsBean> titles) {
        if (titles == null || titles.size() == 0) {
            //列表为空，说明没有菜单，不处理
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;
        layoutChange(orientation == Configuration.ORIENTATION_PORTRAIT);
    }

    /**
     * 屏幕方向的变更
     *
     * @param portrait 是否竖直 true代表屏幕竖直
     */
    private void layoutChange(boolean portrait) {

    }

    @Override
    public void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (videoFullView != null) {
            videoFullView.removeAllViews();
        }
        if (webView != null) {
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                parent.removeView(webView);
            }
            webView.removeAllViews();
            webView.loadUrl(ABOUT_BLANK);
            webView.stopLoading();
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.destroy();
            webView = null;
        }
    }

    MyNotificationManager manager ;

    public FrameLayout getVideoFullView() {
        return videoFullView;
    }

    @Override
    public void onDownloadStart() {
        manager.sendNotification(mActivity,"下载中...","..",R.drawable.fr_empty);
    }

    @Override
    public void onDownloadSuccess(ResInfo resInfo) {
        manager.cancelNotification();
        LogUtil.e("下载完成..." + resInfo);
    }

    @Override
    public void onDownloadFail(Throwable e) {
        manager.cancelNotification();
    }

    @Override
    public void onDownloading(int progress) {
        LogUtil.e(TAG, "progress: " + progress);
        //manager.updateNotification(progress);
    }

    @Override
    public void fullViewAddView(View view) {
        FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
        videoFullView = new FrameLayout(mActivity);
        videoFullView.addView(view);
        decor.addView(videoFullView);
    }

    public boolean goBack() {
        if (webChromeClient == null || webView == null) {
            return true;
        }
        if (webChromeClient.inCustomView()) {
            //全屏播放退出全屏
            hideCustomView();
            return true;
        } else if (webView.canGoBack()) {
            //返回网页的上一页
            webView.goBack();
            return true;
        } else {
            //退出网页
            //webView.loadUrl("about:blank");
            close();
        }
        return false;
    }


    public void hideCustomView() {
        if (webChromeClient != null) {
            webChromeClient.onHideCustomView();
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void close() {
        //直接退出
        mActivity.finish();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fr_back) {
            //处理回退
            goBack();
        } else if (v.getId() == R.id.fr_close) {
            close();
        }
    }


    @Override
    public void hideProgressBar() {
        if (pbProgress != null) {
            pbProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void showWebView() {
        if (webView != null) {
            webView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideWebView() {
        if (webView != null) {
            webView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void startProgress() {
        LogUtil.e(TAG, "startProgress");
    }

    @Override
    public void progressChanged(int newProgress) {
        if (pbProgress != null && showLoading) {
            LogUtil.e(TAG, "newProgress: " + newProgress);
            if (newProgress > 0 && newProgress < 99) {
                pbProgress.setVisibility(View.VISIBLE);
                pbProgress.setProgress(newProgress);
            } else {
                pbProgress.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void addImageClickListener() {
        // 如要点击一张图片在弹出的页面查看所有的图片集合,则获取的值应该是个图片数组
        loadWebPage("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{objs[i].onclick=function(){" +
                "window." + JS_TAG + ".imageClick(" +
                "this.getAttribute(\"src\")" +
                ");}}})()");
        // 遍历所有的a节点,将节点里的属性传递出去
        loadWebPage("javascript:(function(){" +
                "var objs =document.getElementsByTagName(\"a\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{objs[i].onclick=function(){" +
                "window." + JS_TAG + ".textClick(this.getAttribute(\"type\")," +
                "this.getAttribute(\"href\"));" +
                "}}})()");
    }

    @Override
    public void setTitle(String title) {
        //不显示固定标题时才显示网页标题
        if (tvTitle != null && !isShowFixedTitle) {
            tvTitle.setText(StringUtil.getNotNullStr(title));
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        LogUtil.e(TAG, "onPageFinished()");
        hideProgressBar();
        mPageFinish = true;
        //if (!CheckNetwork.isNetworkConnected(mActivity)) {
        hideProgressBar();
        //}
        addImageClickListener();
    }


    @Override
    public void onReceiveSslError(WebView view, SslErrorHandler handler, SslError error) {
        //证书问题时,忽略证书问题
        handler.proceed();
    }

    @Override
    public void showVideoFullView() {
        if (videoFullView != null) {
            videoFullView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideVideoFullView() {
        if (videoFullView != null) {
            videoFullView.setVisibility(View.GONE);
        }
    }


}
