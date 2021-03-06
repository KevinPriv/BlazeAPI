package me.kbrewster.blazeapi.internal.mixin;

import me.kbrewster.blazeapi.api.event.ChatSentEvent;
import me.kbrewster.blazeapi.api.event.RespawnPlayerEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {

    @Inject(method = "sendChatMessage", at = @At(value = "HEAD"), cancellable = true)
    private void sendChatMessage(String message, CallbackInfo ci) {
        ChatSentEvent chatSentEvent = new ChatSentEvent(message);
        if (chatSentEvent.isCancelled()) ci.cancel();
    }

    @Inject(method = "respawnPlayer", at = @At(value = "HEAD"), cancellable = true)
    private void respawnPlayer(CallbackInfo ci) {
        RespawnPlayerEvent respawnPlayerEvent = new RespawnPlayerEvent();
        if (respawnPlayerEvent.isCancelled()) ci.cancel();
    }
}
