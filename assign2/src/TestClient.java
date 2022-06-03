import data.Message;
import utility.Utils;

import java.net.*;
import java.io.*;
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
        try (Socket socket = new Socket(hostname, port)) {

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
                case "join":
                    join(socket);
                    break;
                default:
                    System.out.println("Not implemented");
                    break;
            }

            // TODO THIS CANT BE HERE BECAUSE SOME CASES DO NOT REQUIRE RESPONSE, FOR EXAMPLE JOIN
            /*
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            String receivedMessage = reader.readLine();

            System.out.println("received message: " + receivedMessage);
            
             */


        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }
    }



    public static String getFileContent(String filepath) throws FileNotFoundException {
        StringBuilder message = new StringBuilder();
        //File file = new File("test.txt");
        File file = new File(filepath);
        Scanner myScanner = new Scanner(file);
        while(myScanner.hasNextLine()){
            String line = myScanner.nextLine();
            message.append(line).append("\n");
        }
        myScanner.close();
        return message.toString();

    }

    public static void join(Socket socket) throws IOException {
        OutputStream outstream = socket.getOutputStream();
        PrintWriter out = new PrintWriter(outstream,true);
        String message = "join";
        out.println(message);
    }

    public static int put(String filepath,String hostname, int port , Socket socket) throws IOException {
        OutputStream outstream = socket.getOutputStream();
        PrintWriter out = new PrintWriter(outstream,true);
        String message = getFileContent(filepath);
        String key = Utils.sha256(message);
        System.out.println("KEY = " + key);
        out.println(Message.createPutMessage(key,message));
        return 0;
    }

    public static int get(String key, String hostname, int port , Socket socket) throws IOException {
        OutputStream outstream = socket.getOutputStream();
        PrintWriter out = new PrintWriter(outstream,true);
        out.println(Message.createGetMessage(key));
        InputStreamReader isr = new InputStreamReader(socket.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        StringBuilder message = new StringBuilder();
        message.append(br.readLine()).append("\n");
        while(br.ready()){
            message.append(br.readLine()).append("\n");
        }
        System.out.println("Message addressed to the given key is \n " + message.toString());
        return 0;
    }

    public static int delete(String key, String hostname, int port , Socket socket) throws IOException {
        OutputStream outstream = socket.getOutputStream();
        PrintWriter out = new PrintWriter(outstream,true);
        out.println(Message.createDeleteMessage(key));
        return 0;
    }

}
