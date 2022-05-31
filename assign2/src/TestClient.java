import servers.Message;
import servers.Utils;

import java.net.*;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.net.Socket;


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
        String opnd = "ERROR";
        if (args.length == 3) {opnd = args[2];}
        System.out.println("before");
        try (Socket socket = new Socket(hostname, port)) {
            System.out.println("after");

            switch(operation){
                case "put":
                    put(opnd ,hostname,port, socket);
                    return;
                case "get":
                    get(opnd,hostname,port, socket);
                    return;
                case "delete":
                    delete(opnd,hostname,port, socket);
                    return;
                default:
                    System.out.println("Not implemented");
                    break;
            }

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            String receivedMessage = reader.readLine();

            System.out.println(receivedMessage);


        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }
    }


    public static String getFileContent(String filepath) throws FileNotFoundException {
        StringBuilder message = new StringBuilder();
        File file = new File("test.txt");
        Scanner myScanner = new Scanner(file);
        while(myScanner.hasNextLine()){
            String line = myScanner.nextLine();
            message.append(line).append("\n");
        }
        myScanner.close();
        return message.toString();

    }

    public static int put(String filepath,String hostname, int port , Socket socket) throws IOException {
        OutputStream outstream = socket.getOutputStream();
        PrintWriter out = new PrintWriter(outstream,true);
        String message = getFileContent(filepath);
        String key = Utils.sha256(message);
        System.out.println("MESSAGE : \n " + message);
        System.out.println("KEY = " + key);
        out.println(Message.createPutMessage(key,message));
        return 0;
    }

    public static int get(String key, String hostname, int port , Socket socket) throws IOException {
        OutputStream outstream = socket.getOutputStream();
        PrintWriter out = new PrintWriter(outstream,true);
        out.println(Message.createGetMessage(key));
        return 0;
    }

    public static int delete(String key, String hostname, int port , Socket socket) throws IOException {
        OutputStream outstream = socket.getOutputStream();
        PrintWriter out = new PrintWriter(outstream,true);
        out.println(Message.createDeleteMessage(key));
        return 0;
    }

}
