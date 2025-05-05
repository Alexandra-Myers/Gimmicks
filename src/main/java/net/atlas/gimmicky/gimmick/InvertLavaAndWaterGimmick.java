package net.atlas.gimmicky.gimmick;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

public class InvertLavaAndWaterGimmick extends GenericGimmick<TickEvent.PlayerTickEvent> {
    public InvertLavaAndWaterGimmick() {
        super(TickEvent.PlayerTickEvent.class);
    }

    @Override
    public void handleTypedEvent(TickEvent.PlayerTickEvent event) {
        if (!event.player.isImmuneToFire()) ObfuscationReflectionHelper.setPrivateValue(Entity.class, event.player, true, "isImmuneToFire", "field_70178_ae");
        if (event.player.isInWater()) event.player.attackEntityFrom(DamageSource.drown, 3.0F);
        else if (event.player.isWet() && event.player.getCurrentArmor(3) == null) event.player.attackEntityFrom(DamageSource.drown, 1.0F);
    }

    @Override
    public void finaliseTypedEvent(TickEvent.PlayerTickEvent event) {
        ObfuscationReflectionHelper.setPrivateValue(Entity.class, event.player, false, "isImmuneToFire", "field_70178_ae");
    }
}
