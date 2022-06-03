package servers;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

import data.StoreData;

public class PeriodicLogUDP extends Thread{
    MessageSenderUDP sender;

    public PeriodicLogUDP() throws IOException {
        this.sender = new MessageSenderUDP(StoreData.multicastIP, StoreData.multicastPort);
    }

    public void run() {

        while(StoreData.isLeader) {
            try {
                sleep(1* 1000);

                StringBuilder message = new StringBuilder();
                message.append("periodicLog\n");
                message.append(StoreData.getMembershipLog(StoreData.nodePort));
                sender.multicast(message.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
