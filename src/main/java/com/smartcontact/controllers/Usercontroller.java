package com.smartcontact.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smartcontact.dao.Contactrepository;
import com.smartcontact.dao.Userrepository;
import com.smartcontact.entities.Contact;
import com.smartcontact.entities.User;
import com.smartcontact.helper.Message;

@Controller
@RequestMapping("/user")
public class Usercontroller {
@Autowired
	private Userrepository userrepository;

@Autowired
private Contactrepository contactrepository;

@ModelAttribute
public void commondata(Model model,Principal principal)
{
	String name = principal.getName();

	
	User user = userrepository.getUserByUserName(name);
	model.addAttribute("user", user);
	
	
	




}

	@RequestMapping("/index")
	public String dashboard(Model model,Principal principal)
	{
		
		
		return "User/dashboard";
	}
	
	
	
	@RequestMapping("/addcontacts")
	public String Addcontacts(Model model)
	{
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact",new Contact());
		
		
		return "User/addcontacts";
		
	}
	
	
	@PostMapping("/processcontact")
	public String processcontact(@ModelAttribute Contact contact,@RequestParam("profileimage")MultipartFile file, Principal principal, HttpSession session )
	{
		
		try
		{
		String name = principal.getName();
		User user = userrepository.getUserByUserName(name);
		
		//uploading file
		if(file.isEmpty())
		{
			System.out.println("File is empty");
			
		}
		else
		{
			//upload file to folder
			contact.setImageurl(file.getOriginalFilename());
			File files = new ClassPathResource("static/img").getFile();
			
			Path fpath = Paths.get(files.getAbsolutePath()+File.separator+file.getOriginalFilename());
			
			
			Files.copy(file.getInputStream(),fpath , StandardCopyOption.REPLACE_EXISTING);
			
			
		}
		
		
		contact.setUser(user);
		
		user.getContacts().add(contact);
		
		
		
		userrepository.save(user);
		
		System.out.println("Added to database");
		
		//message success
		
		session.setAttribute("message", new Message("Your contact is added" , "success"));
		}
		
		catch(Exception e)
		{
			
			System.out.println("error" + e.getMessage());
			e.printStackTrace();
			session.setAttribute("message", new Message("Your contact is not added" , "danger"));	
			//error message
		}
		
		
		return "User/addcontacts";
	}

	@RequestMapping("/show-contacts/{page}")
	public String show(@PathVariable("page") Integer page,Model model,Principal principal)
	{
		model.addAttribute("title", "Show user Contacts");
		String name = principal.getName();
		User user = userrepository.getUserByUserName(name);
		
		
		Pageable pageable = PageRequest.of(page, 2);
		Page<Contact> contacts = contactrepository.findContactsByUser(user.getId(),pageable);
		model.addAttribute("contacts",contacts);
		model.addAttribute("currentpage", page);
		model.addAttribute("total", contacts.getTotalPages());
		

		return "User/show_contacts";
	}
	
	
	
	
	//showing contact detail
	
	@RequestMapping("/contact/{cid}")
	public String showparticular(Model model, @PathVariable("cid") Integer cid,Principal principal)
	{
		
		model.addAttribute("title", "Contact details");
		
		Optional<Contact> contact = contactrepository.findById(cid);
		Contact contacts = contact.get();
		String name = principal.getName();
		User user = userrepository.getUserByUserName(name);
		
		if(user.getId()==contacts.getUser().getId())
		{
		model.addAttribute("contact",contacts);
		}
		
		return "User/contactdetails";
		
	}
	
	
	
	@GetMapping("/delete/{cid}")
	public String delete(@PathVariable("cid") Integer cid,Model model,Principal principal,HttpSession session)
	{
		
		Optional<Contact> optional = contactrepository.findById(cid);
		Contact contact = optional.get();
		String name = principal.getName();
		
		User user = userrepository.getUserByUserName(name);

		
		
		if(user.getId()==contact.getUser().getId())
		{
			
			
		contactrepository.delete(contact);
		
		}
		
		session.setAttribute("message", new Message("Contact deleted Successfully","success"));
		
		
		
		
		
		
		return "redirect:/user/show-contacts/0";
		
		
	}
	
	
	
	//update
@PostMapping("/update-contact/{cid}")
	public String update(Model model,@PathVariable("cid") Integer cid)
	{
	model.addAttribute("title", "Update contact");
	
	Contact contact = contactrepository.findById(cid).get();
	model.addAttribute("contact", contact);
		return "User/update_form";
		
	}




@RequestMapping(value = "/process-update",method = RequestMethod.POST)
public String updateview(@ModelAttribute Contact contact,@RequestParam("profileimage") MultipartFile file,Model model,Principal principal)
{
	try
	{
		
		//old contact detail
		
		Contact old = contactrepository.findById(contact.getCid()).get();
		
		if(!file.isEmpty())
		{
			
			//delete
			
File delete = new ClassPathResource("static/img").getFile();
		File file1=new File(delete,old.getImageurl());
		file1.delete();
		
			
			
			//update
			
File files = new ClassPathResource("static/img").getFile();
			
			Path fpath = Paths.get(files.getAbsolutePath()+File.separator+file.getOriginalFilename());
			
			
			Files.copy(file.getInputStream(),fpath , StandardCopyOption.REPLACE_EXISTING);
			contact.setImageurl(file.getOriginalFilename());
			
			
			
		}
		else
		{
			
		contact.setImageurl(old.getImageurl());	
			
		}
		
		
		User name = userrepository.getUserByUserName(principal.getName());
		contact.setUser(name);
		contactrepository.save(contact);
		
		
	}
	
	catch(Exception e)
	
	{
		e.printStackTrace();
	}
	
	return "redirect:/user/contact/"+contact.getCid();
}




@GetMapping("/profile")
public String profile(Model model)
{
	
	model.addAttribute("title", "Profile page");
	return "User/profile";
	
}
	
	
	
	
	
	
}
