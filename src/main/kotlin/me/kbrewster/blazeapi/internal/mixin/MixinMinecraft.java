package me.kbrewster.blazeapi.internal.mixin;

import me.kbrewster.blazeapi.BlazeAPI;
import me.kbrewster.blazeapi.api.event.EventBus;
import me.kbrewster.blazeapi.api.event.PostInitializationEvent;
import me.kbrewster.blazeapi.api.event.PreInitializationEvent;
import me.kbrewster.blazeapi.api.event.ShutdownEvent;
import me.kbrewster.blazeapi.internal.mixin.impl.IMinecraft;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Minecraft.class)
public abstract class MixinMinecraft implements IMinecraft {

    @Inject(method = "init", at = @At("HEAD"))
    private void preInit(CallbackInfo ci) {
        EventBus.post(new PreInitializationEvent());
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void postInit(CallbackInfo ci) {
        EventBus.post(new PostInitializationEvent());
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    private void shutdown(CallbackInfo ci) {
        EventBus.post(new ShutdownEvent());
    }

}
