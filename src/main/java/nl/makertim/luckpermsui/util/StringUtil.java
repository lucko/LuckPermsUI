package nl.makertim.luckpermsui.util;

public class StringUtil {

	public static String readableEnum(Enum<?> e) {
		return readableVar(e.toString());
	}

	public static String readableVar(String javaName) {
		javaName = javaName.toString().toLowerCase().replaceAll("_", " ");
		String ret = "";
		boolean CASE = true;
		for (char c : javaName.toString().toCharArray()) {
			if (Character.isSpaceChar(c)) {
				ret += c;
				continue;
			}
			if (!Character.isLetter(c)) {
				CASE = true;
				continue;
			}
			if (CASE) {
				ret += Character.toUpperCase(c);
				CASE = false;
			} else {
				ret += Character.toLowerCase(c);
			}
		}
		return ret;
	}

	/**
	 * Converts the inserted value to a string with a readable time. This will
	 * only convert up to minutes and returns a string with the format
	 * <i>M:SS:mmm</i>. Note that the format of the minutes can be greater than
	 * 1 character, but the seconds and milliseconds will have leading zeros to
	 * fill the missing spaces.
	 *
	 * @param milliseconds
	 *            the ammount of miliseconds to be converted.
	 * @return a string containing <b>milliseconds</b> converted to a readable
	 *         time.
	 */
	public static String readableMiliseconds(long milliseconds) {
		long miliseconds = milliseconds % 1000;
		long seconds = (milliseconds / 1000) % 60;
		long minutes = (milliseconds / (1000 * 60)) % 60;
		return String.format("%d:%02d:%03d", minutes, seconds, miliseconds);
	}

	/**
	 * Repeats a single string the provided amount of time.<br>
	 * Inserting a value of <i>0</i> as the <b>times</b> parameter will return
	 * an empty string. Inserting a value of <i>null</i> as the <b>toRepeat</b>
	 * parameter will return a string with the given amnount of "null"
	 *
	 * @param times
	 *            the ammount of times the string will repeat.
	 * @param toRepeat
	 *            the string you want to repeat.
	 * @return the input stream <b>toRepeat</b>, repeated <b>times</b> ammount
	 *         of times.
	 */
	public static String repeat(int times, String toRepeat) {
		String str = "";
		for (int i = 0; i < times; i++) {
			str += toRepeat;
		}
		return str;
	}

	/**
	 * Reads the string and outputs a boolean if a match has been found.<br>
	 * Please note that UUID consist only of numbers and lowercase characters
	 * and will be matched by the following pattern:<br>
	 * <br>
	 * &nbsp;
	 * <i>/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}/</i>
	 *
	 * @param string
	 *            the string you want to check.
	 * @return a boolean indicating if the string provided matches the UUID
	 *         pattern.
	 */
	public static boolean isUUID(String string) {
		return string.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
	}

	/**
	 * Reads the string inserted and tries to parse it to double to check it for
	 * being a double.
	 *
	 * @param number
	 *            the String you want to check for being a double.
	 * @return a boolean with the result if it is parsable to a double.
	 */
	public static boolean isDouble(String number) {
		boolean ret = false;
		try {
			Double.parseDouble(number);
			ret = true;
		} catch (Exception ex) {
			ret = false;
		}
		return ret;
	}

	/**
	 * Reads the String inserted and tries to parse it to integer to check it
	 * for being a integer.
	 *
	 * @param number
	 *            the String you want to check for being an integer.
	 * @return a boolean with the result if it is parsable to an integer.
	 */
	public static boolean isInt(String number) {
		boolean ret = false;
		try {
			Integer.parseInt(number);
			ret = true;
		} catch (Exception ex) {
			ret = false;
		}
		return ret;
	}
}
