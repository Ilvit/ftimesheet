package com.timesheet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timesheet.models.TimesheetMail;

@Service
@Transactional
public class TimesheetMailService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String from;
	
	public void sendMail(String to, TimesheetMail timesheetMail) {
		SimpleMailMessage mailMessage=new SimpleMailMessage();
		mailMessage.setFrom(from);
		mailMessage.setSubject(timesheetMail.getSubject());
		mailMessage.setText(timesheetMail.getMessage());
		mailMessage.setTo(to);
		mailSender.send(mailMessage);
	}
}
