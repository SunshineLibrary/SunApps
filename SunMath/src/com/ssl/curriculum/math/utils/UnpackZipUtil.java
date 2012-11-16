package com.ssl.curriculum.math.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipEntry;

import android.util.Log;

/**
* @version
*       1.0, 2012-10-24 下午8:18:56
* @author  秦冲
*/
public class UnpackZipUtil {

	/**
	 * Unzip a zip file from a stream to baseDir, then close the stream
	 * @param inFile input zip stream
	 * @param destDir output base directory
	 * @throws IOException any IOException, "in" will be closed even an IOException is thrown.
	 */

	public static void unZipUtf8(File inFile, File destDir) throws IOException{
		   ZipFile zipFile = null;
	       try {
	           zipFile = new ZipFile(inFile);
	           Enumeration<?> entries = zipFile.getEntries();
	           ZipEntry zipEntry = null ;

	           while (entries.hasMoreElements()) {
	              zipEntry = (ZipEntry) entries.nextElement();
	              File loadFile = new File(destDir, zipEntry.getName());
	              if (zipEntry.isDirectory()) {
	            	  if(loadFile.exists()&&!loadFile.isDirectory()){
	            		  FileUtil.rmr(loadFile);
	            	  }
	                  loadFile.mkdirs();
	              } else {
	            	  if(loadFile.exists()&&!loadFile.isFile()){
	            		  FileUtil.rmr(loadFile);
	            	  }
	                  if (!loadFile.getParentFile().exists())
	                     loadFile.getParentFile().mkdirs();
	                  
	                  //Log.i("zip", loadFile.toString());
	                  InputStream inputStream = null;	                  
	                  OutputStream outputStream = null;
	                  try{
	                	  inputStream = zipFile.getInputStream(zipEntry);
	                	  outputStream = new BufferedOutputStream(new FileOutputStream(loadFile));
	                	  IOUtils.transfer(inputStream, outputStream);
	                  }finally{
	                	  try{
		                	  if(inputStream!=null){
		                		  inputStream.close();
		                	  }
	                	  }finally{
	                		  if(outputStream!=null){
	                			  outputStream.close();
	                		  }
	                	  }
	                  }
	              }
	           }	           
	       } finally{
	    	   if(zipFile!=null){
	    		   zipFile.close();
	    	   }
	       }	       
	    }
}
