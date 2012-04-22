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
	def getTestUser() {
		return new User(userId: 'joe', password: 'secret', homepage: 'http://grailsinaction.com')
	}
	
	
}
