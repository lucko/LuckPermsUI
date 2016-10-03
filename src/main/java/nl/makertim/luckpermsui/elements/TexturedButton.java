package nl.makertim.luckpermsui.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import me.lucko.luckperms.LPStandaloneApp;

public class TexturedButton extends ImageView {

	private Collection<Consumer<MouseEvent>> consumerList = new ArrayList<>();

	public TexturedButton(String url, int size, String tooltip) {
		this(url, size);
		addTooltip(tooltip);
	}

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

	public void addTooltip(String tooltipMessage) {
		Tooltip tooltip = new Tooltip(tooltipMessage);
		tooltip.setFont(LPStandaloneApp.FONT);
		Tooltip.install(this, tooltip);
	}

	public void setOnMouseClicked(Consumer<MouseEvent> onMouseClicked) {
		consumerList.add(onMouseClicked);
	}

}
