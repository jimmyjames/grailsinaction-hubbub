package com.grailsinaction

class UserIntegrationTests extends GroovyTestCase {

    void testFirstSaveEver() {
		def user = getTestUser()
		assertNotNull user.save()
		assertNotNull user.id
		
		def foundUser = User.get(user.id)
		assertNotNull 'joe', foundUser.userId
    }

	void testSaveAndUpdate() {
		def user = getTestUser()
		assertNotNull user.save()
		
		def foundUser = User.get(user.id)
		foundUser.password = 'sesame'
		assertEquals 'sesame', foundUser.password
		
		// now persist change
		foundUser.save()
		def editedUser = User.get(user.id)
		assertEquals 'sesame', editedUser.password
	
	}
	
	void testSaveThenDelete() {
		def user = getTestUser()
		assertNotNull user.save()
		
		def foundUser = User.get(user.id)
		foundUser.delete()
		
		assertFalse User.exists(foundUser.id)
	}
	
	void testEvilSave() {
		def user = new User(userId: 'chuck_norris', password:'tiny', homepage:'not-a-url')
		assertFalse user.validate()
		assertTrue user.hasErrors()
		
		def errors = user.errors
		
		assertEquals "size.toosmall", errors.getFieldError("password").code
		assertEquals "tiny", errors.getFieldError("password").rejectedValue
		
		assertEquals "url.invalid", errors.getFieldError("homepage").code
		assertEquals "not-a-url", errors.getFieldError("homepage").rejectedValue
		
		assertNull errors.getFieldError("userId")
	}
	
	void testUserIdSameAsPassword() {
		def user = new User(userId: 'someuserid', password: 'someuserid', homepage:'http://google.com')
		assertFalse user.validate()
		assertTrue user.hasErrors()
		
		def errors = user.errors
		
		assertEquals "validator.invalid", errors.getFieldError("password").code
		assertEquals "someuserid", errors.getFieldError("password").rejectedValue
	}
	
	void testFollowing() {
		def glen = new User(userId: 'glen', password:'password').save()
		def peter = new User(userId: 'peter', password:'password').save()
		def sven = new User(userId: 'sven', password: 'password').save()
		
		glen.addToFollowing(peter)
		glen.addToFollowing(sven)
		assertEquals "glen should be following two people", 2, glen.following.size()
		
		sven.addToFollowing(peter)
		assertEquals "sven should be following one person", 1, sven.following.size()
		
		assertNull "peter should be following no people", peter.following
	}
	
	def getTestUser() {
		return new User(userId: 'joe', password: 'secret', homepage: 'http://grailsinaction.com')
	}	
}
