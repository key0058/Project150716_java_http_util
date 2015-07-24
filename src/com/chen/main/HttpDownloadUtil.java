package com.chen.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpDownloadUtil {
	
	private static String[] sessionId = null;
	
	public static void saveSession(String entryUrl) {
		HttpURLConnection conn = null;
		
		try {
			URL url = new URL(entryUrl);
			
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(HttpClientUtil.REQUEST_METHOD_GET);
			
			String sessionValue = conn.getHeaderField("Set-Cookie" );
			if (sessionValue != null && sessionValue.length() > 0) {
				sessionId = sessionValue.split(";");
		        System.out.println("Save Session:" + sessionId[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void downloadFile(String targetUrl, String dir, String filename) {
		HttpURLConnection conn = null;
		FileOutputStream writer = null;
		InputStream reader = null;
		
		try {
			URL url = new URL(targetUrl);
			
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(60 * 1000);
			conn.setRequestMethod(HttpClientUtil.REQUEST_METHOD_GET);
			conn.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
			conn.setRequestProperty("Accept","*/*");
			if (sessionId != null) {
				conn.setRequestProperty("Cookie", sessionId[0]);
			}
			
			if (conn.getResponseCode() >= 400) {
				System.out.println("Not found any file for download [error:" + conn.getResponseCode()  + "]");
				return;
			}

			File file = new File(dir);
			if (!file.exists()) {
				file.mkdir();
			}
			
			int len;
			byte[] bs = new byte[1024];
			String targetFilePath = file.getAbsolutePath() + "\\" + filename;
			
			
			reader = conn.getInputStream();
			writer = new FileOutputStream(targetFilePath);
			
			while ((len = reader.read(bs)) != -1) {
				writer.write(bs, 0, len);
			}
			
			reader.close();
			writer.close();
			
			System.out.println("Save file:" + targetFilePath);
			
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
	}
	
	public static void downloadFileBySession(String entryUrl, String targetUrl, String dir, String filename) {
		saveSession(entryUrl);
		downloadFile(targetUrl, dir, filename);
	}
	
	public static void main(String[] args) {
//		String entryUrl = "http://dcimg.awalker.jp/img1.php?id=wUyCHvblzLzzDHZTcHimHIltvA0xBf71nCdx7EQ5050rTuc3cDAYeIWrsqrEDPYF9OWpivRms0w2TptFsX5SNMdvteuyL28EEiEst9mOQSDx0fmuBXPgWPBHQFya69VgFAbd2qqS8Pc3d5DwkcarB62hVphNJLZVUGbc0gFqvHjHcbiwSV89su218bWiMHW07kzWeWWG";
		String targetUrl = "http://dcimg.awalker.jp/img2.php?sec_key=wUyCHvblzLzzDHZTcHimHIltvA0xBf71nCdx7EQ5050rTuc3cDAYeIWrsqrEDPYF9OWpivRms0w2TptFsX5SNMdvteuyL28EEiEst9mOQSDx0fmuBXPgWPBHQFya69VgFAbd2qqS8Pc3d5DwkcarB62hVphNJLZVUGbc0gFqvHjHcbiwSV89su218bWiMHW07kzWeWWG";
//		HttpDownloadUtil.downloadFileBySession(entryUrl, targetUrl, ".", "test.jpg");
		
//		String targetUrl = "http://img.nogizaka46.com/blog/mai.shinuchi/img/2015/07/22/9117037/0006.gif";
		HttpDownloadUtil.downloadFile(targetUrl, ".", "");
	}
	

}
