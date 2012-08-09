package com.sunshine.domain;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.IOException;

public class FileLog {
    private String LOGFILE = "log";
    private FileOutputStream fos;

    private static FileLog fileLog;

    private FileLog() {
    }

    public static FileLog setupLogFile() {
        if (fileLog == null) {
            fileLog = new FileLog();
        }
        return fileLog;
    }

    public void log(Context context, String message) {
        try {
            fos = context.openFileOutput(LOGFILE, Context.MODE_PRIVATE);
            fos.write((message + "\n").getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
