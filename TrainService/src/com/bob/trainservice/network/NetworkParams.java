package com.bob.trainservice.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

import android.util.Log;

public class NetworkParams {

	// 业务服务器端口
	public int mBusinessPort = -1;

	private String mStrSocketAddr = "";

	// 业务服务器IP地址
	private InetAddress mBusinessServiceAddress = null;

	public void setBusinessAddress(InetAddress BussAddr) {
		if (BussAddr != null)
			mBusinessServiceAddress = BussAddr;
	}

	public InetAddress getBusinessAddress() {
		if (mBusinessServiceAddress == null) {
			try {
				Log.w("getBusinessAddress", "addr:"+mStrSocketAddr);
				mBusinessServiceAddress = InetAddress.getByName(mStrSocketAddr);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}
		return mBusinessServiceAddress;
	}

	public NetworkParams(String socketAddr, int nport) {
		mBusinessPort = nport;

		mStrSocketAddr = socketAddr;
	}

}
