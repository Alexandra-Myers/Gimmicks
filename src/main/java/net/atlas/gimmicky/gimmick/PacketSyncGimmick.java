package net.atlas.gimmicky.gimmick;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.nio.charset.StandardCharsets;

public class PacketSyncGimmick implements IMessage {
    private String gimmick;

    public PacketSyncGimmick() {}

    public PacketSyncGimmick(String gimmick) {
        this.gimmick = gimmick;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int len = buf.readInt();
        this.gimmick = new String(buf.readBytes(len).array(), StandardCharsets.UTF_8);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        byte[] bytes = gimmick.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    public static class Handler implements IMessageHandler<PacketSyncGimmick, IMessage> {
        @Override
        public IMessage onMessage(PacketSyncGimmick message, MessageContext ctx) {
            Minecraft.getMinecraft().func_152344_a(() -> {
                EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                IExtendedEntityProperties gimmickProperties = player.getExtendedProperties("gimmicky:gimmick");
                if (!(gimmickProperties instanceof GimmickExtendedEntityProperty)) return;
                ((GimmickExtendedEntityProperty) gimmickProperties).setGimmick(message.gimmick);
            });
            return null;
        }
    }
}
