package org.example.test;

import java.io.IOException;

public class MethodTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec("calc.bat");

        // Wait for the process to complete
        process.waitFor();

        System.out.println("Batch file executed successfully.");
    }
}
