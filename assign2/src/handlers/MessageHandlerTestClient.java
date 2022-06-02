package handlers;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

import data.Message;
import servers.MessageSenderTCP;

import utility.Utils;

import data.NodeStore;
import data.StoreData;

public class MessageHandlerTestClient extends MessageHandler {


    private NodeStore store;

    public MessageHandlerTestClient(Socket clientSocket) throws IOException {
        super(clientSocket);


        this.store = new NodeStore(Utils.sha256(Integer.toString(this.port)));

        // TODO --- ISTO N PODE ESTAR AQUI SE NÃO O TESTCLIENT SERÁ ERRADAMENTE ASSUMIDO COMO UM NODE, tem que estar no MessageHandlerNode
        this.data.nodes.put(Utils.sha256(Integer.toString(this.port)),Integer.toString(this.port));


        System.out.println("PORT " + this.port);
        if(port == 1080){
            this.data.nodes.put("ab9828ca390581b72629069049793ba3c99bb8e5e9e7b97a55c71957e04df9a3","1100");
        }
    }

    public void putNodes(Map<String, String> nodes){
        this.data.nodes.putAll(nodes);
    }

    public void putNode(String id, String host){
        this.data.nodes.put(Utils.sha256(Integer.toString(this.port)),Integer.toString(this.port));
    }

    public String getId() {
        return id;
    }

    public int put(String key, String value) throws IOException {

        if(this.data.nodes != null){
            Map.Entry<String,String> closest = this.data.nodes.ceilingEntry(key);
            if(closest == null){
                closest = this.data.nodes.firstEntry();

            }
            if((Integer.toString(this.port)).equals(closest.getValue())){
                this.store.put(key,value);
            }
            else{
                MessageSenderTCP test = new MessageSenderTCP(Integer.parseInt(closest.getValue()), clientSocket.getInetAddress(), Message.createPutMessage(key,value));
                new Thread(test).start();
            }

        }
        else{
            System.out.println("Cluster is empty, failed to put");
        }
        return 0;
    }

    public String get(String key) throws IOException {
        if(store.hasKey(key)){
            return store.get(key);
        }
        else{
            System.out.println("Key not found, redirecting it ");
            Map.Entry<String,String> closest = this.data.nodes.ceilingEntry(key);
            Socket socket = new Socket(clientSocket.getInetAddress(), Integer.parseInt(closest.getValue()));
            OutputStream outstream = socket.getOutputStream();
            PrintWriter out = new PrintWriter(outstream,true);
            out.println(Message.createGetMessage(key));
            System.out.println("Trying to get message from the cluster");
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            StringBuilder message = new StringBuilder();
            message.append(br.readLine()).append("\n");
            while(br.ready()){
                message.append(br.readLine()).append("\n");
            }
            System.out.println("Message retrieved");
            return message.toString();
        }
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

        switch(inputLine){
            case "put":
                StringBuilder message = new StringBuilder();
                String line;
                while(!(line = bis.readLine()).equals("END")){ //this will only work if there are no new lines change this
                    message.append(line + "\n");
                }
                this.put(key,message.toString());
                break;
            case "get":
                System.out.println("Getting value for key " + key);
                String value = this.get(key);
                System.out.println("Value was found and is " + value);
                OutputStreamWriter outstream = new OutputStreamWriter(clientSocket.getOutputStream());
                BufferedWriter bufferedWriter = new BufferedWriter(outstream);
                bufferedWriter.write(value);
                bufferedWriter.flush();
                System.out.println("Message sucesfully sent to requester");
                break;
            case "delete":
                this.delete(key);
                break;
            case "join":
                System.out.println("received join message");
                break;
            default:
                System.out.println(inputLine + " not implemented");
                break;
        }
    }
}
