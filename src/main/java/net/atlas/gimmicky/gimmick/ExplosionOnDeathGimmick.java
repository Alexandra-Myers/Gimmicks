package net.atlas.gimmicky.gimmick;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

public class ExplosionOnDeathGimmick implements Gimmick {
    @Override
    public boolean appliesOnEvent(Event event) {
        return event instanceof LivingDeathEvent && !((LivingEvent)event).entity.worldObj.isRemote;
    }

    @Override
    public void handleEvent(Event event) {
        if (!(event instanceof LivingDeathEvent)) return;
        LivingDeathEvent livingDeathEvent = (LivingDeathEvent) event;
        livingDeathEvent.entityLiving.worldObj.createExplosion(
            livingDeathEvent.entityLiving,
            livingDeathEvent.entityLiving.posX,
            livingDeathEvent.entityLiving.posY,
            livingDeathEvent.entityLiving.posZ,
            3.0F,
            false);
    }
}
