package com.ssl.support.activities;

import android.os.Bundle;
import android.app.Activity;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.ssl.support.daemon.R;
import com.ssl.support.data.adapters.PackageAdapter;

/**
 * @author Linfeng Yang
 * @version 1.0
 */
public class SignInActivity extends Activity{

    private Spinner sp_school;
    private Spinner sp_grade;
    private Spinner sp_class;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initUI() {
        //mAdapter = new PackageAdapter(this, null);

        sp_school = (Spinner) findViewById(R.id.school_spinner);
        sp_grade = (Spinner) findViewById(R.id.grade_spinner);
        sp_class = (Spinner) findViewById(R.id.class_spinner);
    }
}
