package com.kanishk.mozilladownloader;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
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
        try {
            URL validURL = new URL(url);
            validURL.toURI();
        } catch (MalformedURLException e) {
            throw new NullPointerException("This URL is invalid and is treated as null");
        } catch (URISyntaxException e) {
            throw new NullPointerException("This URL is invalid and is treated as null");
        }
        if (scheduledTime == null) {
            throw new NullPointerException("Download cannot be scheduled at null time");
        }
        return new MozillaDownload(url, targetPath, scheduledTime, timeout, maxSpeed, status, chunkBytes);
    }
}