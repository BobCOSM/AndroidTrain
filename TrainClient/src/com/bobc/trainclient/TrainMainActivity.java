package com.bobc.trainclient;

import com.bobc.trainservice.aidl.ITrainServiceClient;
import com.bobc.trainservice.aidl.ITrainServiceServer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TrainMainActivity extends Activity {
	private TextView mTextLog;
	private Button mLogBtn;
	private static final int MSG_SHOW_LOG = 0x1001;
	private static final String TAG = "TrainClient";
	private ITrainServiceServer mServer;
	private ITrainServiceClient mClient = new ITrainServiceClient.Stub() {
		
		@Override
		public void showStr(String str) throws RemoteException {
			// TODO Auto-generated method stub
			Message msg = mHandler.obtainMessage(MSG_SHOW_LOG);
			msg.obj = str;
			msg.sendToTarget();
		}
	};
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_SHOW_LOG:
				mTextLog.setText((String)msg.obj);
				break;
			}
		}
	};
	
	private ServiceConnection sc = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			try {
				mServer.unregisterClient();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			Log.d(TAG,"TrainClient onServiceConnected");
			mServer = ITrainServiceServer.Stub.asInterface(service);
			try {
				mServer.registerClient(mClient);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_layout);
		mTextLog = (TextView)findViewById(R.id.log_text);
		mLogBtn = (Button)findViewById(R.id.log_btn);
		mLogBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mServer == null){
					Toast.makeText(getApplicationContext(), "server is null ", Toast.LENGTH_SHORT).show();
					bindTrainService();
					return;
				}
				try {
					mServer.showStr("hello world");
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		bindTrainService();
	}
	private void bindTrainService(){
		Intent service = new Intent();
		ComponentName cn = new ComponentName("com.bob.trainservice","com.bobc.trainservice.service.TrainService");
		service.setComponent(cn);
		boolean res = bindService(service, sc, BIND_AUTO_CREATE);
		Log.d(TAG, "bindRes : " + res);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		unbindService(sc);
	}
}
