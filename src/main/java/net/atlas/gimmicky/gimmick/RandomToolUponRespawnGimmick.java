package net.atlas.gimmicky.gimmick;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.atlas.gimmicky.Config;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RandomToolUponRespawnGimmick extends GenericGimmick<PlayerEvent.PlayerRespawnEvent> {
    public RandomToolUponRespawnGimmick() {
        super(PlayerEvent.PlayerRespawnEvent.class);
    }

    @Override
    public boolean appliesOnEvent(Event event) {
        return super.appliesOnEvent(event) && !((PlayerEvent)event).player.worldObj.isRemote;
    }

    public void giveRandomTool(PlayerEvent.PlayerRespawnEvent event) {
        ItemStack result = new ItemStack((Item)Item.itemRegistry.getObject(Config.tools[event.player.getRNG().nextInt(Config.tools.length)]));
        result.stackSize = 1;
        if (!event.player.inventory.addItemStackToInventory(result)) event.player.dropPlayerItemWithRandomChoice(result, false);
        if (event.player instanceof EntityPlayerMP) {
            ((EntityPlayerMP) event.player).sendContainerToPlayer(event.player.openContainer);
        }
    }

    @Override
    public void handleTypedEvent(PlayerEvent.PlayerRespawnEvent event) {
        giveRandomTool(event);
    }

    @Override
    public void finaliseTypedEvent(PlayerEvent.PlayerRespawnEvent event) {
        giveRandomTool(event);
    }
}
