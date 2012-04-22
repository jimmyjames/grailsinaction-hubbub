package com.grailsinaction

class PostIntegrationTests extends GroovyTestCase {

    void testFirstPost() {
		def user = new User(userId: 'joe', password: 'secret').save()
		def post1 = new Post(content: "first post... w00t!")
		user.addToPosts(post1)
		def post2 = new Post(content: "second post")
		user.addToPosts(post2)
		def post3 = new Post(content: "third post")
		user.addToPosts(post3)
		assertEquals 3, User.get(user.id).posts.size()
    }

	void testAccessingPosts() {
		def user = new User(userId: 'joe', password: 'secret').save()
		user.addToPosts(new Post(content: "first post"))
		user.addToPosts(new Post(content: "second post"))
		user.addToPosts(new Post(content: "third post"))
		
		def foundUser = User.get(user.id)
		def postNames = foundUser.posts.collect{post ->
			post.content
		}
		assertEquals(["first post", "second post", "third post"], postNames.sort())
	}
	
	void testPostWithTags() {
		def user = new User(userId: 'joe', password: 'secret').save()
		
		def tagGroovy = new Tag(name:'groovy')
		def tagGrails = new Tag(name:'grails')
		user.addToTags(tagGroovy)
		user.addToTags(tagGrails)
		
		def tagNames = user.tags*.name
		assertEquals(['grails', 'groovy'], tagNames.sort())
		
		def groovyPost = new Post(content: 'a groovy post')
		user.addToPosts(groovyPost)
		groovyPost.addToTags(tagGroovy)
		assertEquals 1, groovyPost.tags.size()
		
		def bothPost = new Post(content: 'a groovy and grails post')
		user.addToPosts(bothPost)
		bothPost.addToTags(tagGroovy)
		bothPost.addToTags(tagGrails)
		assertEquals 2, bothPost.tags.size()
	}
}
