package org.dimamir999.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dimamir999.dao.FileDao;
import org.dimamir999.model.KeyValue;

import javax.management.RuntimeErrorException;
import java.io.File;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class FileMerger implements Runnable {
    private static final Logger LOG = LogManager.getLogger(FileMerger.class);
    private static final String DATA_FILE = "data";
    private static final String MERGED_FILE = "temp_data";
    private int timeout;
    private FileDao fileDao = new FileDao();

    public FileMerger(int timeout) {
        this.timeout = timeout;

        File dataFile = new File(System.getProperty("user.dir") + "/" + DATA_FILE);
        if(!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                LOG.error("New file creating error", e);
                throw new RuntimeErrorException(null);
            }
        }
    }

    private void fileMerge() throws IOException {
        String mergeData = fileDao.read(MERGED_FILE);

        for (String line: mergeData.split("/n")) {
            fileDao.append(line, DATA_FILE);
        }

        LOG.info("Files are successfully merged");
    }

    private void fileClear() throws IOException {
        fileDao.write("", MERGED_FILE);

        LOG.info("Temp file is cleared");
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                fileMerge();
                fileClear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
