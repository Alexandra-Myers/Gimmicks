package net.atlas.gimmicky.gimmick;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import static net.atlas.gimmicky.event.ForgeEventBusHandler.teleportTo;

public class TeleportRandomlyOnHurtGimmick implements Gimmick {
    @Override
    public boolean appliesOnEvent(Event event) {
        return event instanceof LivingHurtEvent && !((LivingEvent)event).entity.worldObj.isRemote;
    }

    @Override
    public void handleEvent(Event event) {
        if (!(event instanceof LivingHurtEvent)) return;
        LivingHurtEvent livingHurtEvent = (LivingHurtEvent) event;
        int count = 0;
        double x, y, z;
        do {
            x = livingHurtEvent.entityLiving.posX + (livingHurtEvent.entityLiving.getRNG().nextDouble() - 0.5D) * 8.0D;
            y = livingHurtEvent.entityLiving.posY + 4;
            z = livingHurtEvent.entityLiving.posZ + (livingHurtEvent.entityLiving.getRNG().nextDouble() - 0.5D) * 8.0D;
            count++;
        } while (!teleportTo(livingHurtEvent.entityLiving, x, y, z) && count < 6);
        if (livingHurtEvent.source.getDamageType().equalsIgnoreCase("fall")) livingHurtEvent.ammount *= 0.5F;
    }
}
