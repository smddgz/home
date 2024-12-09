package org.example.test;

import org.example.entity.ArticleInfo;
import org.example.service.CryptoService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MethodTest {

    @Test
    void bodyTest() throws IOException {
        CryptoService service = new CryptoService();
        List<ArticleInfo> announcementInfo = service.getAnnouncementInfo(new String(Files.readAllBytes(Paths.get("anno.html"))));
        System.out.println(announcementInfo);
    }

    @Test
    void urlCode() throws UnknownHostException {

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec("calc.bat");

        // Wait for the process to complete
        process.waitFor();

        System.out.println("Batch file executed successfully.");

    }
}
