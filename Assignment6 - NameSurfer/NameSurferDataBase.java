import java.io.*;
import java.util.*;

/*
 * File: NameSurferDataBase.java
 * -----------------------------
 * This class keeps track of the complete database of names.
 * The constructor reads in the database from a file, and
 * the only public method makes it possible to look up a
 * name and get back the corresponding NameSurferEntry.
 * Names are matched independent of case, so that "Eric"
 * and "ERIC" are the same names.
 */

public class NameSurferDataBase implements NameSurferConstants {
	
	private Map<String, String> dataBase = new HashMap<>();
	
/* Constructor: NameSurferDataBase(filename) */
/**
 * Creates a new NameSurferDataBase and initializes it using the
 * data in the specified file.  The constructor threw an error
 * exception if the requested file does not exist or if an error
 * occurs as the file is being read, so I surrounded it with try/catch.
 */
	public NameSurferDataBase(String filename) {
		try {
			BufferedReader rd = new BufferedReader(new FileReader(filename));
			while(true){
				String line = rd.readLine();
				if(line == null){
					break;
				}
				String name = getName(line);
				dataBase.put(name, line);
			}
			
			rd.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
/* Method: findEntry(name) */
/**
 * Returns the NameSurferEntry associated with this name, if one
 * exists.  If the name does not appear in the database, this
 * method returns null.
 */
	public NameSurferEntry findEntry(String name) {
		if(dataBase.containsKey(name)){
			NameSurferEntry entry = new NameSurferEntry(dataBase.get(name));
			return entry;
		}
		
		return null;
	}
	
	
/** return name from the given line*/
	private String getName(String str){
		StringTokenizer st = new StringTokenizer(str);
		return st.nextToken();
	}
	
}


