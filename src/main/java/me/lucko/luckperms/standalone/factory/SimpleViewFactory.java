package me.lucko.luckperms.standalone.factory;

import javafx.scene.Scene;
import javafx.stage.Stage;
import me.lucko.luckperms.LPStandaloneApp;
import me.lucko.luckperms.common.groups.Group;
import me.lucko.luckperms.standalone.StandaloneBase;
import me.lucko.luckperms.standalone.controller.GroupController;
import me.lucko.luckperms.standalone.controller.LoginController;
import me.lucko.luckperms.standalone.controller.ManagerController;
import me.lucko.luckperms.standalone.view.centerpane.GroupListManager;
import me.lucko.luckperms.standalone.view.centerpane.UserManager;
import me.lucko.luckperms.standalone.view.scene.Login;
import me.lucko.luckperms.standalone.view.scene.Manager;
import me.lucko.luckperms.standalone.view.sidepane.SideGroup;

public class SimpleViewFactory {

    private static final SimpleViewFactory INSTANCE = new SimpleViewFactory();

    public static SimpleViewFactory getInstance() {
        return INSTANCE;
    }

    private SimpleViewFactory() {
    }

    public void openManager(LPStandaloneApp app, StandaloneBase base) {
        Stage stage = app.getPrimaryStage();
        Manager view = new Manager();
        ManagerController controller = new ManagerController(view, base);
        view.registerController(controller);
        app.setPrimaryScene(new Scene(view, 1024, 768));
        stage.centerOnScreen();
        stage.setResizable(true);
        stage.setMinWidth(750);
        stage.setMinHeight(500);
        stage.setMaxHeight(1080);
    }

    public void openLogin(LPStandaloneApp app) {
        Stage stage = app.getPrimaryStage();
        LoginController controller = new LoginController(app);
        Login login = new Login(controller);
        app.setPrimaryScene(new Scene(login));
        stage.setResizable(false);
	}

	public void openGroup(Manager manager) {
		GroupListManager view = new GroupListManager(manager);
		GroupController controller = new GroupController(view, manager.getController().getBase());
		view.registerController(controller);
		manager.setMainView(view);
	}

	public void openUser(Manager manager) {

		manager.setMainView(new UserManager(manager));
	}

	public SideGroup linkGroup(Group group, Manager parent, StandaloneBase base) {
		SideGroup view = new SideGroup(parent, group);
		GroupController controller = new GroupController(view, base);
		view.registerController(controller);
		return view;
    }
}
