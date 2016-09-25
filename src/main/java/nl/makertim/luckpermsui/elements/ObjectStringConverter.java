package nl.makertim.luckpermsui.elements;

import java.util.Collection;

import javafx.util.StringConverter;

public abstract class ObjectStringConverter<T> extends StringConverter<T> {

	private Collection<T> list;

	public ObjectStringConverter(Collection<T> list) {
		this.list = list;
	}

	@Override
	public T fromString(String string) {
		for (T obj : list) {
			if (toString(obj).equals(string)) {
				return obj;
			}
		}
		return null;
	}

}
