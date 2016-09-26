package nl.makertim.luckpermsui.elements;

import java.util.Collection;

import nl.makertim.luckpermsui.internal.Permission;

public class PermissionStringConverter extends ObjectStringConverter<Permission> {

	public PermissionStringConverter(Collection<Permission> list) {
		super(list);
	}

	@Override
	public String toString(Permission permission) {
		StringBuilder builder = new StringBuilder();
		if (permission.hasNode()) {
			builder.append(permission.getNode());
			builder.append("\t");
		}
		if (permission.hasServer()) {
			builder.append("[");
			builder.append(permission.getServer());
			builder.append("] ");
			if (permission.hasWorld()) {
				builder.append("(");
				builder.append(permission.getWorld());
				builder.append(")");
			}
		}
		return builder.toString();
	}
}
