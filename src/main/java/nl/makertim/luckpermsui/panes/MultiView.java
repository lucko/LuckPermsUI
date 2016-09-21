package nl.makertim.luckpermsui.panes;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MultiView extends HBox {

	Pane mainView;

	public MultiView() {
		setup();
		setWidth(600);
	}

	private void setup() {
		VBox menu = new VBox(10);
		mainView = new Pane();

		Button groupButton = new Button("Groups");
		Button userButton = new Button("User manager");

		groupButton.setOnAction(action -> setMainView(new GroupView()));
		// userButton.setOnAction(action -> setMainView(new UserView()));

		menu.getChildren().addAll(groupButton, userButton);
		getChildren().addAll(menu, mainView);
	}

	private void setMainView(Pane pane) {
		mainView.getChildren().clear();
		mainView.getChildren().add(pane);
	}

}
