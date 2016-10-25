package me.lucko.luckperms.standalone.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lucko.luckperms.standalone.LPStandaloneApp;
import me.lucko.luckperms.standalone.StandaloneBase;
import me.lucko.luckperms.standalone.model.StorageOptions;

@AllArgsConstructor
public class LoginController {

    @Getter
    private LPStandaloneApp app;

    public void startupManageView(StorageOptions options) {
        StandaloneBase base = new StandaloneBase(app, options);
    }

}
