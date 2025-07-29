import java.net.MalformedURLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static void main(String[] args){

        String link = "https://sin-speed.hetzner.com/100MB.bin";
        DownloadManager manager = new DownloadManager(link, 10);
        System.out.println(manager.getFileSize());
        try {
            manager.startDownload();
        } catch (MalformedURLException e) {
            System.out.println("Invalid URL: " + e);
        } catch (InterruptedException e) {
            System.out.println("Download interrupted: " + e);
        }

    }
}
