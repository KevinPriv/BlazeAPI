package me.kbrewster.blazeapi.internal.mixin;

import me.kbrewster.blazeapi.BlazeAPI;
import me.kbrewster.blazeapi.events.ServerDisconnectEvent;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldClient.class)
public class MixinWorldClient {
    @Inject(method = "sendQuittingDisconnectingPacket", at = @At("HEAD"))
    private void disconnect(CallbackInfo ci) {
        BlazeAPI.getEventBus().post(
                new ServerDisconnectEvent()
        );
    }
}
