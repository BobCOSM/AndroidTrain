package com.bobc.trainservice.network;

import android.net.NetworkInfo;

public class NetworkManager {
	public interface NetwokListener{
		public void onNetworkChanged(NetworkInfo info);
	}
}
