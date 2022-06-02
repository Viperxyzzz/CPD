package servers;

import handlers.MessageHandlerNodes;
import handlers.UDPHandler;

import java.io.IOException;
import java.net.*;

public class UDPServer extends Thread {

    private MulticastSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];
    InetAddress group = null;

    public UDPServer(InetAddress multicastIP, int multicastPort) throws IOException {
        socket = new MulticastSocket(multicastPort);
        group = multicastIP;
    }

    public void run() {

        System.out.println("receiving udp messages in port " + this.socket.getLocalPort() + " of ip " + this.socket.getLocalAddress().getHostAddress());


        try {
            socket.joinGroup(group);


            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());

                new UDPHandler(message);
            }
            //socket.leaveGroup(group);
            //socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}