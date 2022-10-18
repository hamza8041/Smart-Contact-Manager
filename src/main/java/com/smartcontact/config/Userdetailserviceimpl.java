package com.smartcontact.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smartcontact.dao.Userrepository;
import com.smartcontact.entities.User;

public class Userdetailserviceimpl implements UserDetailsService {

	@Autowired
	private Userrepository userrepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		User user = userrepository.getUserByUserName(username);
		if(user==null)
		{
			throw new UsernameNotFoundException("Could not found user");
			
		}
		Customuserdetail cud=new Customuserdetail(user);
		
		return cud;
	}

}
