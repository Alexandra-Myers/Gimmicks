package net.atlas.gimmicky.gimmick;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class GlassCannonGimmick extends GenericGimmick<LivingHurtEvent> {
    public GlassCannonGimmick() {
        super(LivingHurtEvent.class);
    }

    @Override
    public void handleTypedEvent(LivingHurtEvent event) {
        event.ammount *= 1.5F;
        if (event.entityLiving.getActivePotionEffect(Potion.damageBoost) == null) event.entityLiving.addPotionEffect(new PotionEffect(Potion.damageBoost.getId(), 400, 1));
    }
}
