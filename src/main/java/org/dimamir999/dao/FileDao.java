package org.dimamir999.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dimamir999.model.KeyValue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileDao {
    private static final Logger log = LogManager.getLogger(FileDao.class);

    public String read(String fileName) throws IOException {
        StringBuilder data = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String currentLine = null;
            while((currentLine = br.readLine()) != null) {
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
        } catch (IOException var14) {
            log.error("I/O exception");
        } finally {
            try {
                if(bw != null) {
                    bw.close();
                }

                if(fw != null) {
                    fw.close();
                }
            } catch (IOException ex) {
                log.error("I/O exception");
                ex.printStackTrace();
            }

        }

        return true;
    }

    public boolean append(String data, String fileName) throws IOException {
        String dataInFile = this.read(fileName);
        return this.write(dataInFile + data, fileName);
    }
}
