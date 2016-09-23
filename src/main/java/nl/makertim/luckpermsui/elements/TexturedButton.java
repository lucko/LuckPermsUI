package nl.makertim.luckpermsui.elements;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class TexturedButton extends ImageView {

	private Collection<Consumer<MouseEvent>> consumerList = new ArrayList<>();

	public TexturedButton(String url, int size) {
		super(url);
		setFitWidth(size);
		setFitHeight(size);
		super.setOnMouseClicked(click -> {
			for (Consumer<MouseEvent> mouseEventConsumer : consumerList) {
				mouseEventConsumer.accept(click);
			}
		});
	}

	public void setOnMouseClicked(Consumer<MouseEvent> onMouseClicked) {
		consumerList.add(onMouseClicked);
	}

}