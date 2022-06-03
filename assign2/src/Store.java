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

        storeData.startTCPforTestClient(nodeId,nodePort);

    }
}