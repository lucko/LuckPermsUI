package me.lucko.luckperms.standalone.view.elements;

import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import me.lucko.luckperms.standalone.LPStandaloneApp;

public class TexturedButton extends ImageView {

    public TexturedButton(String url, int size, String tooltip) {
        this(url, size);
        addTooltip(tooltip);
    }

    public TexturedButton(String url, int size) {
        super(url);
        setFitWidth(size);
        setFitHeight(size);
    }

    public void addTooltip(String tooltipMessage) {
        Tooltip tooltip = new Tooltip(tooltipMessage);
        tooltip.setFont(LPStandaloneApp.FONT);
        Tooltip.install(this, tooltip);
    }
}
