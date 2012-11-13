package com.ssl.support.activities;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.util.Log;
import android.view.KeyEvent;
import com.ssl.support.config.Configurations;
import com.ssl.support.presenter.SignInPresenter;
import com.ssl.support.utils.ConnectionUtils;
import com.ssl.support.utils.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

import com.ssl.support.daemon.R;
import com.ssl.support.data.adapters.PackageAdapter;

/**
 * @author Linfeng Yang
 * @version 1.0
 */
public class SignInActivity extends Activity implements OnItemSelectedListener, View.OnClickListener {

    private Spinner spSchool;
    private Spinner spGrade;
    private Spinner spClass;
    private EditText etName;
    private EditText etBirthday;

    private TextView txError;
    private ImageButton btnConfirm;

    private SignInPresenter mPresenter;

    private String[] schoolStrings;
    private String[] gradeStrings;
    private String[] classStrings;
    private Configurations configs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configs = new Configurations(this);
        if (StringUtils.isEmpty(configs.getString("access_token"))) {
            setContentView(R.layout.sign_in);
            initUI();
            initComponents();
            initSpinners();
        } else {
            setResult(RESULT_OK);
            finish();
        }
    }

    private void initUI() {
        etName = (EditText) findViewById(R.id.name_input_field);
        etBirthday = (EditText) findViewById(R.id.birthday_input_field);
        txError = (TextView) findViewById(R.id.signin_errormsg);
        btnConfirm = (ImageButton) findViewById(R.id.btn_signin);

        spSchool = (Spinner) findViewById(R.id.school_spinner);
        spGrade = (Spinner) findViewById(R.id.grade_spinner);
        spClass = (Spinner) findViewById(R.id.class_spinner);
    }

    private void initSpinners() {
        spSchool.setOnItemSelectedListener(this);
        schoolStrings = new String[] {getString(R.string.fetching_school)};
        spSchool.setAdapter(getAdapterForStrings(schoolStrings));

        // Grade selection
        gradeStrings = getResources().getStringArray(R.array.grade_array);
        spGrade.setAdapter(getAdapterForStrings(gradeStrings));
        spGrade.setOnItemSelectedListener(this);

        // class selection
        classStrings = getResources().getStringArray(R.array.class_array);
        spClass.setAdapter(getAdapterForStrings(classStrings));
        spClass.setOnItemSelectedListener(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                schoolStrings = mPresenter.loadSchools();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                spSchool.setAdapter(getAdapterForStrings(schoolStrings));
            }
        }.execute();
    }

    private void initComponents() {
        mPresenter = new SignInPresenter(this);
        btnConfirm.setOnClickListener(this);
    }

    private ArrayAdapter<String> getAdapterForStrings(String[] strings) {
        return new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strings);
    }

    @Override
    public void onClick(View v) {
        if (v == btnConfirm) {
            if (ConnectionUtils.isConnected(this)) {
                mPresenter.setName(etName.getText().toString());
                mPresenter.setBirthday(etBirthday.getText().toString());
                new AsyncTask<Object, Object, String>() {
                    @Override
                    protected String doInBackground(Object... params) {
                        return mPresenter.authenticate();
                    }

                    @Override
                    protected void onPostExecute(String accessToken) {
                        super.onPostExecute(accessToken);
                        if (!StringUtils.isEmpty(accessToken)) {
                            configs.putString("access_token", accessToken);
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(SignInActivity.this, mPresenter.getErrorMessage(), 3000).show();
                        }
                    }
                }.execute();
            } else {
                Toast.makeText(this, "无法连接网络，请稍后再试", 3000).show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == spSchool) {
            mPresenter.setSchool(schoolStrings[position]);
        } else if (parent == spGrade) {
            mPresenter.setGrade(String.valueOf(position+1));
        } else if (parent == spClass) {
            mPresenter.setClass(String.valueOf(position+1));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (parent == spSchool) {
            mPresenter.setSchool(StringUtils.EMPTY_STRING);
        } else if (parent == spGrade) {
            mPresenter.setGrade(StringUtils.EMPTY_STRING);
        } else if (parent == spClass) {
            mPresenter.setClass(StringUtils.EMPTY_STRING);
        }
    }
}
