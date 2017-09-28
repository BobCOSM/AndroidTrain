package com.bob.trainservice;

import com.bob.trainservice.R;
import com.bob.trainservice.network.HttpManager;
import com.bob.trainservice.network.NetworkParams;
import com.bob.trainservice.network.SocketProxy;
import com.bob.trainservice.service.TrainService;
import com.bob.trainservice.util.JavaJniTrain;
import com.bob.trainservice.aidl.ITrainServiceClient;
import com.bob.trainservice.aidl.ITrainServiceServer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TrainMainActivity extends Activity {
	private TextView mLog;
	private Button mShowLogBtn;
	private ITrainServiceServer mTrainServiceServer;
	private HttpManager mHttpManager = new HttpManager();
	private ImageView mTestImage;
	private SocketProxy mSocketProxy;
	
	static{
		System.loadLibrary("TrainService");
	}
	
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
	private static final int MSG_SHOW_IMAGE = 0x1002;
	private static final int MSG_PING_STATE = 0x1003;
	public static final String TAG = "TrainService";
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_SHOW_LOG:
				mLog.setText((String)msg.obj);
				break;
			case MSG_SHOW_IMAGE:
				mTestImage.setImageBitmap((Bitmap)msg.obj);
				break;
			case MSG_PING_STATE:
				mLog.setText(((Boolean)msg.obj).booleanValue() ? "ok" : "false");
				String str = " ping state : " + (((Boolean)msg.obj).booleanValue() ? "ok" : "false");
				Log.d(TAG, str);
				Toast.makeText(TrainMainActivity.this, str, Toast.LENGTH_SHORT).show();
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
		mTestImage = (ImageView)findViewById(R.id.test_image);
		Intent service = new Intent(TrainMainActivity.this,TrainService.class);
		boolean bindRes = bindService(service, sc, BIND_AUTO_CREATE);
		Toast.makeText(this, "bindRes : " + bindRes, Toast.LENGTH_SHORT).show();

		testHandlerInWorkThread();
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
	
//	private String urlStr = "http://content.52pk.com/files/100623/2230_102437_1_lit.jpg";
	private String urlStr = "http://img.zybus.com/uploads/allimg/140523/1-140523155107.jpg";
	public void onLoadImage(View view){
		testConn();
	}
	
	private Handler mWorkThreadHandler;
	
	private void testNavi(){
		JavaJniTrain jt = new JavaJniTrain();
		String str = jt.getString();
		Log.d(TAG,"str : " + str);
		mLog.setText(str);
		
	}
	
	private void sendMsgToWorkThread(){
		Message msg = new Message();
		msg.obj = "hello";
		msg.what = 24;
		mWorkThreadHandler.sendMessage(msg);
	}
	
	public void onCurTest(View view){
		//sendMsgToWorkThread();
		sendMsgBySocket();
//		testNavi();
	}
	
	private void sendMsgBySocket(){
		NetworkParams netparams = new NetworkParams("192.168.1.104", 8889);
		//final SocketProxy socket = new SocketProxy(netparams);
		if(mSocketProxy == null){
			mSocketProxy = new SocketProxy(netparams);
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub

				mSocketProxy.connect();
				String data = "hello";
				
				mSocketProxy.sendData(data.getBytes(), 0, data.getBytes().length);

				mSocketProxy.recvData();
			}
		}).start();
	}
	public void onEndConnet(View view){
		mSocketProxy.closeSocket();
	}
	private void testHandlerInWorkThread(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Looper.prepare();
				mWorkThreadHandler = new Handler(){
					@Override
					public void handleMessage(Message msg){
						Log.d("TAG"," in workThread what :" + msg.what + " msg : " + msg.obj.toString());
					}
				};
				Looper.loop();
			}
		}).start();
	}
	
	private void testConn(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean isConn = HttpManager.ping();
				Message msg = mHandler.obtainMessage(MSG_PING_STATE);
				msg.obj = new Boolean(isConn);
				msg.sendToTarget();
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Bitmap bitmap = mHttpManager.httpDownLoad(urlStr);
				Message msg = mHandler.obtainMessage(MSG_SHOW_IMAGE);
				msg.obj = bitmap;
				msg.sendToTarget();
			}
		}).start();
	}
	
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		unbindService(sc);
	}
}
