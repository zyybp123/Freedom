package com.bpz.commonlibrary;

import com.bpz.commonlibrary.util.StringUtil;

import org.junit.Test;

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
       assert  StringUtil.isSpace("");
       assertEquals("baidusearch_AndroidPhone_1021446w.apk",
               StringUtil.findFileName("https://downapp.baidu.com/baidusearch/AndroidPhone/10.9.5.10.1/1/1021446w/20180710160025/baidusearch_AndroidPhone_10-9-5-10-1_1021446w.apk?responseContentDisposition=attachment%3Bfilename%3D%22baidusearch_AndroidPhone_1021446w.apk%22&responseContentType=application%2Fvnd.android.package-archive&request_id=1531374882_9276278027&type=static"));
    }
}