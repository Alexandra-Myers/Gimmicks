package net.atlas.gimmicky.gimmick;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;

public class KnockbackResistanceBoostGimmick extends GenericGimmick<TickEvent.PlayerTickEvent> {
    public static final AttributeModifier surefootedBoost = new AttributeModifier("Surefooted Boost", 1.0, 0).setSaved(false);
    public KnockbackResistanceBoostGimmick() {
        super(TickEvent.PlayerTickEvent.class);
    }

    @Override
    public boolean appliesOnEvent(Event event) {
        return super.appliesOnEvent(event) && !((TickEvent.PlayerTickEvent)event).player.worldObj.isRemote;
    }

    @Override
    public void handleTypedEvent(TickEvent.PlayerTickEvent event) {
        if (event.player.getHealth() > 10.0F) {
            IAttributeInstance knockbackResistance = event.player.getEntityAttribute(SharedMonsterAttributes.knockbackResistance);
            if (knockbackResistance == null) knockbackResistance = event.player.getAttributeMap().registerAttribute(SharedMonsterAttributes.knockbackResistance);
            if (knockbackResistance.getModifier(surefootedBoost.getID()) == null) knockbackResistance.applyModifier(surefootedBoost);
        } else {
            IAttributeInstance knockbackResistance = event.player.getEntityAttribute(SharedMonsterAttributes.knockbackResistance);
            if (knockbackResistance == null) knockbackResistance = event.player.getAttributeMap().registerAttribute(SharedMonsterAttributes.knockbackResistance);
            if (knockbackResistance.getModifier(surefootedBoost.getID()) != null) knockbackResistance.removeModifier(surefootedBoost);
        }
    }

    @Override
    public void finaliseTypedEvent(TickEvent.PlayerTickEvent event) {
        IAttributeInstance knockbackResistance = event.player.getEntityAttribute(SharedMonsterAttributes.knockbackResistance);
        if (knockbackResistance == null) knockbackResistance = event.player.getAttributeMap().registerAttribute(SharedMonsterAttributes.knockbackResistance);
        if (knockbackResistance.getModifier(surefootedBoost.getID()) != null) knockbackResistance.removeModifier(surefootedBoost);
    }
}
