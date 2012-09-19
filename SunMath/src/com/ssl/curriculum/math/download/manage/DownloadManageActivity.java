package com.ssl.curriculum.math.download.manage;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.ssl.curriculum.math.R;

/**
 * User: jtong
 * Date: 9/19/12
 */
public class DownloadManageActivity extends Activity {
    GridView gridView;

    final String[] itemNames = new String[] {
            "有理数", "二元一次方称","三元一次方程组", "无理数","有理数", "二元一次方称","三元一次方程组", "无理数" ,"有理数", "二元一次方称","三元一次方程组", "无理数"  };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_management);

        gridView = (GridView) findViewById(R.id.download_manage_view);

        gridView.setAdapter(new DownloadedItemAdapter(this, itemNames));
    }
}
