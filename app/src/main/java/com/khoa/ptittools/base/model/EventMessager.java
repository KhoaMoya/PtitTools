package com.khoa.ptittools.base.model;

public class EventMessager {
    public static enum EVENT{
        EXCEPTION,
        LOGIN_FINNISH,
        LOGIN_FAIL,
        LOGIN_SUCCESS,
        DOWNLOAD_FINNISH,
        DOWNLOAD_FINNISH_TKB,
        DOWNLOAD_FINNISH_PROFILE,
        DOWNLOAD_FINNISH_NOTIFICATION,
        DOWNLOAD_FINNISH_DETAIL_NOTIFICATION,
        DOWNLOAD_FINNISH_HOC_PHI,
        DOWNLOAD_FINNISH_DIEM,
    }

    private EVENT event;
    private String tag;
    private Object data;

    public EventMessager(EVENT event,String tag, Object data) {
        this.event = event;
        this.data = data;
        this.tag = tag;
    }

    public EVENT getEvent() {
        return event;
    }

    public void setEvent(EVENT event) {
        this.event = event;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
