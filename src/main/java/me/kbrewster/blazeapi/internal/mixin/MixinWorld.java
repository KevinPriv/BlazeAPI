package me.kbrewster.blazeapi.internal.mixin;

import me.kbrewster.blazeapi.BlazeAPI;
import me.kbrewster.blazeapi.api.events.SpawnpointChangeEvent;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public class MixinWorld {

    @Inject(method = "setSpawnPoint", at = @At("HEAD"))
    private void setSpawnPoint(BlockPos pos, CallbackInfo ci) {
        BlazeAPI.getEventBus().post(new SpawnpointChangeEvent(pos));
    }

}
