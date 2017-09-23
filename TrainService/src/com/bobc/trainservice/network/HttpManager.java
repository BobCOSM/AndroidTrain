package com.bobc.trainservice.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class HttpManager {
	public interface HttpListener{
		void httpResponse();
		void httpGetImage();
	}


	private static final String TAG = " HttpManager ";
	
	public static final boolean ping() {
		String result = null;
		try {
			String ip = "www.baidu.com";
			Process p = Runtime.getRuntime().exec("ping -w 100 " + ip);
			BufferedInputStream bufIu = new BufferedInputStream(p.getInputStream());
			int size = 0;
			byte[] buffer = new byte[1024];
			StringBuilder sb = new StringBuilder();
			while((size = bufIu.read(buffer)) != -1){
				sb.append(new String(buffer));
			}
			Log.d(TAG, " ping result : " + sb.toString());
			int status = p.waitFor();
			if (status == 0) {
				return true;
			}
		} catch (IOException e) {
			result = "IOException";
		} catch (InterruptedException e) {
			result = "InterruptedException";
		} finally {
			Log.d("ping", "result = " + result);
		}
		return false;
	}
	
	public Bitmap httpDownLoad(String urlStr){
		Bitmap bitmap;
		try {
			URL url = new URL(urlStr.trim());
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.connect();
			int resultCode = conn.getResponseCode();
			if(resultCode != HttpURLConnection.HTTP_OK){
				Log.d(TAG, " resultCode : " + resultCode);
				return null;
			}else {
				bitmap = BitmapFactory.decodeStream(conn.getInputStream());
				return bitmap;
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
