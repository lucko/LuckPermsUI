package me.lucko.luckperms.standalone;

import java.util.List;
import java.util.Map;

import me.lucko.luckperms.common.config.AbstractConfiguration;

public class StandaloneConfiguration extends AbstractConfiguration<StandaloneBase> {
	public StandaloneConfiguration(StandaloneBase plugin) {
		super(plugin, "global", true, "null");
	}

	@Override
	protected void init() {
	}

	@Override
	protected String getString(String path, String def) {
		return def;
	}

	@Override
	protected int getInt(String path, int def) {
		return def;
	}

	@Override
	protected boolean getBoolean(String path, boolean def) {
		return def;
	}

	@Override
	protected List<String> getList(String path, List<String> def) {
		return def;
	}

	@Override
	protected List<String> getObjectList(String path, List<String> def) {
		return def;
	}

	@Override
	protected Map<String, String> getMap(String path, Map<String, String> def) {
		return def;
	}
}