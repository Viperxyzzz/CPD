package servers;

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
        if(!(new File(foldername).mkdir())){ ;
            System.out.println("Failed to initialize folder!");
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
            message.append(data);
        }
        myReader.close();

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
