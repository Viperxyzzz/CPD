package data;

import servers.*;
import utility.Utils;

import java.io.*;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class StoreData {
    public static InetAddress multicastIP;
    public static int multicastPort;
    public static InetAddress nodeId;
    public static int nodePort;
    public static int nodePortTestClient;
    public static Boolean inCluster;
    public static int numberOfMembershipMsgReceived;
    public static ArrayList<BufferedReader> membershipMessages;
    public static Boolean isLeader;

    public static TreeMap<String,String> nodes = new TreeMap<>();

    public StoreData() {}

    public StoreData(InetAddress multicastIP, int multicastPort, InetAddress nodeId , int nodePort) {
        this.multicastIP = multicastIP;
        this.multicastPort = multicastPort;
        this.nodeId = nodeId;
        this.nodePort = nodePort;
        this.nodePortTestClient = nodePort + 1000;
        this.setInitialMembershipCount();
        this.setInitialMembershipLog();
        this.inCluster = false;
        this.numberOfMembershipMsgReceived = 0;
        this.membershipMessages = new ArrayList<>();
        this.isLeader = false;
    }

    public static String getMembershipLog(int nodePort) {
        String str = "";
        try {
            Path fileName = Path.of("Cluster/MembershipLogs/" + Integer.toString(nodePort) + ".txt");
            str = Files.readString(fileName);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return str;
    }

    public static void setMembershipLog(StringBuilder newLog) {
        try {
            File myObj = new File("Cluster/MembershipLogs/" + nodePort + ".txt");
            FileWriter myWriter = new FileWriter(myObj);
            myWriter.write(newLog.toString());
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void setLeader() throws IOException {
        isLeader = true;
        PeriodicLogUDP server = new PeriodicLogUDP();
        new Thread(server).start();
    }

    private void setInitialMembershipLog() {
        try {
            File myObj = new File("Cluster/MembershipLogs/" + Integer.toString(this.nodePort) + ".txt");
            if(myObj.createNewFile()) {
                FileWriter myWriter = new FileWriter(myObj);
                myWriter.write(getLogLine(this.nodeId, this.nodePort, StoreData.getMembershipCount(this.nodePort)));
                myWriter.close();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void addLogLine(int nodePort, String line) throws FileNotFoundException {
        try {
            File myObj = new File("Cluster/MembershipLogs/" + Integer.toString(nodePort) + ".txt");
            Writer output;
            output = new BufferedWriter(new FileWriter(myObj, true));
            output.append(line);
            output.close();
        } catch (IOException e) {
            System.out.println("ERROR -- THERE IS NO LOG FILE");
            e.printStackTrace();
        }
    }

    public static String getLogLine(InetAddress nodeId, int nodePort, String membershipCount) {
        String line =  nodeId.getHostAddress() + ":" + nodePort + ";" + membershipCount + "\n";
        return line;
    }

    public static BufferedReader getMembershipLogBuff(int nodePort) throws FileNotFoundException {
        File myObj = new File("Cluster/MembershipLogs/" + Integer.toString(nodePort) + ".txt");
        FileReader myWriter = new FileReader(myObj);
        BufferedReader bis = new BufferedReader(myWriter);
        return bis;
    }

    public static HashMap<Integer,Integer> LogToMap(BufferedReader bis) throws IOException {
        HashMap<Integer,Integer> result = new HashMap<>();
        String line = bis.readLine();
        while (line != null) {
            String[] lineArray = line.split(";");
            String[] id = lineArray[0].split(":");
            result.put(Integer.parseInt(id[1]), Integer.parseInt(lineArray[1]));

            line = bis.readLine();
        }
        return result;
    }

    private void setInitialMembershipCount() {
        try {
            File myObj = new File("Cluster/MembershipCounters/" + Integer.toString(this.nodePort) + ".txt");
            if(myObj.createNewFile()) {
                FileWriter myWriter = new FileWriter(myObj);
                myWriter.write(Integer.toString(0));
                myWriter.close();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static void setMembershipCount(int nodePort, int val) {
        try {
            File myObj = new File("Cluster/MembershipCounters/" + Integer.toString(nodePort) + ".txt");
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(myObj);
            myWriter.write(Integer.toString(val));
            myWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void increaseMembershipCount(int nodePort) throws IOException {
        String val = StoreData.getMembershipCount(nodePort);
        StoreData.setMembershipCount(nodePort, Integer.parseInt(val)+1);
    }

    public static String getMembershipCount(int nodePort) throws IOException {
        Path fileName = Path.of("Cluster/MembershipCounters/" + Integer.toString(nodePort) + ".txt");
        String str = Files.readString(fileName);
        return str;
    }

    public void putNodes(Map<String, String> nodes){
        this.nodes.putAll(nodes);
    }

    public void putNode(String id, String host){
        this.nodes.put(Utils.sha256(id),id);
    }


}
