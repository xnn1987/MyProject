package com.tec.key;

import android.util.Base64;

public class Ptlmaner {
	public static String tcb;
	private static final String TAG="ProtocolManager";

    static {
	        System.loadLibrary("JniTest");
	    }

	public static native void requestProcess();

  //加密
	public static String eryt(String content){
		if (content!=null) {

			if (tcb==null) {//第一次会调用
				requestProcess();
			}
			if (tcb!=null) {
				byte[] xxtea = Xxtea.eryt(content.getBytes(),tcb.getBytes());
				byte[] base64 = Base64.encode(xxtea, 0);
				return new String(base64);
			}
			return null;
		}else{
			return null;
		}
	}

    //解密
	public static String dryt(String content){
		if (tcb==null) {
			requestProcess();
		}
		if(content!=null&&tcb!=null){
			byte[] arr = Base64.decode(content.getBytes(), 0);
			byte[] resArr = Xxtea.dryt(arr, tcb.getBytes());
			return new String(resArr);
		}else{
			return null;
		}
	}

}
