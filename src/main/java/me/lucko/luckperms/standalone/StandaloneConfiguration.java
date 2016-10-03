package me.lucko.luckperms.standalone;

import java.util.Map;

import me.lucko.luckperms.config.AbstractConfiguration;

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
	protected Map<String, String> getMap(String path, Map<String, String> def) {
		return def;
	}
}