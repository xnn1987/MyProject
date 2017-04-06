package com.cn.aixiyi.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.toolbox.HurlStack;
import com.squareup.okhttp.OkHttpClient;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @version 创建时间：2015年8月31日
 */
public class OkHttpStack extends HurlStack {

	private OkHttpClient okHttpClient;
	private Context context;
	private static String psw="";//密码
	private static String bksName="storecerts.bks";//证书

	/**
	 * Create a OkHttpStack with default OkHttpClient.
	 */
	public OkHttpStack(Context context) {
		this(new OkHttpClient());
		this.context = context;
	}
	public OkHttpStack(Context context,String psw) {
		this(new OkHttpClient());
		OkHttpStack.psw=psw;
		this.context = context;
	}
	/**
	 * Create a OkHttpStack with a custom OkHttpClient
	 * 
	 * @param okHttpClient
	 *            Custom OkHttpClient, NonNull
	 */
	public OkHttpStack(OkHttpClient okHttpClient) {
		this.okHttpClient = okHttpClient;
	}

	@Override
	protected HttpURLConnection createConnection(URL url) throws IOException {
	    	trustAllHosts();//单项认证
	    //	trustServerHosts();//双向认证
		if ("https".equals(url.getProtocol())) {
			HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url
					.openConnection();
			httpsURLConnection
					.setHostnameVerifier(DO_NOT_VERIFY);
			return httpsURLConnection;
		} else {
			return super.createConnection(url);
		}

	}

	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {

		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}

	};

	/**
	 * Trust every server - dont check for any certificate
	 * 单项认证
	 */
	private static void trustAllHosts() {
		final String TAG = "trustAllHosts";
		try {
			KeyStore keyStore = KeyStore.getInstance("BKS");
			keyStore.load(null, null);
			Enumeration<String> enumerations = keyStore.aliases();
		} catch (KeyStoreException e1) {
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
				Log.e(TAG, "checkClientTrusted");

			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
				Log.e(TAG, "checkServerTrusted");
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    //双向认证
	private void trustServerHosts() {
		final String TAG = "trustServerHosts";
		final X509Certificate[] acceptedIssuers;

		KeyStore keyStore = null;
		try {
			keyStore = KeyStore.getInstance("BKS");
			InputStream inputStream = context.getResources().getAssets()
					.open(bksName);
			keyStore.load(inputStream, psw.toCharArray());
			// Loading the CA certificate from store
			final Certificate rootca = keyStore.getCertificate("storecerts");

			// Turn it to X509 format.
			InputStream is = new ByteArrayInputStream(rootca.getEncoded());
			X509Certificate x509ca = (X509Certificate) CertificateFactory
					.getInstance("X.509").generateCertificate(is);
			// AsyncHttpClient.silentCloseInputStream(is);
			if (is != null) {
				try {
					is.close();
					is = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			if (null == x509ca) {
				throw new CertificateException(
						"Embedded SSL certificate has expired.");
			}

			// Check the CA's validity.
			x509ca.checkValidity();

			// Accepted CA is only the one installed in the store.

			acceptedIssuers = new X509Certificate[] { x509ca };
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

				public X509Certificate[] getAcceptedIssuers() {

					return acceptedIssuers;
				}

				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
					Log.e(TAG, "checkClientTrusted");

				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
					Log.e(TAG, "checkServerTrusted");
					Exception error = null;

					if (null == chain || 0 == chain.length) {
						error = new CertificateException(
								"Certificate chain is invalid.");
					} else if (null == authType || 0 == authType.length()) {
						error = new CertificateException(
								"Authentication type is invalid.");
					} else {
						Log.i(TAG, "Chain includes " + chain.length
								+ " certificates.");
						try {
							for (X509Certificate cert : chain) {
								Log.i(TAG, "Server Certificate Details:");
								Log.i(TAG, "---------------------------");
								Log.i(TAG, "IssuerDN: "
										+ cert.getIssuerDN().toString());
								Log.i(TAG, "SubjectDN: "
										+ cert.getSubjectDN().toString());
								Log.i(TAG,
										"Serial Number: "
												+ cert.getSerialNumber());
								Log.i(TAG, "Version: " + cert.getVersion());
								Log.i(TAG, "Not before: "
										+ cert.getNotBefore().toString());
								Log.i(TAG, "Not after: "
										+ cert.getNotAfter().toString());
								Log.i(TAG, "---------------------------");

								// Make sure that it hasn't expired.
								cert.checkValidity();

								// Verify the certificate's public key chain.
								cert.verify(rootca.getPublicKey());
							}
						} catch (InvalidKeyException e) {
							error = e;
						} catch (NoSuchAlgorithmException e) {
							error = e;
						} catch (NoSuchProviderException e) {
							error = e;
						} catch (SignatureException e) {
							error = e;
						}
					}
					if (null != error) {
						Log.e(TAG, "Certificate error", error);
						throw new CertificateException(error);
					}
				}
			} };

			// Install the all-trusting trust manager

			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());

		} catch (KeyStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
