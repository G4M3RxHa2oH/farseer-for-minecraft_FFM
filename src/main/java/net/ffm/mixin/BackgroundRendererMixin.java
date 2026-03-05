package net.ffm.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {

    /**
     * Diese Methode wird aufgerufen, wenn Minecraft den Nebel berechnet.
     * Wir "biegen" das Ende des Nebels nach hinten, damit unsere Fernsicht-Daten
     * nicht vom Standard-Nebel verschluckt werden.
     */
    @Inject(method = "applyFog", at = @At("RETURN"))
    private static void onApplyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {
        // Nur anpassen, wenn wir uns im "TERRAIN"-Modus befinden (nicht unter Wasser/Lava)
        if (fogType == BackgroundRenderer.FogType.FOG_TERRAIN) {
            // Wir setzen das sichtbare Ende extrem weit nach draußen (4096 Blöcke)
            RenderSystem.setShaderFogEnd(4096.0f);
            
            // Der Nebel beginnt weich ab 90% der normalen Sichtweite des Spielers
            RenderSystem.setShaderFogStart(viewDistance * 0.9f);
        }
    }
}
