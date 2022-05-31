package servers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

abstract class MessageHandler {
    Socket clientSocket;
    PrintWriter writer; // if you want to answer to the received message write here
    InetAddress clientIp; // useful in debuggin to know who you are talking to

    public MessageHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.writer = new PrintWriter(clientSocket.getOutputStream(), true);
        this.clientIp = clientSocket.getInetAddress();
        this.handleMessage();
    }

    protected abstract void handleMessage() throws IOException;


}
