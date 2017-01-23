package org.dimamir999.dao;

import org.dimamir999.model.KeyValue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileDao {

    public String read(String fileName) throws IOException{
        StringBuilder data = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String currentLine = null;
            while ((currentLine = br.readLine()) != null) {
                data.append(currentLine + "\n");
            }
        }
        return data.toString();
    }

    public boolean write(String data, String fileName) throws IOException {
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);
            bw.write(data);
        } finally {
            try {
                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

    public boolean append(String data, String fileName) throws IOException {
        String dataInFile = read(fileName);
        return write(dataInFile + data, fileName);
    }
}
