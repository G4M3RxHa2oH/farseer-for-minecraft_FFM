package net.ffm.mixin;

import net.ffm.FarseerEngine;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo info) {
        if ((Object) this instanceof PlayerEntity player && player.getWorld().isClient) {
            if (player.getWorld().getTime() % 60 == 0) { // Seltener scannen reicht (alle 3 Sek)
                World world = player.getWorld();
                BlockPos pPos = player.getBlockPos();
                int viewDist = FarseerEngine.getRenderDistanceBlocks();
                int maxFarsee = 512;

                // Wir scannen nur den äußeren Ring (da wo der Nebel beginnt)
                // Um Lücken zu vermeiden, scannen wir in 1-Block-Schritten
                for (int x = -maxFarsee; x <= maxFarsee; x += 16) { // Grobes Raster für Chunks
                    for (int z = -maxFarsee; z <= maxFarsee; z += 16) {
                        double dist = Math.sqrt(x*x + z*z);
                        if (dist > viewDist && dist <= maxFarsee) {
                            int worldX = pPos.getX() + x;
                            int worldZ = pPos.getZ() + z;
                            int y = world.getTopY(Heightmap.Type.WORLD_SURFACE, worldX, worldZ);
                            
                            // Nur hinzufügen, wenn es nicht der "Null-Punkt" (Ladefehler) ist
                            if (y > world.getBottomY() + 10) {
                                FarseerEngine.SILHOUETTE_BLOCKS.add(new BlockPos(worldX, y, worldZ));
                            }
                        }
                    }
                }
            }
        }
    }
}
