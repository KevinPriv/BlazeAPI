package me.kbrewster.blazeapi.internal.mixin;

import me.kbrewster.blazeapi.api.event.EventBus;
import me.kbrewster.blazeapi.api.event.ServerDisconnectEvent;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiDisconnected.class)
public class MixinGuiDisconnected {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(GuiScreen screen,
                      String reasonLocalizationKey,
                      ITextComponent reason,
                      CallbackInfo ci) {
        EventBus.post(new ServerDisconnectEvent(reason, reasonLocalizationKey));
    }
}
