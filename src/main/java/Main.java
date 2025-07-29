import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {
    public static void main(String[] args){

        String link = "https://ash-speed.hetzner.com/100MB.bin";
        (HttpURLConnection) new URL(link).openConnection();
    }
}
