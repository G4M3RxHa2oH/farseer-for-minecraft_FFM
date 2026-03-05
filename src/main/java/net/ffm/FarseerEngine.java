public class FarseerEngine {
    // Der binäre Cache: Region -> [X][Z] -> Y-Wert
    // Wir nutzen ein Byte (0-255), das reicht für Minecraft-Höhen aus.
    public static final Map<Long, byte[][]> WORLD_HEIGHT_CACHE = new ConcurrentHashMap<>();

    public static int getLodStep(double distance) {
        if (distance < 256) return 1;  // LOD1: 1x1
        if (distance < 512) return 2;  // LOD2: 2x2
        if (distance < 1024) return 4; // LOD3: 4x4
        return 8;                      // LOD4: 8x8
    }
}