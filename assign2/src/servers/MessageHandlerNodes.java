package servers;

import java.io.IOException;
import java.net.Socket;

public class MessageHandlerNodes extends MessageHandler {
    public MessageHandlerNodes(Socket clientSocket) throws IOException {
        super(clientSocket);
    }

    @Override
    protected void handleMessage() throws IOException {

    }
}

