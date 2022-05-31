package servers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

public class MessageHandlerTestClient extends MessageHandler{

    private  String id;
    private  String host;
    private int port;
    private NodeStore store;
    private ServerSocket clientServer;

    private TreeMap<String,String> nodes = new TreeMap<>();
    /**
     * Todos os nodes conhecidos pelo node
     */

    public MessageHandlerTestClient(Socket clientSocket) throws IOException {
        super(clientSocket);
        this.id = String.valueOf(clientSocket.getInetAddress());
        this.port = clientSocket.getLocalPort();
        this.store = new NodeStore(Utils.sha256(this.id));
        nodes.put(Utils.sha256(id),id);
        this.handleMessage();
    }

    public void putNodes(Map<String, String> nodes){
        this.nodes.putAll(nodes);
    }

    public void putNode(String id, String host){
        this.nodes.put(Utils.sha256(id),id);
    }

    public String getId() {
        return id;
    }

    public int put(String key, String value) throws IOException {
        if(nodes != null){
            Map.Entry<String,String> closest = this.nodes.ceilingEntry(key);
            if(closest == null){
                closest = this.nodes.firstEntry();
            }
            if(this.id.equals(closest.getValue())){
                this.store.put(key,value);
            }
            else{

            }

        }
        return 0;
    }

    //TODO return this to testclient, for now it doesnt
    public String get(String key) throws FileNotFoundException {
        if(store.hasKey(key)){
            System.out.println(store.get(key));
            return "";
        }
        else{
            System.out.println("File not found");
        }
        return "";
    }

    public String delete(String key){
        if(store.hasKey(key)){
            if(store.delete(key)){
                System.out.println("Sucesfully deleted file");
            }
            else{
                System.out.println("Failed to delete file");
            }
        }
        else{
            System.out.println("Key was not found!");
        }

        return "";
    }


    protected void handleMessage() throws IOException {

        BufferedReader bis = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine;
        inputLine = bis.readLine();
        String key = bis.readLine();

        System.out.println(inputLine);

        switch(inputLine){
            case "put":
                StringBuilder message = new StringBuilder();
                String line;
                while(!(line = bis.readLine()).equals("END")){ //this will only work if there are no new lines change this
                    System.out.println("LINE " + line);
                    message.append(line + "\n");
                }
                System.out.println("MESSAGE " + message);
                this.put(key,message.toString());
                break;
            case "get":
                this.get(key);
                break;
            case "delete":
                this.delete(key);
                break;
            default:
                System.out.println(inputLine + " not implemented");
                break;
        }
    }
}
