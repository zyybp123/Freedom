package com.bpz.commonlibrary;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

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
    public void methodTest() throws Exception{
       assert SPUtil.getInstance("config").contains("a");
    }
}
