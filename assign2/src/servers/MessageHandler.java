package servers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MessageHandler {
    String message; // received message
    Socket clientSocket;
    PrintWriter writer; // if you want to answer to the received message write here
    InetAddress clientIp; // useful in debuggin to know who you are talking to

    public MessageHandler(String message, Socket clientSocket) throws IOException {
        this.message = message;
        this.clientSocket = clientSocket;
        this.writer = new PrintWriter(clientSocket.getOutputStream(), true);
        this.clientIp = clientSocket.getInetAddress();
        this.handleMessage();
    }

    private void handleMessage() {
        if(message.startsWith("hello")) {

            writer.println("hello to you too, " + clientIp.getHostAddress());
        }

        if(message.startsWith("join")) {
            writer.println("join message received");
        }
    }


}
