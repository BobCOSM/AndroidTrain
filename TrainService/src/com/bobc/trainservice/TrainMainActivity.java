package com.bobc.trainservice;

import com.bob.trainservice.R;
import com.bobc.trainservice.aidl.ITrainServiceClient;
import com.bobc.trainservice.aidl.ITrainServiceServer;
import com.bobc.trainservice.service.TrainService;

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
	private TextView mLog;
	private Button mShowLogBtn;
	private ITrainServiceServer mTrainServiceServer;
	private ITrainServiceClient mTrainServiceClient = new ITrainServiceClient.Stub() {
		
		@Override
		public void showStr(String str) throws RemoteException {
			// TODO Auto-generated method stub
			Message msg = mHandler.obtainMessage(MSG_SHOW_LOG);
			msg.obj = str;
			msg.sendToTarget();
		}
	};
	private static final int MSG_SHOW_LOG = 0x1001;
	public static final String TAG = "TrainService";
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_SHOW_LOG:
				mLog.setText((String)msg.obj);
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState){

		Log.d(TAG, "onCreate " );
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_layout);
		mLog = (TextView)findViewById(R.id.log_show);
		mShowLogBtn = (Button)findViewById(R.id.show_log);
		mShowLogBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				try {
					mTrainServiceServer.showStr(TrainService.testProc());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		Intent service = new Intent(TrainMainActivity.this,TrainService.class);
		boolean bindRes = bindService(service, sc, BIND_AUTO_CREATE);
		Toast.makeText(this, "bindRes : " + bindRes, Toast.LENGTH_SHORT).show();
	}
	
	private ServiceConnection sc = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			mTrainServiceServer = ITrainServiceServer.Stub.asInterface(service);
			try {
				mTrainServiceServer.registerClient(mTrainServiceClient);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	@Override
	public void onDestroy(){
		super.onDestroy();
		unbindService(sc);
	}
}
