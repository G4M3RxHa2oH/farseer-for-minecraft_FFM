package net.ffm;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.Chunk;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FarseerEngine {
    public static final Set<BlockPos> SILHOUETTE_BLOCKS = Collections.newSetFromMap(new ConcurrentHashMap<BlockPos, Boolean>());

    public static int getRenderDistanceBlocks() {
        if (MinecraftClient.getInstance().options == null) return 128;
        return MinecraftClient.getInstance().options.getClampedViewDistance() * 16;
    }
}
