package me.lucko.luckperms.standalone.factory;

public interface Factory<F, T> {
	T create(F f);
}
