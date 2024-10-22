package org.example.test;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class MethodTest {

    @Test
    void urlCode() throws UnknownHostException {
        InetAddress loopbackAddress = Inet4Address.getLoopbackAddress();
        InetAddress localHost = Inet4Address.getLocalHost();
        System.out.println(localHost.getHostAddress());
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec("calc.bat");

        // Wait for the process to complete
        process.waitFor();

        System.out.println("Batch file executed successfully.");


    }
}
