package com.bob.trainservice.service;

import com.bob.trainservice.TrainMainActivity;
import com.bob.trainservice.aidl.ITrainServiceClient;
import com.bob.trainservice.aidl.ITrainServiceServer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class TrainService extends Service {

	private IBinder mBinder;
	@Override
	public void onCreate(){
		Log.d(TrainMainActivity.TAG, "TrainService onCreate");
		mBinder = new ITrainServiceServerProxy();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	
	public static String testProc(){
		return "world";
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TrainMainActivity.TAG, "TrainService onStartCommand");
		Toast.makeText(this, "onStartCommand", Toast.LENGTH_SHORT).show();
		return startId;
	}
	class ITrainServiceServerProxy extends ITrainServiceServer.Stub{
		ITrainServiceClient mClient;
		@Override
		public void showStr(String str) throws RemoteException {
			// TODO Auto-generated method stub
			mClient.showStr(str);
		}

		@Override
		public boolean registerClient(ITrainServiceClient client) throws RemoteException {
			// TODO Auto-generated method stub
			mClient = client;
			return true;
		}

		@Override
		public boolean unregisterClient() throws RemoteException {
			// TODO Auto-generated method stub
			mClient = null;
			return false;
		}
		
	}
}
