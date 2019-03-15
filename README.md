# Downloader library for Android

Android library for scheduling, managing, and ordering downloads.

Goals:

1. Support HTTP and FTP downloads
2. 

Current limitations and potential solutions:

1. If a download is requested at the same time as another download and another Intent is received by `DownloaderService.java`, then the 