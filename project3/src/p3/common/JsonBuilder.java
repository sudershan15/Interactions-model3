package p3.common;

import java.util.HashMap;

import org.codehaus.jackson.map.ObjectMapper;


/**
 * construct data representations using JSON
 * 
 * @author Sudershan
 * 
 */
public class JsonBuilder {
	public static String encode(HashMap<String, String> data) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(data);
		} catch (Exception ex) {
			return null;
		}
	}

	public static String encode(Object data) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(data);
		} catch (Exception ex) {
			return null;
		}
	}

	public static <T> T decode(String data, Class<T> theClass) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(data.getBytes(), theClass);
		} catch (Exception ex) {
			return null;
		}
	}
}
