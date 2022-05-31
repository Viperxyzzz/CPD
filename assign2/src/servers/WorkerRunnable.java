package servers;

import java.io.*;
import java.net.Socket;


public class WorkerRunnable implements Runnable{

    private MessageHandler messageHandler;

    protected Socket clientSocket = null;
    protected Boolean testClient;

    public WorkerRunnable(Socket clientSocket, Boolean testClient ) {
        this.clientSocket = clientSocket;
        this.testClient = testClient;
    }

    public void run() {
        try {
            if (testClient) {
                messageHandler = new MessageHandlerTestClient(clientSocket);
            } else { messageHandler = new MessageHandlerNodes(clientSocket);}

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}