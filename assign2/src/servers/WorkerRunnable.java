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
            InputStream input = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            String message = reader.readLine();

            System.out.println("Received a message: "+ message);

            if (testClient) {
                messageHandler = new MessageHandlerTestClient(message,clientSocket);
            } else { messageHandler = new MessageHandlerNodes(message,clientSocket);}

            messageHandler = new MessageHandler(message, clientSocket);

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}