package nl.makertim.luckpermsui.panes;

import javafx.scene.control.Button;
import javafx.scene.layout.*;

public class MultiView extends BorderPane {

	Pane mainView;
	Pane sideView;

	public MultiView() {
		setup();
		setWidth(600);
	}

	private void setup() {
		VBox menu = new VBox(10);
		mainView = new AnchorPane();
		sideView = new AnchorPane();
		sideView.setMinWidth(100);

		Button groupButton = new Button("Groups");
		Button userButton = new Button("User manager");

		groupButton.setOnAction(action -> setMainView(new GroupView(this)));
		// userButton.setOnAction(action -> setMainView(new UserView(this)));

		menu.getChildren().addAll(groupButton, userButton);
		setLeft(menu);
		setCenter(mainView);
		setRight(sideView);
	}

	public void setMainView(Pane pane) {
		mainView.getChildren().clear();
		if (pane != null) {
			mainView.getChildren().add(pane);
		}
	}

	public void setSideView(Pane pane) {
		sideView.getChildren().clear();
		if (pane != null) {
			sideView.getChildren().add(pane);
		}
	}
}
