package com.ssl.curriculum.math.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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
	           Enumeration<ZipEntry> enumeration = (Enumeration<ZipEntry>) zipFile.entries();
	           ZipEntry zipEntry = null ;
	           while (enumeration.hasMoreElements()) {
	              zipEntry = enumeration.nextElement();	             
	              File loadFile = new File(destDir, zipEntry.getName());
	              FileUtil.rmr(loadFile);
	              if (zipEntry.isDirectory()) {
	                  loadFile.mkdirs();
	              } else {
	                  if (!loadFile.getParentFile().exists())
	                     loadFile.getParentFile().mkdirs();
	 
	                  InputStream inputStream = zipFile.getInputStream(zipEntry);	                  
	                  OutputStream outputStream = null;
	                  
	                  try{
	                	  outputStream = new BufferedOutputStream(new FileOutputStream(loadFile));
	                	  IOUtils.transfer(inputStream, outputStream);
	                  }finally{
	                	  outputStream.close();
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
