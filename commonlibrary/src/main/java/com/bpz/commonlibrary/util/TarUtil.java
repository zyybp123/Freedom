package com.bpz.commonlibrary.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * 针对压缩文件的操作
 */
public class TarUtil {
    private static final String TAG = "TarUtil";

    /**
     * .tar文件的解压缩
     *
     * @param sourceFile 源文件
     * @param targetDir  目标文件夹的绝对路径
     */
    @Contract("null, _ -> fail")
    public static void unArchive(File sourceFile, String targetDir) throws Exception {
        if (sourceFile == null || sourceFile.length() == 0) {
            throw new NullPointerException("source file is null !" + sourceFile);
        }
        if (TextUtils.isEmpty(targetDir)) {
            throw new NullPointerException("targetDir is empty !" + targetDir);
        }
        File targetFile = new File(targetDir);
        if (!targetFile.exists()) {
            boolean mkdirs = targetFile.mkdirs();
            if (!mkdirs) {
                throw new Exception("fail to make dirs !");
            }
        }
        TarArchiveInputStream inputStream = new TarArchiveInputStream(
                new FileInputStream(sourceFile));
        TarArchiveEntry entry;
        byte[] buffer = new byte[1024 * 1024];
        //解压时字节计数
        int count = 0;
        while ((entry = inputStream.getNextTarEntry()) != null) {
            LogUtil.e(TAG, "entry...." + entry.getName());
            targetFile = new File(targetDir + File.separator + entry.getName());
            fileProbe(targetFile);
            if (entry.isDirectory()) {
                //创建文件夹
                boolean mkdirs = targetFile.mkdirs();
                LogUtil.e(TAG, "dir: " + targetFile.getAbsolutePath() + "-- make: " + mkdirs);
            } else {
                //创建文件
                boolean newFile = targetFile.createNewFile();
                LogUtil.e(TAG, "file: " + targetFile.getAbsolutePath() + "-- create: " + newFile);
                //用文件输出流把读取到的内容写入创建的文件或文件夹中
                FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
                while ((count = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, count);
                }
                //关闭流
                fileOutputStream.close();
            }
        }
    }

    /**
     * 解压.zip文件
     *
     * @param sourceFile 源文件
     * @param targetDir  目标文件夹
     */
    @Contract("null, _ -> fail")
    public static void unZip(File sourceFile, String targetDir) throws Exception {
        if (sourceFile == null || sourceFile.length() == 0) {
            throw new NullPointerException("source file is null !" + sourceFile);
        }
        if (TextUtils.isEmpty(targetDir)) {
            throw new NullPointerException("targetDir is empty !" + targetDir);
        }
        File targetFile = new File(targetDir);
        if (!targetFile.exists()) {
            boolean mkdirs = targetFile.mkdirs();
            if (!mkdirs) {
                throw new Exception("fail to make dirs !");
            }
        }
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(sourceFile));
        ZipEntry entry;
        byte[] buffer = new byte[1024 * 1024];
        //解压时字节计数
        int count = 0;
        while ((entry = zipInputStream.getNextEntry()) != null) {
            LogUtil.e(TAG, "entry...." + entry.getName());
            targetFile = new File(targetDir + File.separator + entry.getName());
            fileProbe(targetFile);
            if (entry.isDirectory()) {
                //创建文件夹
                boolean mkdirs = targetFile.mkdirs();
                LogUtil.e(TAG, "dir: " + targetFile.getAbsolutePath() + "-- make: " + mkdirs);
            } else {
                //创建文件
                boolean newFile = targetFile.createNewFile();
                LogUtil.e(TAG, "file: " + targetFile.getAbsolutePath() + "-- create: " + newFile);
                //用文件输出流把读取到的内容写入创建的文件或文件夹中
                FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
                while ((count = zipInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, count);
                }
                //关闭流
                fileOutputStream.close();
            }
        }
    }

    /**
     * 文件探针
     * 当父目录不存在时，创建目录！
     */
    private static void fileProbe(@NotNull File dirFile) {
        File parentFile = dirFile.getParentFile();
        if (!parentFile.exists()) {
            // 递归寻找上级目录
            fileProbe(parentFile);
            boolean b = parentFile.mkdir();
            LogUtil.e(TAG, "dir create success: " + b + parentFile);
        }
    }

    /**
     * 不解压直接读取Zip文件和文件内容
     *
     * @param file zip文件绝对路径
     */
    public static InputStream readZipFile(String file) throws Exception {
        InputStream input = null;
        ZipFile zf = new ZipFile(file);
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        ZipInputStream zin = new ZipInputStream(in);
        ZipEntry ze;
        while ((ze = zin.getNextEntry()) != null) {
            if (!ze.isDirectory()) {
                if (ze.getName().equals("Manifest.xml")) {
                    input = zf.getInputStream(ze);
                }
            }
        }
        zin.closeEntry();
        return input;
    }

    /**
     * 得到json文件中的内容
     *
     * @param filePath json文件的路径
     */
    @NonNull
    public static String getJson(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new NullPointerException("file is not exist !");
        }
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file)));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }
}

