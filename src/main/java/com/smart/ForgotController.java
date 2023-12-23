package com.smart;

import java.util.Random;

import javax.mail.Session;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgotController {
	Random random=new Random(10000);
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepo;
	
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	//email id form open handler
	
	@RequestMapping("/forgot")
	public String openEmailForm()
	{
		
		
		return"forgot_email_form";
	}
	
	
	
	@PostMapping("/send-otp")
	public String sendOtp(@RequestParam("email") String email,HttpSession session)
	{
		
		System.out.println(email);
		
		//generating otp of 4 digit
		
		
		
		
	int otp=random.nextInt(999999);		
	
	System.out.println("OTP"+otp);
	String subject="OTP from SMART CONTACT MANAGER";
	String message=""
			+"<div style='border:1px solid #e2e2e2; padding:20px'>"
			+"<h3>"
	        +"OTP : "
	        +"<b>"+otp
	         
	         +" <h3>"
	         +"</div> ";
	
	String to=email;
	
	boolean flag=this.emailService.sendEmail(subject,message,to);
	
	if(flag)
	{
		session.setAttribute("myOtp", otp);
		session.setAttribute("email", email);
		return"verify_otp";
		
	}else
	{
		session.setAttribute("message", "check your email id !!");
		return"forgot_email_form";
	}
	}
	
//verify otp
	
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp,HttpSession session)
	{
		int myOtp=(int)session.getAttribute("myOtp");
		String email=(String)session.getAttribute("email");
		
		if(myOtp==otp)
		{
		User user=	this.userRepo.getUserByUserName(email);
		
		if(user==null)
		{
			//send error message
			session.setAttribute("message", "user does not exist with this email !! ");
			return "forgot_email_form";
		}else
		{
			//send change password form
		}
			return "password_change_form";
		}
	else{
		
	session.setAttribute("message","You have enterted wrong otp!!");
		return "verify_otp";
	}
	}
	
	
	//change password
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newpassword") String newpassword,HttpSession session)
	{
		String email=(String)session.getAttribute("email");
	User user=	this.userRepo.getUserByUserName(email);
	user.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
	this.userRepo.save(user);
	
		
	session.setAttribute("message","password changed successfully");
	return "login";
	}
	
		
	
	
	
	
	
	
}	

