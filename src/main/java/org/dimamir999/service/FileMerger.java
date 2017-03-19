package org.dimamir999.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dimamir999.dao.FileDao;

import javax.management.RuntimeErrorException;
import java.io.File;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class FileMerger implements Runnable {
    private static final Logger LOG = LogManager.getLogger(FileMerger.class);
    private static String dataFile;
    private static String mergedFile;
    private int timeout;
    private FileDao fileDao = new FileDao();

    public FileMerger(String dataFile, String mergedFile, int timeout) {
        this.timeout = timeout;
        FileMerger.dataFile = dataFile;
        FileMerger.mergedFile = mergedFile;

        File file = new File(System.getProperty("user.dir") + "/" + dataFile);
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                LOG.error("New file creating error", e);
                throw new RuntimeErrorException(null);
            }
        }
    }

    private void fileMerge() throws IOException {
        String mergeData = fileDao.read(mergedFile);

        for (String line: mergeData.split("/n")) {
            fileDao.append(line, dataFile);
        }

        LOG.info("Files are successfully merged");
    }

    private void fileClear() throws IOException {
        fileDao.write("", mergedFile);

        LOG.info("Temp file is cleared");
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(timeout);
            } catch (InterruptedException e) {
                LOG.error("Sleeping thread is interrupted", e);
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
