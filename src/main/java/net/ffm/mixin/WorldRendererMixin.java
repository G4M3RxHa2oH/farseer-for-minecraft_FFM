package net.ffm.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.ffm.FarseerEngine;
import net.ffm.FarseerScanner;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Map;
import java.util.HashMap;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, 
                          net.minecraft.client.render.Camera camera, GameRenderer gameRenderer, 
                          LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, CallbackInfo ci) {
        
        if (FarseerEngine.SILHOUETTE_MAP.isEmpty()) return;

        double camX = camera.getPos().x;
        double camY = camera.getPos().y;
        double camZ = camera.getPos().z;
        int viewDistSq = FarseerEngine.getRenderDistanceBlocks() * FarseerEngine.getRenderDistanceBlocks();

        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        Map<Long, FarseerScanner.HeightData> copyMap = new HashMap<>(FarseerEngine.SILHOUETTE_MAP);

        for (Map.Entry<Long, FarseerScanner.HeightData> entry : copyMap.entrySet()) {
            long key = entry.getKey();
            int x = (int)(key >> 32);
            int z = (int)(key & 0xFFFFFFFFL);
            FarseerScanner.HeightData h = entry.getValue();

            double dx = x - camX;
            double dz = z - camZ;
            double distSq = dx*dx + dz*dz;

            if (distSq > viewDistSq && distSq < 1024 * 1024) {
                float alpha = (float) Math.max(0.01, 0.5 - (Math.sqrt(distSq) / 1024.0));
                float r = 0.05f; float g = 0.15f; float b = 0.25f; // Dunklerer Nebel-Look

                int s = 16;
                buffer.vertex(matrix, (float)dx, (float)(h.h00 - camY), (float)dz).color(r, g, b, alpha).next();
                buffer.vertex(matrix, (float)(dx + s), (float)(h.h10 - camY), (float)dz).color(r, g, b, alpha).next();
                buffer.vertex(matrix, (float)(dx + s), (float)(h.h11 - camY), (float)(dz + s)).color(r, g, b, alpha).next();
                buffer.vertex(matrix, (float)dx, (float)(h.h01 - camY), (float)(dz + s)).color(r, g, b, alpha).next();
            }
        }
        
        tessellator.draw();
        RenderSystem.disableBlend();
    }
}
