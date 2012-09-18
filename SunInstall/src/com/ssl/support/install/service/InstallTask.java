package com.ssl.support.install.service;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;

public abstract class InstallTask extends AsyncTask<Uri, Integer, Boolean>{

    protected Uri apkPath;

    public InstallTask(Uri apkPath) {
        this.apkPath = apkPath;
    }

    @Override
    public Boolean doInBackground(Uri... uris){
        String fileName = apkPath.getPath();
        String[] args = { "pm", "install", "-r", fileName };


        String result = "";
        ProcessBuilder processBuilder;
        Process process;
        InputStream input;
        ByteArrayOutputStream output;
        try {
            processBuilder = new ProcessBuilder(args);
            process = processBuilder.start();
            if (process != null) {
                input = process.getInputStream();
                if (input != null) {
                    output = new ByteArrayOutputStream();
                    int read = -1;
                    while ((read = input.read()) != -1) {
                        output.write(read);
                    }
                    input.close();

                    result = new String(output.toByteArray());
                }
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), "Error running pm install on APK: " + apkPath.toString(), e);
        }

        if(result.equals("Success\n")) {
            Log.i(getClass().getName(), "Successfully Installed APK: " + apkPath.toString());
            return true;
        } else {
            Log.e(getClass().getName(), "Failed to install APK: " + apkPath.toString());
            return false;
        }
    }
}
