package com.ssl.support.activities;

import java.util.ArrayList;

import javax.net.ssl.KeyManager;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.ssl.support.config.AccessToken;
import com.ssl.support.config.Configurations;
import com.ssl.support.presenter.SignInPresenter;
import com.ssl.support.utils.ConnectionUtils;
import com.ssl.support.utils.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
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
    private Spinner spAccountType;
    private Spinner spGrade;
    private Spinner spClass;
    private EditText etName;
    private EditText etYear;
    private EditText etMonth;
    private EditText etDay;

    private TextView txError;
    private ImageButton btnConfirm;

    private SignInPresenter mPresenter;

    private String[] schoolStrings;
    private String[] accountTypeStrings;
    private String[] gradeStrings;
    private String[] classStrings;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (StringUtils.isEmpty(AccessToken.getAccessToken(this))) {
            setContentView(R.layout.sign_in);
            initUI();
            initComponents();
            initSpinners();
        } else {
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    private void initUI() {
        etName = (EditText) findViewById(R.id.name_input_field);
        etYear = (EditText) findViewById(R.id.et_year);
        etMonth = (EditText) findViewById(R.id.et_month);
        etDay = (EditText) findViewById(R.id.et_day);
        
        MyKeyListener mlistener = new MyKeyListener();
        etName.setOnKeyListener(mlistener);
        etYear.setOnKeyListener(mlistener);
        etMonth.setOnKeyListener(mlistener);
        etDay.setOnKeyListener(mlistener);
        
        txError = (TextView) findViewById(R.id.signin_errormsg);
        btnConfirm = (ImageButton) findViewById(R.id.btn_signin);

        spSchool = (Spinner) findViewById(R.id.school_spinner);
        spAccountType = (Spinner) findViewById(R.id.account_type_spinner);
        spGrade = (Spinner) findViewById(R.id.grade_spinner);
        spClass = (Spinner) findViewById(R.id.class_spinner);
    }
    
    private class MyKeyListener implements OnKeyListener{

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if(keyCode == KeyEvent.KEYCODE_ENTER){
				/*int keyId = v.getId();
				if(TextUtils.isEmpty(etName.getText().toString().trim())){
					Toast.makeText(getApplicationContext(), "姓名不能为空", 0).show();
					return true;
				}else if(TextUtils.isEmpty(etYear.getText().toString().trim())){
					Toast.makeText(getApplicationContext(), "生日年份不能为空", 0).show();
					return true;
				}else if(TextUtils.isEmpty(etMonth.getText().toString().trim())){
					Toast.makeText(getApplicationContext(), "生日月份不能为空", 0).show();
					return true;
				}else if(TextUtils.isEmpty(etDay.getText().toString().trim())){
					Toast.makeText(getApplicationContext(), "生日日期不能为空", 0).show();
					return true;
				}*/
				
				InputMethodManager imm = (InputMethodManager) v
                        .getContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(
                        v.getApplicationWindowToken(), 0);
                }
               
                if(v.getId()==R.id.et_day){
                	connect();
                    btnConfirm.setVisibility(View.INVISIBLE);
                }
                return true;
			}
			return false;
		}
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

    private MyArrayAdapter<String> getAdapterForStrings(String[] strings) {
        return new MyArrayAdapter<String>(strings);
    	//return new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, strings);
    }
    
    private class MyArrayAdapter<String> extends BaseAdapter{
    	private String[] strings;
    	public MyArrayAdapter(String[] strings)
    	{
    		this.strings = strings;
    	}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return strings.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return strings[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = null;
			ViewHolder holder;
			if(convertView == null){
				view = View.inflate(getApplicationContext(), R.layout.simple_spinner_item, null);
				holder = new ViewHolder();
				holder.tv_account_type = (TextView) view.findViewById(R.id.tv_account_type);
				view.setTag(holder);
			}else{
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			holder.tv_account_type.setText(strings[position].toString());
			return view;
		}
    	
    }

    private static class ViewHolder{
    	private TextView tv_account_type;
    	
    }
    
    @Override
    public void onClick(View v) {
        if (v == btnConfirm) {
            connect();
        }
    }
    

	private void connect() {
		if (ConnectionUtils.isConnected(this)) {
		    mPresenter.setName(etName.getText().toString());
		    //hereLiu:
		    String birthdayStr = etYear.getText().toString()+"-"+etMonth.getText().toString()+"-"+etDay.getText().toString();
		    mPresenter.setBirthday(birthdayStr);
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
