package cz.tomascejka.storage;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cz.tomascejka.app.domain.NullUser;
import cz.tomascejka.app.domain.User;
import cz.tomascejka.app.storage.DataNotUniqueException;
import cz.tomascejka.app.storage.impl.UserFileRepository;

public class UserFileRepositoryTest {

	private static final String FILE_PATH = "src/test/resources/users.txt";
	private UserFileRepository testedObject;
	private final User user = new User("RedDwarf", "SomethingVerySecret".toCharArray());
	
	@Before
	public void setUp() throws Exception {
		testedObject = new UserFileRepository(FILE_PATH);
		testedObject.deleteAll();
	}

	@Test
	public void testAdd() {
		testedObject.add(user);
		final User foo = testedObject.find(user.getUsername());
		//test result
		Assert.assertNotNull("User must be founded", foo.getId());
	}
	
	@Test(expected=DataNotUniqueException.class)
	public void testAddFail() {
		testedObject.add(user);
		testedObject.add(user);
	}	
	
	@Test
	public void testList() {
		testedObject.add(user);
		testedObject.add(new User("Ninja", "HaoNinpoCho".toCharArray()));
		testedObject.add(new User("Judoga", "JigoroKano".toCharArray()));
		testedObject.add(new User("Karate", "GichinFunakoshi".toCharArray()));
		//test result
		final List<User> users = testedObject.list();
		Assert.assertNotNull("Returned list cannot be null", users);
		Assert.assertFalse("Returned list cannot be empty", users.isEmpty());
		Assert.assertEquals(4, users.size());
	}

	@Test
	public void testDelete() {
		testedObject.add(user);
		testedObject.add(new User("Ninja", "HaoNinpoCho".toCharArray()));
		testedObject.add(new User("Judoga", "JigoroKano".toCharArray()));
		testedObject.add(new User("Karate", "GichinFunakoshi".toCharArray()));
		
		final String userName = user.getUsername();
		testedObject.delete(userName);
		
		//test result
		final List<User> users = testedObject.list();
		Assert.assertEquals(3, users.size());
		Assert.assertEquals(NullUser.NULL_ID, testedObject.find(userName).getId());
	}
	
	@Test(expected=DataNotUniqueException.class)
	public void testDeleteFail() {
		testedObject.add(user);
		testedObject.delete("Kraken");
	}	
}
