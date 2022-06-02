package handlers;

import data.StoreData;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.TreeMap;

public abstract class MessageHandler {
    Socket clientSocket;
    PrintWriter writer; // if you want to answer to the received message write here
    InetAddress clientIp; // useful in debuggin to know who you are talking to
    String id;
    int port;
    StoreData data;

    private TreeMap<String,String> nodes;
    /**
     * Todos os nodes conhecidos pelo node
     */


    public MessageHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.writer = new PrintWriter(clientSocket.getOutputStream(), true);
        this.clientIp = clientSocket.getInetAddress();
        this.id = String.valueOf(this.clientSocket.getInetAddress());
        this.port = clientSocket.getLocalPort();
        this.data = new StoreData();
        this.nodes = this.data.nodes;
        this.handleMessage();
    }

    protected abstract void handleMessage() throws IOException;


}
