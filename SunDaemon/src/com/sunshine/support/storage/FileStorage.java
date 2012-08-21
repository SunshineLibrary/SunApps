package com.sunshine.support.storage;

import java.io.File;
import java.io.IOException;

public interface FileStorage {
    File mkdir(String directory);

    File createFile(File dir, String fileName) throws IOException;

    File getFile(String filePath);
}
