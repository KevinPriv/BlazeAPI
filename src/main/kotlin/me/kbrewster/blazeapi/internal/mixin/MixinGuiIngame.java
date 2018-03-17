package me.kbrewster.blazeapi.internal.mixin;

import me.kbrewster.blazeapi.api.event.EventBus;
import me.kbrewster.blazeapi.api.event.RenderEvent;
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class MixinGuiIngame {
    @Inject(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
    private void renderGameOverlay(float partialTicks, CallbackInfo ci) {
        EventBus.post(new RenderEvent(partialTicks));
    }
}
