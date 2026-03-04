package net.ffm.mixin;

import net.ffm.FarseerEngine;
import net.ffm.FarseerScanner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.concurrent.CompletableFuture;

@Mixin(Entity.class)
public abstract class EntityMixin {
    private boolean isScanning = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo info) {
        if ((Object) this instanceof PlayerEntity player && player.getWorld().isClient) {
            if (player.getWorld().getTime() % 100 == 0 && !isScanning) {
                isScanning = true;
                World world = player.getWorld();
                int px = player.getBlockX();
                int pz = player.getBlockZ();
                int maxFarsee = 512;

                CompletableFuture.runAsync(() -> {
                    int startX = ((px - maxFarsee) / 16) * 16;
                    int endX = ((px + maxFarsee) / 16) * 16;
                    int startZ = ((pz - maxFarsee) / 16) * 16;
                    int endZ = ((pz + maxFarsee) / 16) * 16;

                    for (int x = startX; x <= endX; x += 16) {
                        for (int z = startZ; z <= endZ; z += 16) {
                            long key = FarseerEngine.getChunkKey(x, z);
                            if (!FarseerEngine.SILHOUETTE_MAP.containsKey(key)) {
                                FarseerScanner.HeightData data = FarseerScanner.getPredictedHeightQuad(x, z, world);
                                FarseerEngine.SILHOUETTE_MAP.put(key, data);
                            }
                        }
                    }
                    isScanning = false;
                });
            }
        }
    }
}
