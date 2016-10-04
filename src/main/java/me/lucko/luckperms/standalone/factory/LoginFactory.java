package me.lucko.luckperms.standalone.factory;

import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lucko.luckperms.standalone.controller.LoginController;
import me.lucko.luckperms.standalone.view.scene.Login;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginFactory implements Factory<Stage, Login> {

	@Getter
	private static final LoginFactory instance = new LoginFactory();

	@Override
	public Login create(Stage stage) {
		LoginController controller = new LoginController(stage);
		return new Login(controller);
	}
}
