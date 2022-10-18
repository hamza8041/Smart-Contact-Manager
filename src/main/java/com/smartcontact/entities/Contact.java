package com.smartcontact.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="CONTACT")

public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
private int cid;

private String cname;
private String nickname;
private String work;

@Column(unique = true)
private String email;
private String phone;
private String imageurl;

@Column(length = 500)

private String description;

@ManyToOne
private User user;





public Contact() {
	super();
	// TODO Auto-generated constructor stub
}

public int getCid() {
	return cid;
}

public void setCid(int cid) {
	this.cid = cid;
}

public String getCname() {
	return cname;
}

public void setCname(String cname) {
	this.cname = cname;
}

public String getNickname() {
	return nickname;
}

public void setNickname(String nickname) {
	this.nickname = nickname;
}

public String getWork() {
	return work;
}

public void setWork(String work) {
	this.work = work;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public String getPhone() {
	return phone;
}

public void setPhone(String phone) {
	this.phone = phone;
}

public String getImageurl() {
	return imageurl;
}

public void setImageurl(String imageurl) {
	this.imageurl = imageurl;
}

public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
}

public User getUser() {
	return user;
}

public void setUser(User user) {
	this.user = user;
}

@Override
public String toString() {
	return "Contact [cid=" + cid + ", cname=" + cname + ", nickname=" + nickname + ", work=" + work + ", email=" + email
			+ ", phone=" + phone + ", imageurl=" + imageurl + ", description=" + description + ", user=" + user + "]";
}







	
}
