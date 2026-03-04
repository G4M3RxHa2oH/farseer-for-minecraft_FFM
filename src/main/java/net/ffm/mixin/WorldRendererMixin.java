package net.ffm.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.ffm.FarseerEngine;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, 
                          net.minecraft.client.render.Camera camera, GameRenderer gameRenderer, 
                          LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, CallbackInfo ci) {
        
        if (FarseerEngine.SILHOUETTE_BLOCKS.isEmpty()) return;

        double camX = camera.getPos().x;
        double camY = camera.getPos().y;
        double camZ = camera.getPos().z;
        int viewDistSq = FarseerEngine.getRenderDistanceBlocks() * FarseerEngine.getRenderDistanceBlocks();

        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        
        // Wir nutzen DEBUG_LINES für ein Gitter-Gefühl
        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

        Matrix4f matrix = matrices.peek().getPositionMatrix();

        for (BlockPos pos : FarseerEngine.SILHOUETTE_BLOCKS) {
            double dx = pos.getX() - camX;
            double dy = pos.getY() - camY;
            double dz = pos.getZ() - camZ;
            
            // Distanz-Check: Nur weit entfernte zeichnen
            if (dx*dx + dz*dz < viewDistSq) continue;

            // Zeichne ein kleines Kreuz statt einer ganzen Box (spart 10 Linien pro Punkt)
            // Linie 1 (Horizontal)
            buffer.vertex(matrix, (float)dx, (float)dy, (float)dz).color(0f, 1f, 1f, 0.5f).next();
            buffer.vertex(matrix, (float)dx + 1f, (float)dy, (float)dz).color(0f, 1f, 1f, 0.5f).next();
            // Linie 2 (Vertikal)
            buffer.vertex(matrix, (float)dx, (float)dy, (float)dz).color(0f, 1f, 1f, 0.5f).next();
            buffer.vertex(matrix, (float)dx, (float)dy + 1f, (float)dz).color(0f, 1f, 1f, 0.5f).next();
        }
        
        tessellator.draw();
        RenderSystem.enableDepthTest();
    }
}
