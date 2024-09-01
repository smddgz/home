package org.example.test;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MethodTest {

    @Test
    void urlCode() {
        String a = URLEncoder.encode("新建文本文档.txt", StandardCharsets.UTF_8);
        System.out.println(a);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec("calc.bat");

        // Wait for the process to complete
        process.waitFor();

        System.out.println("Batch file executed successfully.");


    }
}
