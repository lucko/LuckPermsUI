package me.lucko.luckperms.standalone.factory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lucko.luckperms.standalone.StandaloneBase;
import me.lucko.luckperms.standalone.controller.ManagerController;
import me.lucko.luckperms.standalone.view.scene.Manager;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ManagerFactory implements Factory<StandaloneBase, Manager> {

    @Getter
    private static final ManagerFactory instance = new ManagerFactory();

    @Override
    public Manager create(StandaloneBase base) {
        Manager view = new Manager();
        ManagerController controller = new ManagerController(view, base);
        view.setController(controller);
        return view;
    }
}
