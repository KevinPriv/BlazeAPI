package com.github.kevinpriv;

import me.kbrewster.blazeapi.api.event.InvokeEvent;
import me.kbrewster.blazeapi.api.event.RightClickEvent;
import net.minecraft.client.Minecraft;

public class GuiTest {


    @InvokeEvent
    private void test(RightClickEvent event) {
        Minecraft.getMinecraft().displayGuiScreen(new Config());
    }
}
