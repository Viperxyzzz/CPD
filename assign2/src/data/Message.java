package data;

import java.io.IOException;

public class Message {

    public static String createJoinMessage(int port) {
        StringBuilder message = new StringBuilder();
        message.append("join\n");
        message.append(Integer.toString(port) + "\n");
        try {
            message.append(StoreData.getMembershipCount(port));
        }
        catch (IOException e) {
        }

        return message.toString();
    }

    public static String createPutMessage(String key, String value){
        StringBuilder code = new StringBuilder();

        code.append("put\n");
        code.append(key + "\n");
        code.append(value + "\n");
        code.append("END").append("\n");
        return code.toString();
    }

    public static String createPutReplica(String key, String value){
        StringBuilder code = new StringBuilder();

        code.append("putreplica\n");
        code.append(key + "\n");
        code.append(value + "\n");
        code.append("END").append("\n");
        return code.toString();
    }

    public static String createGetMessage(String key){
        StringBuilder code = new StringBuilder();

        code.append("get\n");
        code.append(key + "\n");
        code.append("END\n");
        return code.toString();
    }

    public static String createDeleteMessage(String key){
        StringBuilder code = new StringBuilder();

        code.append("delete\n");
        code.append(key + "\n");
        code.append("END\n");
        return code.toString();
    }
}
