package com.kanishk.mozilladownloader;

import java.util.Date;

public class MozillaDownloadBuilder {
    private String url;
    private String targetPath;
    private Date scheduledTime;
    private int timeout;
    private double maxSpeed;
    private int status;
    private long chunkBytes;

    public MozillaDownloadBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public MozillaDownloadBuilder setTargetPath(String targetPath) {
        this.targetPath = targetPath;
        return this;
    }

    public MozillaDownloadBuilder setScheduledTime(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
        return this;
    }

    public MozillaDownloadBuilder setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public MozillaDownloadBuilder setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
        return this;
    }

    public MozillaDownloadBuilder setStatus(int status) {
        this.status = status;
        return this;
    }

    public MozillaDownloadBuilder setChunkBytes(long chunkBytes) {
        this.chunkBytes = chunkBytes;
        return this;
    }

    public MozillaDownload createMozillaDownload() {
        if (url == null) {
            throw new NullPointerException("URL cannot be null");
        }
        if (scheduledTime == null) {
            throw new NullPointerException("Download cannot be scheduled at null time");
        }
        return new MozillaDownload(url, targetPath, scheduledTime, timeout, maxSpeed, status, chunkBytes);
    }
}