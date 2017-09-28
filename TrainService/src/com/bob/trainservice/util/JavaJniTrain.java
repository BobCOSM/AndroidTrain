package com.bob.trainservice.util;

//import android.util.Log;

public class JavaJniTrain {
	public JavaJniTrain(){
		
	}
	public String getString(){
		String str = getStringForC();
		//Log.d("JavaJniTrain"," getstr : " + str);
		return str;
	}
	public String getTestString(){
		return "nobody";
	}
	
	public native String getStringForC();
	
}
