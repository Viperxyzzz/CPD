package handlers;

import data.Message;
import data.StoreData;
import servers.MessageSenderTCP;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class UDPHandler {

    String message;
    int port;

    public UDPHandler(String message) throws IOException {
        this.message = message;
        this.port = StoreData.nodePort;
        this.handleMessage();
    }

    private void handleMessage() throws IOException {

        Scanner scanner = new Scanner(message);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (Objects.equals(line, Integer.toString(this.port))) {
                scanner.close();
                return;
            }
        }

        if(message.startsWith("join") && (Integer.parseInt(message.split("\n")[2]) % 2 == 0)) {
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

            var var = StoreData.LogToMap(StoreData.getMembershipLogBuff(StoreData.nodePort));
            if(!var.containsKey(destNodePort)) {
                StoreData.addLogLine(this.port, StoreData.getLogLine(StoreData.nodeId, destNodePort, String.valueOf(destMembershipCount)));
            } else {
                StringBuilder newLog = new StringBuilder();
                var currentLog = StoreData.getMembershipLog(StoreData.nodePort);
                var lines = currentLog.split("\n");
                for(var line : lines) {
                    var node =  line.split(";")[0].split(":")[1];
                    if (node.equals(message.split("\n")[1])){
                        continue;
                    } else {
                        newLog.append(line + "\n");
                    }
                }
                newLog.append(StoreData.getLogLine(StoreData.nodeId, destNodePort, String.valueOf(destMembershipCount)) + "\n");
                StoreData.setMembershipLog(newLog);
            }
            StringBuilder message = new StringBuilder();
            message.append("membership\n");
            message.append(StoreData.getMembershipLog(this.port));
            try{
                sleep((int)(Math.random()*(400-1+1)+1));
            } catch (InterruptedException e) {

            }
            MessageSenderTCP test = new MessageSenderTCP(destNodePort, StoreData.nodeId, message.toString());
            new Thread(test).start();
        } else if(message.startsWith("join") && (Integer.parseInt(message.split("\n")[2]) % 2 != 0)) {
            StringBuilder result = new StringBuilder();
            var currentLog = StoreData.getMembershipLog(StoreData.nodePort);
            var currentLogLines = currentLog.split("\n");
            for (var line : currentLogLines) {
                var node =  line.split(";")[0].split(":")[1];
                if (node.equals(message.split("\n")[1])){
                    int counter = Integer.parseInt(line.split(";")[1]);
                    int receivedCounter = Integer.parseInt(message.split("\n")[2]);
                    int newCounter = 0;
                    if(receivedCounter > counter) {
                        newCounter = receivedCounter;
                    } else newCounter = counter;
                    result.append(line.split(";")[0] + ";" + newCounter + "\n");
                } else {
                    result.append(line + "\n");
                }
            }
            StoreData.setMembershipLog(result);

        } else if (message.startsWith("periodicLog")) {
            scanner = new Scanner(message);
            StringBuilder newLog = new StringBuilder();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line != "periodicLog" && line != "") {
                    newLog.append(line + "\n");
                }
            }
            scanner.close();
            newLog.delete(0,12);
            StoreData.setMembershipLog(newLog);
        }



    }


}
