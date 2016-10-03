package me.lucko.luckperms.standalone.util;

public enum ColorCodes {

	BLACK('0'),
	DARK_BLUE('1'),
	DARK_GREEN('2'),
	DARK_AQUA('3'),
	DARK_RED('4'),
	DARK_PURPLE('5'),
	GOLD('6'),
	GRAY('7'),
	DARK_GRAY('8'),
	BLUE('9'),
	GREEN('a'),
	AQUA('b'),
	RED('c'),
	LIGHT_PURPLE('d'),
	YELLOW('e'),
	WHITE('f');

	public final char identifier;

	ColorCodes(char identifier) {
		this.identifier = identifier;
	}

	public static ColorCodes getCode(String code) {
		return getCode(code.charAt(code.length() - 1));
	}

	public static ColorCodes getCode(char c) {
		for (ColorCodes cc : ColorCodes.values()) {
			if (cc.identifier == c) {
				return cc;
			}
		}
		return BLACK;
	}

	public java.awt.Color getAWTColor() {
		switch (this) {
		case BLACK:
			return new java.awt.Color(0, 0, 0);
		case DARK_BLUE:
			return new java.awt.Color(0, 0, 170);
		case DARK_GREEN:
			return new java.awt.Color(0, 170, 0);
		case DARK_AQUA:
			return new java.awt.Color(0, 170, 170);
		case DARK_RED:
			return new java.awt.Color(170, 0, 0);
		case DARK_PURPLE:
			return new java.awt.Color(170, 0, 170);
		case GOLD:
			return new java.awt.Color(255, 170, 0);
		case GRAY:
			return new java.awt.Color(170, 170, 170);
		case DARK_GRAY:
			return new java.awt.Color(85, 85, 85);
		case BLUE:
			return new java.awt.Color(85, 85, 255);
		case GREEN:
			return new java.awt.Color(85, 255, 85);
		case AQUA:
			return new java.awt.Color(85, 255, 255);
		case RED:
			return new java.awt.Color(255, 85, 85);
		case LIGHT_PURPLE:
			return new java.awt.Color(255, 85, 255);
		case YELLOW:
			return new java.awt.Color(255, 255, 85);
		case WHITE:
			return new java.awt.Color(255, 255, 255);
		}
		return new java.awt.Color(0);
	}

	public javafx.scene.paint.Color getFXColor() {
		double a = 170D / 255D;
		double b = 85D / 255D;
		switch (this) {
		case BLACK:
			return new javafx.scene.paint.Color(0, 0, 0, 1);
		case DARK_BLUE:
			return new javafx.scene.paint.Color(0, 0, a, 1);
		case DARK_GREEN:
			return new javafx.scene.paint.Color(0, a, 0, 1);
		case DARK_AQUA:
			return new javafx.scene.paint.Color(0, a, a, 1);
		case DARK_RED:
			return new javafx.scene.paint.Color(a, 0, 0, 1);
		case DARK_PURPLE:
			return new javafx.scene.paint.Color(a, 0, a, 1);
		case GOLD:
			return new javafx.scene.paint.Color(1, a, 0, 1);
		case GRAY:
			return new javafx.scene.paint.Color(a, a, a, 1);
		case DARK_GRAY:
			return new javafx.scene.paint.Color(b, b, b, 1);
		case BLUE:
			return new javafx.scene.paint.Color(b, b, 1, 1);
		case GREEN:
			return new javafx.scene.paint.Color(b, 1, b, 1);
		case AQUA:
			return new javafx.scene.paint.Color(b, 1, 1, 1);
		case RED:
			return new javafx.scene.paint.Color(1, b, b, 1);
		case LIGHT_PURPLE:
			return new javafx.scene.paint.Color(1, b, 1, 1);
		case YELLOW:
			return new javafx.scene.paint.Color(1, 1, b, 1);
		case WHITE:
			return new javafx.scene.paint.Color(1, 1, 1, 1);
		}
		return new javafx.scene.paint.Color(0, 0, 0, 1);
	}

}
