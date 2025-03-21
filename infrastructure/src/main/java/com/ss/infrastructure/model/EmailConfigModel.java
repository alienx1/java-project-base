package com.ss.infrastructure.model;

import java.io.File;
import java.util.List;

import lombok.Data;

@Data
public class EmailConfigModel {
	private String host;
	private String port;
	private String user;
	private String pass;
	private String from;
	private String subject;
	private String messageText;
	private List<String> mailTos;
	private String toEmail;
	private String cc;
	private String bcc;
	private List<File> attachments;
	private String docDate;
	private String docType;
	private Boolean isSend;

}
