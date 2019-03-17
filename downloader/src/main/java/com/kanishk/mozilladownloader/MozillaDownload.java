package com.kanishk.mozilladownloader;

// Representation of a download instance

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import androidx.work.OneTimeWorkRequest;

public class MozillaDownload implements Serializable {

	private String uid;
	private String url;
	private String targetPath;
	private Date scheduledTime;
	private int timeout = 90;
	private double maxSpeed;
	private DownloadStatus status;
	private long totalBytes;
	private long chunkBytes = 128 * 1024 * 1024;
	private long downloadedBytes = 0;
	private List<OneTimeWorkRequest> workRequestList;

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

	protected void setStatus(DownloadStatus status) {
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

	public List<OneTimeWorkRequest> getWorkRequestList() {
		return workRequestList;
	}

	public void setWorkRequestList(List<OneTimeWorkRequest> workRequestList) {
		this.workRequestList = workRequestList;
	}
}