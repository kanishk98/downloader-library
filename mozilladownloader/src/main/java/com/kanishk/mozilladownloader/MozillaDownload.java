package com.kanishk.mozilladownloader;

// Representation of a download instance

public class MozillaDownload {

	/*
		uid: the unique ID assigned to a download instance
		maxSpeed: user-configurable maximum speed of download (network speed by default)
		status: scheduled/in-progress/paused/cancelled
	*/
	
	private String uid;
	private String url;
	private Date scheduledTime;
	private int timeout;
	private double maxSpeed;
	private int status;

	private MozillaDownload(Builder builder) {
		this.uid = builder.uid;
		this.uri = builder.uri;
		this.scheduledTime = builder.scheduledTime;
		this.timeout = builder.timeout;
		this.status = builder.status;
	}

	public static class Builder {
		int timeout = Constants.	
	}
}