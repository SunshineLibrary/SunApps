package com.ssl.support.config;

import android.content.Context;

import java.io.*;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class AccessToken {

    private static final String FILE_NAME = "access_token";

    public static final String getAccessToken(Context context) {
        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            BufferedReader reader= new BufferedReader(new InputStreamReader(fis));
            String accessToken = reader.readLine();
            reader.close();
            return accessToken;
        } catch (IOException e) {
            return null;
        }
    }

    public static final void storeAccessToken(Context context, String accessToken) {
        try {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(accessToken.getBytes());
            fos.close();
        } catch (IOException e) {
            return;
        }
    }
}
