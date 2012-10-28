package com.ssl.curriculum.math.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

/**
* @version
*       1.0, 2012-10-24 下午8:18:56
* @author  秦冲
*/
public class UnpackZipUtil {

	public static String unZip(String zipfile, String destDir) {
	       destDir = destDir.endsWith( "//" ) ? destDir : destDir + "//" ;
	       byte b[] = new byte [1024];
	       int length;
	       String indexPath ="";
	       ZipFile zipFile;
	       try {
	           zipFile = new ZipFile( new File(zipfile));
	           Enumeration enumeration = zipFile.getEntries();
	           ZipEntry zipEntry = null ;
	 
	           while (enumeration.hasMoreElements()) {
	              zipEntry = (ZipEntry) enumeration.nextElement();
	             
	              System.out.println(zipEntry.getName());
	              
	              if(zipEntry.getName().endsWith("index.htm")||zipEntry.getName().endsWith("index.html")){
	            	  indexPath = zipEntry.getName();
	              }
	            	  
	              File loadFile = new File(destDir + zipEntry.getName());
	 
	              if (zipEntry.isDirectory()) {
	                  loadFile.mkdirs();
	              } else {
	                  if (!loadFile.getParentFile().exists())
	                     loadFile.getParentFile().mkdirs();
	 
	                  OutputStream outputStream = new FileOutputStream(loadFile);
	                  InputStream inputStream = zipFile.getInputStream(zipEntry);
	 
	                  while ((length = inputStream.read(b)) > 0)
	                     outputStream.write(b, 0, length);
	              }
	           }
	           System. out .println( " 文件解压成功 " );
	       } catch (IOException e) {
	           e.printStackTrace();
	       }
	       return indexPath;
	    }
	}
