package servers;

import java.io.IOException;
import java.net.*;

public class MessageSenderUDP {
    private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;
    int port;


    public MessageSenderUDP(InetAddress multicastIP, int multicastPort) throws IOException {
        this.port = multicastPort;
        socket = new DatagramSocket();
        group = multicastIP;
    }

    public void multicast(String multicastMessage) throws IOException {
        buf = multicastMessage.getBytes();

        DatagramPacket packet = new DatagramPacket(buf, buf.length, this.group, this.port);
        socket.send(packet);
        socket.close();
    }
}