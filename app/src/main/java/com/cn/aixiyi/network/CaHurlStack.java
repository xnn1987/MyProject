package com.cn.aixiyi.network;

import android.content.Context;

import com.android.volley.toolbox.HurlStack;
import com.squareup.okhttp.OkHttpClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by Administrator on 2016/11/5.
 */
public class CaHurlStack extends HurlStack {
    private Context context;
    private OkHttpClient okHttpClient;
    private SSLSocketFactory sslSocketFactory;
    private final String load = "-----BEGIN CERTIFICATE-----\n" +
            "MIIEBzCCA3CgAwIBAgIBADANBgkqhkiG9w0BAQQFADCBlDELMAkGA1UEBhMCVVMxCzAJBgNVBAgT\n" +
            "AldBMSEwHwYDVQQKExhVbml2ZXJzaXR5IG9mIFdhc2hpbmd0b24xFDASBgNVBAsTC1VXIFNlcnZp\n" +
            "Y2VzMRcwFQYDVQQDEw5VVyBTZXJ2aWNlcyBDQTEmMCQGCSqGSIb3DQEJARYXaGVscEBjYWMud2Fz\n" +
            "aGluZ3Rvbi5lZHUwHhcNMDMwMjI1MTgyNTA5WhcNMzAwOTAzMTgyNTA5WjCBlDELMAkGA1UEBhMC\n" +
            "VVMxCzAJBgNVBAgTAldBMSEwHwYDVQQKExhVbml2ZXJzaXR5IG9mIFdhc2hpbmd0b24xFDASBgNV\n" +
            "BAsTC1VXIFNlcnZpY2VzMRcwFQYDVQQDEw5VVyBTZXJ2aWNlcyBDQTEmMCQGCSqGSIb3DQEJARYX\n" +
            "aGVscEBjYWMud2FzaGluZ3Rvbi5lZHUwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBALwCo6h4\n" +
            "T44m+7ve+BrnEqflqBISFaZTXyJTjIVQ39ZWhE0B3LafbbZYju0imlQLG+MEVAtNDdiYICcBcKsa\n" +
            "pr2dxOi31Nv0moCkOj7iQueMVU4E1TghYIR2I8hqixFCQIP/CMtSDail/POzFzzdVxI1pv2wRc5c\n" +
            "L6zNwV25gbn3AgMBAAGjggFlMIIBYTAdBgNVHQ4EFgQUVdfBM8b6k/gnPcsgS/VajliXfXQwgcEG\n" +
            "A1UdIwSBuTCBtoAUVdfBM8b6k/gnPcsgS/VajliXfXShgZqkgZcwgZQxCzAJBgNVBAYTAlVTMQsw\n" +
            "CQYDVQQIEwJXQTEhMB8GA1UEChMYVW5pdmVyc2l0eSBvZiBXYXNoaW5ndG9uMRQwEgYDVQQLEwtV\n" +
            "VyBTZXJ2aWNlczEXMBUGA1UEAxMOVVcgU2VydmljZXMgQ0ExJjAkBgkqhkiG9w0BCQEWF2hlbHBA\n" +
            "Y2FjLndhc2hpbmd0b24uZWR1ggEAMAwGA1UdEwQFMAMBAf8wKwYDVR0RBCQwIoYgaHR0cDovL2Nl\n" +
            "cnRzLmNhYy53YXNoaW5ndG9uLmVkdS8wQQYDVR0fBDowODA2oDSgMoYwaHR0cDovL2NlcnRzLmNh\n" +
            "Yy53YXNoaW5ndG9uLmVkdS9VV1NlcnZpY2VzQ0EuY3JsMA0GCSqGSIb3DQEBBAUAA4GBAIn0PNmI\n" +
            "JjT9bM5d++BtQ5UpccUBI9XVh1sCX/NdxPDZ0pPCw7HOOwILumpulT9hGZm9Rd+W4GnNDAMV40we\n" +
            "s8REptvOZObBBrjaaphDe1D/MwnrQythmoNKc33bFg9RotHrIfT4EskaIXSx0PywbyfIR1wWxMpr\n" +
            "8gbCjAEUHNF/\n" +
            "-----END CERTIFICATE-----";

    /**
     * Create a OkHttpStack with default OkHttpClient.
     */
    public CaHurlStack(Context context) {
        this.context = context;
        this.sslSocketFactory=initSSLSocketFactory();

    }

    /**
     * 生成SSLSocketFactory
     * 这里的代码与之前相比，没有什么不同
     *
     * @return
     */
    private SSLSocketFactory initSSLSocketFactory() {
        //生成证书:Certificate
        CertificateFactory cf = null;
        SSLSocketFactory factory = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = new ByteArrayInputStream(load.getBytes());
            Certificate ca = null;
            try {
                ca = cf.generateCertificate(caInput);
            } finally {
                try {
                    caInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //初始化公钥:keyStore
            String keyType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            //初始化TrustManagerFactory
            String algorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory managerFactory = TrustManagerFactory.getInstance(algorithm);
            managerFactory.init(keyStore);

            //初始化sslContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, managerFactory.getTrustManagers(), null);
            factory = sslContext.getSocketFactory();

        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return factory;
    }
}
