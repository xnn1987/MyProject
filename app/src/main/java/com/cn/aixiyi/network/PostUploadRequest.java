package com.cn.aixiyi.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.utils.Base64ZipUtil;
import com.cn.aixiyi.utils.DebugLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PostUploadRequest extends Request<String> {
	/**
	 * 正确数据的时候回掉用
	 */
	private Response.Listener mListener;
	private Response.ErrorListener mError;
	/* 请求 数据通过参数的形式传入 */
	private List<File> mListItem;
	private Map<String, String> mMap;

	private String BOUNDARY = "-------------"; // 数据分隔线
	private String MULTIPART_FORM_DATA = "multipart/form-data";
	private static final String PREFIX = "--";
	private static final String LINE_END = "\r\n";
	private static String header = "";
	private  String mType = "";
	public PostUploadRequest(String url, List<File> listItem,String type,
			Map<String, String> maps, Response.Listener listener,Response.ErrorListener error) {
		super(Method.POST, url, null);
		this.mListener = listener;
		this.mError=error;
		this.mType=type;
		setShouldCache(false);
		mListItem = listItem;
		mMap = maps;
		// 设置请求的响应事件，因为文件上传需要较长的时间，所以在这里加大了，设为5秒
		setRetryPolicy(new DefaultRetryPolicy(5000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	}

	/**
	 * 这里开始解析数据
	 * 
	 * @param response
	 *            Response from the network
	 * @return
	 */
	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		try {
			String mString = new String(response.data,HttpHeaderParser.parseCharset(response.headers));
			return Response.success(mString,HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			DebugLog.e("Response", "Exception=" + e);
			mError.onErrorResponse(new ParseError(e));
			return Response.error(new ParseError(e));
		}
	}

	/**
	 * 回调正确的数据
	 * 
	 * @param response
	 *            The parsed response returned by
	 */
	@Override
	protected void deliverResponse(String response) {
		mListener.onResponse(response);
	}


	@Override
	public byte[] getBody() throws AuthFailureError {
		DebugLog.e("Response", "11111111111111" );
		if (mListItem == null || mListItem.size() == 0) {
			return super.getBody();
		}
		DebugLog.e("Response", "22222222222222" );
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int N = mListItem.size();
		DebugLog.e("Response", "NNNNNNNNNNNNNN="+N );
		// FormImage formImage ;
		for (int i = 0; i < N; i++) {
			File file = mListItem.get(i);
			DebugLog.e("Response", "3333333333333=" + dataToSize(file.getAbsolutePath()));
			String filename=file.getName();
			StringBuffer sb = new StringBuffer();
			/* 第一行 */
			// `"--" + BOUNDARY + LINE_END`
			sb.append("--" + BOUNDARY);
			sb.append(LINE_END);
			sb.append("Content-Disposition: form-data;");
			sb.append("name=\"");
			sb.append("object\"");
			sb.append(LINE_END);
			sb.append(LINE_END);
			sb.append("{\"name\":\"HellaaaaLog\",\"type\":\"text/plain\"}");
			sb.append(LINE_END);
			sb.append("--" + BOUNDARY);
			sb.append(LINE_END);
			sb.append("Content-Disposition: form-data;");
			sb.append("name=\"");
			sb.append("filesize\"");
			sb.append(LINE_END);
			sb.append(LINE_END);
			sb.append(dataToSize(file.getAbsolutePath()));
			sb.append(LINE_END);
			sb.append("--" + BOUNDARY);
			sb.append(LINE_END);
			sb.append("Content-Disposition: form-data;");
			sb.append("name=\"");
			sb.append("file\"");
			sb.append("; filename=\"");
			sb.append("HellyyyyLog.txt\"");
			sb.append(LINE_END);
			sb.append("Content-Type: ");
			sb.append("text/plain");
			sb.append(LINE_END);
			sb.append(LINE_END);
			try {
				bos.write(sb.toString().getBytes("utf-8"));
				/* 第五行 */
				// 文件的二进制数据 + LINE_END
				// bos.write(fileToBetyArray(file));
				bos.write(dataToByteArray(file.getAbsolutePath()));
				bos.write(LINE_END.getBytes("utf-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		/* 以下是用于上传参数 */
		if (mMap != null && mMap.size() > 0) {
			Iterator<String> it = mMap.keySet().iterator();
			while (it.hasNext()) {
				StringBuffer sb = null;
				sb = new StringBuffer();
				String key = it.next();
				String value = mMap.get(key);
				sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
				sb.append("Content-Disposition: form-data; name=\"")
						.append(key).append("\"").append(LINE_END)
						.append(LINE_END);
				sb.append(value).append(LINE_END);
				String params = sb.toString();
				try {
					bos.write(params.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		/* 结尾行 */
		// `"--" + BOUNDARY + "--" + LINE_END`
		String endLine = "--" + BOUNDARY + "--" + LINE_END;
		try {
			bos.write(endLine.toString().getBytes("utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}

	// Content-Type: multipart/form-data; boundary=----------8888888888888
	@Override
	public String getBodyContentType() {
		return MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY;
	}
	@Override
	public Map<String, String> getHeaders() {
		HashMap<String, String> headers = new HashMap<String, String>();
		getAuthHeader();
		DebugLog.e("Response", "mType=" + mType);
		headers.put("Authorization", "Basic " + header);
		headers.put("Content-Type",mType);
		return headers;
	}
	public static long dataToSize(String path) {
		long size = 0;
		File inFile = new File(path);
		if (inFile.exists()){
			try {
				FileInputStream fis = null;
				fis = new FileInputStream(inFile);
				size = fis.available();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return size;
	}
	public static byte[] dataToByteArray(String path) {
		String fileContent="";
		try {
			File inFile = new File(path);
			FileInputStream fis = new FileInputStream(inFile);
			byte[] buffer = new byte[1024];
			fis.read(buffer);
			// 对读取的数据进行编码以防止乱码
			// fileContent = EncodingUtils.getString(buffer, "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileContent.getBytes();
	}
	public static byte[] bitmapToByteArray(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回 bm 为空
		options.inJustDecodeBounds = false; // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = (int) (options.outHeight / (float) 320);
		if (be <= 0)
			be = 1;
		options.inSampleSize = be; // 重新读入图片，注意此时已经
		bitmap = BitmapFactory.decodeFile(path, options);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static byte[] fileToBetyArray(File file) {
		FileInputStream fileInputStream = null;
		byte[] bFile = null;
		try {
			bFile = new byte[(int) file.length()];
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bFile);
			fileInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileInputStream.close();
				bFile.clone();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bFile;
	}
	/*
    * 获取头文件Authorization转化为Base64
    * return header
    * */
	private static String getAuthHeader() {
		header = Base64ZipUtil.encodeBase64File(Contanct.USER_NAME);
		header = header.substring(0, header.length() - 1);
		return header;
	}
}
