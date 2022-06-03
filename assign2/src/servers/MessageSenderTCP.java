package servers;

        import java.io.*;
        import java.net.InetAddress;
        import java.net.Socket;
        import java.net.UnknownHostException;

public class MessageSenderTCP implements Runnable{

    protected int serverPort;
    protected InetAddress node_id;
    protected String message;

    public MessageSenderTCP(int port, InetAddress node_id, String message){
        this.node_id = node_id;
        this.serverPort = port;
        this.message = message;
    }

    public void run(){
        try (Socket socket = new Socket(node_id, serverPort)) {

            OutputStream outstream = socket.getOutputStream();
            PrintWriter out = new PrintWriter(outstream,true);
            System.out.println("sending this message : \n" + message);
            out.println(message);


        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}

