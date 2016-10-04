package me.lucko.luckperms.standalone.controller;

import me.lucko.luckperms.standalone.StandaloneBase;
import me.lucko.luckperms.standalone.view.scene.Manager;

public class ManagerController {

	private Manager view;
	private StandaloneBase model;

	public ManagerController(Manager view, StandaloneBase model) {
		this.view = view;
		this.model = model;
	}

}
