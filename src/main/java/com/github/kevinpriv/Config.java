package com.github.kevinpriv;

import me.kbrewster.blazeapi.api.ui.gui.InjectWidget;
import me.kbrewster.blazeapi.api.ui.gui.Window;
import me.kbrewster.blazeapi.api.ui.gui.widget.WidgetButton;

public class Config extends Window {

    @InjectWidget
    private WidgetButton button = new WidgetButton(20, 20, 100, 20, "Test", (button) -> {
        System.out.println("Clicked " + button.getDisplayString());
    });

    public Config() {
        super("Test");
        this.setWindowHeight(100);
        this.setWindowWidth(200);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.centerWindow();
    }
}
