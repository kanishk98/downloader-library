# Downloader library for Android

Android library for scheduling, managing, and ordering downloads.

Goals:

1. Support popular protocols for file downloads using Apache NetCommons
2. Enable multithreaded downloads of each file
3. Maintain wish-list of download links and sync them across user accounts

## FileChannel implementation

Because FileChannel in the NIO package allows our code to take advantage of OS optimisations, I've used the same for downloads. 
Here's a sample implementation in Java of the same concept:

```java
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class DownloadTest {
	public static void main(String[] args) throws InterruptedException {
		try {
            File destinationFile = new File("./google.html");
            URL url = new URL("https://www.google.com");
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileChannel downloadChannel = new FileOutputStream(destinationFile, destinationFile.exists()).getChannel();
            long initialBytes;
            long downloadedBytes = 0;
            int i = 0;
            do {
                initialBytes = downloadedBytes;
                long chunkBytes = downloadChannel.transferFrom(readableByteChannel, downloadedBytes, 64 * 1024);
                downloadedBytes += chunkBytes;
                ++i;
                System.out.println("Downloaded " + downloadedBytes + " bytes");
            } while(downloadedBytes > initialBytes && i < 2);
            System.out.println("DOWNLOAD PAUSED");
            Thread.sleep(3000);
            System.out.println("RESUMING DOWNLOAD");
            do {
                initialBytes = downloadedBytes;
                long chunkBytes = downloadChannel.transferFrom(readableByteChannel, downloadedBytes, 64 * 1024);
                downloadedBytes += chunkBytes;
                System.out.println("Downloaded " + downloadedBytes + " bytes, initial bytes = " + initialBytes);
            } while(downloadedBytes > initialBytes);
        } catch (java.io.IOException e) {
        	e.printStackTrace();
        }
	}
}
```