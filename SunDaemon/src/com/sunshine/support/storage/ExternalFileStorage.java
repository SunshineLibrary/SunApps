package com.sunshine.support.storage;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class ExternalFileStorage implements FileStorage{
    private final File externalStorageRoot;

    public ExternalFileStorage() {
        externalStorageRoot = Environment.getExternalStorageDirectory();
    }

    public File mkdir(String directory) {
        String[] directories = directory.split("/");
        if(directories == null || directories.length == 0) return externalStorageRoot;
        File parentDir = externalStorageRoot;
        File file = null;
        for (String directoryName : directories) {
            file = new File(parentDir, directoryName);
            if (!file.exists() || !file.isDirectory()) {
                file.mkdir();
            }
            parentDir = file;
        }
        return file;
    }

    public File createFile(File dir, String fileName) throws IOException {
        File file = new File(dir, fileName);
        if (!file.exists() || file.isDirectory()) {
            file.createNewFile();
        }
        return file;
    }

    public File readFile(String filePath) {
        return new File(externalStorageRoot, filePath);
    }
}
