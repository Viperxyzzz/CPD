import servers.TCPServer;
import servers.UDPClient;
import servers.UDPServer;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Store {

    public static void main(String[] args) throws UnknownHostException {

        if (args.length < 4) {
            System.out.println("You should provide 4 arguments: <multicastIP> <multicastPort> <node_id>  <Store_port>");
            return;
        }
        InetAddress multicastIP = InetAddress.getByName(args[0]);
        int multicastPort = Integer.parseInt(args[1]);
        InetAddress node_id = InetAddress.getByName(args[2]);
        int port = Integer.parseInt(args[3]);

        startTCP(node_id, port);
        starTCPforTestClient(node_id,port);

        //System.out.println("Stopping Server");
        //server.stop();

    }

    private static void starTCPforTestClient(InetAddress node_id, int port) {
        TCPServer server = new TCPServer(port+1000, node_id);
        new Thread(server).start();
        System.out.println("this is the new verison");

    }

    private static void startTCP(InetAddress node_id, int port) {
        TCPServer server = new TCPServer(port, node_id);
        new Thread(server).start();
        System.out.println("this is the new verison");

    }
}