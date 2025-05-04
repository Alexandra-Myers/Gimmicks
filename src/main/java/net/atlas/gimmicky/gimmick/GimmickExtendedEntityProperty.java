package net.atlas.gimmicky.gimmick;

import cpw.mods.fml.common.eventhandler.Event;
import net.atlas.gimmicky.Gimmicky;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.util.Random;

import static net.atlas.gimmicky.Gimmicky.*;

public class GimmickExtendedEntityProperty implements IExtendedEntityProperties {
    private Random random;
    private String gimmick;
    private String oldGimmick = null;
    @Override
    public void saveNBTData(NBTTagCompound compound) {
        if (oldGimmick != null) compound.setString(Gimmicky.GIMMICK_TAG_NAME + "O", oldGimmick);
        if (!gimmick.isEmpty()) compound.setString(Gimmicky.GIMMICK_TAG_NAME, gimmick);
        else if (compound.hasKey(Gimmicky.GIMMICK_TAG_NAME)) compound.removeTag(GIMMICK_TAG_NAME);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if (compound.hasKey(Gimmicky.GIMMICK_TAG_NAME + "O")) oldGimmick = compound.getString(Gimmicky.GIMMICK_TAG_NAME + "O");
        if (!compound.hasKey(Gimmicky.GIMMICK_TAG_NAME)) {
            gimmick = getRandomGimmick(random);
            return;
        } else if (!gimmicks.containsKey(compound.getString(Gimmicky.GIMMICK_TAG_NAME))) {
            compound.removeTag(Gimmicky.GIMMICK_TAG_NAME);
            gimmick = getRandomGimmick(random);
            return;
        }
        gimmick = compound.getString(Gimmicky.GIMMICK_TAG_NAME);
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

    public void finaliseOldGimmick(Event event) {
        if (oldGimmick == null) return;
        Gimmick gimmick = gimmicks.get(oldGimmick);
        if (gimmick.appliesOnEvent(event)) {
            gimmick.finaliseEvent(event);
            oldGimmick = null;
        }
    }

    public Gimmick getGimmick() {
        return gimmicks.get(gimmick);
    }

    public String getGimmickName() {
        return gimmick;
    }
}
