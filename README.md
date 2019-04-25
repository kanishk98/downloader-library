# Downloader library for Android

Android library for scheduling, pausing, resuming, and canceling downloads. Check out my thought process and offer suggestions at [the project wiki](https://github.com/kanishk98/downloader-library/wiki)! 

I'm currently migrating this library to use [Fetch](https://github.com/tonyofrancis/Fetch) to avoid running into pesky errors with the core downloading logic. You can follow my progress with that [here](https://github.com/kanishk98/downloader-library/tree/fetch-migration).

## Using downloader-library in another project module

Here's how to use this library for your project:

* Scheduling downloads

```java
MozillaDownloader downloader = MozillaDownloader.getDownloader(getApplicationContext());
Calendar calendar = Calendar.getInstance();
MozillaDownloadBuilder builder = new MozillaDownloadBuilder()
        .setUrl("https://www.github.com") // sets a default time at the beginning of 2000
download = builder.createMozillaDownload();
downloader.scheduleDownload(download);
```

* Pausing downloads

```java
downloader.pauseDownload(download);
```

* Resuming downloads

```java
downloader.resumeDownload(download);
```

* Canceling downloads

```java
downloader.cancelDownload(download);
```

## Running app

Currently, the app downloads a publicly available PDF from a hard-coded URL in `MainActivity.java`. As soon as you tap Start, the PDF starts getting downloaded. (`AlarmManager` is scheduled to run immediately for ease in testing at this early stage). 
You may pause the download, cancel it, and resume the last paused download. 

The status of each paused download is saved on the device. My guess is that resuming _specific_ paused downloads as long as the functionality is fine and tested for one is just a UI problem. I intend to finish work on that by April 25. 

## How it works

The library uses a combination of `URL` and `FileChannel` classes to download and save files to your device. Background processing is handled by means of a Foreground Service. More explanation is available at the project wiki.  

## Limitations

Since this library was put together in less than a week, it leaves some things to be desired. I'm currently researching more on them.

1. Since resuming a paused download requires use of the `Range` property, this can only be used in HTTP/HTTPS downloads and for servers that send this header. 
2. A download is broken up into chunks and then these chunks are downloaded sequentially. It would probably be faster to download them in parallel.
3. Network error-handling is not built in (except some basic try-catch blocks). Can be easily improved. 


## Future Goals:

1. Support popular protocols for file downloads using Apache NetCommons
2. Enable multithreaded downloads of each file
3. Maintain wish-list of download links and sync them across user accounts
