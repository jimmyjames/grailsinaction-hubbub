package com.grailsinaction

class User {
	String userId
	String password
	String homepage
	Date dateCreated
	
	static constraints = {
		userId(size:3..20, unique:true)
		password(size:6..8)
		homepage(url:true,nullable:true)
	}
}
