package servers;

import java.io.IOException;
import java.net.Socket;

public class MessageHandlerTestClient extends MessageHandler{
    public MessageHandlerTestClient(String message, Socket clientSocket) throws IOException {
        super(message, clientSocket);
    }


    private void handleMessage() {

        if(message.startsWith("join")) {
            writer.println("join message received");
        }
    }
}
