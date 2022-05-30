package servers;

import java.io.*;
import java.net.Socket;


public class WorkerRunnable implements Runnable{

    private MessageHandler messageHandler;

    protected Socket clientSocket = null;
    protected String serverText   = null;

    public WorkerRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
    }

    public void run() {
        try {
            InputStream input = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            String message = reader.readLine();

            System.out.println("Received a message: "+ message);

            messageHandler = new MessageHandler(message, clientSocket);

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}