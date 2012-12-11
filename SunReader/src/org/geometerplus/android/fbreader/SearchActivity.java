package org.geometerplus.android.fbreader;

import java.io.File;

import org.sunshinelibrary.sunreader.R;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/***********************************************
 * @Copyright: Copyright (c) 2011
 * @Company: ziipin
 * @Title: SearchActivity.java
 * @Package: com.sunreader.activity.choose.desk
 * @Description: TODO(dictionary)
 * @author: yebin
 * @date: 2011-7-12 ����04:13:03
 * @version: 1.0
 ***********************************************/
public class SearchActivity extends Activity implements OnClickListener {

	private final static String TAG = "SearchActivity";

	private static final String DBFILE = "xiandaihanyucidian.db";

	private EditText etSearch;
	private Button btSearch,btSearchCancel,btSearchBack;
	private TextView tvSearchDictionaryName, tvSearchDictionaryExplanation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		initViews();
	}

	private void initViews() {
		etSearch = (EditText) findViewById(R.id.etSearch);
		btSearch = (Button) findViewById(R.id.btSearch);
		tvSearchDictionaryName = (TextView) findViewById(R.id.tvSearchDictionaryName);
		tvSearchDictionaryExplanation = (TextView) findViewById(R.id.tvSearchDictionaryExplanation);
		btSearchCancel=(Button) findViewById(R.id.btSearchCancel);
		btSearchBack=(Button) findViewById(R.id.btSearchBack);

		btSearch.setOnClickListener(this);
		btSearchCancel.setOnClickListener(this);
		btSearchBack.setOnClickListener(this);
	}

	// �����ַ����õ����ַ����ĺ������
	public String getChineseByHanzi(String hanzi) {
		File dbFile = new File(getApplicationContext().getFilesDir(), DBFILE);
		SQLiteDatabase db = SQLiteDatabase.openDatabase(dbFile
				.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"select definition from dict where term=? limit 1",
					new String[] { hanzi });
			if (cursor.moveToFirst()) {
				String text = cursor.getString(0);
				cursor.close();
				db.close();
				text = text.replaceAll("BS", "���ף�");
				text = text.replaceAll("BH", "�ʻ���");
				return text;
			}
		}
		return "û�в�ѯ����Ӧ�Ľ���!";
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// ��ѯ��ť
		if(v==btSearch){
			tvSearchDictionaryName.setText(etSearch.getText().toString().trim());
			tvSearchDictionaryExplanation.setText(getChineseByHanzi(etSearch
					.getText().toString().trim()));
		}
		//�����ʱ�ȡ��
		else if(v==btSearchCancel){
			
		}
		//����
		else if(v==btSearchBack){
			finish();
		}
	}

}
