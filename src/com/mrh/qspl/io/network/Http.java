package com.mrh.qspl.io.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.mrh.qspl.val.types.TObject;

public class Http {
	public TObject createRequest() throws IOException {
		URL url;
		try {
			url = new URL("http://example.com");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			con.setInstanceFollowRedirects(false);
			con.setUseCaches(false);
		    con.setDoOutput(true);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String executeRequest(String targetURL, String urlParameters, String type) {
		HttpURLConnection con = null;
		
		try {
			//Create connection
			URL url = new URL(targetURL);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(type);//POST / GET
			con.setRequestProperty("Content-Type", "application/json");
			
			con.setRequestProperty("Content-Length",
			    Integer.toString(urlParameters.getBytes().length));
			con.setRequestProperty("Content-Language", "en-US"); 
			
			con.setUseCaches(false);
			con.setDoOutput(true);
			
			//Send Request
			DataOutputStream wr = new DataOutputStream (
					con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.close();
			
			//Get Response  
			InputStream is = con.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			if (con != null)
				con.disconnect();
			return response.toString();
		} 
		catch (Exception e) {
			e.printStackTrace();
			if (con != null)
				con.disconnect();
			return null;
		}
	}
}
