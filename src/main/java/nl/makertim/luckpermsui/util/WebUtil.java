package nl.makertim.luckpermsui.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

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

	public static String getPost(URL url, String post) {
		return getResponseWith(url, "");
	}

	public static String getPostWith(URL url, String post, char newLineChar) {
		return getPostWith(url, post, Character.toString(newLineChar));
	}

	public static String getPostWith(URL url, String post, String newLineString) {
		String ret = "";
		for (String responseLine : getPostLines(url, post)) {
			ret += responseLine + newLineString;
		}
		return ret;
	}

	public static String[] getPostLines(URL url, String post) {
		return getPostLines(url, post, c -> {
		});
	}

	public static String[] getPostLines(URL url, String post, Consumer<HttpURLConnection> customContent) {
		List<String> response = new ArrayList<>();
		try {
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			customContent.accept(con);
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(post);
			wr.flush();
			wr.close();
			if (!Integer.toString(con.getResponseCode()).startsWith("2")) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					response.add(inputLine);
				}
				throw new Exception(url.toString() + " responded with " + con.getResponseCode() + "\n" + response);
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.add(inputLine);
			}
			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return response.toArray(new String[response.size()]);
	}
}
