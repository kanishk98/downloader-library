package com.kanishk.mozilladownloader;

// Representation of a download instance

import java.io.Serializable;
import java.util.Date;

public class MozillaDownload implements Serializable {

	private String uid;
	private String url;
	private String targetPath;
	private Date scheduledTime;
	private int timeout = 90;
	private double maxSpeed;
	private int status;
	private long totalBytes;
	private long chunkBytes = 128 * 1024;
	private long downloadedBytes = 0;

	public MozillaDownload(String url, String targetPath, Date scheduledTime, int timeout, double maxSpeed, int status, long chunkBytes) {
		this.uid = Util.generateUID();
		this.url = url;
		this.targetPath = targetPath;
		this.scheduledTime = scheduledTime;
		this.timeout = timeout;
		this.maxSpeed = maxSpeed;
		this.status = status;
		this.chunkBytes = chunkBytes;
	}

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

	public int getStatus() {
		return status;
	}

	protected void setStatus(int status) {
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

	public long getChunkBytes() {
		return chunkBytes;
	}

	public void setChunkBytes(long chunkBytes) {
		this.chunkBytes = chunkBytes;
	}
}