package test;
import org.junit.*;
import servers.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

public class TCPTest {
    TCPServer server;

    @Before
    public void setup() throws UnknownHostException {
        InetAddress node_id = InetAddress.getByName("localhost");
        server = new TCPServer(9000, node_id);
    }

    @Test
    public void test() {
        new Thread(server).start();

        try {
            Thread.sleep(1 * 1000);
            System.out.println("If youre seeing this everything is working");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void shutDown() throws IOException {
        System.out.println("Stopping Server");
        server.stop();
    }
}