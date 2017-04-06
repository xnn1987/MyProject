package com.cn.aixiyi.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Inflater;

import android.util.Base64;



public class Base64ZipUtil {

    /**
     * <p>将普通字符串加密成base64字符</p>
     * @param content
     * @throws Exception
     */
    public static String  encodeBase64File(String content) {
        String encodedString= Base64.encodeToString(content.getBytes(), Base64.DEFAULT);
        return encodedString;


    }
	  /**
     * <p>将base64字符解码保存文件</p>
     * @param base64Code
     * @throws Exception
     */
    public static String  decoderBase64File(String base64Code) throws Exception {
        byte [] baseByte = Base64.decode(base64Code, Base64.DEFAULT);
        String buffer=new String(baseByte);
        buffer=String.copyValueOf(buffer.toCharArray(), 0, baseByte.length);
		return buffer;
        

    }

public static String UnBase64AndUnZip(String s)
{
	if (s == null) return null; 
	
	try { 
		byte[] b = Base64.decode(s, Base64.DEFAULT);;
		byte[] r=unzip(b);
		return new String(r,"utf-8"); 
/*		System.out.println(r.length);
		for (int i=0;i<r.length;i++){
			System.out.println(i+"-" + r[i] +" ");
		}
		return new String(r);*/
	} catch (Exception e) { 
		return null; 
	} 

}

/**
 * 解压被压缩的数据
 *
 * @return
 * @throws IOException
 */
public static byte[] unzip(byte[] compressedData) {
	//byte[] compressedData = null;
	byte[] decompressedData=null;
	try{
    Inflater decompressor = new Inflater();
    decompressor.setInput(compressedData);
    ByteArrayOutputStream bos = new ByteArrayOutputStream(compressedData.length);
    byte[] buf = new byte[1024];
    while (!decompressor.finished()) {
      int count = 0;
      count=decompressor.inflate(buf);
      bos.write(buf, 0, count);
    }
    bos.close();
    decompressedData= bos.toByteArray();
	}
	catch(Exception e){
		
	}
	return decompressedData;
}

/**
 * 把字节数组转换成16进制字符串
 * 
 * @param bArray
 * @return
 */
public static String bytesToHexString(byte[] bArray) {
 StringBuffer sb = new StringBuffer(bArray.length);
 String sTemp;
 for (int i = 0; i < bArray.length; i++) {
  sTemp = Integer.toHexString(0xFF & bArray[i]);
  if (sTemp.length() < 2)
   sb.append(0);
  sb.append(sTemp.toUpperCase());
 }
 return sb.toString();
}

 String source = "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";
 String result = UnBase64AndUnZip(source);


}
