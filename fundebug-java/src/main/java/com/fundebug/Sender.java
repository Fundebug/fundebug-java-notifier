package com.fundebug;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.Map;


public class Sender {
	private final URL url;
	
	public Sender(String url) throws MalformedURLException {
		if(url == null){
			throw new IllegalArgumentException(url);
		}else {
			this.url = new URL(url);			
		}
	}
	
	public void send(Event event) throws IOException{

	    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	    connection.setDoOutput(true);
	    connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "utf-8");
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        connection.setRequestProperty("Accept", "application/json");
        
        Map<String, Object> eventObj = event.asJson();
        
        OutputStream out = connection.getOutputStream();

		JSONObject obj = new JSONObject();
		
		try {
			for (Map.Entry<String, Object> entry : eventObj.entrySet()) {  
				obj.put(entry.getKey(), entry.getValue());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
        out.write(obj.toString().getBytes("utf-8"));
        out.close();
        
        int statusCode = connection.getResponseCode();
        if(statusCode == 200)
        {
        	System.out.println("Send Error to Fundebug success!");
        }
	}
}
