package net.atlas.gimmicky.gimmick;

import static net.atlas.gimmicky.Gimmicky.*;

import java.util.Random;

import net.atlas.gimmicky.Gimmicky;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import cpw.mods.fml.common.eventhandler.Event;

public class GimmickExtendedEntityProperty implements IExtendedEntityProperties {

    private Random random;
    private String gimmick = "nothing";
    private String oldGimmick = null;
    private boolean isDirty = false;

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound customData = compound.getCompoundTag("ForgeData");
        if (customData == null) customData = new NBTTagCompound();
        saveToCustomData(customData);
        compound.setTag("ForgeData", customData);
    }

    public void saveToCustomData(NBTTagCompound customData) {
        NBTTagCompound persistedTagCompound = customData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        if (persistedTagCompound == null) persistedTagCompound = new NBTTagCompound();
        if (oldGimmick != null) persistedTagCompound.setString(Gimmicky.GIMMICK_TAG_NAME + "O", oldGimmick);
        if (!gimmick.isEmpty()) persistedTagCompound.setString(Gimmicky.GIMMICK_TAG_NAME, gimmick);
        else if (persistedTagCompound.hasKey(Gimmicky.GIMMICK_TAG_NAME))
            persistedTagCompound.removeTag(GIMMICK_TAG_NAME);
        customData.setTag(EntityPlayer.PERSISTED_NBT_TAG, persistedTagCompound);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTTagCompound customData = compound.getCompoundTag("ForgeData");
        if (customData == null) customData = new NBTTagCompound();
        loadFromCustomData(customData);
    }

    public void loadFromCustomData(NBTTagCompound customData) {
        NBTTagCompound persistedTagCompound = customData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        if (persistedTagCompound == null) persistedTagCompound = new NBTTagCompound();
        if (persistedTagCompound.hasKey(Gimmicky.GIMMICK_TAG_NAME + "O"))
            oldGimmick = persistedTagCompound.getString(Gimmicky.GIMMICK_TAG_NAME + "O");
        if (!persistedTagCompound.hasKey(Gimmicky.GIMMICK_TAG_NAME)) {
            gimmick = getRandomGimmick(random);
            isDirty = true;
            return;
        } else if (!gimmicks.containsKey(persistedTagCompound.getString(Gimmicky.GIMMICK_TAG_NAME))) {
            persistedTagCompound.removeTag(Gimmicky.GIMMICK_TAG_NAME);
            gimmick = getRandomGimmick(random);
            isDirty = true;
            return;
        }
        gimmick = persistedTagCompound.getString(Gimmicky.GIMMICK_TAG_NAME);
    }

    @Override
    public void init(Entity entity, World world) {
        random = new Random(world.rand.nextLong());
    }

    public Random getRandom() {
        return random;
    }

    public void setGimmick(String gimmick) {
        this.oldGimmick = this.gimmick;
        this.gimmick = gimmick;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void finaliseOldGimmick(Event event) {
        if (oldGimmick == null) return;
        if (oldGimmick.equals(gimmick)) return;
        Gimmick gimmick = gimmicks.get(oldGimmick);
        if (gimmick != null && gimmick.appliesOnEvent(event)) {
            gimmick.finaliseEvent(event);
            oldGimmick = null;
        } else if (gimmick == null) oldGimmick = null;
    }

    public Gimmick getGimmick() {
        return gimmicks.get(gimmick);
    }

    public String getGimmickName() {
        return gimmick;
    }
}
