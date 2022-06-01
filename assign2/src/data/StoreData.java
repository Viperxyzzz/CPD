package data;

import utility.Utils;

import java.net.InetAddress;
import java.util.Map;
import java.util.TreeMap;

public class StoreData {
    public static InetAddress multicastIP;
    public static int multicastPort;
    public static InetAddress nodeId;
    public static int nodePort;

    public static TreeMap<String,String> nodes = new TreeMap<>();

    public StoreData() {}

    public StoreData(InetAddress multicastIP, int multicastPort, InetAddress nodeId , int nodePort) {
        this.multicastIP = multicastIP;
        this.multicastPort = multicastPort;
        this.nodeId = nodeId;
        this.nodePort = nodePort;
    }

    public void putNodes(Map<String, String> nodes){
        this.nodes.putAll(nodes);
    }

    public void putNode(String id, String host){
        this.nodes.put(Utils.sha256(id),id);
    }


}
