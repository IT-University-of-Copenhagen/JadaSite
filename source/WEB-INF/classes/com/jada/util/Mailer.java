/*
 * Copyright 2007-2010 JadaSite.

 * This file is part of JadaSite.
 
 * JadaSite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * JadaSite is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JadaSite.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jada.util;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.jada.jpa.entity.Site;

public class Mailer {
	String smtpHost;
	String smtpAccount;
	String smtpPassword;
	String smtpPort;
	boolean auth;
    Logger logger = Logger.getLogger(Mailer.class);

	public Mailer(Site site) throws Exception {
		smtpHost = site.getMailSmtpHost();
		smtpAccount = site.getMailSmtpAccount();
		smtpPassword = AESEncoder.getInstance().decode(site.getMailSmtpPassword());
		smtpPort = site.getMailSmtpPort();
		
		if (Format.isNullOrEmpty(smtpHost)) {
			logger.error("Unable to send email");
			logger.error("smtpHost = " + smtpHost);
		}

		auth = true;
		if (Format.isNullOrEmpty(smtpAccount)) {
			auth = false;
			smtpAccount = null;
			smtpPassword = null;
		}
	}

	public void sendMail(String mailFrom, String mailTo, String subject, String body) throws AddressException, MessagingException {
		sendMail(mailFrom, mailTo, subject, body, "text/plain");
	}
	
	public void sendMail(String mailFrom, String mailTo, String subject, String body, String contentType) throws AddressException, MessagingException {
	    Properties properties = System.getProperties();
	    properties.put("mail.smtp.host", smtpHost);
	    if (auth) {
	    	properties.put("mail.smtp.auth", "true");
	    }
	    else {
	    	properties.remove("mail.smtp.auth");
	    }
	    if (!Format.isNullOrEmpty(smtpPort)) {
	    	properties.put("mail.smtp.port", smtpPort);
	    }
	    else {
	    	properties.remove("mail.smtp.port");
	    }
	    
	    MyAuthenticator authenticator = null;
	    if (auth) {
	    	authenticator = new MyAuthenticator(smtpAccount, smtpPassword);
	    }
	    Session session = Session.getDefaultInstance(properties, authenticator);
//	    session.setDebug(true);
	    Message message = new MimeMessage(session);
	    message.setFrom(new InternetAddress(mailFrom));
	    Address address[] = new Address[1];
	    address[0] = new InternetAddress(mailFrom);
	    message.setReplyTo(address);
	    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo, false));
	    message.setSubject(subject);
	    message.setContent(body, contentType);
	    
	    Transport.send(message);
	}
	
	public class MyAuthenticator extends Authenticator
    {  
    	String smtpUsername = null;
    	String smtpPassword = null;
     
    	public MyAuthenticator(String username, String password)
    	{
    		smtpUsername = username;
    		smtpPassword = password;
    	}

    	protected PasswordAuthentication getPasswordAuthentication()
    	{
    		return new PasswordAuthentication(smtpUsername,smtpPassword);
    	}
    }
}
