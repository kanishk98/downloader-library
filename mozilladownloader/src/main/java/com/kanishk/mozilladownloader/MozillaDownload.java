package com.kanishk.mozilladownloader;

// Representation of a download instance

import java.util.Date;

public class MozillaDownload {

	private String uid;
	private String url;
	private String targetPath;
	private Date scheduledTime;
	private int timeout;
	private double maxSpeed;
	private DownloadStatus status;
	private long totalBytes;
	private long downloadedBytes;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}

	public Date getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(Date scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public DownloadStatus getStatus() {
		return status;
	}

	public void setStatus(DownloadStatus status) {
		this.status = status;
	}

	public long getTotalBytes() {
		return totalBytes;
	}

	public void setTotalBytes(long totalBytes) {
		this.totalBytes = totalBytes;
	}

	public long getDownloadedBytes() {
		return downloadedBytes;
	}

	public void setDownloadedBytes(long downloadedBytes) {
		this.downloadedBytes = downloadedBytes;
	}
}