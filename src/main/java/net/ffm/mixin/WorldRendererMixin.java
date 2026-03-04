package net.ffm.mixin;

import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow
    private ClientWorld world;

    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, 
                          net.minecraft.client.render.Camera camera, GameRenderer gameRenderer, 
                          LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, CallbackInfo ci) {
        
        // Hier setzen wir unsere "Farseer-Silhouette" an.
        // Wir zeichnen eine Test-Box bei einer festen Koordinate, 
        // selbst wenn sie außerhalb der Renderdistance liegt.
        
        double testX = 200.0;
        double testY = 70.0;
        double testZ = 200.0;

        // Versatz zur Kamera berechnen (damit die Box an der Welt-Position bleibt)
        matrices.push();
        matrices.translate(testX - camera.getPos().x, testY - camera.getPos().y, testZ - camera.getPos().z);

        // Hier nutzen wir den DebugRenderer von Minecraft, um eine einfache Box zu zeichnen
        WorldRenderer.drawBox(matrices, VertexConsumerProvider.immediate(new BufferBuilder(256)), 
                              0, 0, 0, 1, 1, 1, 1f, 0f, 0f, 1f); // Rote Box

        matrices.pop();
    }
}
