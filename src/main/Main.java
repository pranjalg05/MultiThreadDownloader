import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the URL of the file to download: ");
        String fileUrl = scanner.nextLine();

        System.out.print("Enter the number of threads for downloading: ");
        int numberOfThreads;
        while (true) {
            try {
                numberOfThreads = Integer.parseInt(scanner.nextLine());
                if (numberOfThreads <= 0) {
                    System.out.println("Please enter a positive number for threads.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        DownloadManager downloadManager;
        try {
            downloadManager = new DownloadManager(fileUrl, numberOfThreads);
            long fileSize = downloadManager.getFileSize();
            if (fileSize <= 0) {
                System.out.println("Failed to retrieve file size. Please check the URL.");
                return;
            }

            System.out.println("File size: " + fileSize + " bytes");
            System.out.print("Do you want to proceed with the download? (yes/no): ");
            String userInput = scanner.nextLine();
            if (!userInput.equalsIgnoreCase("yes")) {
                System.out.println("Download canceled.");
                return;
            }

            System.out.println("Starting download...");
            downloadManager.startDownload();
            System.out.println("Download completed successfully!");

        } catch (MalformedURLException e) {
            System.out.println("Invalid URL: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Download interrupted: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
