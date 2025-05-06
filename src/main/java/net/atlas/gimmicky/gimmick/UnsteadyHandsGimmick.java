package net.atlas.gimmicky.gimmick;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import cpw.mods.fml.common.eventhandler.Event;

public class UnsteadyHandsGimmick extends GenericGimmick<PlayerInteractEvent> {

    public UnsteadyHandsGimmick() {
        super(PlayerInteractEvent.class);
    }

    @Override
    public boolean appliesOnEvent(Event event) {
        return super.appliesOnEvent(event)
            && ((PlayerInteractEvent) event).action.equals(PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)
            && !((PlayerInteractEvent) event).world.isRemote;
    }

    @Override
    public void handleTypedEvent(PlayerInteractEvent event) {
        Block block = event.world.getBlock(event.x, event.y, event.z);
        if (block instanceof BlockTNT) {
            BlockTNT blockTNT = (BlockTNT) block;
            blockTNT.func_150114_a(event.world, event.x, event.y, event.z, 1, event.entityPlayer);
            event.world.setBlockToAir(event.x, event.y, event.z);
        }
    }
}
