package com.ssl.support.install.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.os.AsyncTask;
import android.util.Log;

public abstract class UninstallTask extends AsyncTask<String, Integer, Boolean>{

    protected String apkPkg;

    public UninstallTask(String apkPkg) {
        this.apkPkg = apkPkg;
    }

    @Override
    public Boolean doInBackground(String... pkgs){
        String pkg = apkPkg;
        String[] args = {"pm", "uninstall", apkPkg };


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
            Log.e(getClass().getName(), "Error running pm uninstall on : " + apkPkg, e);
        }

        if(result.equals("Success\n")) {
            Log.i(getClass().getName(), "Successfully Uninstalled : " + apkPkg);
            return true;
        } else {
            Log.e(getClass().getName(), "Failed to uninstall : " + apkPkg);
            return false;
        }
    }
}
