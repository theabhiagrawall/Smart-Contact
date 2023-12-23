package com.smart;

import static org.mockito.Mockito.mockitoSession;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mysql.cj.Session;

import com.razorpay.*;



@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepo;
	
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder ;
	
	@Autowired
	private MyOrderRepository myOrderRepo;
	
	
	
	//method for adding comming data to response
	@ModelAttribute
	public void addCommonData( Model model,Principal principal)
	{
		String username=principal.getName();
		System.out.println("USERNAME:"+username);
		
		User user=this.userRepository.getUserByUserName(username);
		
		System.out.println("user"+user);
		
		
		
		model.addAttribute("user",user);
		//get the user using username(email)
	}
	
	//dashboard home
	@RequestMapping("/index")
	public String dashboard(Model model,Principal principal)
	{
		model.addAttribute("title", "user dashboard");
		
		return "user/user_dashboard";
	}
	
	
	//open add form handler
	
	@GetMapping("/add-contact")
	public String openddContactForm(Model model)
	{
		
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact",new Contact());
		return "user/add_contact_form";
	}

	
	
	
	
	//processing add contact form
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,
//			@RequestParam("image")MultipartFile file,
			Principal principal,HttpSession session)
	{
		try {
	String name=principal.getName();
	
	User user=this.userRepository.getUserByUserName(name);
	
	
	// processing for uploading image
	
//	if(file.isEmpty())
//	{
//		System.out.println("file is empty");
//		
//	}else
//	{
//		//upload to folder and update it
//		contact.setImage(file.getOriginalFilename());
//	File saveFile=	new ClassPathResource("static/images").getFile();		
//		
//	Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
//	
//	Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//	
//	System.out.println("image is uploaded");
//	}
//	
//	
	
	
	contact.setUser(user);
	user.getContacts().add(contact);
	
	
	
	
	this.userRepository.save(user);
	
		System.out.println("DATA:"+contact);
		
		System.out.println("Added to database");
		
		session.setAttribute("message",new Message1("your contact is added !!", "success"));
		}
		catch(Exception e)
		{
			System.out.println("ERROR"+e.getMessage());
			e.printStackTrace();
			session.setAttribute("message",new Message1("something went wrong", "danger"));
			}
		return"user/add_contact_form";
	}
	
	
	
	
	//show contacts handler
	@GetMapping("/show-contacts/{page}")
	public String ShowContacts(@PathVariable("page") Integer page, Model m,Principal principal)
	{
       String userName=principal.getName();
       User user=  this.userRepository.getUserByUserName(userName);
     
  Pageable  pageable= PageRequest.of(page, 3);
		Page<Contact>  contacts= this.contactRepo.findContactsByUser(user.getId(),pageable);
		m.addAttribute("contacts",contacts);
		m.addAttribute("currentPage","page");
		
		m.addAttribute("totalPages", contacts.getTotalPages());
		
		return"user/show_contacts";
	}
	
	
	
	//showing specific contact details
	
	@GetMapping("/{cID}/contact")
	public String showContactDetails(@PathVariable("cID") Integer cID,Model model,Principal principal)
	{
		
	String userName=	principal.getName();
	
	User user=this.userRepository.getUserByUserName(userName);
		System.out.println("CID"+cID);
	Optional<Contact> contactOptional=	this.contactRepo.findById(cID);
	Contact contact=	contactOptional.get();
	
	if(user.getId()==contact.getUser().getId())
	{
	model.addAttribute("contact",contact);
	}
	return"user/contact_details";
		
	}
	
	
	//deleting contact handler
	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId,Model model,HttpSession session,Principal principal)
	{
		
	Optional<Contact> contactOptional=	this.contactRepo.findById(cId);
	Contact contact=contactOptional.get();	
	contact.setUser(null);
//	this.contactRepo.delete(contact);
	
	
	User user=this.userRepository.getUserByUserName(principal.getName());
	user.getContacts().remove(contact);
	this.userRepository.save(user);
	
session.setAttribute("user-deleted", "success");
	
		return"redirect:/user/show-contacts/0";
	}
	
	//open update form handler
	@PostMapping("/update-contact/{cId}")
	public String updateForm(@PathVariable("cId") Integer cId,Model m)
	{
		m.addAttribute("title","Update Contact");
	Contact contact	= this.contactRepo.findById(cId).get();	
	
	m.addAttribute("contact", contact);
		return"user/update_form";
	}
	
	
	
	
	
	// update contact handler
	@RequestMapping(value="/process-update",method=RequestMethod.POST)
	public String updateHandler(@ModelAttribute Contact contact ,Principal principal)
	{
		
		try {
			User user=this.userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);
			this.contactRepo.save(contact);
			
		}
		
		catch(Exception e)
		{
			e.printStackTrace()	;
			}
		return "redirect:/user/"+contact.getcId()+"/contact";
	}
	
	
	
	
	
	
	
	
	
	
	//your profile handler
	@GetMapping("/profile")
	public String yourProfile(Model model)
	{
		model.addAttribute("title","profile page");
		return"user/profile";
		
	}

	//open settings handler
	@GetMapping("/settings")
	public String openSettings()
	{
		
		return "user/settings";
	}
	
	
	
	//change password handler
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword,Principal principal)
	{
		
		System.out.println("OLD PASSWORD:"+oldPassword);
		System.out.println("NEW PASSWORD:"+newPassword);
		
		
	String userName=	principal.getName();
		
	User currentUser	= this.userRepository.getUserByUserName(userName);
	
	System.out.println(currentUser.getPassword());
	
	
	if(this.bcryptPasswordEncoder.matches(oldPassword, currentUser.getPassword()))
			{
		//change the password
		currentUser.setPassword(this.bcryptPasswordEncoder.encode(newPassword));
		this.userRepository.save(currentUser);
		
		
		
		
		
			}else {
				
				return "redirect:/user/settings";
				
			}
	
	
		return"user/user_dashboard";
		
	}
	
	
	
	
	//creating order for payment
	
	@PostMapping("/create_order")
	@ResponseBody
	public String createOrder(@RequestBody Map<String,Object> data,Principal principal) throws Exception
	{
	System.out.println(data);
	
	int amt=Integer.parseInt(data.get("amount").toString());
	
	 RazorpayClient client=	new RazorpayClient("rzp_test_qz4OlqWmztsgCz", "QC39g2cmwZkI2aXELe4ClVgv");
	 
	 
	 JSONObject ob=new JSONObject();
	 ob.put("amount", amt*100 );
	 ob.put("currency", "INR");
	 ob.put("receipt", "txn_235425");
	 
	 //creating new order
	 Order order=client.Orders.create(ob);
	 
	 System.out.println(order);
	 
	 
	 
	 //saving the order in the database
	 MyOrder  myOrder  =new  MyOrder ();
	 
	 myOrder.setAmount(order.get("amount"));
	 myOrder.setOrderId(order.get("order_id"));
	 
	 myOrder.setStatus("created");
	 
	 myOrder.setUser(this.userRepository.getUserByUserName(principal.getName()));
	 
	 myOrder.setReceipt(order.get("receipt"));
	 
	 this.myOrderRepo.save(myOrder);
	
		return"order.toString()";
	}
	
	
	
	
}

