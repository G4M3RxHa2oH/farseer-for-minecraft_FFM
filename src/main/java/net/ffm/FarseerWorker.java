package net.ffm;

import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.server.world.ServerWorld;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class FarseerWorker {
    // Ein dedizierter Thread für die Mathe-Arbeit
    private static final ExecutorService SCAN_THREAD = Executors.newSingleThreadExecutor();

    public static void submitScan(World world, int centerX, int centerZ, int radius) {
        SCAN_THREAD.submit(() -> {
            // Hier implementieren wir die Logik für LOD-Zonen
            for (int x = -radius; x <= radius; x += 16) {
                for (int z = -radius; z <= radius; z += 16) {
                    // Berechnung der NoiseColumn ohne Chunk-Load
                    // (Logik folgt im nächsten Schritt via Mixin)
                }
            }
        });
    }
}