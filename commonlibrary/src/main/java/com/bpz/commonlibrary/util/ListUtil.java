package com.bpz.commonlibrary.util;

import java.util.List;

public class ListUtil {
    private ListUtil() {
    }

    /**
     * 拿到正确的索引值，容错处理
     *
     * @param dataList 数据集合
     * @param position 索引
     * @return 返回-1时，集合为空
     */
    public static int getCorrectPosition(List dataList, int position) {
        if (dataList == null || dataList.size() == 0) {
            return -1;
        }
        if (position < 0) {
            return 0;
        }
        if (position >= dataList.size()) {
            return dataList.size() - 1;
        }
        return position;
    }
}
