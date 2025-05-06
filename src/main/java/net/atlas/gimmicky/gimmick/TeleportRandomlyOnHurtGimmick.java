package net.atlas.gimmicky.gimmick;

import static net.atlas.gimmicky.event.ForgeEventBusHandler.teleportTo;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import cpw.mods.fml.common.eventhandler.Event;

public class TeleportRandomlyOnHurtGimmick extends GenericGimmick<LivingHurtEvent> {

    public TeleportRandomlyOnHurtGimmick() {
        super(LivingHurtEvent.class);
    }

    @Override
    public boolean appliesOnEvent(Event event) {
        return super.appliesOnEvent(event) && !((LivingEvent) event).entity.worldObj.isRemote;
    }

    @Override
    public void handleTypedEvent(LivingHurtEvent event) {
        int count = 0;
        double x, y, z;
        do {
            x = event.entityLiving.posX + (event.entityLiving.getRNG()
                .nextDouble() - 0.5D) * 8.0D;
            y = event.entityLiving.posY + 4;
            z = event.entityLiving.posZ + (event.entityLiving.getRNG()
                .nextDouble() - 0.5D) * 8.0D;
            count++;
        } while (!teleportTo(event.entityLiving, x, y, z) && count < 6);
        if (event.source.getDamageType()
            .equalsIgnoreCase("fall")) event.ammount *= 0.5F;
    }
}
