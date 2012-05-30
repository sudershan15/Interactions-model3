package p3.common;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppProps {

	private static AppProps theApplicationProps;
	private static String filename = null;
	private Properties props = new Properties();


	private AppProps() {
		loadFile();
	}

	public static AppProps getInstance(String fname) {
		filename = fname;
		theApplicationProps = null;
		return getInstance();
	}

	public static AppProps getInstance() {
		if (theApplicationProps == null) {
			theApplicationProps = new AppProps();
		}
		return theApplicationProps;
	}

	public Object clone() throws CloneNotSupportedException {
		// this is a singleton, cannot be cloned!
		throw new CloneNotSupportedException();
	}

	public void loadFile() {
		FileInputStream in = null;
		try {
			in = new FileInputStream(filename);
			props.load(in);
		}
		catch (Exception e) {
			System.out.println("Cannot find " + filename);
		}
		finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					System.out.println("Error closing input stream for: " + filename);
				}
			}
		}
	}

	public String getStringValue(String key) {
		return props.getProperty (key);
	}

	public Long getLongValue(String key) {
		return Long.parseLong(props.getProperty(key));
	}

	public Integer getIntegerValue(String key) {
		return Integer.parseInt(props.getProperty(key));
	}
}
