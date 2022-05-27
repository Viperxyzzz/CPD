import servers.TCPServer;
import servers.UDPClient;
import servers.UDPServer;

public class Store {
    TCPServer tcpServer;
    UDPServer udpServer;
    UDPClient udpClient;

    public static void main(String[] args) {


        TCPServer server = new TCPServer(9000);
        new Thread(server).start();

        try {
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stopping Server");
        server.stop();

    }
}