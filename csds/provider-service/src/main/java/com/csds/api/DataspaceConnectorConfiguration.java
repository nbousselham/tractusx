package com.csds.api;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import feign.auth.BasicAuthRequestInterceptor;
import okhttp3.OkHttpClient;

@Configuration
public class DataspaceConnectorConfiguration {

	@Value("${connector.username}")
	private String username;

	@Value("${connector.password}")
	private String password;

	@Bean
	public BasicAuthRequestInterceptor adminAuth() {
		return new BasicAuthRequestInterceptor(username, password);
	}

	@Bean
	@Primary
	public OkHttpClient client() throws Exception {

		SSLContext sslContest = org.apache.http.ssl.SSLContexts.custom()
				.loadTrustMaterial(new TrustSelfSignedStrategy()).build();
		
		return new OkHttpClient().newBuilder()
				.sslSocketFactory(sslContest.getSocketFactory(), getDefaultJavaTrustManager())
				.hostnameVerifier(new HostnameVerifier()).build();
	}

	private class HostnameVerifier implements javax.net.ssl.HostnameVerifier {
		@Override
		public boolean verify(String arg0, SSLSession arg1) {
			return true;
		}
	}

	public static X509TrustManager getDefaultJavaTrustManager() throws Exception {
		TrustManagerFactory tmf;
		try {
			tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init((KeyStore) null);
		} catch (NoSuchAlgorithmException | KeyStoreException e) {
			throw new Exception("Unable to retrieve default TrustManagerFactory", e);
		}
		for (TrustManager tm : tmf.getTrustManagers()) {
			if (tm instanceof X509TrustManager) {
				return (X509TrustManager) tm;
			}
		}
		throw new Exception("No X509TrustManager found");
	}

}
