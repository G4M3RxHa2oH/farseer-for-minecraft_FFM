package net.ffm;

import net.minecraft.client.MinecraftClient;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;

public class FarseerScanner {
    
    public static class HeightData {
        public final int h00, h10, h01, h11;
        public HeightData(int h00, int h10, int h01, int h11) {
            this.h00 = h00; this.h10 = h10;
            this.h01 = h01; this.h11 = h11;
        }
    }

    public static HeightData getPredictedHeightQuad(int x, int z, World world) {
        MinecraftClient client = MinecraftClient.getInstance();
        int step = 16;

        if (client.getServer() != null) {
            ServerWorld serverWorld = client.getServer().getWorld(world.getRegistryKey());
            if (serverWorld != null) {
                NoiseConfig noiseConfig = serverWorld.getChunkManager().getNoiseConfig();
                ChunkGenerator generator = serverWorld.getChunkManager().getChunkGenerator();
                
                int v00 = generator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG, serverWorld, noiseConfig);
                int v10 = generator.getHeight(x + step, z, Heightmap.Type.WORLD_SURFACE_WG, serverWorld, noiseConfig);
                int v01 = generator.getHeight(x, z + step, Heightmap.Type.WORLD_SURFACE_WG, serverWorld, noiseConfig);
                int v11 = generator.getHeight(x + step, z + step, Heightmap.Type.WORLD_SURFACE_WG, serverWorld, noiseConfig);
                
                return new HeightData(v00, v10, v01, v11);
            }
        }
        
        int h = world.getTopY(Heightmap.Type.WORLD_SURFACE, x, z);
        return new HeightData(h, h, h, h);
    }
}
