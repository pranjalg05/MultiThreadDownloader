import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadThreads implements Runnable{

    private long lowByte;
    private long highByte;
    private URL url;
    private String outputPath;

    public DownloadThreads(long lowByte, long highByte, String url) throws MalformedURLException {
        this.lowByte = lowByte;
        this.highByte = highByte;
        this.url = new URL(url);
        File downloadsDir = new File("downloads");
        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs();
        }
        String fileName = new File(this.url.getPath()).getName();
        this.outputPath = "downloads" + File.separator + (fileName.isEmpty() ?
                "downloaded_file_" + System.currentTimeMillis() : fileName);
    }

    @Override
    public void run() {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Range", "bytes=" + lowByte+"-"+highByte);
            connection.connect();

            if(connection.getResponseCode()!=206){
                System.out.println("DoesNot allow partial connect!!");
                return;
            }

            try (InputStream in = connection.getInputStream();
                 RandomAccessFile raf = new RandomAccessFile(outputPath, "rw")) {
                raf.seek(lowByte);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    raf.write(buffer, 0, bytesRead);
                }
            }


            System.out.println("Thread [" + lowByte + "-" + highByte + "] done");


        } catch (IOException e) {
            System.out.println("Connection Error: "  + e );
        } finally {
            if(connection!=null)
                connection.disconnect();
        }
    }

}
