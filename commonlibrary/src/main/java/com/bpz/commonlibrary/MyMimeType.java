package com.bpz.commonlibrary;

import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.MimeTypeFilter;
import android.support.v4.util.ArraySet;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.zhihu.matisse.internal.utils.PhotoMetadataUtils;

import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;

import okhttp3.MediaType;

public enum MyMimeType {

    // ============== images ==============
    JPEG("image/jpeg", "jpg", "jpeg"),
    PNG("image/png", "png"),
    GIF("image/gif", "gif"),
    BMP("image/x-ms-bmp", "bmp"),
    WEBP("image/webp", "webp"),

    // ============== videos ==============
    MPEG("video/mpeg", "mpeg", "mpg"),
    MP4("video/mp4", "mp4", "m4v"),
    MOV("video/quicktime", "mov"),
    THREEGPP("video/3gpp", "3gp", "3gpp"),
    THREEGPP2("video/3gpp2", "3g2", "3gpp2"),
    MKV("video/x-matroska", "mkv"),
    WEBM("video/webm", "webm"),
    TS("video/mp2ts", "ts"),
    AVI("video/avi", "avi"),
    FLV("video/x-flv", "flv"),
    SWF("application/x-shockwave-flash", "swf"),

    // ============== document ==============
    DOC("application/msword", "doc"),
    DOCX("application/vnd.openxmlformats-officedocument" +
            ".wordprocessingml.document", "docx"),
    DOTX("application/vnd.openxmlformats-officedocument" +
            ".wordprocessingml.template", "dotx"),
    XLS("application/vnd.ms-excel", "xls"),
    XLSX("application/vnd.openxmlformats-officedocument" +
            ".spreadsheetml.sheet", "xlsx"),
    XLTX("application/vnd.openxmlformats-officedocument" +
            ".spreadsheetml.template", "xltx"),
    PPT("application/vnd.ms-powerpoint", "ppt"),
    PPTX("application/vnd.openxmlformats-officedocument" +
            ".presentationml.presentation", "pptx"),
    POTX("vnd.openxmlformats-officedocument" +
            ".presentationml.template", "potx"),
    PPSX("vnd.openxmlformats-officedocument" +
            ".presentationml.slideshow", "ppsx"),
    TEXT("text/plain", "txt"),
    PDF("application/pdf", "pdf"),
    //=============== apk ==================
    APK("application/vnd.android.package-archive", "apk");


    private String mMimeTypeName;
    private Set<String> mExtensions;

    MyMimeType(String mimeTypeName, String... extensions) {
        mMimeTypeName = mimeTypeName;
        mExtensions = arraySetOf(extensions);
    }


    @NonNull
    public static Set<String> arraySetOf(String... suffixes) {
        if (suffixes == null) {
            return new ArraySet<>();
        }
        return new ArraySet<>(Arrays.asList(suffixes));
    }

    public static Set<MyMimeType> ofAll() {
        return EnumSet.allOf(MyMimeType.class);
    }

    public static Set<MyMimeType> of(MyMimeType type, MyMimeType... rest) {
        return EnumSet.of(type, rest);
    }

    public static Set<MyMimeType> ofImage() {
        return EnumSet.of(JPEG, PNG, GIF, BMP, WEBP);
    }

    public static Set<MyMimeType> ofVideo() {
        return EnumSet.of(MPEG, MP4, MOV, THREEGPP, THREEGPP2, MKV, WEBM, TS, AVI);
    }

    public static Set<String> ofVideoExtensions() {
        ArraySet<String> set = new ArraySet<>();
        set.add("avi");
        set.add("flv");
        set.add("mp4");
        return set;
    }
    public static Set<String> ofVideoType() {
        ArraySet<String> set = new ArraySet<>();
        set.add(AVI.getMimeTypeName());
        set.add(FLV.getMimeTypeName());
        set.add(MP4.getMimeTypeName());
        return set;
    }

    public static Set<String> ofDocExtensions() {
        ArraySet<String> set = new ArraySet<>();
        set.add("doc");
        set.add("docx");
        set.add("dotx");
        set.add("txt");
        set.add("pdf");
        set.add("xls");
        set.add("xlsx");
        set.add("ppt");
        set.add("pptx");
        set.add("potx");
        set.add("ppsx");
        return set;
    }

    public static Set<String> ofDocType() {
        ArraySet<String> set = new ArraySet<>();
        set.add(DOC.getMimeTypeName());
        set.add(DOCX.getMimeTypeName());
        set.add(DOTX.getMimeTypeName());
        set.add(TEXT.getMimeTypeName());
        set.add(PDF.getMimeTypeName());
        set.add(XLS.getMimeTypeName());
        set.add(XLSX.getMimeTypeName());
        set.add(PPT.getMimeTypeName());
        set.add(PPTX.getMimeTypeName());
        set.add(POTX.getMimeTypeName());
        set.add(PPSX.getMimeTypeName());
        return set;
    }

    public static Set<String> ofImageExtensions() {
        ArraySet<String> set = new ArraySet<>();
        set.add("png");
        set.add("jpg");
        set.add("jpeg");
        set.add("gif");
        return set;
    }

    public static Set<String> ofImageType() {
        ArraySet<String> set = new ArraySet<>();
        set.add(PNG.getMimeTypeName());
        set.add(JPEG.getMimeTypeName());
        set.add(GIF.getMimeTypeName());
        return set;
    }

    @Contract(pure = true)
    public String getMimeTypeName() {
        return mMimeTypeName;
    }

    public void setMimeTypeName(String mMimeTypeName) {
        this.mMimeTypeName = mMimeTypeName;
    }

    @Contract(pure = true)
    public Set<String> getExtensions() {
        return mExtensions;
    }

    public void setExtensions(Set<String> mExtensions) {
        this.mExtensions = mExtensions;
    }

    @Contract(pure = true)
    @Override
    public String toString() {
        return mMimeTypeName;
    }

    public boolean checkType(ContentResolver resolver, Uri uri) {
        MimeTypeMap map = MimeTypeMap.getSingleton();
        if (uri == null) {
            return false;
        }
        String type = map.getExtensionFromMimeType(resolver.getType(uri));
        String path = null;
        // lazy load the path and prevent resolve for multiple times
        boolean pathParsed = false;
        for (String extension : mExtensions) {
            if (extension.equals(type)) {
                return true;
            }
            if (!pathParsed) {
                // we only resolve the path for one time
                path = PhotoMetadataUtils.getPath(resolver, uri);
                if (!TextUtils.isEmpty(path)) {
                    path = path.toLowerCase(Locale.US);
                }
                pathParsed = true;
            }
            if (path != null && path.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}
