package com.bpz.commonlibrary;

import com.bpz.commonlibrary.util.FileExtensionUtil;
import com.bpz.commonlibrary.util.SPUtil;
import com.bpz.commonlibrary.util.SpanUtil;
import com.bpz.commonlibrary.util.StringUtil;

import org.junit.Test;

import java.net.URLDecoder;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        //assertEquals(4, 2 + 2);
        FileExtensionUtil.getFileExtensionName();
    }
}