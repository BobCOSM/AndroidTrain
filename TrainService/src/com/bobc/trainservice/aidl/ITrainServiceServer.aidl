package com.bobc.trainservice.aidl;

import com.bobc.trainservice.aidl.ITrainServiceClient;
interface ITrainServiceServer{
	void showStr(String str);
	boolean registerClient(ITrainServiceClient client);
}