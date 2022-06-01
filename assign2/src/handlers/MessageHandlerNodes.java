package handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MessageHandlerNodes extends MessageHandler {
    public MessageHandlerNodes(Socket clientSocket) throws IOException {
        super(clientSocket);
    }

    @Override
    protected void handleMessage() throws IOException {
        BufferedReader bis = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine;
        inputLine = bis.readLine();
        String key = bis.readLine();

        switch (inputLine) {
            default:
                System.out.println("message received:" + inputLine);
        }

    }
}

