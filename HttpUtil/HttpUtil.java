package com.wondersgroup.smile.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpUtil {
	/*private static HttpClient client;*/
	
	public static String getMethod(String url, String param){
		String response = null;
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url); 
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			if(param != null && !param.isEmpty()){
				method.setQueryString(URLEncoder.encode(param, "UTF-8"));
			}
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {  
				System.out.println("Request failed! Errorcode:"+statusCode);
			} else {
				response = method.getResponseBodyAsString();
			}
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getLocalizedMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			method.releaseConnection();
			((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
		}
		return response;
	} 
	
	public static String postMethod(String url, NameValuePair[] nvp) {
		String response = null;
		HttpClient client = new HttpClient();
		PostMethod	method = new PostMethod(url);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		if(nvp != null){
			method.setRequestBody(nvp);
		}
		try {
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {  
				System.out.println("Request failed! Errorcode:"+statusCode);
			} else {
				response = method.getResponseBodyAsString();
			}
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			method.releaseConnection();
			((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
		}
		return response;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException{
		System.out.println(HttpUtil.getMethod("http://localhost:6060/MANAGE_ESB/http/getUserInfo", "{'nm':'student','pwd':'student'}"));
	}
}
