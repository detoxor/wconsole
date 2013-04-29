package cz.tomascejka.app.storage.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cz.tomascejka.app.domain.User;
import cz.tomascejka.app.security.PasswordManager;
import cz.tomascejka.app.storage.ConsoleException;
import cz.tomascejka.app.storage.DataAccessFailException;
import cz.tomascejka.app.storage.DataNotFoundException;
import cz.tomascejka.app.storage.DataNotUniqueException;
import cz.tomascejka.app.storage.Repository;
import cz.tomascejka.app.util.Tool;
/**
 * Implementation of text file repository. File can be delete if data storage have to be delete.
 * @author tomascejka
 */
public class UserFileRepository implements Repository<User> {
	
	private static final String SAVE_FAIL = "File corrupted - user cannot be saved";
	private static final String READ_FAIL = "File corrupted - file cannot be readed";
	private static final String FILE_FOUND_FAIL = "File is not founded";
	private static final String SEPARATOR = "|";
	private static final String REGEX_SPLIT = "\\"+SEPARATOR;
	private final StringBuilder sbLine;
	private final String filePath;
	private Map<String, String> cache;
	private Random random;
	/**
	 * @param filePath to file which is used to persist users
	 */
	public UserFileRepository(final String filePath) {
		this.filePath = filePath;
		this.cache = new HashMap<String,String>();
		this.sbLine = new StringBuilder();
		this.random = new Random();
		BufferedReader reader = null;
		
		//init cache and lastId
		try {
			final File dataSource = new File(filePath);
			if(!dataSource.exists()) {
				dataSource.createNewFile();
			}			
			reader = new BufferedReader(new FileReader(dataSource));
			String line;
			while((line = reader.readLine()) != null) {  // NOPMD tomascejka on 26.4.13 14:56
				final String[] items = line.split(REGEX_SPLIT,3);
				cache.put(items[0], items[1]);
			}
		} catch (FileNotFoundException e) {
			throw new ConsoleException(FILE_FOUND_FAIL, e);
		} catch (IOException e) {
			throw new ConsoleException(READ_FAIL, e);
		} finally {
			Tool.close(reader);			
		}		
	}
	/** 
	 * Persist given user to specific file
	 * @param user entity to persist to file
	 * @throws DataNotUniqueException if username is not unique
	 * @throws ConsoleException if persist process to file fails
	 * @return true if user is successfully persisted to file
	 */
	public void add(final User user) throws DataNotUniqueException, DataAccessFailException {
		BufferedWriter writer = null;
		final String userName = user.getUsername();
		try {
			// check unique
			if(cache.containsValue(userName)) {
				throw new DataNotUniqueException("Username "+userName+" is not unique!");
			}
			int id = random.nextInt(10000);//tohle je pouze v ramci prikladu v produkci sofistikovanejsi reseni
			cache.put(String.valueOf(id), userName);
			//encrypt password
			final String encrypted = PasswordManager.encrypt(user.getPassword());
			sbLine.setLength(0);
			sbLine.append(id).append(SEPARATOR).append(userName).append(SEPARATOR).append(encrypted);
			//write result to storage
			writer = new BufferedWriter(new FileWriter(filePath, true));
			writer.write(sbLine.toString());
			writer.newLine();
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
	 * @throws DataNotFoundException if user is not founded
	 * @return true if user is correctly deleted from file
	 * @throws DataAccessFailException 
	 */
	public void delete(final String userName) throws DataNotFoundException, DataAccessFailException {
		if(!cache.containsValue(userName)) {
			throw new DataNotFoundException("User with username "+userName+" not exist in file!\n");
		}
		
		final File dataSource = new File(filePath);
		if(!dataSource.exists()) {
			throw new DataAccessFailException("File is not found");
		}
		BufferedReader reader = null;
		BufferedWriter writer = null;
		File temporary = null;
		try {
			HashMap<String, String> tempCache = new HashMap<String, String>();
			temporary = File.createTempFile("temporary", ".txt");
			reader = new BufferedReader(new FileReader(dataSource));
			writer = new BufferedWriter(new FileWriter(temporary));
			String line;
			while((line = reader.readLine()) != null) {  // NOPMD tomascejka tocecz on 26.4.13 14:55
				final String[] items = line.split(REGEX_SPLIT,3);
				if(!items[1].equals(userName))  {
					writer.write(line);
					writer.newLine();
					tempCache.put(items[0], items[1]);
				}
			}
			//safety invalidation of cache
			cache = tempCache;
		} catch (FileNotFoundException e) {
			throw new ConsoleException(FILE_FOUND_FAIL, e);
		} catch (IOException e) {
			throw new ConsoleException(READ_FAIL, e);
		} finally {
			Tool.close(reader, writer);			
		}
		final File old = new File(filePath);
		if(old.delete() && temporary != null) {
			temporary.renameTo(old);
		}
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
			final File dataSource = new File(filePath);
			reader = new BufferedReader(new FileReader(dataSource));
			String line;
			while((line = reader.readLine()) != null) { // NOPMD by tomascejka on 26.4.13 14:55
				final String[] items = line.split(REGEX_SPLIT,3);
				final User user = new User(Integer.valueOf(items[0]), items[1], items[2].toCharArray());
				users.add(user);
			}
		} catch (FileNotFoundException e) {
			throw new ConsoleException(FILE_FOUND_FAIL, e);
		} catch (IOException e) {
			throw new ConsoleException(READ_FAIL, e);
		} finally {
			Tool.close(reader);
		}
		return users;
	}
	/** 
	 * Find user by username - username is unique (only one result is returned).
	 * @throws IndexOutOfBoundsException if line is not in consistent format id|username|password
	 * @throws DataNotFoundException if user is not founded
	 * @return user (can return NullUser - if user is not founded) domain object
	 */
	public User find(final String username) throws DataNotFoundException {
		if(!cache.containsValue(username)) {
			throw new DataNotFoundException("Username "+username+" is not exist in file!\n");
		}
		BufferedReader reader = null;
		User user = null;
		try {
			final File dataSource = new File(filePath);
			if(!dataSource.exists()) {
				dataSource.createNewFile();
			}
			reader = new BufferedReader(new FileReader(dataSource));
			String line;
			while((line = reader.readLine()) != null) { // NOPMD by tomascejka on 26.4.13 14:55
				final String[] items = line.split("\\"+SEPARATOR,3);
				if(items[1].equals(username)) {
					final String decrypt = PasswordManager.decrypt(items[2]);					
					user = new User(Integer.valueOf(items[0]), items[1], decrypt.toCharArray());
					break;
				}
			}
		} catch (FileNotFoundException e) {
			throw new ConsoleException(FILE_FOUND_FAIL, e);
		} catch (IOException e) {
			throw new ConsoleException(READ_FAIL, e);
		} finally {
			Tool.close(reader);
		}
		if(user == null) {
			throw new DataNotFoundException("Username "+username+" is not exist!");
		}
		return user;
	}
	/**
	 * Delete file
	 */
	public void deleteAll() {
		cache.clear();
		new File(filePath).delete();
	}
}
