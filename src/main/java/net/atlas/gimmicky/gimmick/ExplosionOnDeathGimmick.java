package net.atlas.gimmicky.gimmick;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

public class ExplosionOnDeathGimmick extends GenericGimmick<LivingDeathEvent> {
    public ExplosionOnDeathGimmick() {
        super(LivingDeathEvent.class);
    }

    @Override
    public boolean appliesOnEvent(Event event) {
        return super.appliesOnEvent(event) && !((LivingEvent)event).entity.worldObj.isRemote;
    }

    @Override
    public void handleTypedEvent(LivingDeathEvent event) {
        event.entityLiving.worldObj.createExplosion(
            event.entityLiving,
            event.entityLiving.posX,
            event.entityLiving.posY,
            event.entityLiving.posZ,
            3.0F,
            false);
    }
}
