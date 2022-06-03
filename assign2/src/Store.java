import servers.MessageSenderTCP;
import servers.MessageSenderUDP;
import servers.TCPServer;

import data.*;
import servers.UDPServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static java.lang.Thread.sleep;

public class Store {

    public static void main(String[] args) throws IOException, InterruptedException {


        if (args.length < 4) {
            System.out.println("You should provide 4 arguments: <multicastIP> <multicastPort> <node_id>  <Store_port>");
            return;
        }
        InetAddress multicastIP = InetAddress.getByName(args[0]);
        int multicastPort = Integer.parseInt(args[1]);
        InetAddress nodeId = InetAddress.getByName(args[2]);
        int nodePort = Integer.parseInt(args[3]);

        StoreData storeData = new StoreData(multicastIP,multicastPort,nodeId,nodePort);

        startTCP(nodeId, nodePort);
        startTCPforTestClient(nodeId,nodePort);
        startUDP( multicastIP, multicastPort);









    }

    private static void startUDP(InetAddress multicastIP, int multicastPort) throws IOException {
        new UDPServer(multicastIP, multicastPort).start();
    }

    private static void startTCPforTestClient(InetAddress node_id, int port) {
        TCPServer server = new TCPServer(port+1000, node_id, true);
        new Thread(server).start();
        System.out.println("Receiving messages from test client on port " + (port+1000));

    }

    private static void startTCP(InetAddress node_id, int port) {
        TCPServer server = new TCPServer(port, node_id, false);
        new Thread(server).start();
        System.out.println("Receiving messages from another nodes on port " + port);

    }
}