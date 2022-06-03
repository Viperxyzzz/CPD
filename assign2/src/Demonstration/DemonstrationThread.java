package Demonstration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DemonstrationThread extends Thread{
    String port;
    String name;
    public DemonstrationThread(String port, String name) {
        this.port = port;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            runNode(this.port,this.name);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runNode(String port, String name) throws IOException, InterruptedException {
        String command = "java Store 224.0.0.0 8080 127.0.0.1 " + port;
        Process proc = Runtime.getRuntime().exec(command);

        // Read the output

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(proc.getInputStream()));

        String line = "";
        while((line = reader.readLine()) != null) {
            System.out.print(name + ": " + line + "\n");
        }
    }
}
