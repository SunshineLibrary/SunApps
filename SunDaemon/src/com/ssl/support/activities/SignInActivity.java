package com.ssl.support.activities;

import java.util.ArrayList;
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
public class SignInActivity extends Activity{

    private Spinner spSchool;
    private Spinner spGrade;
    private Spinner spClass;
    private EditText etName;
    private EditText etBirthday;
    private TextView txError;
    private ImageButton btnConfirm;

    private String  stSchool;
    private String  stGrade;
    private String  stClass;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        etName = (EditText) findViewById(R.id.name_input_field);
        etBirthday = (EditText) findViewById(R.id.birthday_input_field);
        txError = (TextView) findViewById(R.id.signin_errormsg);
        this.initSpinners();
        btnConfirm = (ImageButton) findViewById(R.id.btn_signin);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("name", etName.getText().toString()));
                postParameters.add(new BasicNameValuePair("birthday", etBirthday.getText().toString()));
                postParameters.add(new BasicNameValuePair("school", stSchool));
                postParameters.add(new BasicNameValuePair("grade", stGrade));
                postParameters.add(new BasicNameValuePair("class", stClass));

                //TODO: leverage existing daemon code
                String response = null;
                try {
                    response = CustomHttpClient.executeHttpPost("<target page url>", postParameters);
                    String res = response.toString();
                    res = res.replaceAll("\\s+", "");
                    if (res.equals("1"))
                    error.setText("Correct Username or Password");
                    else
                    error.setText("Sorry!! Incorrect Username or Password");
                } catch (Exception e) {
                    un.setText(e.toString());
                }
            }
        });
    }

    private void initSpinners() {
        //mAdapter = new PackageAdapter(this, null);

        spSchool = (Spinner) findViewById(R.id.school_spinner);
        spGrade = (Spinner) findViewById(R.id.grade_spinner);
        spClass = (Spinner) findViewById(R.id.class_spinner);

        //TODO: load school list

        // Grade selection
        final String[] gradeStrings = getResources().getStringArray(R.array.grade_array);
        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gradeStrings);
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGrade.setAdapter(gradeAdapter);
        spGrade.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View spinner, int position, long id) {
                stGrade = gradeStrings[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // class selection
        final String[] classStrings = getResources().getStringArray(R.array.class_array);
        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, classStrings);
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spClass.setAdapter(classAdapter);
        spClass.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View spinner, int position, long id) {
                stClass = classStrings[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
