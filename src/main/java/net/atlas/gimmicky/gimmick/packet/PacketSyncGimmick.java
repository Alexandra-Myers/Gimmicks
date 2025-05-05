package net.atlas.gimmicky.gimmick.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.atlas.gimmicky.gimmick.GimmickExtendedEntityProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.nio.charset.StandardCharsets;

public class PacketSyncGimmick implements IMessage {
    private String gimmick;
    private int entityID;

    public PacketSyncGimmick() {}

    public PacketSyncGimmick(String gimmick, int entityID) {
        this.gimmick = gimmick;
        this.entityID = entityID;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityID = buf.readInt();
        int len = buf.readInt();
        this.gimmick = new String(buf.readBytes(len).array(), StandardCharsets.UTF_8);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityID);
        byte[] bytes = gimmick.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    public static class Handler implements IMessageHandler<PacketSyncGimmick, IMessage> {
        @Override
        public IMessage onMessage(PacketSyncGimmick message, MessageContext ctx) {
            Minecraft.getMinecraft().func_152344_a(() -> {
                Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(message.entityID);
                IExtendedEntityProperties gimmickProperties = entity.getExtendedProperties("gimmicky:gimmick");
                if (!(gimmickProperties instanceof GimmickExtendedEntityProperty)) return;
                ((GimmickExtendedEntityProperty) gimmickProperties).setGimmick(message.gimmick);
            });
            return null;
        }
    }
}
