package com.wondersgroup.smile.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.struts2.ServletActionContext;

public class EmailUtil {  
	
	public static void sendMessage(String to, String subject, String username) throws MessagingException,IOException {
		// Step 1:  Configure the mail session
		Properties props = new Properties();
		props.load(EmailUtil.class.getClassLoader().getResourceAsStream("email.properties"));
		Session mailSession = Session.getDefaultInstance(props);
		mailSession.setDebug(Boolean.parseBoolean(props.getProperty("debug")));

		// Step 2:  Construct the message
		InternetAddress fromAddress = new InternetAddress(props.getProperty("from"));
		InternetAddress toAddress = new InternetAddress(to);

		MimeMessage testMessage = new MimeMessage(mailSession);
		testMessage.setFrom(fromAddress);
		testMessage.addRecipient(javax.mail.Message.RecipientType.TO, toAddress);
		testMessage.setSentDate(new java.util.Date());
		//testMessage.setSubject(MimeUtility.encodeText(subject,"gb2312","B"));
		testMessage.setSubject(MimeUtility.encodeText(subject,"utf-8","B"));
		
		//File file = new File(ClassLoader.getSystemResource("email_templet.html").getPath());
		String realPath = ServletActionContext.getServletContext().getRealPath("email_templet.html");
		System.out.println("realPath"+realPath);
		File file = new File(realPath);
		StringBuilder sb = new StringBuilder("亲爱的"+username+":<br/><br/>");
		//BufferedReader reader = new BufferedReader(new FileReader(file));
		//BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)), "utf-8");
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));

		String tempString = null;
		while ((tempString = reader.readLine()) != null) {
			sb.append(tempString);
        }
        reader.close();
		testMessage.setContent(sb.toString(), "text/html;charset=utf-8");
		
		// Step 3:  Now send the message
		Transport transport = mailSession.getTransport("smtp");
		transport.connect(props.getProperty("host"), props.getProperty("account"), props.getProperty("password"));
		transport.sendMessage(testMessage, testMessage.getAllRecipients());
		transport.close();
	}
	
	public static void main(String[] args) throws MessagingException, IOException {
		String to = "maliang@wondersgroup.com";
		String subject = "html邮件测试";
		EmailUtil.sendMessage(to, subject, "马亮");
	}
}
