package net.ffm.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo info) {
        if ((Object) this instanceof PlayerEntity player) {
            // Nur alle 20 Ticks (ca. jede Sekunde) etwas ausgeben, um die Konsole nicht zu fluten
            if (player.getWorld().getTime() % 20 == 0) {
                double x = player.getX();
                double y = player.getY();
                double z = player.getZ();
                
                // Hier werden wir später die Daten an Farseer senden
                // Fürs Erste loggen wir es nur:
                // System.out.println("FFM - Spieler Position: X=" + x + " Y=" + y + " Z=" + z);
            }
        }
    }
}
