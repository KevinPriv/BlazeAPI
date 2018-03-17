package me.kbrewster.blazeapi.internal.mixin;

import me.kbrewster.blazeapi.api.event.EventBus;
import me.kbrewster.blazeapi.api.event.GuiScreenCloseEvent;
import me.kbrewster.blazeapi.api.event.GuiScreenOpenEvent;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public class MixinGuiScreen {
    @Inject(method = "initGui", at = @At("HEAD"))
    private void init(CallbackInfo ci) {
        GuiScreen screen = ((GuiScreen) (Object) this);
        EventBus.post(new GuiScreenOpenEvent(screen));
    }

    @Inject(method = "onGuiClosed", at = @At("HEAD"))
    private void onGuiClose(CallbackInfo ci) {
        EventBus.post(new GuiScreenCloseEvent());
    }

}
