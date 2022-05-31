package servers;

public class Message {

    public static String createPutMessage(String key, String value){
        StringBuilder code = new StringBuilder();

        code.append("put\n");
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
