import java.net.*;
import java.io.*;

/**
 * This program demonstrates a simple TCP/IP socket client.
 *
 * @author www.codejava.net
 */
public class TestClient {

    public static void main(String[] args) {
        if (args.length < 2) return;

        String[] hostnameAndPort = args[0].split(":");

        String hostname = hostnameAndPort[0];
        int port = Integer.parseInt(hostnameAndPort[1]);
        String operation = args[1];
        if (args.length == 3) {String opnd = args[2];}

        try (Socket socket = new Socket(hostname, port)) {

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.println(operation.toString());

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            String time = reader.readLine();

            System.out.println(time);


        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}