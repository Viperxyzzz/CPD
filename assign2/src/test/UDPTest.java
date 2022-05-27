package test;
import org.junit.*;
import servers.*;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

public class UDPTest {
    UDPClient client;

    @Before
    public void setup() throws SocketException, UnknownHostException {
        new UDPServer().start();
        client = new UDPClient();
    }

    @Test
    public void whenCanSendAndReceivePacket_thenCorrect() throws IOException {
        String echo = client.sendEcho("hello server");
        assertEquals("hello server", echo);
        echo = client.sendEcho("server is working");
        assertFalse(echo.equals("hello server"));
    }

    @After
    public void tearDown() throws IOException {
        client.sendEcho("end");
        client.close();
    }
}
