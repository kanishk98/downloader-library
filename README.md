# Downloader library for Android

Android library for scheduling, managing, and ordering downloads.

Goals:

1. Support popular protocols for file downloads using Apache NetCommons
2. Enable multithreaded downloads of each file
3. Maintain wish-list of download links and sync them across user accounts

## FileChannel implementation

Because FileChannel in the NIO package allows our code to take advantage of OS optimisations, I've used the same for downloads. 
Since this part of the library is most useful, I've added code for the relevant method below:

```java
private void download(MozillaDownload download) {
        try {
            download.setTargetPath(getApplicationContext().getExternalFilesDir("/mozilla/") + download.getUid() +
            download.getUrl().substring(download.getUrl().lastIndexOf(".")));
            File destinationFile = new File(download.getTargetPath());
            URL url = new URL(download.getUrl());
            URLConnection connection = url.openConnection();
            if (destinationFile.exists()) {
                connection.setRequestProperty("Range", "bytes=" + destinationFile.length() + "-");
            }
            ReadableByteChannel readableByteChannel = Channels.newChannel(connection.getInputStream());
            FileChannel downloadChannel = new FileOutputStream(destinationFile, destinationFile.exists()).getChannel();
            download.setStatus(DownloadStatus.RUNNING);
            long initialBytes;
            long downloadedBytes = destinationFile.length();
            do {
                initialBytes = downloadedBytes;
                long chunkBytes = downloadChannel.transferFrom(readableByteChannel, downloadedBytes, download.getChunkBytes());
                downloadedBytes += chunkBytes;
                download.setDownloadedBytes(downloadedBytes);
            } while(downloadedBytes > initialBytes && !pause && !cancel);
            if (pause) {
                pause(download);
            }
            if (cancel) {
                cancel(download);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }
```

## Running app

Currently, the app downloads a publicly available PDF from a hard-coded URL in `MainActivity.java`. As soon as you open the app, the download begins (the `AlarmManager` has been instructed to run immediately in debug mode for easier testing). 
You may pause the download, cancel it, and resume the last paused download. 

The status of each paused download is saved on the device. My guess is that resuming _specific_ paused downloads as long as the functionality is fine and tested for one is just a UI problem. Working on that. 
