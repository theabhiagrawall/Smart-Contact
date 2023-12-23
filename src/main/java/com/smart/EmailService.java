package com.smart;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	public boolean sendEmail(String subject, String message, String to )
	{
	boolean f=false;
	String host="smtp.gmail.com";	
	
	//get the system properties
	Properties properties=System.getProperties();
		
		
    
	//setting important information to properties object
	
	//set host
	properties.put("mail.smtp.host", host);
	properties.put("mail.smtp.port", "465");
	properties.put("mail.smtp.ssl.enable", "true");
	properties.put("mail.smtp.auth", "true");
	
	//step !:to get the session object
	
	Session session=Session.getInstance(properties, new Authenticator()
			
			{

				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					// TODO Auto-generated method stub
					return new PasswordAuthentication("mayurlawankar2002@gmail.com","ojjzififzscymkrc");
				}
	
			});
	
	
	session.setDebug(true);
	
	
	
	//step:2:compose the message [text,multimedia]
	MimeMessage m=new MimeMessage(session);
	
	//from email
	
	try{
	m.setFrom();
	//adding recepeint
m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));


//adding subject to message
m.setSubject(subject);

//adding text to message
m.setText(message);
m.setContent(message,"text/html");


//step:3 //send the message using transport class

Transport.send(m);

System.out.println("send successfully");
f=true;
	
	}catch(Exception e)
	{
		e.printStackTrace();
		
	}
	return f;


}
}
