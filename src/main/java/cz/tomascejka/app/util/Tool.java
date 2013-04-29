package cz.tomascejka.app.util;

import java.io.Closeable;
import java.io.IOException;

import cz.tomascejka.app.storage.ConsoleException;

public final class Tool {

	private Tool () {
		super();
	}
	
	public static void close(final Closeable ...streams) {
		if(streams.length > 0) {
			for (int i = 0; i < streams.length; i++) {
				try {
					if(streams[i] != null) {
						streams[i].close();
					}
				} catch (IOException e) {
					throw new ConsoleException("Stream cannot be closed", e);
				}				
			}
		}		
	}
	
}
