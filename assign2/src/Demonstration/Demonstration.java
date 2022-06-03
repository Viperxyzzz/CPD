package Demonstration;

import java.io.IOException;

public class Demonstration {

    public static void main(String[] args) throws IOException, InterruptedException {


        var node1 = new DemonstrationThread("9000", "Node1");
        var node2 = new DemonstrationThread("9001", "Node2");
        node1.start();
        node2.start();

    }

}

