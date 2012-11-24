package com.ssl.support.activities;

import java.util.ArrayList;
import java.util.Calendar;

import android.os.AsyncTask;
import android.util.Log;
import android.view.KeyEvent;
import com.ssl.support.config.AccessToken;
import com.ssl.support.config.Configurations;
import com.ssl.support.presenter.SignInPresenter;
import com.ssl.support.utils.ConnectionUtils;
import com.ssl.support.utils.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    private Spinner spAccountType;
    private Spinner spGrade;
    private Spinner spClass;
    private EditText etName;
    private TextView etBirthday;

    private TextView txError;
    private ImageButton btnConfirm;

    private SignInPresenter mPresenter;

    private String[] schoolStrings;
    private String[] accountTypeStrings;
    private String[] gradeStrings;
    private String[] classStrings;
    
    private int mYear;
    private int mMonth;
    private int mDay;
    static final int DATE_DIALOG_ID = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (StringUtils.isEmpty(AccessToken.getAccessToken(this))) {
            setContentView(R.layout.sign_in);
            initUI();
            initComponents();
            initSpinners();
            addListener();
        } else {
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    private void initUI() {
        etName = (EditText) findViewById(R.id.name_input_field);
        etBirthday = (TextView) findViewById(R.id.birthday_input_field);
        txError = (TextView) findViewById(R.id.signin_errormsg);
        btnConfirm = (ImageButton) findViewById(R.id.btn_signin);

        spSchool = (Spinner) findViewById(R.id.school_spinner);
        spAccountType = (Spinner) findViewById(R.id.account_type_spinner);
        spGrade = (Spinner) findViewById(R.id.grade_spinner);
        spClass = (Spinner) findViewById(R.id.class_spinner);
        
        // get the current date
        mYear = 2000;
        mMonth = 0;
        mDay = 1;

        // display the current date
        updateDisplay();
    }

    private void initSpinners() {
        spSchool.setOnItemSelectedListener(this);
        schoolStrings = new String[] {getString(R.string.fetching_school)};
        spSchool.setAdapter(getAdapterForStrings(schoolStrings));

        accountTypeStrings = new String[] {
                getString(R.string.account_type_student),
                getString(R.string.account_type_teacher),
                getString(R.string.account_type_staff)
        };
        spAccountType.setAdapter(getAdapterForStrings(accountTypeStrings));
        spAccountType.setOnItemSelectedListener(this);

        // Grade selection
        gradeStrings = getResources().getStringArray(R.array.grade_array);
        spGrade.setAdapter(getAdapterForStrings(gradeStrings));
        spGrade.setOnItemSelectedListener(this);

        // class selection
        classStrings = getResources().getStringArray(R.array.class_array);
        spClass.setAdapter(getAdapterForStrings(classStrings));
        spClass.setOnItemSelectedListener(this);
    }
    
    private void addListener()
    {
    	etBirthday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
    	
    	etName.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				//Close the IME if the enter is pressed
				if (keyCode == KeyEvent.KEYCODE_ENTER)
				{
					InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					in.hideSoftInputFromWindow(v.getApplicationWindowToken(),
	                        				   InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});
    }
    
    private void updateDisplay() {
        this.etBirthday.setText(
            new StringBuilder()
            	    .append(mYear).append("-")
                    .append(mMonth + 1).append("-") // Month is 0 based so add 1
                    .append(mDay).append(" "));
    }
    
    private DatePickerDialog.OnDateSetListener mDateSetListener =
    	    new DatePickerDialog.OnDateSetListener() {
    	        public void onDateSet(DatePicker view, int year, 
    	                              int monthOfYear, int dayOfMonth) {
    	            mYear = year;
    	            mMonth = monthOfYear;
    	            mDay = dayOfMonth;
    	            updateDisplay();
    	        }
    	    };
    	    
    @Override
    protected Dialog onCreateDialog(int id) {
       switch (id) {
       case DATE_DIALOG_ID:
          return new DatePickerDialog(this,
                    mDateSetListener,
                    mYear, mMonth, mDay);
       }
       return null;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                return schoolStrings = mPresenter.loadSchools();
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (schoolStrings == null || schoolStrings.length == 0) {
                    schoolStrings = new String[] {getString(R.string.no_network_connection)};
                }
                spSchool.setAdapter(getAdapterForStrings(schoolStrings));
            }
        }.execute();
    }

    private void initComponents() {
        mPresenter = new SignInPresenter(this);
        btnConfirm.setOnClickListener(this);
    }

    private ArrayAdapter<String> getAdapterForStrings(String[] strings) {
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strings);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        return adapter;
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
                            AccessToken.storeAccessToken(SignInActivity.this, mPresenter.getName(), accessToken);
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
        } else if (parent == spAccountType) {
            if (position == 0) {
                mPresenter.setAccountType("student");
                spGrade.setVisibility(View.VISIBLE);
                spClass.setVisibility(View.VISIBLE);
            } else {
                mPresenter.setAccountType((position == 1) ? "teacher" : "staff");
                spGrade.setVisibility(View.GONE);
                spClass.setVisibility(View.GONE);
            }
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
        } else if (parent == spAccountType) {
            mPresenter.setAccountType(StringUtils.EMPTY_STRING);
        } else if (parent == spGrade) {
            mPresenter.setGrade(StringUtils.EMPTY_STRING);
        } else if (parent == spClass) {
            mPresenter.setClass(StringUtils.EMPTY_STRING);
        }
    }
}
