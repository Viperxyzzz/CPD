package servers;

import java.io.IOException;
import java.net.Socket;

public class MessageHandlerNodes extends MessageHandler {
    public MessageHandlerNodes(String message, Socket clientSocket) throws IOException {
        super(message, clientSocket);
    }

    private void handleMessage() {
        if (message.startsWith("hello")) {

            writer.println("hello to you too, " + clientIp.getHostAddress());
        }
    }
}

