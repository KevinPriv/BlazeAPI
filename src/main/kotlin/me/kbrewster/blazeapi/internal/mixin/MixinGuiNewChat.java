package me.kbrewster.blazeapi.internal.mixin;

import me.kbrewster.blazeapi.api.event.ChatReceivedEvent;
import me.kbrewster.blazeapi.api.event.EventBus;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat {

    @Shadow
    public abstract void printChatMessageWithOptionalDeletion(ITextComponent chatComponent, int chatLineId);

    @Inject(method = "printChatMessage", at = @At("HEAD"), cancellable = true)
    private void printChatMessage(ITextComponent chatComponent, CallbackInfo ci) {
        ChatReceivedEvent event = new ChatReceivedEvent(chatComponent);
        EventBus.post(event);
        if (!event.isCancelled()) {
            this.printChatMessageWithOptionalDeletion(event.getChat(), 0);
        }
        ci.cancel();
    }

}
