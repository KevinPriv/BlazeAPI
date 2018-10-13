package me.kbrewster.blazeapi.internal.mixin;

import me.kbrewster.blazeapi.BlazeAPI;
import me.kbrewster.blazeapi.events.*;
import me.kbrewster.blazeapi.internal.addons.Addon;
import me.kbrewster.blazeapi.internal.addons.AddonMinecraftBootstrap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Inject(method = "startGame", at = @At("HEAD"))
    private void preInit(CallbackInfo ci) {
        AddonMinecraftBootstrap.init();
        BlazeAPI.getEventBus().post(new PreInitializationEvent());
    }

    @Inject(method = "startGame", at = @At("RETURN"))
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

    @Inject(method = "dispatchKeypresses", at = @At(value = "INVOKE_ASSIGN", target = "Lorg/lwjgl/input/Keyboard;getEventKeyState()Z"))
    private void dispatchKeyPresses(CallbackInfo ci) {
        int key = Keyboard.getEventKey();
        boolean press = Keyboard.getEventKeyState();
        boolean repeat = Keyboard.isRepeatEvent();
        boolean down = Keyboard.getEventKeyState();
        BlazeAPI.getEventBus().post(new InputEvents.Keypress(key, repeat, press, down));
    }

    @Inject(method = "clickMouse", at = @At("RETURN"))
    private void clickMouse(CallbackInfo ci) {
        BlazeAPI.getEventBus().post(new InputEvents.LeftClick());
    }

    @Inject(method = "rightClickMouse", at = @At("RETURN"))
    private void rightClickMouse(CallbackInfo ci) {
        BlazeAPI.getEventBus().post(new InputEvents.RightClick());
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    private void shutdown(CallbackInfo ci) {
        BlazeAPI.getLoadedAddons().forEach(Addon::onDisable);
        BlazeAPI.getEventBus().post(new ShutdownEvent());
    }

}
