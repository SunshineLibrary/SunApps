package com.ssl.support.config;

import android.content.Context;
import com.ssl.support.utils.StringUtils;

import java.io.*;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class AccessToken {

    private static final String FILE_NAME = "access_token";
    private static String mAccessToken = StringUtils.EMPTY_STRING;
    private static String mUserName = StringUtils.EMPTY_STRING;

    public static final String getAccessToken(Context context) {
        if (StringUtils.isEmpty(mAccessToken)) {
            retrieveAccessToken(context);
        }
        return mAccessToken;
    }

    public static final String getUserName(Context context) {
        if (StringUtils.isEmpty(mUserName)) {
            retrieveAccessToken(context);
        }
        return mUserName;
    }

    public static final void storeAccessToken(Context context, String name, String accessToken) {
        try {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(String.format("%s&%s", name, accessToken).getBytes());
            fos.close();
            mAccessToken = accessToken;
            mUserName = name;
        } catch (IOException e) {
            return;
        }
    }

    public static final void retrieveAccessToken(Context context) {
        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            BufferedReader reader= new BufferedReader(new InputStreamReader(fis));
            String str = reader.readLine();
            reader.close();
            int index = str.indexOf("&");
            if (index >= 0) {
                mUserName = str.substring(0, index);
                mAccessToken = str.substring(index + 1);
            }
        } catch (IOException e) {}
    }
}
