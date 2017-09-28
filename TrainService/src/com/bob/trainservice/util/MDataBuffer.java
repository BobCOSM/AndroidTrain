package com.bob.trainservice.util;


import java.util.ArrayList;

import android.util.Log;

public class MDataBuffer {
	public static final int EACH_SIZE = 128;

	private static final String TAG = "BinDataBuffer";
	
	private int mPoint = -1;
	private int mOffset = 0;
	private ArrayList<byte[]> mBuf = null;
	public MDataBuffer(){
		mBuf = new ArrayList<byte[]>();
		addArray();
	}
	
	public void appandBytes(byte[] source,int offset){
		if(source == null || offset < 0){
			return ;
		}
		
		byte[] curArray = mBuf.get(mPoint);
		int byteCount = appandByteArray(curArray,mOffset,source,offset);
		if(byteCount == 0){
			return ;
		}
		if(byteCount >= EACH_SIZE - mOffset ){
			addArray();
			appandBytes(source,offset + byteCount);
		}else{
			mOffset += byteCount; 
		}
		return ;
	}
	
	public byte[] getBinData(long offsetaddr,int length){
		byte[] res = null;
		int point = (int) (offsetaddr/EACH_SIZE);
		if(point > mPoint /*|| offsetaddr + length > mPoint * 128 + mOffset*/){
			return res;
		}
		
		byte[] offsetArray = mBuf.get(point);
		int enableSize = mPoint * EACH_SIZE + mOffset - point * EACH_SIZE;
		
		if(enableSize > length){
			res = getBinByte(length);
			for(int i = 0; i < length;i++){
				res[i] = offsetArray[i%EACH_SIZE];
				if( (i + 1) % EACH_SIZE == 0){
					point += 1;
					offsetArray = mBuf.get(point);
				}
			}
		} else {
			res = getBinByte(length);
			for(int i = 0; i < enableSize; i++){
				res[i] = offsetArray[i%EACH_SIZE];
				if( (i + 1) % EACH_SIZE == 0){
					point += 1;
					offsetArray = mBuf.get(point);
				}
			}
		}
		return res;
	}
	
	private byte[] getBinByte(int size){
		byte[] res = new byte[size];
		for(int i = 0;i < size;i++){
			res[i] = (byte) 0xff;
		}
		return res;
	}
	
	private void addArray(){
		mBuf.add(new byte[EACH_SIZE]);
		mPoint += 1;
		mOffset = 0;
	}
	
	private int appandByteArray(byte[] target,int tOffset,byte[] source,int sOffset){
		int byteCount = 0;
		if(target == null || source == null){
			return byteCount;
		}
		int enableSize = target.length - tOffset;
		int dataSize = source.length - sOffset;
		if(enableSize > dataSize){
			for(int i = 0; i < dataSize ; i++){
				target[i + tOffset] = source[i + sOffset];
			}
			byteCount = dataSize;
//			Log.d(TAG,"enableSize = " + enableSize + "  dataSize = " + dataSize);
		} else {
			for(int i = 0; i < enableSize ;i++){
				target[i + tOffset] = source[i + sOffset];
			}
			byteCount = enableSize;
		}
		
		return byteCount;
	}
}

