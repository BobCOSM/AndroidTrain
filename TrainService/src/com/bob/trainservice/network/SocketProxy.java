package com.bob.trainservice.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.util.Log;

public class SocketProxy {
	
	private static final String TAG = "SocketProxy";

	private Socket mSocket;
	
	private NetworkParams mNetworkParams;
	
	public SocketProxy(NetworkParams netparams){
		mNetworkParams = netparams;
	}
	
	public boolean connect(){
		if(mSocket != null){
			return true;
		}
		
		try {
			mSocket = new Socket(mNetworkParams.getBusinessAddress(),mNetworkParams.mBusinessPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG," connet false ");
			return false;
		}
		Log.d(TAG," connet ok ");
		return true;
	}
	
	public boolean sendData(byte[] data, int offset, int len) {
		Log.d(TAG, "开始发送数据-----> " + data.toString());
		if (data == null || len <= 0) {
			return false;
		}
		if (mSocket == null) {
			return false;
		}
		
		try {
			OutputStream outputStream = mSocket.getOutputStream();
			Log.d(TAG,".sendData "+data+"  len: " +len);
			outputStream.write(data, offset, len);
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	public int recvData(){
		int size = 0;
		try {
			InputStream inputStream = mSocket.getInputStream();
			while(true){
				byte[] buf = new byte[1024];
				
				size = inputStream.read(buf);
				String byteData = "";
				
				for(byte b : buf){
					byteData += b + " ";
				}
				Log.d(TAG, "size" + size + " data: " + new String(buf) );
				Log.d(TAG, "data : " + byteData);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return size;
	}
	
	public void closeSocket(){
		try {
			if(mSocket == null ){
				return;
			}
			mSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
