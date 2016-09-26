package nl.makertim.luckpermsui.util;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WebUtil {

	public static String getResponse(URL url) {
		return getResponseWith(url, "");
	}

	public static String getResponseWith(URL url, char newLineChar) {
		return getResponseWith(url, Character.toString(newLineChar));
	}

	public static String getResponseWith(URL url, String newLineString) {
		String ret = "";
		for (String responseLine : getResponseLines(url)) {
			ret += responseLine + newLineString;
		}
		return ret;
	}

	public static String[] getResponseLines(URL url) {
		List<String> response = new ArrayList<>();
		try {
			InputStream is = url.openStream();
			Scanner in = new Scanner(is);
			while (in.hasNextLine()) {
				response.add(in.nextLine());
			}
			in.close();
			is.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return response.toArray(new String[response.size()]);
	}
}
