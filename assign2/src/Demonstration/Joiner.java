package Demonstration;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class Joiner {
    public static void main(String[] args) throws IOException, InterruptedException {
        sleep(500);
        Runtime.getRuntime().exec("java 127.0.0.1:10001 join");
        sleep(2*1000);

        Runtime.getRuntime().exec("java 127.0.0.1:10002 join");

        Runtime.getRuntime().exec("java 127.0.0.1:10003 join");
    }
}
