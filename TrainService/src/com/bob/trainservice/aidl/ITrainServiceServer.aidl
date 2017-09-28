package com.bob.trainservice.aidl;

import com.bob.trainservice.aidl.ITrainServiceClient;
interface ITrainServiceServer{
	void showStr(String str);
	boolean registerClient(ITrainServiceClient client);
	boolean unregisterClient();
}