package com.ssl.support.presenter;

import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import com.ssl.support.activities.SignInActivity;
import com.ssl.support.api.ApiClient;
import com.ssl.support.api.ApiClientFactory;
import com.ssl.support.daemon.R;
import com.ssl.support.utils.JSONUtils;
import com.ssl.support.utils.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Linfeng Yang
 * @version 1.0
 */
public class SignInPresenter {

    private static final String TAG = "SignInPresenter";

    private SignInActivity mSignInActivity;
    private ApiClient apiClient;
    private String machineId = StringUtils.EMPTY_STRING;

    private String mSchoolId = StringUtils.EMPTY_STRING;
    private String mAccountType = StringUtils.EMPTY_STRING;
    private String mBirthday = StringUtils.EMPTY_STRING;
    private String mGrade = StringUtils.EMPTY_STRING;
    private String mClass = StringUtils.EMPTY_STRING;
    private String mName = StringUtils.EMPTY_STRING;
    private String errMessage = StringUtils.EMPTY_STRING;

    private String[] schoolStrings;
    private Map<String, String> schoolIds;

    public SignInPresenter(SignInActivity signInActivity) {
        mSignInActivity = signInActivity;
        apiClient = ApiClientFactory.newApiClient(signInActivity);

        machineId = Settings.Secure.getString(mSignInActivity.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (machineId == null) {
            machineId = StringUtils.EMPTY_STRING;
        }
    }

    public String authenticate() {
        HttpPost post = getPostMessage();
        JSONObject response = JSONUtils.fetchJSONObjectFromUri(apiClient, post);
        String status;
        try {
            if ("200".equals(response.getString("status"))) {
                return response.getString("access_token");
            } else {
                errMessage = response.getString("message");
            }
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse status.", e);
            errMessage =  mSignInActivity.getString(R.string.auth_failure);
        }
        return null;
    }

    public void setSchool(String schoolName) {
        mSchoolId = (schoolIds != null) ? schoolIds.get(schoolName) : StringUtils.EMPTY_STRING;
        mSchoolId = (mSchoolId != null) ? mSchoolId : StringUtils.EMPTY_STRING;
    }

    public void setAccountType(String accountType) {
        mAccountType = accountType;
    }

    public void setGrade(String grade) {
        mGrade = grade;
    }

    public void setClass(String cls) {
        mClass = cls;
    }

    public String getName() {
        return mName;
    }

    public String getAccountType() {
        return mAccountType;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setBirthday(String birthday) {
        mBirthday = birthday;
    }

    public String getErrorMessage() {
        return errMessage;
    }

    public String[] loadSchools() {
        if (schoolStrings == null) {
            HttpGet request = new HttpGet(apiClient.getAllSchoolsUri().toString());
            JSONArray jsonArr = JSONUtils.fetchJSONArrayFromUri(apiClient, request);
            try {
                schoolStrings = new String[jsonArr.length()];
                schoolIds = new HashMap<String, String>();
                String name, id;
                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject row = jsonArr.getJSONObject(i);
                    id = row.getString("id");
                    name = row.getString("name");
                    schoolIds.put(name, id);
                    schoolStrings[i] = name;
                }
            } catch (JSONException e) {
                Log.e("SignInActivity", "Unable to parse school list: " + jsonArr);
            }
        }
        return schoolStrings;
    }

    private HttpPost getPostMessage() {
        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("machine_id", machineId));
        postParameters.add(new BasicNameValuePair("android_version", Build.VERSION.RELEASE));
        postParameters.add(new BasicNameValuePair("name", mName));
        postParameters.add(new BasicNameValuePair("user_type", mAccountType));
        postParameters.add(new BasicNameValuePair("school_id", mSchoolId));
        postParameters.add(new BasicNameValuePair("grade", mGrade));
        postParameters.add(new BasicNameValuePair("class", mClass));
        postParameters.add(new BasicNameValuePair("birthday", mBirthday));

        HttpPost post = new HttpPost(apiClient.getLoginUri().toString());
        try {
            post.setEntity(new UrlEncodedFormEntity(postParameters, "utf8"));
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Failed to create post message for authentication.", e);
        }
        return post;
    }
}
