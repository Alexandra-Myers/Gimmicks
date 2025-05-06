package net.atlas.gimmicky.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.atlas.gimmicky.gimmick.Gimmick;
import net.atlas.gimmicky.gimmick.GimmickExtendedEntityProperty;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;

public class ForgeEventBusHandler {
    @SubscribeEvent
    public void playerCreation(EntityEvent.EntityConstructing event) {
        if (!(event.entity instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.entity;
        player.registerExtendedProperties("gimmicky:gimmick", new GimmickExtendedEntityProperty());
    }
    @SubscribeEvent
    public void handleLivingDeath(LivingDeathEvent event) {
        handleEntityEvent(event);
    }
    @SubscribeEvent
    public void handleLivingHurt(LivingHurtEvent event) {
        handleEntityEvent(event);
    }
    @SubscribeEvent
    public void handleItemUse(PlayerInteractEvent event) {
        ItemStack itemStackIn = event.entityPlayer.inventory.getCurrentItem();
        if (!event.action.equals(PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) && itemStackIn != null && itemStackIn.getItem() instanceof ItemArmor) {
            int i = EntityLiving.getArmorPosition(itemStackIn) - 1;
            ItemStack itemstack1 = event.entityPlayer.getCurrentArmor(i);

            if (itemstack1 != null) {
                event.entityPlayer.setCurrentItemOrArmor(i + 1, itemStackIn.copy());
                event.entityPlayer.setCurrentItemOrArmor(0, itemstack1.copy());
                itemStackIn.stackSize = 0;
                itemstack1.stackSize = 0;
            }
        }
        handleEntityEvent(event);
    }
    public void handleEntityEvent(EntityEvent event) {
        if (!(event.entity instanceof EntityPlayer)) return;
        IExtendedEntityProperties gimmickProperties = event.entity.getExtendedProperties("gimmicky:gimmick");
        if (!(gimmickProperties instanceof GimmickExtendedEntityProperty)) return;
        ((GimmickExtendedEntityProperty) gimmickProperties).finaliseOldGimmick(event);
        Gimmick gimmick = ((GimmickExtendedEntityProperty) gimmickProperties).getGimmick();
        if (gimmick != null && gimmick.appliesOnEvent(event)) gimmick.handleEvent(event);
    }

    // Clone of Enderman Teleport
    public static boolean teleportTo(EntityLivingBase livingBase, double x, double y, double z) {
        EnderTeleportEvent event = new EnderTeleportEvent(livingBase, x, y, z, 0);
        if (MinecraftForge.EVENT_BUS.post(event)){
            return false;
        }
        double oriX = livingBase.posX;
        double oriY = livingBase.posY;
        double oriZ = livingBase.posZ;
        livingBase.posX = event.targetX;
        livingBase.posY = event.targetY;
        livingBase.posZ = event.targetZ;
        boolean flag = false;
        int i = MathHelper.floor_double(livingBase.posX);
        int j = MathHelper.floor_double(livingBase.posY);
        int k = MathHelper.floor_double(livingBase.posZ);

        if (livingBase.worldObj.blockExists(i, j, k)) {
            boolean flag1 = false;

            while (!flag1 && j > 0) {
                Block block = livingBase.worldObj.getBlock(i, j - 1, k);

                if (block.getMaterial().blocksMovement()) {
                    flag1 = true;
                } else {
                    --livingBase.posY;
                    --j;
                }
            }

            if (flag1) {
                livingBase.mountEntity(null);
                livingBase.setPositionAndUpdate(livingBase.posX, livingBase.posY, livingBase.posZ);

                if (livingBase.worldObj.getCollidingBoundingBoxes(livingBase, livingBase.boundingBox).isEmpty() && !livingBase.worldObj.isAnyLiquid(livingBase.boundingBox)) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            livingBase.setPositionAndUpdate(oriX, oriY, oriZ);
            return false;
        } else {
            livingBase.worldObj.playSoundEffect(oriX, oriY, oriZ, "mob.endermen.portal", 1.0F, 1.0F);
            livingBase.playSound("mob.endermen.portal", 1.0F, 1.0F);
            return true;
        }
    }
}
