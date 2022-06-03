package handlers;

import data.StoreData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class MessageHandlerNodes extends MessageHandler {
    public MessageHandlerNodes(Socket clientSocket) throws IOException {
        super(clientSocket);
    }

    protected void handleMessage() throws IOException {
        BufferedReader bis = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine;
        inputLine = bis.readLine();

        switch (inputLine) {
            case "membership":
                if(StoreData.inCluster) {
                    break;
                }

                StoreData.numberOfMembershipMsgReceived++;
                StoreData.membershipMessages.add(bis);

                if (StoreData.numberOfMembershipMsgReceived > 2) {

                    StringBuilder newLog = this.mix3logs();
                    if (checkIfShouldUpdate(newLog)) {StoreData.setMembershipLog(newLog);}
                    System.out.println("joined the cluster");
                    StoreData.startUDP(StoreData.multicastIP, StoreData.multicastPort);
                    StoreData.inCluster = true;
                    break;
                }

                break;

            default:
                System.out.println("message received:" + inputLine);
        }

    }

    private StringBuilder mix3logs() throws IOException {
        StringBuilder result = null;
        for(int i = 0; i <3; i++) {
            result = this.mixLogs(StoreData.membershipMessages.get(i));
        }
        return result;
    }


    protected StringBuilder mixLogs(BufferedReader receivedLog) throws IOException {


        BufferedReader currentLog = StoreData.getMembershipLogBuff(StoreData.nodePort);
        HashMap<Integer,Integer> currentLogMap = StoreData.LogToMap(currentLog);

        StringBuilder newLog = new StringBuilder();


        String line = receivedLog.readLine();
        while (line != null && !line.equals("")) {
            String[] lineArray = line.split(";");
            String[] id = lineArray[0].split(":");
            int port = Integer.parseInt(id[1]);
            int membershipCount = Integer.parseInt(lineArray[1]);
            line = receivedLog.readLine();

            if(!currentLogMap.containsKey(port)) {
                newLog.append(StoreData.getLogLine(StoreData.nodeId,port, String.valueOf(membershipCount)));
                continue;
            }

            if(currentLogMap.get(port) > membershipCount ) {
                newLog.append(StoreData.getLogLine(StoreData.nodeId,port, String.valueOf(currentLogMap.get(port))));
            } else {
                newLog.append(StoreData.getLogLine(StoreData.nodeId,port, String.valueOf(membershipCount)));
            }
        }
        receivedLog.close();
        currentLog.close();
        return newLog;

    }

    protected Boolean checkIfShouldUpdate(StringBuilder newLog) throws IOException {
        BufferedReader currentLog = StoreData.getMembershipLogBuff(StoreData.nodePort);
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while ((line = currentLog.readLine()) != null) {
            stringBuilder.append(line);
        }

        String currentLogStr = stringBuilder.toString();
        String newLogStr = newLog.toString();

        if(currentLogStr.length() < newLogStr.length()) {
            return true;
        } else {
            return false;
        }
    }
}



