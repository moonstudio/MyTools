package com.wondersgroup.smile.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.httpclient.NameValuePair;

// 火尼短信HTTP开发接口1.3
public class SMSUtil {
	
	public static String sendMessage(String message, String phonelist) throws IOException{
		Properties p = new Properties();
		//p.load(ClassLoader.getSystemResourceAsStream("sms.properties"));
		System.out.println("3"+SMSUtil.class.getClassLoader().getResourceAsStream("sms.properties"));
		p.load(SMSUtil.class.getClassLoader().getResourceAsStream("sms.properties"));
		NameValuePair[] nvp = new NameValuePair[6];
		nvp[0] = new NameValuePair("account", p.getProperty("account"));
		nvp[1] = new NameValuePair("password", p.getProperty("password"));
		nvp[2] = new NameValuePair("content", message);
		nvp[3] = new NameValuePair("sendtime","");
		nvp[4] = new NameValuePair("phonelist", phonelist);
		nvp[5] = new NameValuePair("taskId", p.getProperty("account")+"_"+new SimpleDateFormat("yyyyMMddHHss").format(new Date())+"_http_"+Math.round((Math.random()) * 100000));
		return HttpUtil.postMethod(p.getProperty("url"), nvp);
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println(sendMessage("一条短信有多长？一条短信有多长？一条短信有多长？一条短信有多长？一条短信有多长？一条短信有多长？一条短信有多长？一条短信有多长？一条短信有多长？一条短信有多长？一条短信有多长？一条短信有多长？一条短信有多长？一条短信有多长？一条短信有多长？一条短信有多长？一条短信有多长？一条短信有多长？一条短信有多长？一条短信有多长？一条短信有多长？一条短信有多长？一条短信有多长？一条短信有多长？一条短信有多长？!@#$%^&*()_+", "15821410035"));
	}
}
