package com.bpz.commonlibrary;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.util.MD5Util;
import com.bpz.commonlibrary.util.SPUtil;
import com.bpz.commonlibrary.util.StringUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        //Context appContext = InstrumentationRegistry.getTargetContext();

        //assertEquals("com.bpz.commonlibrary.test", appContext.getPackageName());

        long t1 = System.currentTimeMillis();
        StringUtil.isSpace(" \r\n\t           ");
        long t2 = System.currentTimeMillis();
        System.out.print("time: " + (t2 - t1));
    }

    @Test
    public void methodTest() throws Exception {
        //assert SPUtil.getInstance("config").contains("a");
        String md5 = MD5Util.md5("1234");
        Log.e("testMd5", "md5: " + md5);
        Context appContext = InstrumentationRegistry.getTargetContext();
        //appContext.getFilesDir().getAbsolutePath() + "/"
        String fileMD5 = MD5Util.getFileMD5("/sdcard/xysx.com.tzq/others/20000135.tar");
        Log.e("testMd5", "fileMD5: " + fileMD5);
        assertEquals("30974e4a4c2ee6171011002fbf0f61c9", fileMD5);
        SimpleArrayMap<String, String> dirMD5 = MD5Util.getDirMD5("/sdcard/xysx.com.tzq/others",
                new SimpleArrayMap<String, String>(), true);
        Log.e("testMd5", "map: " + dirMD5);


    }
}
