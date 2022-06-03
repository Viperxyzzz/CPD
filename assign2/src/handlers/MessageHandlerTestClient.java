package handlers;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

import data.Message;
import servers.MessageSenderTCP;

import servers.MessageSenderUDP;
import utility.Utils;

import data.NodeStore;
import data.StoreData;

import static java.lang.Thread.sleep;

public class MessageHandlerTestClient extends MessageHandler {


    public MessageHandlerTestClient(Socket clientSocket) throws IOException {
        super(clientSocket);

        //this.store = new NodeStore(Utils.sha256(Integer.toString(this.port)));

        // TODO --- ISTO N PODE ESTAR AQUI SE NÃO O TESTCLIENT SERÁ ERRADAMENTE ASSUMIDO COMO UM NODE, tem que estar no MessageHandlerNode
        /*
        this.data.nodes.put(Utils.sha256(Integer.toString(this.port)),Integer.toString(this.port));
        System.out.println("PORT " + this.port);
        if(port == 1080){
            this.data.nodes.put("ab9828ca390581b72629069049793ba3c99bb8e5e9e7b97a55c71957e04df9a3","1100");
        }

         */
    }

    public void putNodes(Map<String, String> nodes){
        this.nodes.putAll(nodes);
    }

    public void putNode(String id, String host){
        this.nodes.put(Utils.sha256(Integer.toString(this.port)),Integer.toString(this.port));
    }

    public String getId() {
        return id;
    }

    public int putReplica(String key, String value) throws IOException {
        this.store.put(key,value);
        return 0;
    }

    public int put(String key, String value) throws IOException {
        var nodeStore = StoreData.getKnownNodes();
        if(nodeStore != null){
            Map.Entry<String,String> closest = nodeStore.ceilingEntry(key);
            if(closest == null){
                closest = nodeStore.firstEntry();
            }
            if((Integer.toString(this.port)).equals(closest.getValue())){
                int replicationFactor = StoreData.getReplicationFactor();
                replicationFactor--;
                var saveStore = StoreData.getKnownNodes();
                Map.Entry<String, String> finalClosest = closest;
                saveStore.entrySet().removeIf(entry -> entry.getValue().equals(finalClosest.getValue()));
                System.out.println("REPLICATION FACTOR " + replicationFactor);
                while(replicationFactor != 0){
                    System.out.println("SAVE STORE " + saveStore);
                    var replica = saveStore.ceilingEntry(key);
                    System.out.println("REPLICA " + replica);
                    if(replica == null){
                        replica = saveStore.firstEntry();
                    }
                    MessageSenderTCP test = new MessageSenderTCP(Integer.parseInt(replica.getValue()),clientSocket.getInetAddress(), Message.createPutReplica(key,value));
                    new Thread(test).start();
                    Map.Entry<String, String> finalReplica = replica;
                    saveStore.entrySet().removeIf(entry -> entry.getValue().equals(finalReplica.getValue()));
                    replicationFactor--;
                }
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
            var nodeStore = StoreData.getKnownNodes();
            System.out.println("Key not found, redirecting it ");
            Map.Entry<String,String> closest = nodeStore.ceilingEntry(key);
            if(closest == null)
            {
                closest = nodeStore.firstEntry();
            }
            //in case a fault happened
            if((Integer.toString(this.port)).equals(closest.getValue())) {
                //we try to find it somewhere
                Map.Entry<String, String> finalClosest = closest;
                nodeStore.entrySet().removeIf(entry -> entry.getValue().equals(finalClosest.getValue()));
                closest = nodeStore.ceilingEntry(key);
                if(closest == null){
                    closest = nodeStore.firstEntry();
                }
            }

            if(closest == null){
                //the key wasnt found anywhere else
                System.out.println("Unable to find the key\n");
                return "ERROR\n";
            }

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
            var nodeStore = StoreData.getKnownNodes();
            Map.Entry<String,String> closest = nodeStore.ceilingEntry(key);
            System.out.println("Key was not found, redirecting it to the cluster");
            MessageSenderTCP test = new MessageSenderTCP(Integer.parseInt(closest.getValue()), clientSocket.getInetAddress(), Message.createDeleteMessage(key));
            new Thread(test).start();
        }

        return "";
    }

    protected void join() throws IOException {
        if(!StoreData.getMembershipCount(StoreData.nodePort).equals("0")) {
            StoreData.increaseMembershipCount(StoreData.nodePort);
        }

        StoreData.startTCP(StoreData.nodeId,StoreData.nodePort);
        MessageSenderUDP test = new MessageSenderUDP( StoreData.multicastIP,StoreData.multicastPort);
        String message = Message.createJoinMessage(StoreData.nodePort);
        test.multicast(message);

        try{
            sleep(500);
            if(!StoreData.inCluster) {
                System.out.println("Have not received 3 memberships yet, sending join for the second time");
                test.multicast(message);
            }
            sleep(500);
            if(!StoreData.inCluster) {
                System.out.println("Have not received 3 memberships yet, sending join for the third time");
                test.multicast(message);
            }
            sleep(500);
            if(!StoreData.inCluster) {
                System.out.println("im the first node, initializing cluster");
                StoreData.startUDP(StoreData.multicastIP, StoreData.multicastPort);
                StoreData.inCluster = true;
            }
        } catch (InterruptedException e) {
        }
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
                this.join();
                break;
            case "putreplica":
                System.out.println("Used as a replica\n");
                StringBuilder message2 = new StringBuilder();
                String line2;
                while(!(line2 = bis.readLine()).equals("END")){ //this will only work if there are no new lines change this
                    message2.append(line2 + "\n");
                }
                this.putReplica(key,message2.toString());
                break;
            default:
                System.out.println(inputLine + " not implemented");
                break;
        }
    }
}
