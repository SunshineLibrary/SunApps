package com.sunshine.support.installer.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.net.Uri;
import android.util.Log;

public class InstallerImpl {

	//静默安装方法
	public boolean doActualInstall(Uri apkFile){
		//apkName="AndroidTest111.apk";  
        
		String fileName = apkFile.getPath();
		
		String[] args = { "pm", "install", "-r", fileName };
		String result = "";
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		Process process = null;
		InputStream errIs = null;
		InputStream inIs = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int read = -1;
			process = processBuilder.start();
			errIs = process.getErrorStream();
			while ((read = errIs.read()) != -1) {
				//baos.write(read);
			}
			//baos.write('\n');
			inIs = process.getInputStream();
			while ((read = inIs.read()) != -1) {
				baos.write(read);
			}
			byte[] data = baos.toByteArray();
			result = new String(data);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (errIs != null) {
					errIs.close();
				}
				if (inIs != null) {
					inIs.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (process != null) {
				process.destroy();
			}
		}
		Log.i("output", "out:"+result);
		if(result.equals("Success\n"))
			return true;
		else return false;
	}
	
}
