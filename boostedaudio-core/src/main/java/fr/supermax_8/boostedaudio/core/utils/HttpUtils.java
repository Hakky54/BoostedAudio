package fr.supermax_8.boostedaudio.core.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtils {

    public static String downloadAudioSafely(String urlString, String saveDir) {
        File downloadedFile = downloadFile(urlString, saveDir);
        if (downloadedFile == null) return "";
        if (!downloadedFile.exists()) return "";
        String[] split = downloadedFile.getName().split("\\.");
        String fileExtension = split[split.length - 1].toLowerCase();
        switch (fileExtension) {
            case "wav", "mp3", "flac", "ogg", "webm", "opus", "aac", "m4a" -> {
                return downloadedFile.getName();
            }
            default -> {
                downloadedFile.delete();
                System.out.println("Weird audio file downloaded, PLEASE send a message to the dev of this software, " + urlString + " extension name " + fileExtension);
                return "";
            }
        }
    }

    public static File downloadFile(String fileURL, String saveDir) {
        try {
            URL url = new URL(fileURL);
            // open the connection
            URLConnection con = url.openConnection();

            // get and verify the header field
            String fieldValue = con.getHeaderField("Content-Disposition");
            if (fieldValue == null || !fieldValue.contains("filename=\"")) {
                // no file name there -> throw exception ...
            }
            // parse the file name from the header field
            String filename = fieldValue.substring(fieldValue.indexOf("filename=\"") + 10, fieldValue.length() - 1);
            filename = filename.replaceAll(" ", "_").replaceAll("[^a-zA-Z0-9._]", "");

            File saveDirFile = new File(saveDir);
            if (!saveDirFile.exists()) saveDirFile.mkdirs();
            // create file in systems temporary directory
            File download = new File(saveDir, filename);

            // open the stream and download
//            ReadableByteChannel rbc = Channels.newChannel(con.getInputStream());
//            try (FileOutputStream fos = new FileOutputStream(download)) {
//                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
//            }

            int fileSize = con.getContentLength();

            int fileSizekB = fileSize / 1024;

            long startTime = System.currentTimeMillis();
            int intervalInfo = 10;
            int progress = 0;

            try (FileOutputStream outputStream = new FileOutputStream(download); InputStream inputStream = con.getInputStream()) {
                byte[] buffer = new byte[1024]; // Buffer for reading data from the input stream
                int bytesRead;
                long totalBytesRead = 0;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;
                    int currentProgress = (int) ((totalBytesRead * 100) / fileSize);
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    double downloadSpeed = (totalBytesRead / 1024.0) / (elapsedTime / 1000.0); // in KB/s

                    double estimatedTime = (fileSizekB - ((double) totalBytesRead / 1024)) / downloadSpeed;

                    if (estimatedTime >= 200) intervalInfo = 1;
                    else if (estimatedTime >= 100) intervalInfo = 2;
                    else if (estimatedTime >= 50) intervalInfo = 10;
                    else if (estimatedTime >= 10) intervalInfo = 15;
                    else if (estimatedTime >= 5) intervalInfo = 20;
                    else if (estimatedTime >= 2) intervalInfo = 50;

                    if ((currentProgress % intervalInfo == 0 || currentProgress == 100) && currentProgress != progress)
                        System.out.println("Download Progress: " + currentProgress + "%, Etimated Time: " + String.format("%.2f", estimatedTime) + "s, Download Speed: " + String.format("%.2f", downloadSpeed) + " KB/s");
                    progress = currentProgress;
                }
            }

            System.out.println("File " + filename + " download complete");
            return download;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonObject httpGet(String urlString) {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Configuration de la requête HTTP
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:104.0) Gecko/20100101 Firefox/104.0");
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");

            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return new JsonParser().parse(response.toString()).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}