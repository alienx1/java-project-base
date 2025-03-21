package com.ss.infrastructure.service;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.ss.infrastructure.model.EmailConfigModel;

@Service
public class EmailService {

    public void sendEmail(EmailConfigModel emailConfig) throws Exception {
        Properties prop = new Properties();
        prop.put("mail.transport.protocol", "smtp");
        prop.put("mail.smtp.host", emailConfig.getHost());
        prop.put("mail.smtp.port", emailConfig.getPort());

        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.ssl.trust", "*");
        prop.put("mail.smtp.socketFactory.fallback", "false");
        prop.put("mail.smtp.socketFactory", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailConfig.getUser(), emailConfig.getPass());
            }
        });
        String mailToString;
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailConfig.getFrom()));
        if (StringUtils.isNotEmpty(emailConfig.getToEmail())) {
            mailToString = emailConfig.getToEmail();
        } else {
            mailToString = StringUtils.join(emailConfig.getMailTos(), ',');
        }
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailToString));
        if (StringUtils.isNotEmpty(emailConfig.getCc())) {
            message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(emailConfig.getBcc()));
        }

        if (StringUtils.isNotEmpty(emailConfig.getBcc())) {
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(emailConfig.getCc()));
        }
        message.setSubject(emailConfig.getSubject(), "utf-8");

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(emailConfig.getMessageText(), "text/html; charset=utf-8");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        if (ObjectUtils.isNotEmpty(emailConfig.getAttachments())) {
            for (File file : emailConfig.getAttachments()) {
                addAttachment(multipart, file);
            }
        }

        message.setContent(multipart);

        Transport.send(message);
    }

    private static void addAttachment(Multipart multipart, File file) throws Exception {
        FileDataSource source = new FileDataSource(file);
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(file.getName());
        multipart.addBodyPart(messageBodyPart);
    }

}
