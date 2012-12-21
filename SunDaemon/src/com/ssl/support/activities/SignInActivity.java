package com.ssl.support.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ssl.support.config.AccessToken;
import com.ssl.support.daemon.R;
import com.ssl.support.presenter.SignInPresenter;
import com.ssl.support.utils.ConnectionUtils;
import com.ssl.support.utils.StringUtils;

/**
 * @author Linfeng Yang
 * @version 1.0
 */
public class SignInActivity extends FragmentActivity implements OnItemSelectedListener, View.OnClickListener, 
									View.OnKeyListener, View.OnFocusChangeListener, OnDateSetListener {

    private Spinner spSchool;
    private Spinner spAccountType;
    private Spinner spGrade;
    private Spinner spClass;
    private EditText etName;
    private TextView txBirthday;

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
    	    
    /**
     * A static support class which is used to show date picker fragment dialog
     */
	public static class DatePickerFragment extends DialogFragment {
		private int mYear;
		private int mMonth;
		private int mDay;
		private OnDateSetListener mDateSetListener;

		public void setDate(int year, int month, int day) {
			mYear  = year;
			mMonth = month;
			mDay   = day;
		}
		
		public void setOnDateSetListener(OnDateSetListener listener){
			mDateSetListener = listener;
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), mDateSetListener, mYear, mMonth, mDay);
		}
	}

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (StringUtils.isEmpty(AccessToken.getAccessToken(this))) {
        	/**
        	 * This is a tricky way to handle with the compatibility issue. 
        	 * As our xiaoshu_7 (android version is 2.2) doesn't support Holo 
        	 * theme. And hence cannot use the new L&F data picker dialog in it.
        	 * However we should make our xiaoshu_8 (android version is 4.0.3) 
        	 * support the new Holo theme to improve the user experience.
        	 */
        	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        		setTheme(android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
        	else
        		setTheme(android.R.style.Theme_Black_NoTitleBar);
        	
            setContentView(R.layout.sign_in);
            initUI();
            initComponents();
            initSpinners();
        } else {
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    /**
     * Initialize UI
     */
    private void initUI() {
        etName = (EditText) findViewById(R.id.name_input_field);
        txBirthday = (TextView) findViewById(R.id.birthday_input_field);
        txError = (TextView) findViewById(R.id.signin_errormsg);
        btnConfirm = (ImageButton) findViewById(R.id.btn_signin);

        spSchool = (Spinner) findViewById(R.id.school_spinner);
        spAccountType = (Spinner) findViewById(R.id.account_type_spinner);
        spGrade = (Spinner) findViewById(R.id.grade_spinner);
        spClass = (Spinner) findViewById(R.id.class_spinner);
        
        //forbid ime full screen when the screen orientation is "landscape" 
        etName.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
    }
        
    /**
     * Initialize components
     */
    private void initComponents() {
        mPresenter = new SignInPresenter(this);
        btnConfirm.setOnClickListener(this);
    	txBirthday.setOnClickListener(this);
    	etName.setOnKeyListener(this);
    	etName.setOnFocusChangeListener(this);
    	
        // display the default initial date
        updateInitialBirthday();
    }

    private void initSpinners() {
        // School selection
        schoolStrings = new String[] {getString(R.string.fetching_school)};
        spSchool.setAdapter(getAdapterForStrings(schoolStrings));
        spSchool.setOnItemSelectedListener(this);

        // Account type selection
        accountTypeStrings = getResources().getStringArray(R.array.account_type_array);
        spAccountType.setAdapter(getAdapterForStrings(accountTypeStrings));
        spAccountType.setOnItemSelectedListener(this);

        // Grade selection
        gradeStrings = getResources().getStringArray(R.array.grade_array);
        spGrade.setAdapter(getAdapterForStrings(gradeStrings));
        spGrade.setOnItemSelectedListener(this);

        // Class selection
        classStrings = getResources().getStringArray(R.array.class_array);
        spClass.setAdapter(getAdapterForStrings(classStrings));
        spClass.setOnItemSelectedListener(this);
    }
    
    /**
     * Update the initial birthday based on the given account type
     */
    private void updateInitialBirthday() {
		if ("student".equals(mPresenter.getAccountType())){
			//for a student, the default initial birthday is 2000-1-1
	        mYear  = 2000;
	        mMonth = 0;
	        mDay   = 1;
		}else if ("teacher".equals(mPresenter.getAccountType())){
			//for a teacher, the default initial birthday is 1980-1-1
	        mYear  = 1980;
	        mMonth = 0;
	        mDay   = 1;
		}else if ("staff".equals(mPresenter.getAccountType())){
			//for a stuff, the default initial birthday is 1990-1-1
	        mYear  = 1990;
	        mMonth = 0;
	        mDay   = 1;
		}else{
			//for any other account type, the default initial birthday is 2000-1-1
	        mYear  = 2000;
	        mMonth = 0;
	        mDay   = 1;
		}
		
		updateBirthdayDisplay();
	}
    
    /**
     * Update the birthday textfield by the given year/month/day
     */
    private void updateBirthdayDisplay() {
        this.txBirthday.setText(
            new StringBuilder()
            	    .append(mYear).append("-")
                    .append(mMonth + 1).append("-") // Month is 0 based so add 1
                    .append(mDay).append(" "));
    }
    
    /**
     *  Execute the SignIn action
     */
    private void doSignIn(){
        if (ConnectionUtils.isConnected(this)) {
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
                            //create an intent and persist username info
                            Intent data = new Intent();
                            data.putExtra("name", mPresenter.getName());
                            setResult(RESULT_OK, data);
                            finish();
                    } else {
                        Toast.makeText(SignInActivity.this, mPresenter.getErrorMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }.execute();
        } else {
            Toast.makeText(this, getString(R.string.no_network_try_later), Toast.LENGTH_LONG).show();
        }
    }
	
	/**
	 * Display the date picker dialog
	 */
	private void showDatePickerDialog() {
		DatePickerFragment datePicker = new DatePickerFragment();
		datePicker.setDate(mYear, mMonth, mDay);
		datePicker.setOnDateSetListener(this);
		datePicker.show(getSupportFragmentManager(), "datePicker");
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

    private ArrayAdapter<String> getAdapterForStrings(String[] strings) {
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strings);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        return adapter;
    }
    
    /**
     * Check and verify whether the user name is valid
     * 
     * @return	"" if it is valid, or the error message if not 
     */
    private String verifyName()
    {
    	if (StringUtils.isEmpty(etName.getText().toString()))
    		return getString(R.string.error_login_name_is_empty);
    	
    	return "";
    }

    @Override
    public void onClick(View v) {
        if (v == btnConfirm) {
        	mPresenter.setName(etName.getText().toString());
        	mPresenter.setBirthday(txBirthday.getText().toString());
        	doSignIn();
        } else if (v == txBirthday) {
        	showDatePickerDialog();
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
            
            // if the user hasn't input his/her own birthday, adjust the 
            // initial birthday based on his/her account type
            if (StringUtils.isEmpty(mPresenter.getBirthday()))
            	updateInitialBirthday();
            
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

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (v == etName) {
			if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
				String errMsg = verifyName();
				// Verify the user name first
				if (StringUtils.isEmpty(errMsg)) {
					// Close the IME if the enter key is pressed
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),
	                        				   InputMethodManager.HIDE_NOT_ALWAYS);
					
					mPresenter.setName(etName.getText().toString());
					
					if (StringUtils.isEmpty(mPresenter.getBirthday())) {
						// if user has not input his/her birthday info, 
						// open birthday input dialog directly
						showDatePickerDialog();
					} else {
						// the user has input all his information, we can 
						// submit info to sign in now
						doSignIn();
					}
				} else {
					Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
				}
				
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (v == etName && hasFocus){
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		}
		
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mYear  = year;
        mMonth = monthOfYear;
        mDay   = dayOfMonth;
        
        updateBirthdayDisplay();
        mPresenter.setBirthday(txBirthday.getText().toString());
        mPresenter.setName(etName.getText().toString());
        
        if (StringUtils.isEmpty(mPresenter.getName())){
        	// if user has not input his/her name, set focus back to name input text 
        	// field and show up the soft keyboard to make the user input it
			etName.requestFocus();
        }
        else {
        	// the user has input all his information, we can submit info and sign in now
        	doSignIn();
        }
	}
}
