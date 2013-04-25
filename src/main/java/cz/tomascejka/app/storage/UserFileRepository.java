package cz.tomascejka.app.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.tomascejka.app.Tool;
import cz.tomascejka.app.domain.NullUser;
import cz.tomascejka.app.domain.User;
import cz.tomascejka.security.PasswordManager;
/**
 * Basic implemetation file repository
 * @author tomascejka
 */
public class UserFileRepository implements Repository<User> {

	private static final String SAVE_FAIL = "File corrupted - user cannot be saved";
	private static final String FAIL_LOAD = "File corrupted - users cannot be loaded";
	private static final String FAIL_READ = "File corrupted - file cannot be readed";
	private static final String SEPARATOR = "|";
	private static final String REGEX_SPLIT = "\\"+SEPARATOR;
	private Integer lastId = 0;
	private StringBuilder sb = new StringBuilder();
	private final String filePath;
	private List<String> cache = new ArrayList<String>();
	/**
	 * @param filePath to file which is used to persist users
	 */
	public UserFileRepository(String filePath) {
		this.filePath = filePath;
	}
	/** 
	 * Persist given user to specific file
	 * @throws DataNotUniqueException if username is not unique
	 * @throws DataException if persist process to file fails
	 * @param user entity to persist to file
	 * @return true if user is successfully persisted 
	 */
	public boolean add(User user) throws DataNotUniqueException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(filePath, true));
			// check unique
			String userName = user.getUsername();
			if(cache.contains(userName)) throw new DataNotUniqueException("Username "+userName+" is not unique!");
			cache.add(userName);
			//encrypt password
			String encrypted = PasswordManager.encrypt(user.getPassword());
			sb.setLength(0);
			sb.append(++lastId).append(SEPARATOR).append(userName).append(SEPARATOR).append(encrypted);
			//write resutl to storage
			writer.write(sb.toString());
			writer.newLine();
			return true;
		} catch (IOException e) {
			throw new DataException(SAVE_FAIL, e);
		} finally {
			Tool.close(writer);
		}
	}
	/** 
	 * Safety approach to delete given user from is realized as
	 * copy others users to new file and old file is deleted.
	 * @param userName unique key to find and delete user 
	 */
	public boolean delete(String userName) {
		if(!cache.contains(userName)) throw new DataNotUniqueException("User with username "+userName+" not exist in file");
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			File temp = File.createTempFile("temporary", ".txt");
			reader = new BufferedReader(new FileReader(filePath));
			writer = new BufferedWriter(new FileWriter(temp));
			String line;
			while((line = reader.readLine()) != null) {
				String[] items = line.split(REGEX_SPLIT,3);
				//indexoutofbound == corrupted file
				if(!items[1].equals(userName)) {
					writer.write(line);
				}
			}
			File old = new File(filePath);
			if(old.delete()) temp.renameTo(old);
			return true;
		} catch (FileNotFoundException e) {
			throw new DataException(FAIL_LOAD, e);
		} catch (IOException e) {
			throw new DataException(FAIL_READ, e);
		} finally {
			Tool.close(reader, writer);			
		}
	}
	/** 
	 * @inheritDoc
	 */
	public List<User> list() {
		BufferedReader reader = null;
		List<User> users = new ArrayList<User>();
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line;
			while((line = reader.readLine()) != null) {
				String[] items = line.split(REGEX_SPLIT,3);
				User user = new User(Integer.valueOf(items[0]), items[1], null);
				users.add(user);
			}
		} catch (FileNotFoundException e) {
			throw new DataException(FAIL_LOAD, e);
		} catch (IOException e) {
			throw new DataException(FAIL_READ, e);
		} finally {
			Tool.close(reader);
		}
		return users;
	}
	/** 
	 * @inheritDoc
	 */
	public User find(String username) {
		BufferedReader reader = null;
		User user = null;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line;
			while((line = reader.readLine()) != null) {
				String[] items = line.split("\\"+SEPARATOR,3);
				if(items[1].equals(username)) {//indexoutofbound == corrupted file
					user = new User(Integer.valueOf(items[0]), items[1], null);
					break;
				}
			}
		} catch (FileNotFoundException e) {
			throw new DataException(FAIL_LOAD, e);
		} catch (IOException e) {
			throw new DataException(FAIL_READ, e);
		} finally {
			Tool.close(reader);
		}
		return user == null ? new NullUser() : user;
	}

	public void deleteAll() {
		new File(filePath).delete();
	}
}
