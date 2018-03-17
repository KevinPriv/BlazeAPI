package me.kbrewster.blazeapi.internal.mixin;

import me.kbrewster.blazeapi.api.event.EventBus;
import me.kbrewster.blazeapi.api.event.ServerJoinEvent;
import net.minecraft.client.multiplayer.GuiConnecting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiConnecting.class)
public class MixinGuiConnecting {


    @Inject(method = "connect", at = @At("HEAD"))
    private void connect(String ip, int port, CallbackInfo ci) {
        EventBus.post(new ServerJoinEvent(ip, port));
    }
}
