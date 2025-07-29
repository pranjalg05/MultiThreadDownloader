import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DownloadManager {

    private final String url;
    private final int nthreads;
    private long fileSize;

    DownloadManager(String url, int threads){
        this.url = url;
        this.nthreads = threads;
    }

    public void startDownload() throws MalformedURLException, InterruptedException {

        fileSize = getFileSize();
        if (fileSize <= 0) {
            System.out.println("Failed to retrieve file size.");
            return;
        }
        long chunksize = fileSize/nthreads;
        ExecutorService service = Executors.newFixedThreadPool(nthreads);

        for (int i = 0; i < nthreads; i++) {
            long low = i* chunksize;
            long high = (i==nthreads-1)? fileSize-1:(low + chunksize -1);

            service.submit(new DownloadThreads(low, high, url));
        }

        service.shutdown();
        service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

    public long getFileSize(){
        this.fileSize = computeSize();
        return this.fileSize;
    }

    private long computeSize(){

        try{
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Range", "bytes=0-0");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setInstanceFollowRedirects(true);

            connection.connect();
            int responseCode = connection.getResponseCode();
            if(responseCode==206){
                String contentRange = connection.getHeaderField("Content-Range");
                if(contentRange!=null){
                    long size = Long.parseLong(contentRange.split("/")[1]);
                    return size;
                }
                else {
                    return connection.getContentLengthLong();
                }
            }
            else {
                return connection.getContentLengthLong();
            }

        } catch (MalformedURLException e){
            System.out.println("Invalid URL: " + e);
        } catch (IOException e){
            System.out.println("Connection Error: "  + e );
        }

        return -1;

    }

}
