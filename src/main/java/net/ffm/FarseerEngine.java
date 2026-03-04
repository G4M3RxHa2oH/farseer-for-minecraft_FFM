package net.ffm;

import net.minecraft.client.MinecraftClient;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FarseerEngine {
    // Speichert X_Z als Key für schnellen Zugriff und Lückenlosigkeit
    // Als Value speichern wir jetzt die Höhendaten für alle 4 Ecken
    public static final Map<Long, FarseerScanner.HeightData> SILHOUETTE_MAP = new ConcurrentHashMap<>();

    public static long getChunkKey(int x, int z) {
        return ((long)x << 32) | (z & 0xFFFFFFFFL);
    }

    public static int getRenderDistanceBlocks() {
        if (MinecraftClient.getInstance().options == null) return 128;
        return MinecraftClient.getInstance().options.getClampedViewDistance() * 16;
    }
}
