package me.kbrewster.blazeapi.internal.mixin;

import me.kbrewster.blazeapi.BlazeAPI;
import me.kbrewster.blazeapi.events.PlayerDespawnEvent;
import me.kbrewster.blazeapi.events.PlayerSpawnEvent;
import me.kbrewster.blazeapi.events.SpawnpointChangeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class MixinWorld {

    @Inject(method = "setSpawnPoint", at = @At("HEAD"))
    private void setSpawnPoint(BlockPos pos, CallbackInfo ci) {
        BlazeAPI.getEventBus().post(
                new SpawnpointChangeEvent(pos)
        );
    }

    @Inject(method = "spawnEntityInWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateAllPlayersSleepingFlag()V"))
    private void addPlayerToWorld(Entity entityIn, CallbackInfoReturnable<Boolean> cir) {
        BlazeAPI.getEventBus().post(
                new PlayerSpawnEvent((EntityPlayer) entityIn)
        );
    }

    @Inject(method = "removeEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateAllPlayersSleepingFlag()V"))
    private void removePlayerFromWorld(Entity entityIn, CallbackInfo ci) {
        BlazeAPI.getEventBus().post(
                new PlayerDespawnEvent((EntityPlayer) entityIn)
        );
    }
}
