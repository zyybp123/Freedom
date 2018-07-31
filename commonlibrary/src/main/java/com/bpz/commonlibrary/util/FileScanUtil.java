package com.bpz.commonlibrary.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;

import com.bpz.commonlibrary.MyMimeType;
import com.bpz.commonlibrary.entity.FileDetailEntity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.bpz.commonlibrary.interf.FileType.IMAGE_LIST;
import static com.bpz.commonlibrary.interf.FileType.VIDEO_LIST;


/**
 * Created by Administrator on 2017/12/14.
 * 从数据库获取出对应类型的文件
 */

public class FileScanUtil {
    private static final String TAG = "ScanAllFiles";
    private static final Uri FILE_URI_EXTERNAL = MediaStore
            .Files.getContentUri("external");
    private static final Uri FILE_URI_INTERNAL = MediaStore
            .Files.getContentUri("internal");
    private static final Uri VIDEO_URI_EXTERNAL = MediaStore.Video
            .Media.EXTERNAL_CONTENT_URI;
    private static final Uri VIDEO_URI_INTERNAL = MediaStore.Video
            .Media.INTERNAL_CONTENT_URI;
    private static final Uri IMAGE_URI_EXTERNAL = MediaStore.Images
            .Media.EXTERNAL_CONTENT_URI;
    private static final Uri IMAGE_URI_INTERNAL = MediaStore.Images
            .Media.INTERNAL_CONTENT_URI;

    /**
     * 搜索指定目录的指定文件
     *
     * @param file        文件或文件夹
     * @param allFileList 所有找到的文件的集合
     * @param extensions  文件扩展名集合
     * @return 返回指定目录所有符合条件的文件
     */
    private static List<FileDetailEntity> getAllFile(
            @NotNull File file,
            @NotNull List<FileDetailEntity> allFileList,
            Set<String> extensions
    ) {
        if (file.exists()) {
            //判断文件是否是文件夹，如果是，开始递归
            if (file.isDirectory()) {
                File f[] = file.listFiles();
                for (File file2 : f) {
                    getAllFile(file2, allFileList, extensions);
                }
            } else {
                String path = file.getAbsolutePath();
                String extension = StringUtil.getFileSuffixName(path);
                if (extensions.contains(extension)) {
                    //如果该文件后缀名在给定的范围内
                    String name = file.getName();
                    FileDetailEntity entity = new FileDetailEntity();
                    entity.setData(path);
                    entity.setFullName(name);
                    entity.setDateModify(file.lastModified());
                    entity.setSize(file.length());
                    String type = MimeTypeMap.getSingleton()
                            .getMimeTypeFromExtension(extension);
                    entity.setType(type);
                    allFileList.add(entity);
                }
            }
        }
        return allFileList;
    }

    /**
     * 返回指定类型的文件
     */
    public ArrayList<FileDetailEntity> scanAllFile(Context mContext, String[] selectionArg) {
        //要查询的列
        String[] columns = new String[]{
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns.SIZE
        };
        //查询SD卡
        Uri uri = MediaStore.Files.getContentUri("external");
        //查询条件的拼接
        //SELECT * FROM Persons WHERE (mime_type) IN ('args[0]','arg[1]')
        //按时间降序排序
        String order = MediaStore.Images.Media.DATE_MODIFIED + " DESC";
        String selections = "";
        if (selectionArg != null) {
            for (String aSelectionArg : selectionArg) {
                if (!TextUtils.isEmpty(aSelectionArg)) {
                    selections = selections + "\'" + aSelectionArg + "\'" + ",";
                }
            }
            if (selections.length() > 1 && selections.endsWith(",")) {
                selections = selections.substring(0, selections.length() - 1);
            }
        }
        String selection = MediaStore.Files.FileColumns.MIME_TYPE + ") IN (" + selections;
        //游标
        Cursor c = null;
        ArrayList<FileDetailEntity> fileList = new ArrayList<>();
        try {
            c = mContext.getContentResolver().query(uri, columns, selection, null, order);
            if (c != null) {
                while (c.moveToNext()) {
                    long fileId = c.getLong(c.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID));
                    String title = c.getString(c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE));
                    String data = c.getString(c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                    String type = c.getString(c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE));
                    long dateModify = c.getLong(c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED));
                    long size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE));
                    FileDetailEntity file = new FileDetailEntity(fileId, title, type, data, dateModify, size);
                    fileList.add(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "...文件....数据获取异常....." + e.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return fileList;
    }

    /**
     * 返回指定类型的文件，jpeg,jpg,png
     */
    public ArrayList<FileDetailEntity> scanImageFile(Context mContext) {
        //要查询的列
        String[] columns = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.TITLE,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.MIME_TYPE,
                MediaStore.Images.ImageColumns.DATE_MODIFIED,
                MediaStore.Images.ImageColumns.SIZE
        };
        //查询图片的uri
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        //查询条件
        String selection = MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=?";
        //按时间排序
        String order = MediaStore.Images.Media.DATE_MODIFIED + " DESC";
        //游标
        Cursor c = null;
        ArrayList<FileDetailEntity> fileList = new ArrayList<>();
        try {
            c = mContext.getContentResolver().query(uri, columns, selection, IMAGE_LIST, order);
            if (c != null) {
                while (c.moveToNext()) {
                    long fileId = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID));
                    String title = c.getString(c.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.TITLE));
                    String data = c.getString(c.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));
                    String type = c.getString(c.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.MIME_TYPE));
                    long dateModify = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_MODIFIED));
                    long size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.SIZE));
                    FileDetailEntity file = new FileDetailEntity(fileId, title, type, data, dateModify, size);
                    fileList.add(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "....图片...数据获取异常....." + e.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return fileList;
    }

    /**
     * 返回指定类型的文件，mp4,avi,swf,flv
     */
    public ArrayList<FileDetailEntity> scanVideoFile(Context mContext) {
        //要查询的列
        String[] columns = new String[]{
                MediaStore.Video.VideoColumns._ID,
                MediaStore.Video.VideoColumns.TITLE,
                MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.VideoColumns.MIME_TYPE,
                MediaStore.Video.VideoColumns.DATE_MODIFIED,
                MediaStore.Video.VideoColumns.SIZE
        };
        //查询视频的uri
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        //查询条件
        String selection = MediaStore.Video.Media.MIME_TYPE + "=?";
        //按时间排序
        String order = MediaStore.Video.Media.DATE_MODIFIED + " DESC";
        //游标
        Cursor c = null;
        ArrayList<FileDetailEntity> fileList = new ArrayList<>();
        try {
            c = mContext.getContentResolver().query(uri, columns, selection, VIDEO_LIST, order);
            if (c != null) {
                while (c.moveToNext()) {
                    long fileId = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.VideoColumns._ID));
                    String title = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.TITLE));
                    String data = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATA));
                    String type = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.MIME_TYPE));
                    long dateModify = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATE_MODIFIED));
                    long size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.SIZE));
                    FileDetailEntity file = new FileDetailEntity(fileId, title, type, data, dateModify, size);
                    fileList.add(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "....视频...数据获取异常....." + e.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return fileList;
    }
}
