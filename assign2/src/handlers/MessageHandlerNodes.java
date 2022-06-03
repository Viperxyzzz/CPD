package handlers;

import data.StoreData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.HashMap;

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
                StringBuilder newLog = this.mixLogs(bis);
                StoreData.setMembershipLog(newLog);
                break;
            default:
                System.out.println("message received:" + inputLine);
        }

    }


    protected StringBuilder mixLogs(BufferedReader receivedLog) throws IOException {


        BufferedReader currentLog = StoreData.getMembershipLogBuff(this.port);
        HashMap<Integer,Integer> currentLogMap = StoreData.LogToMap(currentLog);

        StringBuilder newLog = new StringBuilder();


        String line = receivedLog.readLine();
        while (line != null) {
            String[] lineArray = line.split(";");
            String[] id = lineArray[0].split(":");
            int port = Integer.parseInt(id[1]);
            int membershipCount = Integer.parseInt(lineArray[1]);
            line = receivedLog.readLine();

            if(!currentLogMap.containsKey(port)) {
                newLog.append(StoreData.getLogLine(StoreData.nodeId,port, String.valueOf(membershipCount)) + "\n" );
                continue;
            }

            if(currentLogMap.get(port) > membershipCount ) {
                newLog.append(StoreData.getLogLine(StoreData.nodeId,port, String.valueOf(currentLogMap.get(port))) + "\n");
            } else {
                newLog.append(StoreData.getLogLine(StoreData.nodeId,port, String.valueOf(membershipCount)) + "\n" );
            }
        }
        receivedLog.close();
        currentLog.close();
        return newLog;

    }
}

