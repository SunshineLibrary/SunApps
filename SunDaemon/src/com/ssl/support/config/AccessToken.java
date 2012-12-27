package com.ssl.support.config;

import android.content.Context;
import com.ssl.support.utils.StringUtils;

import java.io.*;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class AccessToken {

    public static final String ACCOUNT_TYPE_STUDENT = "student";
    public static final String ACCOUNT_TYPE_TEACHER = "teacher";

    private static final String FILE_NAME = "access_token";
    private static String mAccessToken = StringUtils.EMPTY_STRING;
    private static String mUserName = StringUtils.EMPTY_STRING;
    private static String mAccountType = StringUtils.EMPTY_STRING;

    public static final String getAccessToken(Context context) {
        if (StringUtils.isEmpty(mAccessToken)) {
            retrieveAccessToken(context);
        }
        return mAccessToken;
    }

    public static final String getAccountType(Context context) {
        if (StringUtils.isEmpty(mAccountType)) {
            retrieveAccessToken(context);
        }
        return mAccountType;
    }

    public static final String getUserName(Context context) {
        if (StringUtils.isEmpty(mUserName)) {
            retrieveAccessToken(context);
        }
        return mUserName;
    }

    public static final void storeAccessToken(Context context, String name, String accountType, String accessToken) {
        try {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(String.format("%s&%s&%s", name, accountType, accessToken).getBytes());
            fos.close();
            mAccessToken = accessToken;
            mAccountType = accountType;
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
            String[] values = str.split("&");
            if (values.length == 3) {
                mUserName = values[0];
                mAccountType = values[1];
                mAccessToken = values[2];
            }
        } catch (IOException e) {}
    }
}
