package com.smart;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class HomeController{
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	
	@GetMapping("/test")
	@ResponseBody
	public String test()
	{
		
		User user=new User();
		user.setName("mayur lawankar");
		user.setEmail("may@gmail.com");
		userRepository.save(user);
		return "working";
	}
	
	
	@RequestMapping("/home")
	public String home(Model model)
	{
		
		return "home";
	}
	
	@RequestMapping("/about")
	public String about()
	{
		
		return "about";
	}
	
	
	
	@RequestMapping("/sign")
	public String signup(Model model)
	{
//		model.addAttribute(model);
		model.addAttribute("user",new User());
		return "signup";
	}
	
	
	
	//handler for registering user
	@RequestMapping(value="/do_register",method=RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result,@RequestParam(value="agreement",defaultValue="false") boolean agreement,Model model ,javax.servlet.http.HttpSession session)
	{
		try {
			if(!agreement)
			{
				System.out.println("you have not agreed the terms and condition");
			throw new Exception("you have not agreed the terms and condition");
			}
			
			if(result.hasErrors())
			{
				System.out.println("ERROR"+result.toString());
				model.addAttribute("user",user);
				return "signup";
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			
			
			System.out.println("Agreement"+agreement);
			System.out.println("User"+user);
			User result1=this.userRepository.save(user);
			
			
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message1("Successfully Registered !!","alert-successs"));
		
	
	return "signup";
			
		}catch(Exception e)
		{
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message1("something went wrong!"+e.getMessage(),"alert-danger"));
			}
		
		return "signup";
	}
	//handler for login
	@GetMapping("/login2")
	public String customLogin(Model model)
	{
		model.addAttribute("title","login page");
		return "login";
	}
	
	
	
	
}
	
	
	

