#include <jni.h>
#include <stdio.h>
//#include <stdlib.h>
#include "TrainServer.h"

JNIEXPORT jstring JNICALL Java_com_bob_trainservice_util_JavaJniTrain_getStringForC
  (JNIEnv *env, jobject){
	jclass clazz;
	clazz = env->FindClass("com/bob/trainservice/util/JavaJniTrain");
	if(clazz == NULL){
		printf(" no this class \n");
		return NULL;
	}
	jmethodID method_construct = env->GetMethodID(clazz, "<init>", "()V");
	if(method_construct == NULL){
		printf(" no default construct method \n");
		return NULL;
	}
	jmethodID method_instance = env->GetMethodID(clazz, "getTestString", "()Ljava/lang/String;");
	if(method_instance == NULL){
		printf("no this method \n");
		return NULL;
	}
	jobject clazz_obj = env->NewObject(clazz, method_construct);
	if(clazz_obj == NULL){
		printf(" create obj error \n ");
		return NULL;
	}
	jstring jstr = (jstring)env->CallObjectMethod(clazz_obj, method_instance);
	const char* c_str = NULL;
	char buf[128];
	jboolean isCopy;
	c_str = env->GetStringUTFChars(jstr, &isCopy);
	sprintf(buf,"%s",c_str);
	env->ReleaseStringUTFChars(jstr, c_str);
	return env->NewStringUTF(buf);

}
