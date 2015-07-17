package com.chen.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpClientUtil {
	
	public static String REQUEST_METHOD_GET = "GET";
	public static String REQUEST_METHOD_PUT = "PUT";
	public static String REQUEST_METHOD_POST = "POST";
	public static String REQUEST_METHOD_DELETE = "DELETE";

	private static class TrustAnyTrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	public static String sendHttpRequest(String method, String targetUrl,
			String bodyObject, Map<String, String> headerObjects) {
		StringBuilder result = new StringBuilder();
		HttpURLConnection conn = null;
		PrintWriter writer = null;
		BufferedReader reader = null;
		
		try {
			URL url = new URL(targetUrl);
			
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			
			if (method != null) {
				conn.setRequestMethod(method);
			} else {
				throw new Exception("No request method confirm.");
			}
			
			if (headerObjects != null && headerObjects.size() > 0) {
				Iterator<String> iterator = headerObjects.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					conn.setRequestProperty(key, headerObjects.get(key));
				}
			}
			
			if (bodyObject != null) {
				writer = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
				writer.write(bodyObject);
				writer.flush();
				writer.close();
			}
			
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				result.append(line).append("\r\n");
			}
			reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return result.toString();
	}

	public static String sendHttpsRequest(String method, String targetUrl,
			String bodyObject, Map<String, String> headerObjects) {
		StringBuilder result = new StringBuilder();
		HttpsURLConnection conn = null;
		PrintWriter writer = null;
		BufferedReader reader = null;
		
		try {
			URL url = new URL(targetUrl);
			
			SSLContext sc = SSLContext.getInstance("SSL");  
			sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());  
			
			conn = (HttpsURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			
			if (method != null) {
				conn.setRequestMethod(method);
			} else {
				throw new Exception("No request method confirm.");
			}
			
			if (headerObjects != null && headerObjects.size() > 0) {
				Iterator<String> iterator = headerObjects.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					conn.setRequestProperty(key, headerObjects.get(key));
				}
			}
			
			if (bodyObject != null) {
				writer = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
				writer.write(bodyObject);
				writer.flush();
				writer.close();
			}
			
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				result.append(line).append("\r\n");
			}
			reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return result.toString();

	}
	
	public static void main(String[] args) {
		String url = "https://api.bmob.cn/1/classes/Video?where={\"type\":\"cooking\"}";
		Map<String, String> headerObjects = new HashMap<String, String>();
		headerObjects.put("X-Bmob-Application-Id", "8cd3d14a8aba3ed604857566054d9bde");
	    headerObjects.put("X-Bmob-REST-API-Key", "539a161c3836483ca1a4aa3e1623dc7c");
	    headerObjects.put("Content-Type", "application/json");
	    HttpClientUtil.sendHttpsRequest(HttpClientUtil.REQUEST_METHOD_GET, url, null, headerObjects);
	}

}
