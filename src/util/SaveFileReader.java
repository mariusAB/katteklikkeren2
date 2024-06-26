package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SaveFileReader {
    private Scanner reader;
    private FileWriter writer;
    private int monKil;
    public SaveFileReader() {
    }

    public void readSaveFile() throws IOException {
        reader = new Scanner(new File("save.txt"));
        if (reader.hasNextLine()) {
            String line = reader.nextLine();
            monKil = Integer.parseInt(line);
        } else {
            writeSaveFile(monKil);
        }
        reader.close();
    }

    public int getMonKil() {
        return monKil;
    }

    public void writeSaveFile(int monKil) throws IOException {
        writer = new FileWriter(new File("save.txt"));
        writer.write(String.valueOf(monKil));
        writer.close();
    }
}
