package handlers;

import data.NodeStore;
import data.StoreData;
import utility.Utils;

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
    NodeStore store;



    TreeMap<String,String> nodes;
    /**
     * Todos os nodes conhecidos pelo node
     */


    public MessageHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.writer = new PrintWriter(clientSocket.getOutputStream(), true);
        this.clientIp = clientSocket.getInetAddress();
        this.id = String.valueOf(this.clientSocket.getInetAddress());
        this.port = clientSocket.getLocalPort();
        this.nodes = StoreData.nodes;
        this.store = new NodeStore(Utils.sha256(Integer.toString(this.port)));
        this.handleMessage();
    }

    protected abstract void handleMessage() throws IOException;


}
