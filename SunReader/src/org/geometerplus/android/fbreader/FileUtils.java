package org.geometerplus.android.fbreader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.sunshinelibrary.sunreader.R;

import android.content.res.Resources;

class FileUtils {
 
     public static void loadDbFile(int rawId, File file, Resources res,
             String pkgname) {
         InputStream dbInputStream = res.openRawResource(rawId);
         FileOutputStream fos = null;
 
         try {
             fos = new FileOutputStream(file);
 
             byte[] bytes = new byte[1024];
             int length;
             while ((length = dbInputStream.read(bytes)) > 0) {
                 fos.write(bytes, 0, length);
             }
 
         } catch (FileNotFoundException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         } finally {
             try {
                 fos.close();
                 dbInputStream.close();
             } catch (IOException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
             }
         }
     }
 }
