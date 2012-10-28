package com.ssl.curriculum.math.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.ssl.metadata.provider.MetadataContract;

public class FileUtil {
	
	private FileUtil(){
		
	}
	
	/**
	 * remove the file and all its subfiles(if file is a directory).
	 * @param file
	 */
	public static void rmr(File file){
		if(file.exists()){
			if(file.isFile()){
				file.delete();
			}else if(file.isDirectory()){
				for(File f:file.listFiles()){
					rmr(f);
				}
				file.delete();
			}
		}
	}
	
}
