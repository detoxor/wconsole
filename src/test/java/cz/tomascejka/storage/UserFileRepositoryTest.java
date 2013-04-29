package cz.tomascejka.storage;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cz.tomascejka.app.domain.User;
import cz.tomascejka.app.storage.ConsoleException;
import cz.tomascejka.app.storage.DataAccessFailException;
import cz.tomascejka.app.storage.DataNotFoundException;
import cz.tomascejka.app.storage.DataNotUniqueException;
import cz.tomascejka.app.storage.impl.UserFileRepository;

public class UserFileRepositoryTest {

	private static final String FILE_PATH = "src/test/resources/users.txt";
	private UserFileRepository testedObject;
	private final User user = new User("RedDwarf",
			"SomethingVerySecret".toCharArray());

	@Before
	public void setUp() throws Exception {
		testedObject = new UserFileRepository(FILE_PATH);
		testedObject.deleteAll();
	}

	@Test
	public void testAdd() {
		try {
			testedObject.add(user);
		} catch (DataAccessFailException e) {
			throw new ConsoleException("Fail test add user", e);
		}
		final User foo = testedObject.find(user.getUsername());
		// test result
		Assert.assertNotNull("User must be founded", foo.getId());
	}

	@Test(expected = DataNotUniqueException.class)
	public void testAddFail() {
		try {
			testedObject.add(user);
			testedObject.add(user);
			//test result is in expected excpetion in annotation
		} catch (DataAccessFailException e) {
			throw new ConsoleException("Fail test with add user", e);
		}
	}

	@Test
	public void testList() {
		try {
			testedObject.add(user);
			testedObject.add(new User("Ninja", "HaoNinpoCho".toCharArray()));
			testedObject.add(new User("Judoga", "JigoroKano".toCharArray()));
			testedObject.add(new User("Karate", "GichinFunakoshi".toCharArray()));
		} catch (DataAccessFailException e) {
			throw new ConsoleException("Fail test list users", e);
		}
		// test result
		final List<User> users = testedObject.list();
		Assert.assertNotNull("Returned list cannot be null", users);
		Assert.assertFalse("Returned list cannot be empty", users.isEmpty());
		Assert.assertEquals(4, users.size());
	}

	@Test
	public void testDelete() {
		try {
			testedObject.add(user);
			testedObject.add(new User("Ninja", "HaoNinpoCho".toCharArray()));
			testedObject.add(new User("Judoga", "JigoroKano".toCharArray()));
			testedObject.add(new User("Karate", "GichinFunakoshi".toCharArray()));
		} catch (DataAccessFailException e) {
			throw new ConsoleException("Fail test delete user", e);
		}
		final String userName = user.getUsername();
		try {
			testedObject.delete(userName);
		} catch (DataAccessFailException e) {
			throw new ConsoleException("Fail test delete user", e);
		}
		// test result
		final List<User> users = testedObject.list();
		Assert.assertEquals(3, users.size());
	}

	@Test(expected = DataNotFoundException.class)
	public void testDeleteFail() {
		try {
			testedObject.add(user);
			//test result is in expected excpetion in annotation
		} catch (DataAccessFailException e) {
			throw new ConsoleException("Fail test delete user", e);
		}
		try {
			testedObject.delete("Kraken");
		} catch (DataAccessFailException e) {
			throw new ConsoleException("Fail test delete user", e);
		}
	}
	
	@Test
	public void testFind(){
		try {
			testedObject.add(user);
			//test result is in expected excpetion in annotation
		} catch (DataAccessFailException e) {
			throw new ConsoleException("Fail test find user", e);
		}
		final User userFoo = testedObject.find(user.getUsername());
		Assert.assertNotNull(userFoo);
	}
}
