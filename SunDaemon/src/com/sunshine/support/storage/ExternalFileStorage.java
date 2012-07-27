package com.sunshine.support.storage;

import android.os.Environment;
import com.sunshine.metadata.provider.MetadataContract;

import java.io.File;
import java.io.IOException;

public class ExternalFileStorage implements FileStorage{
    private final File externalStorageRoot;
    private static final String BASE_PATH = ".contents";

    public ExternalFileStorage() {
        externalStorageRoot = new File(Environment.getExternalStorageDirectory(), BASE_PATH);
        externalStorageRoot.mkdirs();
    }

    public File mkdir(String directory) {
        if (directory.length() == 0) {
            return externalStorageRoot;
        }

        if (directory.charAt(0) == '/') {
            directory = directory.substring(1,directory.length());
        }

        File file = new File(externalStorageRoot, directory);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
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
