package nl.makertim.luckpermsui.popup;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.makertim.luckpermsui.internal.Group;

public class GroupPopup extends Stage {

    private VBox content;
    private Group group;

    public GroupPopup(Group group) {
        super();
        content = new VBox();
        this.group = group;
        setScene(new Scene(content));
        setup();
    }

    private void setup() {
        content.getChildren().add(new Label(group.getName()));
    }

}
