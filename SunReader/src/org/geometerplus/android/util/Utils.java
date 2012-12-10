package org.geometerplus.android.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import android.util.Log;

//import com.sunreader.data.ScrollPageEntity;
import org.geometerplus.zlibrary.ui.android.library.ZLAndroidApplication;

public class Utils {
	//public static final String UPLOAD_URL = ZLAndroidApplication.Instance().getIp() + "/Client/Books/UserData?Id=";
	public static boolean isChineseEncode(char c) {
		if (c >= 0 && c <= 9) {
			// 是数字
			return false;
		} else if ((c >= 'a' && c <= 'z')) {
			// 是小写字母
			return false;
		} else if ((c >= 'A' && c <= 'z')) {
			// 是大写字母
			return false;
		} else if (Character.isLetter(c)) {
			// 是汉字
			return true;
		} else {
			// 是特殊符号
			return false;
		}
	}

//	public static HttpURLConnection createConnection(List<ScrollPageEntity> scrollPageEntities, String userId)
//			throws IOException {
//		StringBuilder stringBuilder = new StringBuilder();
//		for (ScrollPageEntity scrollPageEntity : scrollPageEntities) {
//			stringBuilder.append(URLEncoder.encode(scrollPageEntity.getMyBookName(), "UTF-8"));
//			stringBuilder.append("$");
//			stringBuilder.append(scrollPageEntity.getMyBeginTime());
//			stringBuilder.append("$");
//			stringBuilder.append(scrollPageEntity.getMyEndTime());
//			stringBuilder.append("$");
//			stringBuilder.append(scrollPageEntity.getMyPageNumber());
//			stringBuilder.append("-");
//		}
//		String requestdata = stringBuilder.toString();
//		//String postData = new String(requestdata.getBytes(), "UTF-8");
//		URL url = new URL(UPLOAD_URL + userId + "&data=" + requestdata);
//		Log.e("----RequestData", "-:-" + UPLOAD_URL + userId + "&data=" + "53353");
//		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//		conn.setDoInput(true);
//		conn.setDoOutput(true);
//		conn.setUseCaches(false);
//		conn.setConnectTimeout(30 * 1000);
//		conn.setRequestMethod("POST");
//		conn.setRequestProperty("Content-Type",
//		"application/soap+xml; charset=utf-8");
//		OutputStream os = conn.getOutputStream();
//		Log.e("YINJIA", "requestdata:" + requestdata);
//		// String test =
//		// "<?xml version=\"1.0\" encoding=\"UTF-8\"?><CwapMessage xmlns=\"http://wap.homeinns.com/cwap\"><version>1.0</version><TxtMsgMessage><Lisence>HiSoft</Lisence><GustNm>13818878048</GustNm><TelNumber>111111</TelNumber></TxtMsgMessage></CwapMessage>";
//		byte[] buff = requestdata.getBytes();
//		Log.e("conn->responseCode", "Code:" + conn.getResponseCode());
//		os.write(buff);
//		os.flush();
//		os.close();
//		return conn;
//	}
}
