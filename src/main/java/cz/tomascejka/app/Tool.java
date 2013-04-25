package cz.tomascejka.app;

import java.io.Closeable;
import java.io.IOException;

public class Tool {

	public static final void close(Closeable ...streams) {
		if(streams.length > 0) {
			for (int i = 0; i < streams.length; i++) {
				try {
					streams[i].close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}				
			}
		}		
	}
	
}
