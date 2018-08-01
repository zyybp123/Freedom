package com.bpz.commonlibrary.util;

import com.bpz.commonlibrary.interf.FileType;

import java.lang.reflect.Field;

public class FileExtensionUtil {

    private FileExtensionUtil(){}
    public static void getFileExtensionName(){
        System.out.println("getFileExtensionName");
        Class<? extends Class> fileTypeClass = FileType.class.getClass();
        Field[] fields = fileTypeClass.getFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            System.out.println(field);
        }
    }
}
