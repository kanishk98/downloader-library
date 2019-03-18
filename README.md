# Downloader library for Android

Android library for scheduling, managing, and ordering downloads.

## How it works

The library takes in a `Date` object representing the scheduled time of the download. `AlarmManager` is used to register an alarm at that time to launch `DownloadService.java`. 
Depending on the flags passed to `DownloadService` (represented in `DownloadStatus`), the service delegates work to the foreground service, `DownloadExecutor.java`. 

If the work to be done is to initiate/resume a download, `DownloadExecutor` executes the code in its `onHandleIntent()` method and quits. 
If a download needs to be paused/cancelled, `DownloadService.java` sends a `Broadcast` to `DownloadExecutor.java`, which again uses `DownloadStatus` to determine if the download should be paused or cancelled. If it's the latter, the download file is deleted from the device and if it's the former, the ID of the download and the Download object are stored in JSON in `SharedPreferences`. (available for resuming later)

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

## Limitations

Since this library was put together in less than a week, it leaves some things to be desired. I'm currently researching more on them.

1. Since resuming a paused download requires use of the `Range` property, this can only be used in HTTP/HTTPS downloads and for servers that send this header. 
2. A download is broken up into chunks and then these chunks are downloaded sequentially. It would probably be faster to download them in parallel. 
3. Network error-handling is not built in (except some basic try-catch blocks). Can be easily improved. 


## Future Goals:

1. Support popular protocols for file downloads using Apache NetCommons
2. Enable multithreaded downloads of each file
3. Maintain wish-list of download links and sync them across user accounts