package com.zhan.qiwen.page.adapter.base;

public class BaseFooter {
    public static final int STATUS_NORMAL = 1;//正常状态
    public static final int STATUS_LOADING = 2;//正在加载中
    public static final int STATUS_NO_MORE = 3;//没有更多了
    private int status;

    public BaseFooter(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}