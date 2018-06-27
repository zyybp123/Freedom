package com.bpz.freedom.net;

import com.bpz.commonlibrary.interf.SomeFields;

public interface KaiHost {
    String BASE_URL_KAI_SHU_STORY_TEST = "http://tapi.kaishustory.com/";
    String BASE_URL_KAI_SHU_STORY_DEVELOP = "http://dapi.kaishustory.com/";
    String BASE_URL_KAI_SHU_STORY_GAME = "http://gapi.kaishustory.com/";
    String BASE_URL_KAI_SHU_STORY_NORMAL = "https://api.kaishustory.com/";
    String TAG_KAI_SHU = "kaiShu";
    String HEADER_KAI_SHU = SomeFields.URL_FLAG + ":" + TAG_KAI_SHU;
}
