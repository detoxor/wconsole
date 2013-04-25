package cz.tomascejka.data;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cz.tomascejka.app.domain.User;
import cz.tomascejka.app.storage.DataNotUniqueException;
import cz.tomascejka.app.storage.UserFileRepository;

public class UserFileRepositoryTest {

	private static final String filePath = "src/test/resources/users.txt";
	private UserFileRepository testObject;
	private User user = new User("RedDwarf", "SomethingVerySecret".toCharArray());
	
	@Before
	public void setUp() throws Exception {
		testObject = new UserFileRepository(filePath);
		testObject.deleteAll();
	}

	@Test
	public void testAdd() {
		testObject.add(user);
		User foo = testObject.find(user.getUsername());
		Assert.assertNotNull("User must be founded", foo.getId());
	}
	
	@Test(expected=DataNotUniqueException.class)
	public void testAddFail() {
		testObject.add(user);
		testObject.add(user);
	}	
	
	@Test
	public void testList() {
		testObject.add(user);
		testObject.add(new User("Ninja", "HaoNinpoCho".toCharArray()));
		testObject.add(new User("Judoga", "JigoroKano".toCharArray()));
		testObject.add(new User("Karate", "GichinFunakoshi".toCharArray()));
		
		List<User> users = testObject.list();
		Assert.assertNotNull("Returned list cannot be null", users);
		Assert.assertFalse("Returned list cannot be empty", users.isEmpty());
		Assert.assertEquals(4, users.size());
	}

	@Test
	public void testDelete() {
		testObject.add(user);
		testObject.add(new User("Ninja", "HaoNinpoCho".toCharArray()));
		testObject.add(new User("Judoga", "JigoroKano".toCharArray()));
		testObject.add(new User("Karate", "GichinFunakoshi".toCharArray()));
		
		String userName = user.getUsername();
		testObject.delete(userName);
		List<User> users = testObject.list();
		Assert.assertEquals(3, users.size());
		Assert.assertNull(testObject.find(userName).getId());
	}
	
	@Test(expected=DataNotUniqueException.class)
	public void testDeleteFail() {
		testObject.add(user);
		testObject.delete("Kraken");
	}	
}
