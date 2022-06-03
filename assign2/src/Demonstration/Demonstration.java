package Demonstration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static java.lang.Thread.sleep;


public class Demonstration {

    public static void main(String[] args) throws IOException, InterruptedException {


        File directory = new File("Cluster/MembershipLogs");

        for (File file: Objects.requireNonNull(directory.listFiles())) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }

        directory = new File("Cluster/MembershipCounters");

        for (File file: Objects.requireNonNull(directory.listFiles())) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }


        new DemonstrationThread("9001", "Node1").start();
        new DemonstrationThread("9002", "Node2").start();
        new DemonstrationThread("9003", "Node3").start();
        new DemonstrationThread("9004", "Node4").start();
        new DemonstrationThread("9005", "Node5").start();

        joinnodes();

        sleep(2*1000);

        Runtime.getRuntime().exec("java TestClient 127.0.0.1:10004 leave");

        sleep(2*1000);

        Runtime.getRuntime().exec("java TestClient 127.0.0.1:10004 join");



    }

    public static void joinnodes() throws InterruptedException, IOException {
        sleep(500);
        Runtime.getRuntime().exec("java TestClient 127.0.0.1:10001 join");

        sleep(2*1000);

        Runtime.getRuntime().exec("java TestClient 127.0.0.1:10002 join");

        sleep(2*1000);

        Runtime.getRuntime().exec("java TestClient 127.0.0.1:10003 join");

        sleep(2*1000);

        Runtime.getRuntime().exec("java TestClient 127.0.0.1:10004 join");

        sleep(2*1000);

        Runtime.getRuntime().exec("java TestClient 127.0.0.1:10005 join");

    }

}

