package com.smartcontact.controllers;

import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import com.smartcontact.dao.Userrepository;
import com.smartcontact.entities.User;
import com.smartcontact.helper.Message;

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private Userrepository us;
	
@RequestMapping("/")	
public String home(Model model)
{
	
	model.addAttribute("title", "Home-Smart Contact Manager");
	
	
	return "home";
}
	


@RequestMapping("/signup")
public String signup(Model model)
{
	
	
	model.addAttribute("title", "signup- Smart Contact Manager");
	model.addAttribute("user", new User());
	
	
	return "signup";
	
}


@RequestMapping(value="do_register", method = RequestMethod.POST)
public String registeruser( @ModelAttribute("user") User user, @RequestParam(value = "agreement",defaultValue = "false") boolean agreement, Model model, HttpSession session)
{
	try {
		
		
		if(!agreement)
		{
			System.out.println("You have not agreed to terms");
			throw new Exception("You have not agreed to terms");
			
			
		}
		
		
		
		
		
		
		user.setRole("ROLE_USER");
		user.setEnabled(true);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		
		User u=us.save(user);
		model.addAttribute("user", new User());
		
	    session.setAttribute("message", new Message("Successfully registered", "alert-success"));
		
		return "signup";
		
		
	} catch (Exception e) {
		
		
		e.printStackTrace();
		model.addAttribute("user",user);
		session.setAttribute("message", new Message("Something went wrong" + e.getMessage(),"alert-danger"));
		return "signup";
	}
	
	
	
	

	
	
}

@RequestMapping("/signin")
public String customlogin(Model model)
{
	
	model.addAttribute("title", "Login-Smart Contact Manager");
	
	return "login";
}




}
