package cz.tomascejka.app.storage.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.tomascejka.app.domain.NullUser;
import cz.tomascejka.app.domain.User;
import cz.tomascejka.app.security.PasswordManager;
import cz.tomascejka.app.storage.ConsoleException;
import cz.tomascejka.app.storage.DataNotUniqueException;
import cz.tomascejka.app.storage.Repository;
import cz.tomascejka.app.util.Tool;
/**
 * Implementation of text file repository. File can be delete if data storage have to be delete.
 * @author tomascejka
 */
public class UserFileRepository implements Repository<User> {
	
	private static final String SAVE_FAIL = "File corrupted - user cannot be saved";
	private static final String FAIL_LOAD = "File corrupted - users cannot be loaded";
	private static final String FAIL_READ = "File corrupted - file cannot be readed";
	private static final String SEPARATOR = "|";
	private static final String REGEX_SPLIT = "\\"+SEPARATOR;
	private Integer lastId = 0;
	private final StringBuilder sb;
	private final String filePath;
	private final List<String> cache;
	/**
	 * @param filePath to file which is used to persist users
	 */
	public UserFileRepository(final String filePath) {
		this.filePath = filePath;
		this.cache = new ArrayList<String>();
		this.sb = new StringBuilder();
	}
	/** 
	 * Persist given user to specific file
	 * @param user entity to persist to file
	 * @throws DataNotUniqueException if username is not unique
	 * @throws ConsoleException if persist process to file fails
	 * @return true if user is successfully persisted to file
	 */
	public boolean add(final User user) throws DataNotUniqueException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(filePath, true));
			// check unique
			final String userName = user.getUsername();
			if(cache.contains(userName)) {
				throw new DataNotUniqueException("Username "+userName+" is not unique!");
			}
			cache.add(userName);
			//encrypt password
			final String encrypted = PasswordManager.encrypt(user.getPassword());
			sb.setLength(0);
			sb.append(++lastId).append(SEPARATOR).append(userName).append(SEPARATOR).append(encrypted);
			//write result to storage
			writer.write(sb.toString());
			writer.newLine();
			return true;
		} catch (IOException e) {
			throw new ConsoleException(SAVE_FAIL, e);
		} finally {
			Tool.close(writer);
		}
	}
	/** 
	 * Safety approach to delete given user from is realized as copy others users to new file and old file is deleted.
	 * @param userName unique key to find and delete user 
	 * @throws IndexOutOfBoundsException if line is not in consistent format id|username|password)
	 * @return true if user is correctly deleted from file
	 */
	public boolean delete(final String userName) {
		if(!cache.contains(userName)) {
			throw new DataNotUniqueException("User with username "+userName+" not exist in file");
		}
		BufferedReader reader = null;
		BufferedWriter writer = null;
		File temp = null;
		try {
			temp = File.createTempFile("temporary", ".txt");
			reader = new BufferedReader(new FileReader(filePath));
			writer = new BufferedWriter(new FileWriter(temp));
			String line;
			while((line = reader.readLine()) != null) { 
				final String[] items = line.split(REGEX_SPLIT,3);
				if(!items[1].equals(userName)) {
					writer.write(line);
					writer.newLine();
				}
			}
		} catch (FileNotFoundException e) {
			throw new ConsoleException(FAIL_LOAD, e);
		} catch (IOException e) {
			throw new ConsoleException(FAIL_READ, e);
		} finally {
			Tool.close(reader, writer);			
		}
		final File old = new File(filePath);
		if(old.delete() && temp != null) {
			temp.renameTo(old);		
		}
		return true;
	}
	/** 
	 * Read each line in file and construct and fill domain object
	 * @throws IndexOutOfBoundsException if line is not in consistent format id|username|password
	 * @return list of user objects
	 */
	public List<User> list() {
		BufferedReader reader = null;
		final List<User> users = new ArrayList<User>();
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line;
			while((line = reader.readLine()) != null) {
				final String[] items = line.split(REGEX_SPLIT,3);
				final User user = new User(Integer.valueOf(items[0]), items[1], null);
				users.add(user);
			}
		} catch (FileNotFoundException e) {
			throw new ConsoleException(FAIL_LOAD, e);
		} catch (IOException e) {
			throw new ConsoleException(FAIL_READ, e);
		} finally {
			Tool.close(reader);
		}
		return users;
	}
	/** 
	 * Find user by username - username is unique (only one result is returned).
	 * @throws IndexOutOfBoundsException if line is not in consistent format id|username|password
	 * @return user (can return NullUser - if user is not founded) domain object
	 */
	public User find(final String username) {
		BufferedReader reader = null;
		User user = null;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line;
			while((line = reader.readLine()) != null) {
				final String[] items = line.split("\\"+SEPARATOR,3);
				if(items[1].equals(username)) {
					user = new User(Integer.valueOf(items[0]), items[1], null);
					break;
				}
			}
		} catch (FileNotFoundException e) {
			throw new ConsoleException(FAIL_LOAD, e);
		} catch (IOException e) {
			throw new ConsoleException(FAIL_READ, e);
		} finally {
			Tool.close(reader);
		}
		return user == null ? new NullUser() : user;
	}
	/**
	 * Delete file
	 */
	public void deleteAll() {
		new File(filePath).delete();
	}
}
