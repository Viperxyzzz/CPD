package handlers;

import data.Message;
import data.StoreData;
import servers.MessageSenderTCP;

import java.net.InetAddress;
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
            int destNodePort = 0;
            int destMembershipCount = -1;
            int i = 0;
            scanner = new Scanner(message);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(i == 1) {
                    destNodePort = Integer.parseInt(line);
                }
                if(i == 2) {
                    destMembershipCount = Integer.parseInt(line);
                }
                i++;
            }
            scanner.close();

            StoreData.addLogLine(this.port, StoreData.getLogLine(StoreData.nodeId, destNodePort, String.valueOf(destMembershipCount)));
            StringBuilder message = new StringBuilder();
            message.append("membership\n");
            message.append(StoreData.getMembershipLog(this.port));


            MessageSenderTCP test = new MessageSenderTCP(destNodePort, StoreData.nodeId, message.toString());
            new Thread(test).start();


        }



    }


}
