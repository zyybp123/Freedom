package com.bpz.commonlibrary.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.bpz.commonlibrary.util.StringUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 离线包配置数据实体
 */
public class HpmFileBean implements Parcelable {


    public static final Parcelable.Creator<HpmFileBean> CREATOR = new Parcelable.Creator<HpmFileBean>() {
        @Override
        public HpmFileBean createFromParcel(Parcel source) {
            return new HpmFileBean(source);
        }

        @Override
        public HpmFileBean[] newArray(int size) {
            return new HpmFileBean[size];
        }
    };
    /**
     * appid : 20000135
     * name : h5-traffic-search
     * author : 青也<shaowei.zsw@alibaba-inc.com>
     * description : 交通线首页 based on React
     * launchParams : {"url":"/build_offline/pages/search/index.html","showTitleBar":true,"showTitle":true,"showCloseIcon":true,"showMenu":true,"showToolBar":false,"showLoading":false}
     * menuParams : [{"name":"了解详情","url":"https://www.baidu.com"},{"name":"gogogo!","url":"https://www.xysxtzq.cn"}]
     * version : 0.2.3
     */

    private String appid;
    private String name;
    private String author;
    private String description;
    private LaunchParamsBean launchParams;
    private String version;
    private List<MenuParamsBean> menuParams;

    public HpmFileBean() {
    }

    protected HpmFileBean(Parcel in) {
        this.appid = in.readString();
        this.name = in.readString();
        this.author = in.readString();
        this.description = in.readString();
        this.launchParams = in.readParcelable(LaunchParamsBean.class.getClassLoader());
        this.version = in.readString();
        this.menuParams = new ArrayList<MenuParamsBean>();
        in.readList(this.menuParams, MenuParamsBean.class.getClassLoader());
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LaunchParamsBean getLaunchParams() {
        return launchParams;
    }

    public void setLaunchParams(LaunchParamsBean launchParams) {
        this.launchParams = launchParams;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<MenuParamsBean> getMenuParams() {
        return menuParams;
    }

    public void setMenuParams(List<MenuParamsBean> menuParams) {
        this.menuParams = menuParams;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static class LaunchParamsBean implements Parcelable {
        public static final Creator<LaunchParamsBean> CREATOR = new Creator<LaunchParamsBean>() {
            @Override
            public LaunchParamsBean createFromParcel(Parcel source) {
                return new LaunchParamsBean(source);
            }

            @Override
            public LaunchParamsBean[] newArray(int size) {
                return new LaunchParamsBean[size];
            }
        };
        /**
         * url : /build_offline/pages/search/index.html
         * 后面是默认值
         * showTitleBar : true
         * showTitle : true
         * showCloseIcon : false
         * showMenu : false
         * showToolBar : false
         * showLoading : true
         * <p>
         * isShowFixedTitle : false//是否显示固定的标题
         * fixedTitle //固定标题
         */

        private String url;
        private boolean showTitleBar = true;
        private boolean showTitle = true;
        private boolean showCloseIcon = false;
        private boolean showMenu = false;
        private boolean showToolBar = false;
        private boolean showLoading = true;
        private boolean showFixedTitle = false;
        private String fixedTitle;

        public LaunchParamsBean(boolean showFixedTitle, String fixedTitle) {
            this.showFixedTitle = showFixedTitle;
            this.fixedTitle = fixedTitle;
        }

        public LaunchParamsBean() {
        }

        protected LaunchParamsBean(Parcel in) {
            this.url = in.readString();
            this.showTitleBar = in.readByte() != 0;
            this.showTitle = in.readByte() != 0;
            this.showCloseIcon = in.readByte() != 0;
            this.showMenu = in.readByte() != 0;
            this.showToolBar = in.readByte() != 0;
            this.showLoading = in.readByte() != 0;
            this.showFixedTitle = in.readByte() != 0;
            this.fixedTitle = in.readString();
        }

        public boolean isShowFixedTitle() {
            return showFixedTitle;
        }

        public void setShowFixedTitle(boolean showFixedTitle) {
            this.showFixedTitle = showFixedTitle;
        }

        public String getFixedTitle() {
            return StringUtil.getNotNullStr(fixedTitle);
        }

        public void setFixedTitle(String fixedTitle) {
            this.fixedTitle = fixedTitle;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isShowTitleBar() {
            return showTitleBar;
        }

        public void setShowTitleBar(boolean showTitleBar) {
            this.showTitleBar = showTitleBar;
        }

        public boolean isShowTitle() {
            return showTitle;
        }

        public void setShowTitle(boolean showTitle) {
            this.showTitle = showTitle;
        }

        public boolean isShowCloseIcon() {
            return showCloseIcon;
        }

        public void setShowCloseIcon(boolean showCloseIcon) {
            this.showCloseIcon = showCloseIcon;
        }

        public boolean isShowMenu() {
            return showMenu;
        }

        public void setShowMenu(boolean showMenu) {
            this.showMenu = showMenu;
        }

        public boolean isShowToolBar() {
            return showToolBar;
        }

        public void setShowToolBar(boolean showToolBar) {
            this.showToolBar = showToolBar;
        }

        public boolean isShowLoading() {
            return showLoading;
        }

        public void setShowLoading(boolean showLoading) {
            this.showLoading = showLoading;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.url);
            dest.writeByte(this.showTitleBar ? (byte) 1 : (byte) 0);
            dest.writeByte(this.showTitle ? (byte) 1 : (byte) 0);
            dest.writeByte(this.showCloseIcon ? (byte) 1 : (byte) 0);
            dest.writeByte(this.showMenu ? (byte) 1 : (byte) 0);
            dest.writeByte(this.showToolBar ? (byte) 1 : (byte) 0);
            dest.writeByte(this.showLoading ? (byte) 1 : (byte) 0);
            dest.writeByte(this.showFixedTitle ? (byte) 1 : (byte) 0);
            dest.writeString(this.fixedTitle);
        }

        @Override
        public int describeContents() {
            return 0;
        }


    }

    public static class MenuParamsBean implements Parcelable {
        public static final Creator<MenuParamsBean> CREATOR = new Creator<MenuParamsBean>() {
            @Override
            public MenuParamsBean createFromParcel(Parcel source) {
                return new MenuParamsBean(source);
            }

            @Override
            public MenuParamsBean[] newArray(int size) {
                return new MenuParamsBean[size];
            }
        };
        /**
         * name : 了解详情
         * url : https://www.baidu.com
         * isOpenNew:true //是否打开新的窗口显示
         */

        private String name;
        private String url;
        private boolean isOpenNew = true;

        public MenuParamsBean() {
        }

        protected MenuParamsBean(Parcel in) {
            this.name = in.readString();
            this.url = in.readString();
            this.isOpenNew = in.readByte() != 0;
        }

        public boolean isOpenNew() {
            return isOpenNew;
        }

        public void setOpenNew(boolean openNew) {
            isOpenNew = openNew;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return StringUtil.getNotNullStr(name);
        }

        @Override
        public int describeContents() {
            return 0;
        }


        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.url);
            dest.writeByte(this.isOpenNew ? (byte) 1 : (byte) 0);
        }


    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appid);
        dest.writeString(this.name);
        dest.writeString(this.author);
        dest.writeString(this.description);
        dest.writeParcelable(this.launchParams, flags);
        dest.writeString(this.version);
        dest.writeList(this.menuParams);
    }


}
