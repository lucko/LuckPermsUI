package me.lucko.luckperms.standalone.controller;

import lombok.Getter;
import me.lucko.luckperms.standalone.StandaloneBase;
import me.lucko.luckperms.standalone.view.scene.Manager;

public class ManagerController {

    @Getter
    private Manager view;
    @Getter
    private StandaloneBase base;

    public ManagerController(Manager view, StandaloneBase base) {
        this.view = view;
        this.base = base;
    }
}
