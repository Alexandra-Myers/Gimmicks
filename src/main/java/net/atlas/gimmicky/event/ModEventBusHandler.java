package net.atlas.gimmicky.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.atlas.gimmicky.Config;
import net.atlas.gimmicky.Gimmicky;
import net.atlas.gimmicky.gimmick.Gimmick;
import net.atlas.gimmicky.gimmick.GimmickExtendedEntityProperty;
import net.atlas.gimmicky.gimmick.PacketSyncGimmick;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.IExtendedEntityProperties;

import static net.atlas.gimmicky.Gimmicky.getRandomGimmick;

public class ModEventBusHandler {
    @SubscribeEvent
    public void playerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        EntityPlayer player = event.player;
        IExtendedEntityProperties gimmickProperties = player.getExtendedProperties("gimmicky:gimmick");
        if (!(gimmickProperties instanceof GimmickExtendedEntityProperty)) return;
        GimmickExtendedEntityProperty gimmickExtendedEntityProperty = (GimmickExtendedEntityProperty) gimmickProperties;
        if (Config.persistGimmickAcrossDeath && !"nothing".equalsIgnoreCase(gimmickExtendedEntityProperty.getGimmickName())) return;
        gimmickExtendedEntityProperty.setGimmick(getRandomGimmick(gimmickExtendedEntityProperty.getRandom()));
        if (player instanceof EntityPlayerMP) Gimmicky.proxy.simpleNetworkWrapper.sendTo(new PacketSyncGimmick(gimmickExtendedEntityProperty.getGimmickName()), (EntityPlayerMP) player);
    }
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        IExtendedEntityProperties gimmickProperties = event.player.getExtendedProperties("gimmicky:gimmick");
        if (!(gimmickProperties instanceof GimmickExtendedEntityProperty)) return;
        Gimmick gimmick = ((GimmickExtendedEntityProperty) gimmickProperties).getGimmick();
        if (gimmick != null && gimmick.appliesOnEvent(event)) gimmick.handleEvent(event);
    }
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.player instanceof EntityPlayerMP)) return;
        EntityPlayerMP player = (EntityPlayerMP) event.player;
        IExtendedEntityProperties gimmickProperties = player.getExtendedProperties("gimmicky:gimmick");
        if (!(gimmickProperties instanceof GimmickExtendedEntityProperty)) return;
        GimmickExtendedEntityProperty gimmickExtendedEntityProperty = (GimmickExtendedEntityProperty) gimmickProperties;
        Gimmicky.proxy.simpleNetworkWrapper.sendTo(new PacketSyncGimmick(gimmickExtendedEntityProperty.getGimmickName()), player);
    }
}
