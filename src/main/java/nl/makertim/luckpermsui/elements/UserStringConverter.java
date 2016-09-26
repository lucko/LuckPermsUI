package nl.makertim.luckpermsui.elements;

import java.util.Collection;

import nl.makertim.luckpermsui.internal.User;

public class UserStringConverter extends ObjectStringConverter<User> {

	public UserStringConverter(Collection<User> list) {
		super(list);
	}

	@Override
	public String toString(User user) {
		return String.format("[%s]\t\t(%s)\t%s", user.getUuid(), user.getDefaultGroup(), user.getName());
	}
}
