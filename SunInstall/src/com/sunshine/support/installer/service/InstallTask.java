package com.sunshine.support.installer.service;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class InstallTask extends AsyncTask<Uri, Integer, Boolean>{

    @Override
    public Boolean doInBackground(Uri... uris){
        if (uris.length == 1) {
            Uri uri = uris[0];
            String fileName = uri.getPath();
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
                    process.destroy();
                }
            } catch (Exception e) {
                Log.e(getClass().getName(), "Error running pm install on APK: " + uri.toString(), e);
            }

            if(result.equals("Success\n")) {
                Log.i(getClass().getName(), "Successfully Installed APK: " + uri.toString());
                return true;
            } else {
                Log.e(getClass().getName(), "Failed to install APK: " + uri.toString());
                return false;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
}
