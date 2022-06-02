package handlers;

import data.StoreData;

import java.net.MulticastSocket;
import java.util.Objects;
import java.util.Scanner;

public class UDPHandler {

    String message;
    int port;

    public UDPHandler(String message) {
        this.message = message;
        this.port = StoreData.nodePort;
        this.handleMessage();
    }

    private void handleMessage() {

        Scanner scanner = new Scanner(message);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (Objects.equals(line, Integer.toString(this.port))) {
                scanner.close();
                return;
            }
        }


        if(message.startsWith("join")) {
            int nodePort = 0;
            int i = 0;
            scanner = new Scanner(message);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(i == 1) {
                    nodePort = Integer.parseInt(line);
                }
                i++;
            }
            scanner.close();
            System.out.println("TODO - SEND TCP JOIN MESSAGE FOR NODE " + nodePort);
        }



    }


}
