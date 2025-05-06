package net.atlas.gimmicky.gimmick;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class RandomDupeGimmick extends GenericGimmick<PlayerEvent.ItemCraftedEvent> {

    public RandomDupeGimmick() {
        super(PlayerEvent.ItemCraftedEvent.class);
    }

    @Override
    public boolean appliesOnEvent(Event event) {
        return super.appliesOnEvent(event) && !((PlayerEvent) event).player.worldObj.isRemote;
    }

    @Override
    public void handleTypedEvent(PlayerEvent.ItemCraftedEvent event) {
        if (event.crafting.getMaxStackSize() == 1) return;
        if (event.player.getRNG()
            .nextDouble() < 0.15) {
            ItemStack result = event.crafting.copy();
            result.stackSize = 1;
            if (!event.player.inventory.addItemStackToInventory(result))
                event.player.dropPlayerItemWithRandomChoice(result, false);
            if (event.player instanceof EntityPlayerMP) {
                ((EntityPlayerMP) event.player).sendContainerToPlayer(event.player.openContainer);
            }
            event.player.worldObj.playSoundAtEntity(event.player, "random.levelup", 0.5F, 1.0F);
        }
    }
}
