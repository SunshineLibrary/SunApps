package com.sunshine.support.webclient;


import com.sunshine.support.concurrent.ListenableFuture;
import com.sunshine.support.pkgmgr.Package;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;
import java.util.Vector;


public class WebClient {
	private final String list = "http://";
	private  String url = "http://";
	
	public WebClient(){
	}
	
	public ListenableFuture< List<Package> > getServerPackageList(){
		HttpGet get = new HttpGet(list);
		List<Package> ret = new Vector<Package>();
		HttpClient client = new DefaultHttpClient();
		try{
			HttpResponse res = client.execute(get);
			if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity entity = res.getEntity();  
                BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
                StringBuilder builder = new StringBuilder();
                for(String line = null; (line = reader.readLine()) != null; ){
                	builder.append(line);
                }
				JSONArray array = new JSONArray(builder.toString());
				for( int i = 0; i < array.length(); ++i ){
					JSONObject item = (JSONObject)array.opt(i);
					Package pkg = new Package( item.getString("name"), item.getString("version"), item.getString("id") );
					ret.add(pkg);
				}
                //response = new JSONObject(new JSONTokener(new InputStreamReader(entity.getContent(), "UTF-8"))); 
			}
		}
		catch(Exception e){
			//TODO:report the exception?
			e.printStackTrace();
		}
		finally{
			//TODO:Async is not implemented yet...
			return null;
		}
	}
	
	public ListenableFuture<URI> getPackageUrl(String id){
		String url = this.url + "/" + "apk/" + id; 
		HttpGet get = new HttpGet(url);
		URI ret = null;
		HttpClient client = new DefaultHttpClient();
		try{
			HttpResponse res = client.execute(get);
			if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity entity = res.getEntity();  
                BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
                StringBuilder builder = new StringBuilder();
                for(String line = null; (line = reader.readLine()) != null; ){
                	builder.append(line);
                }
				JSONObject json = new JSONObject(builder.toString());
				ret = new URI(json.getString("url")); 
			}
		}
		catch(Exception e){
			//TODO:report the exception?
			e.printStackTrace();
		}
		finally{
			//TODO:Async is not implemented yet...
			return null;
		}
	}
	
}
