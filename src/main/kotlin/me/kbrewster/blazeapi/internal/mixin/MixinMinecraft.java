package me.kbrewster.blazeapi.internal.mixin;

import me.kbrewster.blazeapi.BlazeAPI;
import me.kbrewster.blazeapi.api.event.*;
import me.kbrewster.blazeapi.internal.addons.Addon;
import me.kbrewster.blazeapi.internal.addons.AddonMinecraftBootstrap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Inject(method = "init", at = @At("HEAD"))
    private void preInit(CallbackInfo ci) {
        AddonMinecraftBootstrap.init();
        BlazeAPI.getEventBus().post(new PreInitializationEvent());
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void postInit(CallbackInfo ci) {
        BlazeAPI.getEventBus().post(new InitializationEvent());
    }

    @Inject(method = "runTick", at = @At("RETURN"))
    private void runTick(CallbackInfo ci) {
        BlazeAPI.getEventBus().post(new ClientTickEvent());
    }

    @Inject(method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V", at = @At("HEAD"))
    private void loadWorld(WorldClient worldClientIn, String loadingMessage, CallbackInfo ci) {
        if (worldClientIn != null) {
            BlazeAPI.getEventBus().post(new WorldLoadEvent(worldClientIn, loadingMessage));
        }
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    private void shutdown(CallbackInfo ci) {
        BlazeAPI.getLoadedAddons().forEach(Addon::onDisable);
        BlazeAPI.getEventBus().post(new ShutdownEvent());
    }

}
