package net.atlas.gimmicky.item;

import net.atlas.gimmicky.Gimmicky;
import net.atlas.gimmicky.gimmick.GimmickExtendedEntityProperty;
import net.atlas.gimmicky.gimmick.packet.PacketSyncGimmick;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.util.List;

import static net.atlas.gimmicky.Gimmicky.getRandomGimmick;

public class ItemTomeOfGimmicks extends Item {
    public ItemTomeOfGimmicks() {
        this.setUnlocalizedName("tomeOfGimmicks");
        this.setTextureName("gimmicky:tome_of_gimmicks");
        this.setMaxStackSize(1);
        this.setMaxDamage(4);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player) {
        IExtendedEntityProperties gimmickProperties = player.getExtendedProperties("gimmicky:gimmick");
        if (!(gimmickProperties instanceof GimmickExtendedEntityProperty)) return super.onItemRightClick(itemStackIn, worldIn, player);
        GimmickExtendedEntityProperty gimmickExtendedEntityProperty = (GimmickExtendedEntityProperty) gimmickProperties;
        gimmickExtendedEntityProperty.setGimmick(getRandomGimmick(gimmickExtendedEntityProperty.getRandom()));
        gimmickExtendedEntityProperty.saveToCustomData(player.getEntityData());
        Gimmicky.proxy.simpleNetworkWrapper.sendToAll(new PacketSyncGimmick(gimmickExtendedEntityProperty.getGimmickName(), player.getEntityId()));
        if (!player.capabilities.isCreativeMode) itemStackIn.damageItem(1, player);
        return itemStackIn;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> lines, boolean p_77624_4_) {
        if (!player.inventory.hasItem(this)) return;
        IExtendedEntityProperties gimmickProperties = player.getExtendedProperties("gimmicky:gimmick");
        if (!(gimmickProperties instanceof GimmickExtendedEntityProperty)) return;
        GimmickExtendedEntityProperty gimmickExtendedEntityProperty = (GimmickExtendedEntityProperty) gimmickProperties;
        lines.add("");
        lines.add(String.format(StatCollector.translateToLocal("commands.gimmick.retrieve"), player.getDisplayName(), gimmickExtendedEntityProperty.getGimmickName()));
    }
}
