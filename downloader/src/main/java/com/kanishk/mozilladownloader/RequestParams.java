package com.kanishk.mozilladownloader;

import com.tonyodev.fetch2.EnqueueAction;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.Extras;

import java.util.Map;

public class RequestParams {
    private Map<String, String> headers;
    private int priority;
    private NetworkType networkType;
    private String tag;
    private EnqueueAction enqueueAction;
    private boolean downloadOnEnqueue;
    private Extras extras;

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public NetworkType getNetworkType() {
        return networkType;
    }

    public void setNetworkType(NetworkType networkType) {
        this.networkType = networkType;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public EnqueueAction getEnqueueAction() {
        return enqueueAction;
    }

    public void setEnqueueAction(EnqueueAction enqueueAction) {
        this.enqueueAction = enqueueAction;
    }

    public boolean isDownloadOnEnqueue() {
        return downloadOnEnqueue;
    }

    public void setDownloadOnEnqueue(boolean downloadOnEnqueue) {
        this.downloadOnEnqueue = downloadOnEnqueue;
    }

    public Extras getExtras() {
        return extras;
    }

    public void setExtras(Extras extras) {
        this.extras = extras;
    }
}
