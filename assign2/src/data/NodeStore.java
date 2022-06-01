package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class NodeStore {
    String foldername;
    public NodeStore(String foldername){
        this.foldername = foldername;
        File file = new File(foldername);
        if (file.exists()) {
            System.out.println("Using folder " + foldername);
        }
        else{
            file.mkdir();
        }
    }
    public void put(String key, String value) throws IOException {
        File file = new File(foldername + "/" + key + ".txt");
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(foldername + "/" + key + ".txt");
        fileWriter.write(value);
        fileWriter.close();
    }

    public String get(String key) throws FileNotFoundException {
        File file = new File(foldername + "/" + key + ".txt");
        StringBuilder message = new StringBuilder();
        Scanner myReader = new Scanner(file);

        while(myReader.hasNextLine()){
            String data = myReader.nextLine();
            message.append(data).append("\n");
        }
        myReader.close();
        message.append("\n");
        return message.toString();
    }

    public boolean delete(String key){

        File file = new File(foldername + "/" + key + ".txt");

        return file.delete();
    }

    public boolean hasKey(String key){

        return Files.isReadable(Path.of(foldername + "/" + key + ".txt"));
    }

}
