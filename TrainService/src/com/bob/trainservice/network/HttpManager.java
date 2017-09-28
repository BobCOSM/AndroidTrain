package com.bob.trainservice.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
			Log.d(TAG, " start ping ");
			Process p = Runtime.getRuntime().exec("ping -c 1 -w 100 " + ip);
			BufferedReader bufIu = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			String str = "";
			int status = p.waitFor();

			while((str = bufIu.readLine()) != null){
				Log.d(TAG, " ping result : " + str);
			}
			
			Log.d(TAG, " ping status : " + status);
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
