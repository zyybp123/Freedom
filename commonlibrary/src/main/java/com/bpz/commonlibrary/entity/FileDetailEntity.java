package com.bpz.commonlibrary.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

public class FileDetailEntity implements Parcelable {
    public static final Parcelable.Creator<FileDetailEntity> CREATOR = new Parcelable.Creator<FileDetailEntity>() {
        @Override
        public FileDetailEntity createFromParcel(Parcel source) {
            return new FileDetailEntity(source);
        }

        @Override
        public FileDetailEntity[] newArray(int size) {
            return new FileDetailEntity[size];
        }
    };
    /**
     * 文件Id
     */
    public long fileId;
    /**
     * 文件名
     */
    public String displayName;
    /**
     * 文件名不带后缀
     */
    public String title;
    /**
     * 文件名带后缀
     */
    public String fullName;
    /**
     * 文件类型
     */
    public String type;
    /**
     * 文件路径
     */
    public String data;
    /**
     * 文件的最后修改日期
     */
    public long dateModify;
    /**
     * 文件的大小
     */
    public long size;
    /**
     * 文件是否选中的标识
     */
    public boolean isSelect;

    public FileDetailEntity() {
    }

    public FileDetailEntity(long fileId, String name, String type, String data, long dateModify, long size) {
        this(name, type, data, dateModify, size);
        this.fileId = fileId;
    }

    public FileDetailEntity(String name, String type, String data, long dateModify, long size) {
        this(name, type, data);
        this.dateModify = dateModify;
        this.size = size;
    }

    public FileDetailEntity(String title, String type, String data) {
        this.title = title;
        this.data = data;
        this.type = type;
        File file = new File(data);
        if (file.exists()) {
            this.fullName = file.getName();
        } else {
            this.fullName = title;
        }
    }

    protected FileDetailEntity(Parcel in) {
        this.fileId = in.readLong();
        this.displayName = in.readString();
        this.title = in.readString();
        this.fullName = in.readString();
        this.type = in.readString();
        this.data = in.readString();
        this.dateModify = in.readLong();
        this.size = in.readLong();
        this.isSelect = in.readByte() != 0;
    }

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getDateModify() {
        return dateModify;
    }

    public void setDateModify(long dateModify) {
        this.dateModify = dateModify;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "" + fileId + fullName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.fileId);
        dest.writeString(this.displayName);
        dest.writeString(this.title);
        dest.writeString(this.fullName);
        dest.writeString(this.type);
        dest.writeString(this.data);
        dest.writeLong(this.dateModify);
        dest.writeLong(this.size);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
    }
}
