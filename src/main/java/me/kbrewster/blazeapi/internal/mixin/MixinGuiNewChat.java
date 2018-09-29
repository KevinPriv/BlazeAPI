package me.kbrewster.blazeapi.internal.mixin;

import me.kbrewster.blazeapi.BlazeAPI;
import me.kbrewster.blazeapi.events.ChatReceivedEvent;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat {

    @Shadow
    public abstract void printChatMessageWithOptionalDeletion(IChatComponent chatComponent, int chatLineId);

    @Inject(method = "printChatMessage", at = @At("HEAD"), cancellable = true)
    private void printChatMessage(IChatComponent chatComponent, CallbackInfo ci) {
        ChatReceivedEvent event = new ChatReceivedEvent(chatComponent);
        BlazeAPI.getEventBus().post(event);
        if (!event.isCancelled()) {
            this.printChatMessageWithOptionalDeletion(event.getChat(), 0);
        }
        ci.cancel();
    }

}
