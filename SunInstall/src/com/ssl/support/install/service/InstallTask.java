package com.ssl.support.install.service;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.*;

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
        Process process = null;
        InputStream input = null;
        try {
            processBuilder = new ProcessBuilder(args);
            processBuilder.redirectErrorStream(true);
            process = processBuilder.start();
            if (process != null) {
                Log.v(getClass().getName(), "Install Started...");
                input = process.getInputStream();

                if (input != null) {
                    Log.v(getClass().getName(), "Reading Result Output ...");
                    result = readInput(input);
                }
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), "Error running pm install on APK: " + apkPath.toString(), e);
        } finally {
            try {
                if (input != null)
                    input.close();
            } catch (IOException e) {}
            if (process != null)
                process.destroy();
        }

        if(result.indexOf("Success") >= 0) {
            Log.i(getClass().getName(), "Successfully Installed APK: " + apkPath.toString());
            new File(fileName).delete();
            return true;
        } else {
            Log.e(getClass().getName(), "Failed to install APK: " + apkPath.toString());
            return false;
        }
    }

    private String readInput(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        String line;
        while ((line = reader.readLine()) != null) {
            Log.v(getClass().getName(), "Output: " + line);
            if (line.indexOf("Success") >= 0 || line.indexOf("Failure") >= 0) {
                return line;
            }
        }

        return "Failure [OUTPUT_NOT_VALID]";
    }
}
